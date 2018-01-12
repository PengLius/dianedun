package cn.dianedun.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LocalInfo;
import com.videogo.util.RotateViewUtil;
import com.videogo.util.Utils;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.api.ViseApi;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;
import cn.dianedun.adapter.NormalFragmentAdapter;
import cn.dianedun.base.BaseActivity;
import cn.dianedun.bean.AccesstokenBean;
import cn.dianedun.bean.CommonBean;
import cn.dianedun.bean.DepartPlacesListBean;
import cn.dianedun.fragment.NewSpitVideoFragment;
import cn.dianedun.fragment.VideoEntiryFragment;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.view.DateTimeDialogOnlyYMD;
import cn.dianedun.view.EZUIPlayer.EZUIPlayer;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static cn.dianedun.tools.App.getOpenSDK;

/**
 * Created by Administrator on 2017/12/16.
 */

public class VideoPlayActivity extends BaseActivity implements NewSpitVideoFragment.OnSpitVideoSelect {


    @Bind(R.id.av_viewpager_mult)
    ViewPager mViewPagerMult;

    @Bind(R.id.av_viewpager_single)
    ViewPager mViewPagerSingle;

    @Bind(R.id.av_rl_videoplay)
    RelativeLayout mRlVideoPlay;

    @Bind(R.id.av_tv_places)
    TextView mTvPlaces;

    @Bind(R.id.av_img_fullscreen)
    ImageView mImgFullScreen;

    @Bind(R.id.av_rl_place)
    RelativeLayout mRlPlace;

    @Bind(R.id.av_rl_titlelayout)
    RelativeLayout mRlTitleLayout;

    @Bind(R.id.av_img_back)
    ImageView mImgBack;

    @Bind(R.id.av_tv_tip)
    TextView mTvTip;

    private ViewGroup.LayoutParams mVCLayouyParams;

    private MyOrientationDetector mOrientationDetector;

    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;

    private Boolean m4BoxOr9Box = null; //四分屏

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mOrientation = newConfig.orientation;
        if (mVideoEntiryFragments == null) return;
//        if (mControlPopupView!=null && mControlPopupView.isShowing())
//            mControlPopupView.dismiss();
        updateOperatorUI();
        setSurfaceSize();
    }

    /**
     * 打开云台控制窗口
     *
     * @see
     * @since V1.8.3
     */
    private LocalInfo mLocalInfo = null;
    private PopupWindow mPtzPopupWindow;
    private Rect mRealPlayRect = null;
    private void initRealPlayPageLy() {
        /** 测量状态栏高度 **/
        ViewTreeObserver viewTreeObserver = mRlTitleLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mRealPlayRect == null) {
                    // 获取状况栏高度
                    mRealPlayRect = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(mRealPlayRect);
                }
            }
        });
    }
    private void closePtzPopupWindow() {
        if (mPtzPopupWindow != null) {
            dismissPopWindow(mPtzPopupWindow);
            mPtzPopupWindow = null;
        }
    }
    private void dismissPopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && !isFinishing()) {
            try {
                popupWindow.dismiss();
            } catch (Exception e) {

            }
        }
    }
    private void openPtzPopupWindow(View parent) {
        closePtzPopupWindow();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_ptz_wnd, null, true);

        ImageButton ptzCloseBtn = (ImageButton) layoutView.findViewById(R.id.ptz_close_btn);
        ptzCloseBtn.setOnClickListener(mOnPopWndClickListener);
        ImageButton ptzTopBtn = (ImageButton) layoutView.findViewById(R.id.ptz_top_btn);
        ptzTopBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzBottomBtn = (ImageButton) layoutView.findViewById(R.id.ptz_bottom_btn);
        ptzBottomBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzLeftBtn = (ImageButton) layoutView.findViewById(R.id.ptz_left_btn);
        ptzLeftBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzRightBtn = (ImageButton) layoutView.findViewById(R.id.ptz_right_btn);
        ptzRightBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzMultipletAddBtn = (ImageButton) layoutView.findViewById(R.id.rpw_ib_multiple_add);
        ptzMultipletAddBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzMultipletDelBtn = (ImageButton) layoutView.findViewById(R.id.rpw_ib_multiple_del);
        ptzMultipletDelBtn.setOnTouchListener(mOnTouchListener);

        ImageButton ptzFlipBtn = (ImageButton) layoutView.findViewById(R.id.ptz_flip_btn);
        ptzFlipBtn.setOnClickListener(mOnPopWndClickListener);


        mLocalInfo = getLocalInfo();
        int height = mLocalInfo.getScreenHeight() - mRlVideoPlay.getHeight()
                - (mRealPlayRect != null ? mRealPlayRect.top : mLocalInfo.getNavigationBarHeight()) - mRlTitleLayout.getHeight();
        mPtzPopupWindow = new PopupWindow(layoutView, RelativeLayout.LayoutParams.MATCH_PARENT, height, true);
        mPtzPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPtzPopupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        mPtzPopupWindow.setFocusable(true);
        mPtzPopupWindow.setOutsideTouchable(true);
        mPtzPopupWindow.showAsDropDown(parent);
        mPtzPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mPtzPopupWindow = null;
                closePtzPopupWindow();
            }
        });
        mPtzPopupWindow.update();
    }
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            int action = motionevent.getAction();
            VideoEntiryFragment fragment = getInTopVideo();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    switch (view.getId()) {
//                        case R.id.talkback_control_btn:
//                            mTalkRingView.setVisibility(View.VISIBLE);
//                            mEZPlayer.setVoiceTalkStatus(true);
//                            break;
                        case R.id.ptz_top_btn:

                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_UP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART);

                            break;
                        case R.id.ptz_bottom_btn:

                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_DOWN);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_left_btn:
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_right_btn:
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.rpw_ib_multiple_del:
                            //变小
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomOut, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.rpw_ib_multiple_add:
                            //变大
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomIn, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
//                        case R.id.rpw_ib_focalize_del:
//                            //焦变小
//                            break;
//                        case R.id.rpw_ib_focalize_add:
//                            //焦变大
//                            break;

                        default:
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    switch (view.getId()) {
                        case R.id.ptz_top_btn:
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_bottom_btn:
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_left_btn:
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_right_btn:
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.rpw_ib_multiple_del:
                            //变小
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomOut, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.rpw_ib_multiple_add:
                            //变大
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomIn, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private View.OnClickListener mOnPopWndClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ptz_close_btn:
                    closePtzPopupWindow();
                    break;
                case R.id.ptz_flip_btn:
                    break;
//                case R.id.talkback_close_btn:
//                    closeTalkPopupWindow(true, false);
//                    break;
                default:
                    break;
            }
        }
    };
    private void updateOrientation() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private void setSurfaceSize() {
        if (m4BoxOr9Box == null){
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            for (int i=0; i<mVideoEntiryFragments.length;i++)
                mVideoEntiryFragments[i].setSurfaceSize(dm.widthPixels);
        }else{
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            if (m4BoxOr9Box){
                for (int i=0; i<mSpitFragments.length;i++)
                    mSpitFragments[i].setSurfaceSize(dm.widthPixels/2);
            }else{
                for (int i=0; i<mSpitFragments.length;i++)
                    mSpitFragments[i].setSurfaceSize(dm.widthPixels/3);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mOrientationDetector!=null)
            mOrientationDetector.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOrientationDetector!=null)
            mOrientationDetector.disable();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void updateOperatorUI() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            // 显示状态栏
            fullScreen(false);
            mRlTitleLayout.setVisibility(VISIBLE);
            mRlVideoPlay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRlVideoPlay.setLayoutParams(mVCLayouyParams);
                }
            },200);
        } else {
            // 隐藏状态栏
            fullScreen(true);
            mRlTitleLayout.setVisibility(GONE);
            mRlVideoPlay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    realPlayPlayRlLp.gravity = Gravity.CENTER;
                    mRlVideoPlay.setLayoutParams(realPlayPlayRlLp);
                }
            },200);
        }
    }
    private void fullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_videoplay);
        ButterKnife.bind(this);
    }
    private LocalInfo getLocalInfo(){
        if(mLocalInfo == null) {
            mLocalInfo = LocalInfo.getInstance();
            if(mLocalInfo == null) {
                LocalInfo.init(App.getInstance());
                mLocalInfo = LocalInfo.getInstance();
            }
        }
        return mLocalInfo;
    }
    @Override
    protected void initView() {
//        setToolBarColor(getResources().getColor(R.color.theme_bule));
//        setTvTitleText(getIntent().getStringExtra("title"));
        // 获取配置信息操作对象
        mLocalInfo = getLocalInfo();
        if (mLocalInfo!=null){
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            mLocalInfo.setScreenWidthHeight(metric.widthPixels, metric.heightPixels);
            mLocalInfo.setNavigationBarHeight((int) Math.ceil(25 * getResources().getDisplayMetrics().density));
            mLocalInfo.setSoundOpen(false);
        }
        initRealPlayPageLy();
        setControlBtnEnable(false);
    }

    private int m16to9Height = 0;

    @Override
    protected void bindEvent() {
        mRlVideoPlay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (m16to9Height == 0) {
                    m16to9Height = (int) Math.round(mRlVideoPlay.getWidth() / 1.77);
                    mRlVideoPlay.getLayoutParams().height = m16to9Height;
                }
            }
        });
        mImgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEzDeviceInfo!=null && mEzDeviceInfo.size() > 0)
                    updateOrientation();
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrientation != Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return;
                }
                AppManager.getInstance().finishActivity(VideoPlayActivity.this);
            }
        });
        bindControlView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String mDepartName;
//    private int mCurVideoId;

    private int mDefalutPos;
    @Override
    protected void initData() {
        mDepartName = getIntent().getStringExtra("place");
        mDefalutPos = getIntent().getIntExtra("pos",0);
//        int tag = mDefalutPos + 1;
//        mTvPlaces.setText(mDepartName + "-" +  tag  + "号机");
        mOrientationDetector = new MyOrientationDetector(this);
        mRecordRotateViewUtil = new RotateViewUtil();
    }

    private AccesstokenBean.DataBean mAccessTokenBean;
    private String mDepartId;
    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        mVCLayouyParams = mRlVideoPlay.getLayoutParams();
        mDepartId = getIntent().getStringExtra("id");
        ViseApi api = new ViseApi.Builder(this).build();
        HashMap hashMap = new HashMap();
        hashMap.put("departId",mDepartId);
        api.apiPost(AppConfig.GETACCESSTOKEN, App.getInstance().getToken(),hashMap,false,new ApiCallback<AccesstokenBean.DataBean>(){
            @Override
            public void onNext(AccesstokenBean.DataBean dataBean) {
                mAccessTokenBean = dataBean;
                //设置授权accesstoken
                EZUIKit.setAccessToken(mAccessTokenBean.getAccessToken());
                new GetCamersInfoListTask().execute();
            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                if (e.getCode() == 2001)
                    startActivity(new Intent(VideoPlayActivity.this, LoginActivity.class));
                mTvTip.setText("暂无设备");
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }
        });
    }

    //验证token
    private void getTokenEffect(final Runnable runnable){
        ViseApi api = new ViseApi.Builder(this).build();
        api.apiPost(AppConfig.GETDEPARTPLACES, App.getInstance().getToken(),new HashMap(),false,new ApiCallback<DepartPlacesListBean.DataBean>(){
            @Override
            public void onStart() {

            }

            @Override
            public void onNext(DepartPlacesListBean.DataBean dataBean){
                runOnUiThread(runnable);
            }

            @Override
            public void onError(ApiException e) {
                final VideoEntiryFragment fragment = getInTopVideo();
                if (fragment == null){
                    if (fragment.getIsRecord())
                        fragment.stopRealPlayRecord();
                    if (fragment.getIsOnTalk())
                        fragment.stopVoiceTalk();
                }
                if(e.getCode() == 2001)
                    startActivity(new Intent(VideoPlayActivity.this, LoginActivity.class));
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    /**
     * 获取摄像头列表
     */
    List<EZDeviceInfo> mEzDeviceInfo = null;
    private class GetCamersInfoListTask extends AsyncTask<Void, Void, List<EZDeviceInfo>> {
        private int mErrorCode = 0;

        public GetCamersInfoListTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<EZDeviceInfo> doInBackground(Void... params) {
            if (VideoPlayActivity.this.isFinishing()) {
                return null;
            }

            if (!ConnectionDetector.isNetworkAvailable(VideoPlayActivity.this)) {
                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
                return null;
            }

            try{
                List<EZDeviceInfo> result = null;
                result = getOpenSDK().getDeviceList(0, 20);
                return result;

            } catch (BaseException e) {
                final ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(errorInfo.toString());
                        mTvTip.setText(errorInfo.toString());
                        mTvTip.setVisibility(VISIBLE);
                    }
                });
                mErrorCode = errorInfo.errorCode;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<EZDeviceInfo> result) {
            super.onPostExecute(result);
            if (VideoPlayActivity.this.isFinishing()) {
                return;
            }

            mEzDeviceInfo = result;
            if (mEzDeviceInfo != null && mEzDeviceInfo.size() > 0){
                int spitId = getIntent().getIntExtra("spit",0);
                if (spitId < 0) {
                    //进入分屏
//                    mDefalutPos = -1;
                    buildSingleViewPager();
                    if (spitId == -1) {
                        prepareMultPager(true);
                    } else {
                        prepareMultPager(false);
                    }
                    mRlPlace.setVisibility(GONE);
                }else{
                    buildSingleViewPager();
                }
            }else{
                showToast("暂无设备信息");
                mTvTip.setText("暂无设备信息");
                mTvTip.setVisibility(VISIBLE);
            }

            if (mErrorCode != 0) {
                onError(mErrorCode);
            }
        }

        protected void onError(int errorCode) {
            switch (errorCode) {
                case ErrorCode.ERROR_WEB_SESSION_ERROR:
                case ErrorCode.ERROR_WEB_SESSION_EXPIRE:
                    showToast("已过期，请重新登录");
                    startActivity(new Intent(VideoPlayActivity.this,LoginActivity.class));
                    break;
                default:
                    showToast("获取摄像头设备列表失败");
                    break;
            }
        }
    }

    private VideoEntiryFragment[] mVideoEntiryFragments;

    private NormalFragmentAdapter mSpitAdapter;
    private NewSpitVideoFragment[] mSpitFragments;

    private void removeSpitPlayer(){
        if (mSpitFragments==null) return;
        for (int i=0; i<mSpitFragments.length; i++){
            mSpitFragments[i].releasePlay();
        }
        mSpitFragments = null;
    }

    private void buildMultViewPager(){
        int tag;
        if (m4BoxOr9Box) {
            tag = 4;
        }else{
            tag = 9;
        }
        int pagerSize = (int)Math.ceil(mEzDeviceInfo.size() / (tag+0.0f));
        mSpitFragments = new NewSpitVideoFragment[pagerSize];
        for (int i=0; i < pagerSize; i++){
            mSpitFragments[i] = NewSpitVideoFragment.getInstance();
            int type;
            boolean autoPlay = false;
            if (i == 0){
                autoPlay = true;
            }
            if (tag == 4)
                type = 0;
            else
                type = 1;
            // 4*i+1     4*(i+1)
            int left = tag * (i);
            int right = tag * (i+1) - 1;
            int max = right > mEzDeviceInfo.size()-1 ? mEzDeviceInfo.size()-1 : right;
            ArrayList<EZDeviceInfo> deviceList = new ArrayList<>();
            for ( int j=left; j<= max ;j++){
                deviceList.add(mEzDeviceInfo.get(j));
            }
            mSpitFragments[i].setData(type,autoPlay,deviceList);
            mSpitFragments[i].setOnSpitVideoSelect(this);
        }
        mViewPagerMult.setOffscreenPageLimit(mSpitFragments.length);
        mSpitAdapter = new NormalFragmentAdapter(getSupportFragmentManager(),mSpitFragments);
        mViewPagerMult.setAdapter(mSpitAdapter);
        mSpitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoSelect(EZDeviceInfo deviceInfo, int pos) {
        m4BoxOr9Box = null;
        removeSpitPlayer();
        mRlPlace.setVisibility(VISIBLE);
        mRlVideoPlay.removeView(mViewPagerMult);
        if (mRlVideoPlay.findViewById(R.id.av_viewpager_single) == null)
            mRlVideoPlay.addView(mViewPagerSingle,0);
        int i=0;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        for (;i<mVideoEntiryFragments.length;i++){
            if (mVideoEntiryFragments[i].getDataBean().getDeviceSerial().equals(deviceInfo.getDeviceSerial())){
                mViewPagerSingle.setCurrentItem(i);
                mVideoEntiryFragments[i].onSupportVisible();
                mVideoEntiryFragments[i].setSurfaceSize(m4BoxOr9Box !=null  ? (m4BoxOr9Box ? dm.widthPixels/2 : dm.widthPixels/3) : (dm.widthPixels));
            }else {
                mVideoEntiryFragments[i].setSurfaceSize(m4BoxOr9Box !=null  ? (m4BoxOr9Box ? dm.widthPixels/2 : dm.widthPixels/3) : (dm.widthPixels));
            }
        }
    }

    private void buildSingleViewPager() {
        mVideoEntiryFragments = new VideoEntiryFragment[mEzDeviceInfo.size()];
        for (int i=0; i<mEzDeviceInfo.size();i++){
            mVideoEntiryFragments[i] = VideoEntiryFragment.getInstance(i,
                    i == mDefalutPos, mEzDeviceInfo.get(i)).setVideoParent(new OnVideoSelect() {

                @Override
                public void onVideoTalkBackState(boolean state,ErrorInfo errorInfo) {
                    if (state){
                        //开启对讲成功
                        handleVoiceTalkSucceed();
                    }else{
                        //开启对讲失败
                        if (errorInfo == null)
                            handleVoiceTalkStoped();
                        else
                            handleVoiceTalkFailed(errorInfo);
                    }
                }

                @Override
                public void onVideoClick(){

                }

                @Override
                public void onVideoSelect(int pos,EZDeviceInfo bean) {
                    int tag = pos + 1;
                    mTvPlaces.setText(mDepartName + "-" +  tag  + "号机");
                    setControlBtnEnable(false);
//                    mRlControl.setEnabled(false);
//                    mImgControl.setImageResource(R.mipmap.ic_nor_control);
//                    mImgControl.setVisibility(VISIBLE);
//                    mTvControl.setVisibility(View.INVISIBLE);
//                    mTvControl.setText("控制");
//                    mTvControl.setTag(null);
//                    mCurVideoId = bean.getId();
                }

                @Override
                public void onVideoPlayState(int state,EZUIError ezuiError) {
                    if (state == EZUIPlayer.STATUS_PLAY){
//                        mImgStartVideo.setImageResource(R.mipmap.ic_nor_stop_2);
                        setControlBtnEnable(true);
                        if (m4BoxOr9Box == null)
                            mRlPlace.setVisibility(VISIBLE);

                        if (mNeedOpenTalkBack)
                            doTalkBack();
                        else if (mNeedOpenVideoRecord)
                            doRecorde();
                    }else{
                        setControlBtnEnable(false);
//                        mImgStartVideo.setImageResource(R.mipmap.ic_nor_);
                    }
                }

                @Override
                public void onVideoVoiceControl(boolean bOpen) {
                    if (bOpen){
                        mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
                    }else{
                        mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_greenhorn);
                    }
                }

                @Override
                public void onVideoRecordState(boolean bStart) {
                    if (bStart){
                        mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStart,
                                mImgVideoStop, 0, 90);
                    }else{
                        mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStop,
                                mImgVideoStart, 0, 90);
                    }
                }
            });
        }
        NormalFragmentAdapter fragmentAdapter = new NormalFragmentAdapter(getSupportFragmentManager(),mVideoEntiryFragments);
        mViewPagerSingle.setAdapter(fragmentAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPagerSingle.setCurrentItem(mDefalutPos,false);
            }
        },100);
        mViewPagerSingle.setOffscreenPageLimit(fragmentAdapter.getCount());
    }

    private void handleVoiceTalkFailed(ErrorInfo errorInfo) {
        switch (errorInfo.errorCode) {
            case ErrorCode.ERROR_TRANSF_DEVICE_TALKING:
                Utils.showToast(VideoPlayActivity.this, R.string.realplay_play_talkback_fail_ison);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_PRIVACYON:
                Utils.showToast(VideoPlayActivity.this, R.string.realplay_play_talkback_fail_privacy);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_OFFLINE:
                Utils.showToast(VideoPlayActivity.this, R.string.realplay_fail_device_not_exist);
                break;
            case ErrorCode.ERROR_TTS_MSG_REQ_TIMEOUT:
            case ErrorCode.ERROR_TTS_MSG_SVR_HANDLE_TIMEOUT:
            case ErrorCode.ERROR_TTS_WAIT_TIMEOUT:
            case ErrorCode.ERROR_TTS_HNADLE_TIMEOUT:
                Utils.showToast(VideoPlayActivity.this, R.string.realplay_play_talkback_request_timeout, errorInfo.errorCode);
                break;
            case ErrorCode.ERROR_CAS_AUDIO_SOCKET_ERROR:
            case ErrorCode.ERROR_CAS_AUDIO_RECV_ERROR:
            case ErrorCode.ERROR_CAS_AUDIO_SEND_ERROR:
                Utils.showToast(VideoPlayActivity.this, R.string.realplay_play_talkback_network_exception, errorInfo.errorCode);
                break;
            default:
                Utils.showToast(VideoPlayActivity.this, R.string.realplay_play_talkback_fail, errorInfo.errorCode);
                break;
        }
        mLLTalkBack.setEnabled(true);
        mImgTalkBack.setImageResource(R.mipmap.ic_nor_novoice);
    }

    private void handleVoiceTalkStoped() {
        showToast(this,"对讲结束");
        mLLTalkBack.setEnabled(true);
        mImgTalkBack.setImageResource(R.mipmap.ic_nor_novoice);
    }

    private void handleVoiceTalkSucceed() {
        mLLTalkBack.setEnabled(true);
        showToast(this,"对讲中…");
        mImgTalkBack.setImageResource(R.mipmap.ic_nor_voice);
    }

    private void setControlBtnEnable(boolean enable){
        mRlVideoPlay.setEnabled(enable);
        mLLVoice.setEnabled(enable);
        mLLStream.setEnabled(enable);
        mRealPlayCaptureBtn.setEnabled(enable);
        mRealPlayRecordBtn.setEnabled(enable);
//        mLLPlayBack.setEnabled(enable);
        mLLlight.setEnabled(enable);
        mLLTalkBack.setEnabled(enable);
        mLLControl.setEnabled(enable);
    }

    @Override
    public void onBackPressedSupport() {
        if (mOrientation != Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressedSupport();
    }

    private RotateViewUtil mRecordRotateViewUtil;
    @Bind(R.id.av_ll_voice)
    LinearLayout mLLVoice;

    @Bind(R.id.av_ll_photo)
    LinearLayout mRealPlayCaptureBtn;

    @Bind(R.id.av_ll_video)
    LinearLayout mRealPlayRecordBtn;

    @Bind(R.id.av_ll_stream)
    LinearLayout mLLStream;

    @Bind(R.id.av_ll_talkback)
    LinearLayout mLLTalkBack;

    @Bind(R.id.av_ll_control)
    LinearLayout mLLControl;

    @Bind(R.id.av_ll_playback)
    LinearLayout mLLPlayBack;

    @Bind(R.id.av_ll_light)
    LinearLayout mLLlight;

    @Bind(R.id.av_img_sound)
    ImageView mRealPlaySoundBtn;

    @Bind(R.id.av_fl_videocontainer)
    FrameLayout mRealPlayRecordContainer;
    @Bind(R.id.av_img_videostart)
    ImageView mImgVideoStart;
    @Bind(R.id.av_img_videostop)
    ImageView mImgVideoStop;


    @Bind(R.id.av_img_spit)
    ImageView mImgSpit;

    protected void doRecorde(){
        VideoEntiryFragment fragment = getInTopVideo();
        if (fragment!=null){
            mNeedOpenVideoRecord = false;
            fragment.onRecordBtnClick();
        }
    }
    @Bind(R.id.av_img_talkback)
    ImageView mImgTalkBack;

    private void doTalkBack(){
        VideoEntiryFragment fragment = getInTopVideo();
        if (fragment!=null){
            mNeedOpenTalkBack = false;
            mLLTalkBack.setEnabled(false);
            fragment.startVoiceTalk();
        }
    }

    private String getMonth(Calendar calendar){
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10){
            return "0" + month;
        }
        return String.valueOf(month);
    }

    private void openPlayBackWindow(final String deviceSerial){
        new DateTimeDialogOnlyYMD(VideoPlayActivity.this, new DateTimeDialogOnlyYMD.MyOnDateSetListener() {
            @Override
            public void onDateSet(Date date) {
                VideoEntiryFragment fragment = getInTopVideo();
                if (fragment==null)
                    return;
                String url = "ezopen://open.ys7.com/";
//                        if (mDeviceInfo.getIsEncrypt() == 1){
//                            加密
//                            url = "MDWBOZ@"+ url + mDeviceInfo.getDeviceSerial() + "/1.rec";
//                        }else{
                url += deviceSerial + "/1.rec?mute=true&";
//                        }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                url += "begin=";
                String prefixStr = url;
                String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                if (Integer.parseInt(day) < 10){
                    day = "0" + day;
                }
                url += calendar.get(Calendar.YEAR) + getMonth(calendar) + day;
//                        String dateStr = calendar.get(Calendar.YEAR) + "年" +  month + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";

                PlayerBackActiviaty.startPlayBackActivity(VideoPlayActivity.this,mAccessTokenBean.getAccessToken(),url,prefixStr,calendar,
                        mTvPlaces.getText().toString());
            }
        },true,true,true).show();
    }

    private boolean mStatusLight = false;
    private int mLightCmd = 1; // 0-关 1-开
    @Bind(R.id.av_img_light)
    ImageView mImgLight;

    private void bindControlView() {
        mLLlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (mStatusLight){
                            return;
                        }
                        mStatusLight = true;
//                Toast.makeText(VideoPlayActivity.this, "正在灯控操作中", Toast.LENGTH_LONG).show();
//                Utils.showToast(VideoPlayActivity.this,"正在灯控操作中");
                        showToast(VideoPlayActivity.this,"正在灯控操作中");
                        ViseApi api = new ViseApi.Builder(VideoPlayActivity.this).build();
                        HashMap hashMap = new HashMap();
                        hashMap.put("departId",mDepartId);
                        hashMap.put("cmd",mLightCmd);
                        if (m4BoxOr9Box == null) {
                            VideoEntiryFragment fragment = getInTopVideo();
                            if (fragment!=null)
                                hashMap.put("deviceSerial", fragment.getDataBean().getDeviceSerial());
                        }
                        api.post(AppConfig.CONTROLLIGHT, App.getInstance().getToken(),hashMap,false,new ApiCallback<CommonBean>(){
                            @Override
                            public void onNext(CommonBean dataBean) {
                                if (dataBean.getCode() == 0){
                                    if (mLightCmd == 1){
                                        showToast(VideoPlayActivity.this,"开灯操作成功");
//                                showToast("开灯操作成功");
                                        mLightCmd = 0;
                                        mImgLight.setImageResource(R.mipmap.ic_nor_openlight);
                                    }else{
                                        showToast(VideoPlayActivity.this,"关灯操作成功");
//                                showToast("关灯操作成功");
                                        mLightCmd = 1;
                                        mImgLight.setImageResource(R.mipmap.ic_nor_closelight);
                                    }
                                }else{
                                    showToast(dataBean.getMsg());
                                }
                                mStatusLight = false;
                            }

                            @Override
                            public void onError(ApiException e) {
                                if (e.getCode() == 2001)
                                    startActivity(new Intent(VideoPlayActivity.this, LoginActivity.class));
                                showToast(e.getMessage());
                                mStatusLight = false;
//                        setRealPlayFailUI("暂无设备在线");
                            }

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onCompleted() {

                            }
                        });
                    }
                });
            }
        });

        mLLPlayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        final VideoEntiryFragment fragment = getInTopVideo();
                        if (fragment == null)
                            return;
                        if (fragment.getIsRecord()){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("该操作会中断录像，是否继续？");
                            builder.setPositiveButton("取消", null);
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    fragment.stopRealPlayRecord();
                                    dialog.dismiss();
                                    openPlayBackWindow(fragment.getDataBean().getDeviceSerial());
                                }
                            });
                            builder.show();
                            return;
                        }
                        openPlayBackWindow(fragment.getDataBean().getDeviceSerial());
                    }
                });
            }
        });
        mLLTalkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (m4BoxOr9Box != null){
                            showToast("请选择具体设备");
                            return;
                        }

                        if (AndPermission.hasPermission(VideoPlayActivity.this, RECORD_AUDIO)){
                            doTalkBack();
                        }else{
                            AndPermission.with(VideoPlayActivity.this).permission(RECORD_AUDIO).requestCode(1).send();
                        }
                    }
                });
            }
        });
        mRealPlayRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (m4BoxOr9Box != null){
                            showToast("请选择具体设备");
                            return;
                        }
                        if (AndPermission.hasPermission(VideoPlayActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            doRecorde();
                        }else{
                            AndPermission.with(VideoPlayActivity.this).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).requestCode(0).send();
                        }
                    }
                });
            }
        });

        mLLControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (m4BoxOr9Box != null) {
                            showToast("请选择具体设备");
                            return;
                        }
                        openPtzPopupWindow(mRlVideoPlay);
                    }
                });
            }
        });

        mRealPlayCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (m4BoxOr9Box != null){
                            showToast("请选择具体设备");
                            return;
                        }

                        VideoEntiryFragment fragment = getInTopVideo();
                        if (fragment!=null){
                            fragment.onCapturePicBtnClick();
                        }
                    }
                });
            }
        });

        mLLVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (m4BoxOr9Box != null){
                            showToast("请选择具体设备");
                            return;
                        }

                        //声音控制
                        VideoEntiryFragment fragment = getInTopVideo();
                        if (fragment!=null){
                            Boolean res = fragment.onSoundBtnClick();
                            if (res!=null){
                                if (res){
                                    mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_greenhorn);
                                }else{
                                    mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
                                }
                            }
                        }
                    }
                });
            }
        });

        mImgSpit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        //分屏窗口
                        if (mAccessTokenBean == null){
                            showToast("暂无设备信息");
                            return;
                        }
                        openSpitPopWindow(v);
                    }
                });
            }
        });

        mLLStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenEffect(new Runnable() {
                    @Override
                    public void run() {
                        if (m4BoxOr9Box != null){
                            showToast("请选择具体设备");
                            return;
                        }
                        final VideoEntiryFragment fragment = getInTopVideo();
                        if (!fragment.isVideoNormal())
                            return;
                        if (fragment.getIsRecord()){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("该操作会中断录像，是否继续？");
                            builder.setPositiveButton("取消", null);
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    fragment.stopRealPlayRecord();
                                    dialog.dismiss();
                                    openQaWindows();
                                }
                            });
                            builder.show();
                            return;
                        }
                        openQaWindows();
                    }
                });
            }
        });
    }

    private boolean mNeedOpenTalkBack = false;
    private boolean mNeedOpenVideoRecord = false;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantPermissions) {
                for (int i=0;i<grantPermissions.size();i++){
                    if (grantPermissions.get(i).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && requestCode == 0){
                        mNeedOpenVideoRecord = true;
                        doRecorde();
                        break;
                    }else if (grantPermissions.get(i).equals(RECORD_AUDIO) && requestCode == 1){
                        mNeedOpenTalkBack = true;
                        doTalkBack();
                        break;
                    }
                }
            }

            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                for (int i=0;i<deniedPermissions.size();i++){
                    if (deniedPermissions.get(i).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && requestCode == 0){
                        showToast("录像失败");
                        break;
                    }else if (deniedPermissions.get(i).equals(RECORD_AUDIO) && requestCode == 1){
//                        doTalkBack();
                        break;
                    }
                }
            }
        });
    }

    private PopupWindow mSpitPopupWindow = null;
    private void openSpitPopWindow(View parent){
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.view_realplay_spit, null, true);
        final LinearLayout vrs_ll_4box = (LinearLayout) layoutView.findViewById(R.id.vrs_ll_4box);
        final LinearLayout vrs_ll_9box = (LinearLayout) layoutView.findViewById(R.id.vrs_ll_9box);
        final ImageView vrs_img_4box = (ImageView) layoutView.findViewById(R.id.vrs_img_4box);
        final TextView vrs_tv_4box = (TextView) layoutView.findViewById(R.id.vrs_tv_4box);
        final ImageView vrs_img_9box = (ImageView)layoutView.findViewById(R.id.vrs_img_9box);
        final TextView vrs_tv_9box = (TextView) layoutView.findViewById(R.id.vrs_tv_9box);

        if (m4BoxOr9Box!=null){
            if (m4BoxOr9Box){
                vrs_img_4box.setImageResource(R.mipmap.ic_nor_blue4box);
                vrs_tv_4box.setTextColor(getResources().getColor(R.color.theme_bule));
            }else{
                vrs_img_9box.setImageResource(R.mipmap.ic_nor_blue9box);
                vrs_tv_9box.setTextColor(getResources().getColor(R.color.theme_bule));
            }
        }
        vrs_ll_4box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //4分
//                stopVoiceTalk();
                //关闭声音
//                if (mLocalInfo.isSoundOpen()) {
//                    mLocalInfo.setSoundOpen(false);
//                    mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
//                    mEZPlayer.closeSound();
//                }
                if (mEzDeviceInfo==null || mEzDeviceInfo.size() == 0)
                    return;
                prepareMultPager(true);
                closeSpitPopupWindow();
                mRlPlace.setVisibility(GONE);
            }
        });
        vrs_ll_9box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //9分
//                stopVoiceTalk();
//                关闭声音
//                if (mLocalInfo.isSoundOpen()) {
//                    mLocalInfo.setSoundOpen(false);
//                    mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
//                    mEZPlayer.closeSound();
//                }
//                show9SpitLayout();
                if (mEzDeviceInfo==null || mEzDeviceInfo.size() == 0)
                    return;
                prepareMultPager(false);
                closeSpitPopupWindow();
                mRlPlace.setVisibility(GONE);
            }
        });

        mSpitPopupWindow = new PopupWindow(layoutView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mSpitPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        mSpitPopupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        mSpitPopupWindow.setFocusable(true);
        mSpitPopupWindow.setOutsideTouchable(true);
        mSpitPopupWindow.showAsDropDown(parent);
        mSpitPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mSpitPopupWindow = null;
                closeSpitPopupWindow();
            }
        });
        mSpitPopupWindow.update();
    }
    private void closeSpitPopupWindow() {
        if (mSpitPopupWindow != null) {
            dismissPopWindow(mSpitPopupWindow);
            mSpitPopupWindow = null;
        }
    }
    private void prepareMultPager(final boolean b4BoxOr9Box) {
        final VideoEntiryFragment fragment = getInTopVideo();
        if (fragment!=null){
            if (fragment.getIsRecord()){
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                builder.setTitle("提示");
                builder.setMessage("该操作会中断录像，是否继续？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m4BoxOr9Box = b4BoxOr9Box;
                        fragment.stopRealPlayRecord();
                        stopCurPlayer();
                        removeSpitPlayer();
                        mRlVideoPlay.removeView(mViewPagerSingle);
                        if (mRlVideoPlay.findViewById(R.id.av_viewpager_mult) == null)
                            mRlVideoPlay.addView(mViewPagerMult,0);
                        showSpitWindow();
                        mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
                    }
                });
                builder.show();
                return;
            }
        }
        m4BoxOr9Box = b4BoxOr9Box;
        stopCurPlayer();
        removeSpitPlayer();
        mRlVideoPlay.removeView(mViewPagerSingle);
        if (mRlVideoPlay.findViewById(R.id.av_viewpager_mult) == null)
            mRlVideoPlay.addView(mViewPagerMult,0);
        showSpitWindow();
        mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
    }

    private void openQaWindows(){
        final VideoEntiryFragment fragment = getInTopVideo();
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        builder.setTitle("选择码流");
        builder.setItems( new String[]{"高清","标清"}, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (fragment != null) {
                    switch (position) {
                        case 0:
                            //高清
                            fragment.setVideoQa(VideoEntiryFragment.QA_HD);
//                            mTvVideoQa.setText("高清");
                            dialog.dismiss();
                            break;
                        case 1:
                            //标清
                            fragment.setVideoQa(VideoEntiryFragment.QA_BA);
//                            mTvVideoQa.setText("标清");
                            dialog.dismiss();
                            break;
                    }
                }
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

//    private Message buildMessage(boolean bOpen){
//        Message message = new Message();
//        message.obj = bOpen;
//        message.what = bOpen ? 0 : 1;
//        return message;
//    }

    private void showSpitWindow() {
        buildMultViewPager();
        mViewPagerMult.setVisibility(View.VISIBLE);
    }

    private void stopCurPlayer() {
        VideoEntiryFragment fragment = getInTopVideo();
        if (fragment!=null){
            fragment.onSupportInvisible();
        }
    }

    private VideoEntiryFragment getInTopVideo(){
        if (mVideoEntiryFragments == null)
            return null;
        for (int i=0; i<mVideoEntiryFragments.length;i++){
            VideoEntiryFragment fragment = mVideoEntiryFragments[i];
            if (fragment.isInTop())
                return fragment;
        }
        return null;
    }

    public class MyOrientationDetector extends OrientationEventListener {

        private WindowManager mWindowManager;
        private int mLastOrientation = 0;

        public MyOrientationDetector(Context context) {
            super(context);
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        public boolean isWideScrren() {
            Display display = mWindowManager.getDefaultDisplay();
            Point pt = new Point();
            display.getSize(pt);
            return pt.x > pt.y;
        }
        @Override
        public void onOrientationChanged(int orientation) {
            int value = getCurentOrientationEx(orientation);
            if (value != mLastOrientation) {
                mLastOrientation = value;
                int current = getRequestedOrientation();
                if (current == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        || current == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }
        }

        private int getCurentOrientationEx(int orientation) {
            int value = 0;
            if (orientation >= 315 || orientation < 45) {
                // 0度
                value = 0;
                return value;
            }
            if (orientation >= 45 && orientation < 135) {
                // 90度
                value = 90;
                return value;
            }
            if (orientation >= 135 && orientation < 225) {
                // 180度
                value = 180;
                return value;
            }
            if (orientation >= 225 && orientation < 315) {
                // 270度
                value = 270;
                return value;
            }
            return value;
        }
    }

    public interface OnVideoSelect {
        void onVideoClick();
        void onVideoSelect(int pos, EZDeviceInfo bean);
        void onVideoPlayState(int state, EZUIError ezuiError);
        void onVideoVoiceControl(boolean bOpen);
        void onVideoRecordState(boolean bStart);
        void onVideoTalkBackState(boolean state, ErrorInfo errorInfo);
    }
}
