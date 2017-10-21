package cn.dianedun.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import com.ezvizuikit.open.EZUIKit;
import com.videogo.constant.Config;
import com.videogo.constant.Constant;
import com.videogo.constant.IntentConsts;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.MediaScanner;
import com.videogo.util.RotateViewUtil;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.widget.CheckTextButton;
import com.videogo.widget.CustomRect;
import com.videogo.widget.CustomTouchListener;
import com.videogo.widget.RingView;
import com.videogo.widget.TitleBar;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.api.ViseApi;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.adapter.SpitFragmentAdapter;
import cn.dianedun.base.BaseActivity;
import cn.dianedun.bean.AccesstokenBean;
import cn.dianedun.bean.CarmerListBean;
import cn.dianedun.fragment.SpitVideoFragment;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.AudioPlayUtil;

import cn.dianedun.tools.DataManager;
import cn.dianedun.tools.EZUtils;
import cn.dianedun.tools.ScreenOrientationHelper;
import cn.dianedun.tools.VerifyCodeInput;
import cn.dianedun.tools.ViewServer;
import cn.dianedun.view.DateTimeDialog;
import cn.dianedun.view.DateTimeDialogOnlyTime;
import cn.dianedun.view.DateTimeDialogOnlyYMD;
import cn.dianedun.view.EZUIPlayer.EZUIPlayer;
import cn.dianedun.view.SpitVideoPopView;
import cn.dianedun.view.WaitDialog;
import cn.dianedun.view.timeshaftbar.TimerShaftBar;

import static android.view.View.GONE;
import static cn.dianedun.tools.App.AppKey;
import static cn.dianedun.tools.App.getOpenSDK;

/**
 * Created by Administrator on 2017/9/16.
 */

public class VideoShowActivity extends BaseActivity  implements View.OnClickListener, SurfaceHolder.Callback,
        Handler.Callback, View.OnTouchListener, VerifyCodeInput.VerifyCodeInputListener,SpitVideoFragment.OnSpitVideoSelect,
        DateTimeDialogOnlyYMD.MyOnDateSetListener, DateTimeDialogOnlyTime.MyOnDateSetListener, DateTimeDialog.MyOnDateSetListener {
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

    @Bind(R.id.av_ll_set)
    LinearLayout mLLSet;

    @Bind(R.id.realplay_sv)
    SurfaceView mRealPlaySv;

    @Bind(R.id.av_tv_places)
    TextView mTvPlaces;

    @Bind(R.id.av_ctb_fullscreen)
    CheckTextButton mCtbFullscreen;

    @Bind(R.id.av_rl_bottomlayout)
    RelativeLayout mRlBottomLayout;

    @Bind(R.id.realplay_capture_rl)
    RelativeLayout mRealPlayCaptureRl;

    @Bind(R.id.realplay_full_ptz_prompt_iv)
    ImageView mRealPlayFullPtzPromptIv;

    @Bind(R.id.realplay_loading_rl)
    RelativeLayout mRealPlayLoadingRl;

    @Bind(R.id.realplay_tip_tv)
    TextView mRealPlayTipTv;

    @Bind(R.id.realplay_play_iv)
    ImageView mRealPlayPlayIv;

    @Bind(R.id.realplay_loading)
    ProgressBar mRealPlayPlayLoading;

    @Bind(R.id.realplay_privacy_ly)
    LinearLayout mRealPlayPlayPrivacyLy;

    @Bind(R.id.realplay_page_anim_iv)
    ImageView mPageAnimIv;

    @Bind(R.id.title_bar_landscape)
    TitleBar mLandscapeTitleBar;

    @Bind(R.id.av_ll_fccontainer)
    LinearLayout mLlFcContainer;

    @Bind(R.id.realplay_prompt_rl)
    RelativeLayout mRlPrompt;

    /**
     * //回放相关---------------
     */
//
//    @Bind(R.id.av_ezuiplayerback)
//    EZUIPlayer mEzPlayerBack;
//
//    @Bind(R.id.av_ll_playbackcontainer)
//    LinearLayout mLlPlayBackContainer;
//
//    @Bind(R.id.av_rl_playback_1_)
//    RelativeLayout mRlPlayback_1_;
//
//    @Bind(R.id.av_rl_playback_2_)
//    RelativeLayout mRlPlayback_2_;
//
//    @Bind(R.id.av_rl_playback_1)
//    RelativeLayout mRlPlayback_1;
//
//    @Bind(R.id.av_rl_playback_2)
//    RelativeLayout mRlPlayback_2;
//
//    @Bind(R.id.av_rl_playback_go)
//    RelativeLayout mRlPlayback_go;
//
//    @Bind(R.id.av_rl_playback_voice)
//    RelativeLayout mRlPlayBackVoice;
//
//    @Bind(R.id.av_rl_playback_takephoto)
//    RelativeLayout mRlPlayBackTakePhoto;
//
//    @Bind(R.id.av_rl_playback_video)
//    RelativeLayout mRlPlayBackVideo;
//
//    @Bind(R.id.av_rl_playback_stream)
//    RelativeLayout mRlPlayBackStream;
//
//    @Bind(R.id.av_timershaftbar)
//    TimerShaftBar mTimerShaftBar;

    private static final String TAG = "RealPlayerActivity";
    /**
     * 动画时间
     */
    private static final int ANIMATION_DURING_TIME = 500;

    public static final float BIG_SCREEN_RATIO = 1.60f;

    // UI消息
    public static final int MSG_PLAY_UI_UPDATE = 200;

    public static final int MSG_AUTO_START_PLAY = 202;

    public static final int MSG_CLOSE_PTZ_PROMPT = 203;

    public static final int MSG_HIDE_PTZ_DIRECTION = 204;

    public static final int MSG_HIDE_PAGE_ANIM = 205;

    public static final int MSG_PLAY_UI_REFRESH = 206;

    public static final int MSG_PREVIEW_START_PLAY = 207;

    public static final int MSG_SET_VEDIOMODE_SUCCESS = 105;

    /**
     * 设置视频质量成功
     */
    public static final int MSG_SET_VEDIOMODE_FAIL = 106;

    // 视频广场URL
    private String mRtspUrl = null;
    // 视频广场播放信息
    private RealPlaySquareInfo mRealPlaySquareInfo = null;
    protected RotateViewUtil mRecordRotateViewUtil;
    private AudioPlayUtil mAudioPlayUtil = null;
    private LocalInfo mLocalInfo = null;
    private Handler mHandler = null;
    private ScreenOrientationHelper mScreenOrientationHelper;
    private float mRealRatio = Constant.LIVE_VIEW_RATIO;
    /**
     * 标识是否正在播放
     */
    private int mStatus = RealPlayStatus.STATUS_INIT;
    private boolean mIsOnStop = false;
    /**
     * 屏幕当前方向
     */
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    private int mForceOrientation = 0;
    private Rect mRealPlayRect = null;



    /**
     * 演示点预览控制对象
     */
    private EZPlayer mEZPlayer = null;

    private EZConstants.EZVideoLevel mCurrentQulityMode = EZConstants.EZVideoLevel.VIDEO_LEVEL_HD;
    private EZDeviceInfo mDeviceInfo = null;
    private EZCameraInfo mCameraInfo = null;
    private String mVerifyCode;


    // 对讲模式
    private boolean mIsOnTalk = false;

    // 弱提示预览信息
    private long mStartTime = 0;
    private long mStopTime = 0;

    // 云台控制状态
    private float mZoomScale = 0;


    /**
     * 监听锁屏解锁的事件
     */
    private RealPlayBroadcastReceiver mBroadcastReceiver = null;
    /**
     * 定时器
     */
    private Timer mUpdateTimer = null;
    /**
     * 定时器执行的任务
     */
    private TimerTask mUpdateTimerTask = null;

    @Bind(R.id.realplay_capture_iv)
    ImageView mRealPlayCaptureIv;

    @Bind(R.id.realplay_capture_watermark_iv)
    ImageView mRealPlayCaptureWatermarkIv;

    @Bind(R.id.av_viewpager_spitscreen)
    ViewPager mViewPagerSpit;

    private RelativeLayout.LayoutParams mRealPlayCaptureRlLp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoshow);
//        ViewServer.get(this).addWindow(this);
    }

    @Bind(R.id.av_rl_videocontainer)
    RelativeLayout mRealPlayPlayRl;

    @Bind(R.id.av_fl_displaycontainer)
    FrameLayout mFlDisplayContainer;

    @Bind(R.id.realplay_ptz_direction_iv)
    ImageView mRealPlayPtzDirectionIv;

    @Bind(R.id.realplay_record_ly)
    LinearLayout mRealPlayRecordLy;

    @Bind(R.id.realplay_record_iv)
    ImageView mRealPlayRecordIv;

    @Bind(R.id.realplay_record_tv)
    TextView mRealPlayRecordTv;

    @Bind(R.id.realplay_ratio_tv)
    TextView mRealPlayRatioTv;

    @Bind(R.id.av_img_back)
    ImageView mImgBack;

    @Bind(R.id.av_img_spit)
    ImageView mImgSpit;

    private SurfaceHolder mRealPlaySh = null;
    private CustomTouchListener mRealPlayTouchListener = null;
    @Override
    protected void initView() {
        super.initView();

        initRealPlayPageLy();

        mRealPlayCaptureRlLp = (RelativeLayout.LayoutParams) mRealPlayCaptureRl.getLayoutParams();

        // 获取本地信息
        mAudioPlayUtil = AudioPlayUtil.getInstance(App.getInstance());
        // 获取配置信息操作对象
        mLocalInfo = LocalInfo.getInstance();
        // 获取屏幕参数
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mLocalInfo.setScreenWidthHeight(metric.widthPixels, metric.heightPixels);
        mLocalInfo.setNavigationBarHeight((int) Math.ceil(25 * getResources().getDisplayMetrics().density));
        mLocalInfo.setSoundOpen(false);
        mHandler = new Handler(this);
        mRecordRotateViewUtil = new RotateViewUtil();

        mBroadcastReceiver = new RealPlayBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mBroadcastReceiver, filter);

        mRealPlaySquareInfo = new RealPlaySquareInfo();
        Intent intent = getIntent();
        if (intent != null) {
            mCameraInfo = intent.getParcelableExtra(IntentConsts.EXTRA_CAMERA_INFO);
            mDeviceInfo = intent.getParcelableExtra(IntentConsts.EXTRA_DEVICE_INFO);
            mRtspUrl = intent.getStringExtra(IntentConsts.EXTRA_RTSP_URL);
            if (mCameraInfo != null) {
                mCurrentQulityMode = (mCameraInfo.getVideoLevel());
            }
            LogUtil.debugLog(TAG, "rtspUrl:" + mRtspUrl);

            getRealPlaySquareInfo();
        }
        if (mDeviceInfo != null && mDeviceInfo.getIsEncrypt() == 1) {
            mVerifyCode = DataManager.getInstance().getDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial());
        }
    }
    private void startZoom(float scale) {
        if (mEZPlayer == null) {
            return;
        }

        hideControlRlAndFullOperateBar(false);
        boolean preZoomIn = mZoomScale > 1.01 ? true : false;
        boolean zoomIn = scale > 1.01 ? true : false;
        if (mZoomScale != 0 && preZoomIn != zoomIn) {
            LogUtil.debugLog(TAG, "startZoom stop:" + mZoomScale);
            //            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
            //                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_STOP);
            mZoomScale = 0;
        }
        if (scale != 0 && (mZoomScale == 0 || preZoomIn != zoomIn)) {
            mZoomScale = scale;
            LogUtil.debugLog(TAG, "startZoom start:" + mZoomScale);
            //            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
            //                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_START);
        }
    }

    @Override
    public void onDateSet(Date date, int type) {
        Log.e("d","d");
        String url = "ezopen://open.ys7.com/";
        if (mDeviceInfo.getIsEncrypt() == 1){
            //加密
            url = "MDWBOZ@"+ url + mDeviceInfo.getDeviceSerial() + "/1.rec";
        }else{
            url += mDeviceInfo.getDeviceSerial() + "/1.rec";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        url += "?begin=" + calendar.get(Calendar.YEAR) + getMonth(calendar) + calendar.get(Calendar.DAY_OF_MONTH);

        PlayerBackActiviaty.startPlayBackActivity(this,mAccessTokenBean.getAccessToken(),url);
    }

    private String getMonth(Calendar calendar){
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10){
            return "0" + month;
        }
        return String.valueOf(month);
    }

    @Override
    public void onDateSet(Date date) {

    }

    private void stopZoom() {
        if (mEZPlayer == null) {
            return;
        }
        if (mZoomScale != 0) {
            LogUtil.debugLog(TAG, "stopZoom stop:" + mZoomScale);
            //            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
            //                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_STOP);
            mZoomScale = 0;
        }
    }
    private int mCaptureDisplaySec = 0;

    @Override
    protected void onResume() {
        super.onResume();
//        ViewServer.get(this).setFocusedWindow(this);
        if (m4BoxOr9Box != null){
            for (int i= 0;i < mSpitPlayerArr.size(); i++){
                EZUIPlayer ezuiPlayer = mSpitPlayerArr.get(i);
                if (ezuiPlayer!=null)
                    ezuiPlayer.resumePlay();
            }
        }else if (mEzDeviceInfo!=null && mDeviceInfo!=null && mEzDeviceInfo!=null){
            setDeviceUiInfo();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ViewServer.get(this).removeWindow(this);
        //防止黑色遮罩
        for (int i= 0;i < mSpitPlayerArr.size(); i++){
            EZUIPlayer ezuiPlayer = mSpitPlayerArr.get(i);
            if (ezuiPlayer!=null)
                ezuiPlayer.releasePlayer();
        }

        if (mSpitFragments!=null){
            for (int i=0; i<mSpitFragments.length;i++){
                mSpitFragments[i].releasePlay();
            }
        }

        closeSpitPopupWindow();

        if (mEZPlayer != null) {
            mEZPlayer.release();
        }

        mHandler.removeMessages(MSG_AUTO_START_PLAY);
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
        mHandler.removeMessages(MSG_HIDE_PAGE_ANIM);
        mHandler = null;

        if (mBroadcastReceiver != null) {
            // 取消锁屏广播的注册
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }

        mScreenOrientationHelper = null;
    }

    @Override
    public void onBackPressedSupport() {
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            mScreenOrientationHelper.portrait();
            return;
        }
        exit();
    }
    private void  exit(){
        closePtzPopupWindow();
        closeTalkPopupWindow(true, false);
        if (mStatus != RealPlayStatus.STATUS_STOP) {
            stopRealPlay();
            setRealPlayStopUI();
        }
        mHandler.removeMessages(MSG_AUTO_START_PLAY);
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
        mHandler.removeMessages(MSG_HIDE_PAGE_ANIM);
        if (mBroadcastReceiver != null) {
            // 取消锁屏广播的注册
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }

        if (mFlDisplayContainer.findViewById(R.id.av_viewpager_spitscreen) != null)
            mFlDisplayContainer.removeView(mViewPagerSpit);


        finish();
    }


    private void initCaptureUI() {
        mCaptureDisplaySec = 0;
        mRealPlayCaptureRl.setVisibility(GONE);
        mRealPlayCaptureIv.setImageURI(null);
        mRealPlayCaptureWatermarkIv.setTag(null);
        mRealPlayCaptureWatermarkIv.setVisibility(GONE);
    }

    private void setRealPlaySvLayout() {
        // 设置播放窗口位置
//        final int screenWidth = mLocalInfo.getScreenWidth();
//        final int screenHeight = (mOrientation == Configuration.ORIENTATION_PORTRAIT) ? (mLocalInfo.getScreenHeight() - mLocalInfo
//                .getNavigationBarHeight()) : mLocalInfo.getScreenHeight();
//        final RelativeLayout.LayoutParams realPlaySvlp = Utils.getPlayViewLp(mRealRatio, mOrientation,
//                mLocalInfo.getScreenWidth(), (int) (mLocalInfo.getScreenWidth() * Constant.LIVE_VIEW_RATIO),
//                screenWidth, screenHeight);
//
//        RelativeLayout.LayoutParams loadingR1Lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                realPlaySvlp.height);
//        //        loadingR1Lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        //        mRealPlayLoadingRl.setLayoutParams(loadingR1Lp);
//        //        mRealPlayPromptRl.setLayoutParams(loadingR1Lp);
//        RelativeLayout.LayoutParams svLp = new RelativeLayout.LayoutParams(realPlaySvlp.width, realPlaySvlp.height);
//        //mj svLp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mRealPlaySv.setLayoutParams(svLp);

//        if (mRtspUrl == null) {
////                        LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
////                                LinearLayout.LayoutParams.WRAP_CONTENT);
////                        realPlayPlayRlLp.gravity = Gravity.CENTER;
////                        mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
//        } else {
//            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.MATCH_PARENT);
//            realPlayPlayRlLp.gravity = Gravity.CENTER;
//            //realPlayPlayRlLp.weight = 1;
//            mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
//        }
////        mRealPlayTouchListener.setSacaleRect(Constant.MAX_SCALE, 0, 0, realPlaySvlp.width, realPlaySvlp.height);
        setPlayScaleUI(1, null, null);
    }

    // 播放比例
    private float mPlayScale = 1;
    private void setPlayScaleUI(float scale, CustomRect oRect, CustomRect curRect) {
        if (scale == 1) {
            if (mPlayScale == scale) {
                return;
            }
            mRealPlayRatioTv.setVisibility(GONE);
            try {
                if (mEZPlayer != null) {
                    mEZPlayer.setDisplayRegion(false, null, null);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (mPlayScale == scale) {
                try {
                    if (mEZPlayer != null) {
                        mEZPlayer.setDisplayRegion(true, oRect, curRect);
                    }
                } catch (BaseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            RelativeLayout.LayoutParams realPlayRatioTvLp = (RelativeLayout.LayoutParams) mRealPlayRatioTv
                    .getLayoutParams();
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 10), Utils.dip2px(this, 10), 0, 0);
            } else {
                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 70), Utils.dip2px(this, 20), 0, 0);
            }
            mRealPlayRatioTv.setLayoutParams(realPlayRatioTvLp);
            String sacleStr = String.valueOf(scale);
            mRealPlayRatioTv.setText(sacleStr.subSequence(0, Math.min(3, sacleStr.length())) + "X");
            mRealPlayRatioTv.setVisibility(GONE);
            hideControlRlAndFullOperateBar(false);
            try {
                if (mEZPlayer != null) {
                    mEZPlayer.setDisplayRegion(true, oRect, curRect);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mPlayScale = scale;
    }
    private void hideControlRlAndFullOperateBar(boolean excludeLandscapeTitle) {
        closeQualityPopupWindow();
        if (excludeLandscapeTitle && mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!mIsOnTalk && !mIsOnPtz) {
                mLandscapeTitleBar.setVisibility(View.VISIBLE);
            }
        } else {
            mLandscapeTitleBar.setVisibility(GONE);
        }
    }
    private PopupWindow mQualityPopupWindow = null;
    private void closeQualityPopupWindow() {
        if (mQualityPopupWindow != null) {
            dismissPopWindow(mQualityPopupWindow);
            mQualityPopupWindow = null;
        }
    }




    private void initOperateBarUI(boolean bigScreen) {

    }

    /**
     * 开始播放
     *
     * @see
     * @since V2.0
     */
    private void startRealPlay() {
        // 增加手机客户端操作信息记录
        LogUtil.debugLog(TAG, "startRealPlay");

        if (mStatus == RealPlayStatus.STATUS_START || mStatus == RealPlayStatus.STATUS_PLAY) {
            return;
        }

        // 检查网络是否可用
        if (!ConnectionDetector.isNetworkAvailable(this)) {
            // 提示没有连接网络
            setRealPlayFailUI("视频播放失败，请检查您的网络");
            return;
        }

        mStatus = RealPlayStatus.STATUS_START;
        setRealPlayLoadingUI();

        if (mCameraInfo != null) {
            if (mEZPlayer == null) {
                mEZPlayer = getOpenSDK().createPlayer(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo());
            }

            if (mEZPlayer == null)
                return;
            if (mDeviceInfo == null) {
                return;
            }
            if (mDeviceInfo.getIsEncrypt() == 1) {
                mEZPlayer.setPlayVerifyCode(DataManager.getInstance().getDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial()));
            }

            mEZPlayer.setHandler(mHandler);
            mEZPlayer.setSurfaceHold(mRealPlaySh);
            mEZPlayer.startRealPlay();
        } else if (mRtspUrl != null) {
            mEZPlayer = getOpenSDK().createPlayerWithUrl(mRtspUrl);
            //mStub.setCameraId(mCameraInfo.getCameraId());////****  mj
            if (mEZPlayer == null)
                return;
            mEZPlayer.setHandler(mHandler);
            mEZPlayer.setSurfaceHold(mRealPlaySh);

            mEZPlayer.startRealPlay();
        }
        updateLoadingProgress(0);
    }

    private void onGetDeviceInfoSucess(){
        if (setDeviceInfo()){
            setDeviceUiInfo();
        }
    }

    private void setDeviceUiInfo() {
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return;
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mRealPlaySv != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mRealPlaySv.getWindowToken(), 0);
                }
            }
        }, 200);

        initUI();
        mRealPlaySv.setVisibility(View.VISIBLE);

        LogUtil.infoLog(TAG, "onResume real play status:" + mStatus);
        if (mCameraInfo != null && mDeviceInfo != null &&  mDeviceInfo.getStatus() != 1) {
            if (mStatus != RealPlayStatus.STATUS_STOP) {
                stopRealPlay();
            }
            setRealPlayFailUI("设备不在线");
        } else {
            if (mStatus == RealPlayStatus.STATUS_INIT || mStatus == RealPlayStatus.STATUS_PAUSE
                    || mStatus == RealPlayStatus.STATUS_DECRYPT) {
                // 开始播放
                startRealPlay();
            }
        }
        mIsOnStop = false;
    }

    //默认显示列表第一个摄像头
    private boolean setDeviceInfo() {
        int spitId = getIntent().getIntExtra("spit",0);
        if (spitId >= 0){
            mDeviceInfo = mEzDeviceInfo.get(spitId);
            mCameraInfo = EZUtils.getCameraInfoFromDevice(mDeviceInfo, 0);
            return true;
        }else{
            //进入分屏
            if (spitId == -1){
                show4SpitLayout();
            }else{
                show9SpitLayout();
            }
            return false;
        }
    }

    private AccesstokenBean.DataBean mAccessTokenBean;
    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        ViseApi api = new ViseApi.Builder(this).build();
        HashMap hashMap = new HashMap();
        hashMap.put("departId",getIntent().getStringExtra("id"));
        api.apiPost(AppConfig.GETACCESSTOKEN,App.getInstance().getToken(),hashMap,false,new ApiCallback<AccesstokenBean.DataBean>(){
            @Override
            public void onNext(AccesstokenBean.DataBean dataBean) {
                mAccessTokenBean = dataBean;
                EZUIKit.setDebug(true);
                //appkey初始化
                EZUIKit.initWithAppKey(App.getInstance(),AppKey);
                //设置授权accesstoken
                EZUIKit.setAccessToken(mAccessTokenBean.getAccessToken());
                App.getOpenSDK().setAccessToken(mAccessTokenBean.getAccessToken());
                new GetCamersInfoListTask().execute();
            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                setRealPlayFailUI("暂无设备在线");
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }
        });
    }

    @Bind(R.id.av_img_sound)
    ImageView mRealPlaySoundBtn;

    // 初始化UI
    @SuppressWarnings("deprecation")
    private void initUI() {
//        mPageAnimDrawable = null;
//        mRealPlaySoundBtn.setVisibility(View.VISIBLE);

        if (mCameraInfo != null) {
//            mPortraitTitleBar.setTitle(mCameraInfo.getCameraName());
            mLandscapeTitleBar.setTitle(mCameraInfo.getCameraName());

//            setCameraInfoTiletRightBtn();

            if (mLocalInfo.isSoundOpen()) {
                mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_greenhorn);
//                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundon_btn_selector);
            } else {
                mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
//                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundoff_btn_selector);
            }
            mRealPlayFullPtzPromptIv.setVisibility(GONE);

            updateUI();
        } else if (mRtspUrl != null) {
            if (!TextUtils.isEmpty(mRealPlaySquareInfo.mCameraName)) {
//                mPortraitTitleBar.setTitle(mRealPlaySquareInfo.mCameraName);
                mLandscapeTitleBar.setTitle(mRealPlaySquareInfo.mCameraName);
            }
            mRealPlaySoundBtn.setVisibility(GONE);
//            mRealPlayQualityBtn.setVisibility(View.GONE);
        }

        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateOperatorUI();
        }
    }

    @Bind(R.id.av_ll_container)
    LinearLayout mLlRootContainer;

    ViewGroup.LayoutParams mSvLayoutParams;

    private void updateOperatorUI() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            // 显示状态栏
            fullScreen(false);
            updateOrientation();
            mRlTitleLayout.setVisibility(View.VISIBLE);
            mFlDisplayContainer.setLayoutParams(mSvLayoutParams);
        } else {
            // 隐藏状态栏
            fullScreen(true);
            mRlTitleLayout.setVisibility(GONE);
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            mFlDisplayContainer.setLayoutParams(realPlayPlayRlLp);
        }
        closeQualityPopupWindow();
        if (mStatus == RealPlayStatus.STATUS_START) {
            showControlRlAndFullOperateBar();
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

    private void updateUI() {
        //通过能力级设置
        setRealPlayTalkUI();

        setVideoLevel();
        {
//            mRealPlaySslBtnLy.setVisibility(View.GONE);
        }

//        if (getSupportPtz() == 1) {
////            mRealPlayPtzBtnLy.setVisibility(View.VISIBLE);
////            mRealPlayFullPtzBtn.setVisibility(View.VISIBLE);
//        } else {
//            //mRealPlayPtzBtnLy.setVisibility(View.GONE);
//            //mRealPlayFullPtzBtn.setVisibility(View.GONE);
////            mRealPlayPtzBtnLy.setEnabled(false);
////            mRealPlayFullPtzBtn.setEnabled(false);
//        }

//        updatePermissionUI();
    }
    private int getSupportPtz() {
        if (mEZPlayer == null || mDeviceInfo == null) {
            return 0;
        }

        if (mDeviceInfo.isSupportPTZ() || mDeviceInfo.isSupportZoom()) {
            return 1;
        } else {
            return 0;
        }
    }
    private void setVideoLevel() {
        if (mCameraInfo == null || mEZPlayer == null || mDeviceInfo == null) {
            return;
        }

        if (mDeviceInfo.getStatus() == 1) {
//            mRealPlayQualityBtn.setEnabled(true);
        } else {
//            mRealPlayQualityBtn.setEnabled(false);
        }

        /************** 本地数据保存 需要更新之前获取到的设备列表信息，开发者自己设置 *********************/
        mCameraInfo.setVideoLevel(mCurrentQulityMode.getVideoLevel());

        // 视频质量，2-高清，1-标清，0-流畅
        if (mCurrentQulityMode.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET.getVideoLevel()) {
//            mRealPlayQualityBtn.setText(R.string.quality_flunet);
        } else if (mCurrentQulityMode.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED.getVideoLevel()) {
//            mRealPlayQualityBtn.setText(R.string.quality_balanced);
        } else if (mCurrentQulityMode.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_HD.getVideoLevel()) {
//            mRealPlayQualityBtn.setText(R.string.quality_hd);
        }
    }

    private void setRealPlayTalkUI() {
        if (mEZPlayer != null && mDeviceInfo != null && (mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport)) {
//            mRealPlayTalkBtnLy.setVisibility(View.VISIBLE);
            if (mCameraInfo != null && mDeviceInfo.getStatus() == 1) {
                mLLTalkBack.setEnabled(true);
            } else {
                mLLTalkBack.setEnabled(false);
            }
//            if (mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport) {
//                mRealPlayFullTalkBtn.setVisibility(View.VISIBLE);
//            } else {
//                mRealPlayFullTalkBtn.setVisibility(View.GONE);
//            }
        } else {
//            mRealPlayTalkBtnLy.setVisibility(View.GONE);
//            mRealPlayFullTalkBtn.setVisibility(View.GONE);
        }
//        mRealPlayTalkBtnLy.setVisibility(View.VISIBLE);
        //mRealPlayTalkBtn.setEnabled(false);
    }

    private void updateLoadingProgress(final int progress) {
//        mRealPlayPlayLoading.setTag(Integer.valueOf(progress));
//        mRealPlayPlayLoading.setText(progress + "%");
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mRealPlayPlayLoading != null) {
//                    Integer tag = (Integer) mRealPlayPlayLoading.getTag();
//                    if (tag != null && tag.intValue() == progress) {
//                        Random r = new Random();
//                        mRealPlayPlayLoading.setText((progress + r.nextInt(20)) + "%");
//                    }
//                }
//            }
//        }, 500);
    }
    private void setRealPlayLoadingUI() {
        mStartTime = System.currentTimeMillis();
        mRealPlaySv.setVisibility(View.INVISIBLE);
        mRealPlaySv.setVisibility(View.VISIBLE);
        setStartloading();
//        mRealPlayBtn.setBackgroundResource(R.drawable.play_stop_selector);

        if (mCameraInfo != null  && mDeviceInfo != null) {
            mRealPlayCaptureBtn.setEnabled(false);
            mRealPlayRecordBtn.setEnabled(false);
            mLLVoice.setEnabled(false);
            mLLStream.setEnabled(false);
            mLLTalkBack.setEnabled(false);
            mLLControl.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
//                mRealPlayQualityBtn.setEnabled(true);
            } else {
//                mRealPlayQualityBtn.setEnabled(false);
            }
//            mRealPlayPtzBtn.setEnabled(false);

//            mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_stop_selector);
//            mRealPlayFullCaptureBtn.setEnabled(false);
//            mRealPlayFullRecordBtn.setEnabled(false);
//            mRealPlayFullFlowLy.setVisibility(View.GONE);
//            mRealPlayFullPtzBtn.setEnabled(false);
        }

        showControlRlAndFullOperateBar();
    }
    private int mControlDisplaySec = 0;
    private void showControlRlAndFullOperateBar() {
        if (mRtspUrl != null || mOrientation == Configuration.ORIENTATION_PORTRAIT) {
//            mRealPlayControlRl.setVisibility(View.VISIBLE);
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!mIsOnTalk && !mIsOnPtz) {
                    mLandscapeTitleBar.setVisibility(View.VISIBLE);
                }
            } else {
                mLandscapeTitleBar.setVisibility(GONE);
            }
            mControlDisplaySec = 0;
        } else {
            if (!mIsOnTalk && !mIsOnPtz) {
//                mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
                //                mFullscreenFullButton.setVisibility(View.VISIBLE);
                mLandscapeTitleBar.setVisibility(View.VISIBLE);
            }
            mControlDisplaySec = 0;
        }
    }
    private void setStartloading() {
        mRealPlayLoadingRl.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(GONE);
//        mImgReplay.setVisibility(View.GONE);
        mRealPlayPlayLoading.setVisibility(View.VISIBLE);
        mRealPlayPlayIv.setVisibility(GONE);
        mRealPlayPlayPrivacyLy.setVisibility(GONE);
    }
    private void setRealPlayFailUI(String errorStr) {
        mStopTime = System.currentTimeMillis();
        showType();

        stopUpdateTimer();
        updateOrientation();

        {
            setLoadingFail(errorStr);
        }
//        mRealPlayFullFlowLy.setVisibility(View.GONE);
//        mRealPlayBtn.setBackgroundResource(R.drawable.play_play_selector);

        hideControlRlAndFullOperateBar(true);

        if (mCameraInfo != null && mDeviceInfo != null) {
            closePtzPopupWindow();
            setFullPtzStopUI(false);

            mRealPlayCaptureBtn.setEnabled(false);
            mRealPlayRecordBtn.setEnabled(false);
            mLLVoice.setEnabled(false);
            mLLStream.setEnabled(false);
            mLLTalkBack.setEnabled(false);
            mLLControl.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1 && (mEZPlayer == null)) {
//                mRealPlayQualityBtn.setEnabled(true);
            } else {
//                mRealPlayQualityBtn.setEnabled(false);
            }
//            mRealPlayPtzBtn.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
//                mRealPlayPrivacyBtn.setEnabled(true);
//                mRealPlaySslBtn.setEnabled(true);
            } else {
//                mRealPlayPrivacyBtn.setEnabled(false);
//                mRealPlaySslBtn.setEnabled(false);
            }

//            mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_play_selector);
//            mRealPlayFullCaptureBtn.setEnabled(false);
//            mRealPlayFullRecordBtn.setEnabled(false);
//            mRealPlayFullPtzBtn.setEnabled(false);
        }
    }

    public void setLoadingFail(String errorStr) {
        mRealPlayLoadingRl.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setText(errorStr);
//        mImgReplay.setVisibility(View.VISIBLE);
        mRealPlayPlayLoading.setVisibility(GONE);
        mRealPlayPlayIv.setVisibility(GONE);
        mRealPlayPlayPrivacyLy.setVisibility(GONE);
    }

    /**
     * 这里对方法做描述
     *
     * @see
     * @since V1.8
     */
    private void showType() {
        if (Config.LOGGING && mEZPlayer != null) {
            Utils.showLog(VideoShowActivity.this, "getType " + ",取流耗时：" + (mStopTime - mStartTime));
        }
    }

    /**
     * 停止播放
     *
     * @see
     * @since V1.0
     */
    private void stopRealPlay() {
        LogUtil.debugLog(TAG, "stopRealPlay");
        mStatus = RealPlayStatus.STATUS_STOP;

        stopUpdateTimer();
        if (mEZPlayer != null) {
            stopRealPlayRecord();

            mEZPlayer.stopRealPlay();
        }
    }

    private void setRealPlayStopUI() {
        stopUpdateTimer();
        updateOrientation();
        setRealPlaySvLayout();
        setStopLoading();
        hideControlRlAndFullOperateBar(true);
//        mRealPlayBtn.setBackgroundResource(R.drawable.play_play_selector);
        if (mCameraInfo != null && mDeviceInfo != null) {
            closePtzPopupWindow();
            setFullPtzStopUI(false);
            mRealPlayCaptureBtn.setEnabled(false);
            mRealPlayRecordBtn.setEnabled(false);
            mLLVoice.setEnabled(false);
            mLLStream.setEnabled(false);
            mLLTalkBack.setEnabled(false);
            mLLControl.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
//                mRealPlayQualityBtn.setEnabled(true);
            } else {
//                mRealPlayQualityBtn.setEnabled(false);
            }
//            mRealPlayFullPtzBtn.setEnabled(false);
            if (mDeviceInfo.getStatus() == 1) {
//                mRealPlayPrivacyBtn.setEnabled(true);
//                mRealPlaySslBtn.setEnabled(true);
            } else {
//                mRealPlayPrivacyBtn.setEnabled(false);
//                mRealPlaySslBtn.setEnabled(false);
            }

//            mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_play_selector);
//            mRealPlayFullCaptureBtn.setEnabled(false);
//            mRealPlayFullRecordBtn.setEnabled(false);
//            mRealPlayPtzBtn.setEnabled(false);
        }
    }

    private void setFullPtzStopUI(boolean startAnim) {
        mIsOnPtz = false;
//        if (startAnim) {
////            mRealPlayFullPtzAnimBtn.setVisibility(View.GONE);
////            mFullscreenFullButton.setVisibility(View.GONE);
////            mRealPlayFullAnimBtn.setBackgroundResource(R.drawable.yuntai_pressed);
////            startFullBtnAnim(mRealPlayFullAnimBtn, mEndXy, mStartXy, new Animation.AnimationListener() {
////
////                @Override
////                public void onAnimationStart(Animation animation) {
////                }
////
////                @Override
////                public void onAnimationRepeat(Animation animation) {
////                }
////
////                @Override
////                public void onAnimationEnd(Animation animation) {
////                    mRealPlayFullAnimBtn.setVisibility(View.GONE);
////                    onRealPlaySvClick();
////                }
////            });
//        } else {
////            mRealPlayFullPtzAnimBtn.setVisibility(View.GONE);
////            mFullscreenFullButton.setVisibility(View.GONE);
//        }
        mRealPlayFullPtzPromptIv.setVisibility(GONE);
        mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
    }

    public void setStopLoading() {
        mRealPlayLoadingRl.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(GONE);
        mRealPlayPlayLoading.setVisibility(GONE);
        mRealPlayPlayIv.setVisibility(View.VISIBLE);
        mRealPlayPlayPrivacyLy.setVisibility(GONE);
    }

    private void updateOrientation() {
        if (mIsOnTalk) {
            if (mEZPlayer != null && mDeviceInfo != null && mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport) {
                setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else {
                setForceOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            if (mStatus == RealPlayStatus.STATUS_PLAY) {
                setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else {
                if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }
        }
    }

    private void setOrientation(int sensor) {
        if (mForceOrientation != 0) {
            LogUtil.debugLog(TAG, "setOrientation mForceOrientation:" + mForceOrientation);
            return;
        }

        if (mScreenOrientationHelper==null) return;
        if (sensor == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            mScreenOrientationHelper.enableSensorOrientation();
        else
            mScreenOrientationHelper.disableSensorOrientation();
    }

    @Override
    public void onVideoSelect(EZDeviceInfo deviceInfo) {
        m4BoxOr9Box = null;
        removeSpitPlayer();
        closeSpitPopupWindow();
        mFlDisplayContainer.removeView(mViewPagerSpit);
        mFlDisplayContainer.addView(mRealPlayPlayRl);
        mDeviceInfo = deviceInfo;
        mCameraInfo = EZUtils.getCameraInfoFromDevice(mDeviceInfo, 0);
        mEZPlayer = getOpenSDK().createPlayer(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo());
        startRealPlay();
    }

    /**
     * 停止定时器
     *
     * @see
     * @since V1.0
     */
    private void stopUpdateTimer() {
        mCaptureDisplaySec = 4;
        updateCaptureUI();
        if (mHandler!=null)
            mHandler.removeMessages(MSG_PLAY_UI_UPDATE);
        // 停止录像计时
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }

        if (mUpdateTimerTask != null) {
            mUpdateTimerTask.cancel();
            mUpdateTimerTask = null;
        }
    }

    // 更新抓图/录像显示UI
    private void updateCaptureUI() {
        if (mRealPlayCaptureRl.getVisibility() == View.VISIBLE) {
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                mRealPlayCaptureRlLp.setMargins(0, 0, 0, Utils.dip2px(this, 40));
                mRealPlayCaptureRl.setLayoutParams(mRealPlayCaptureRlLp);
            } else {
                RelativeLayout.LayoutParams realPlayCaptureRlLp = new RelativeLayout.LayoutParams(
                        Utils.dip2px(this, 65), Utils.dip2px(this, 45));
                realPlayCaptureRlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                realPlayCaptureRlLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mRealPlayCaptureRl.setLayoutParams(realPlayCaptureRlLp);
            }
            if (mRealPlayCaptureWatermarkIv.getTag() != null) {
                mRealPlayCaptureWatermarkIv.setVisibility(View.VISIBLE);
                mRealPlayCaptureWatermarkIv.setTag(null);
            }
        }
        if (mCaptureDisplaySec >= 4) {
            initCaptureUI();
        }
    }



    @Override
    protected void initData() {
        super.initData();
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        initTitleBar();
//        initRealPlayPageLy();
//        initLoadingUI();

//        mRealPlayPlayRl = (RelativeLayout) findViewById(R.id.realplay_play_rl);
//        mRealPlaySv = (SurfaceView) findViewById(R.id.realplay_sv);
        mRealPlaySh = mRealPlaySv.getHolder();
        mRealPlaySh.addCallback(this);
        mRealPlayTouchListener = new CustomTouchListener() {

            @Override
            public boolean canZoom(float scale) {
                if (mStatus == RealPlayStatus.STATUS_PLAY) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean canDrag(int direction) {
                if (mStatus != RealPlayStatus.STATUS_PLAY) {
                    return false;
                }
                if (mEZPlayer != null && mDeviceInfo != null) {
                    // 出界判断
                    if (DRAG_LEFT == direction || DRAG_RIGHT == direction) {
                        // 左移/右移出界判断
                        if (mDeviceInfo.isSupportPTZ()) {
                            return true;
                        }
                    } else if (DRAG_UP == direction || DRAG_DOWN == direction) {
                        // 上移/下移出界判断
                        if (mDeviceInfo.isSupportPTZ()) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void onSingleClick() {
                onRealPlaySvClick();
            }

            @Override
            public void onDoubleClick(MotionEvent e) {
            }

            @Override
            public void onZoom(float scale) {
                LogUtil.debugLog(TAG, "onZoom:" + scale);
                if (mEZPlayer != null && mDeviceInfo != null &&  mDeviceInfo.isSupportZoom()) {
                    startZoom(scale);
                }
            }

            @Override
            public void onDrag(int direction, float distance, float rate) {
                LogUtil.debugLog(TAG, "onDrag:" + direction);
                if (mEZPlayer != null) {
                    //Utils.showLog(RealPlayActivity.this, "onDrag rate:" + rate);
//                    startDrag(direction, distance, rate);
                }
            }

            @Override
            public void onEnd(int mode) {
                LogUtil.debugLog(TAG, "onEnd:" + mode);
                if (mEZPlayer != null) {
//                    stopDrag(false);
                }
                if (mEZPlayer != null && mDeviceInfo != null && mDeviceInfo.isSupportZoom()) {
                    stopZoom();
                }
            }

            @Override
            public void onZoomChange(float scale, CustomRect oRect, CustomRect curRect) {
                LogUtil.debugLog(TAG, "onZoomChange:" + scale);
                if (mEZPlayer != null && mDeviceInfo != null && mDeviceInfo.isSupportZoom()) {
                    //采用云台调焦
                    return;
                }
                if (mStatus == RealPlayStatus.STATUS_PLAY) {
                    if (scale > 1.0f && scale < 1.1f) {
                        scale = 1.1f;
                    }
                    setPlayScaleUI(scale, oRect, curRect);
                }
            }
        };

        mSvLayoutParams = mFlDisplayContainer.getLayoutParams();
        initCaptureUI();
        mScreenOrientationHelper = new ScreenOrientationHelper(this, mCtbFullscreen, /*mFullscreenFullButton*/null);

        mWaitDialog = new WaitDialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        mWaitDialog.setCancelable(false);
    }
    /**
     * screen状态广播接收者
     */
    private class RealPlayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                closePtzPopupWindow();
                closeTalkPopupWindow(true, false);
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    mStatus = RealPlayStatus.STATUS_PAUSE;
                    setRealPlayStopUI();
                }
            }
        }
    }


    private PopupWindow mTalkPopupWindow = null;
    private RingView mTalkRingView = null;
    private Button mTalkBackControlBtn = null;

    private WaitDialog mWaitDialog = null;
    private void closeTalkPopupWindow(boolean stopTalk, boolean startAnim) {
        if (mTalkPopupWindow != null) {
            LogUtil.infoLog(TAG, "closeTalkPopupWindow");
            dismissPopWindow(mTalkPopupWindow);
            mTalkPopupWindow = null;
        }
        mTalkRingView = null;
        if (stopTalk)
            stopVoiceTalk();
    }

    private int[] mStartXy = new int[2];
    private int[] mEndXy = new int[2];
    private void handleVoiceTalkStoped() {
        if (mIsOnTalk) {
            showToast("对讲结束");
            mImgTalkBack.setImageResource(R.mipmap.ic_nor_voice);
            mIsOnTalk = false;
            setForceOrientation(0);
        }
//        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            if (startAnim) {
//                mRealPlayFullTalkAnimBtn.setVisibility(View.GONE);
//                mFullscreenFullButton.setVisibility(View.GONE);
//                mRealPlayFullAnimBtn.setBackgroundResource(R.drawable.speech_1);
//                startFullBtnAnim(mRealPlayFullAnimBtn, mEndXy, mStartXy, new Animation.AnimationListener() {
//
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        mRealPlayFullAnimBtn.setVisibility(View.GONE);
//                        onRealPlaySvClick();
//                    }
//                });
//            } else {
//                mRealPlayFullTalkAnimBtn.setVisibility(View.GONE);
//                mFullscreenFullButton.setVisibility(View.GONE);
//            }
//        }
        mLLTalkBack.setEnabled(true);
//        mRealPlayFullTalkBtn.setEnabled(true);
//        mRealPlayFullTalkAnimBtn.setEnabled(true);

        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            if (mEZPlayer != null) {
                if (mLocalInfo.isSoundOpen()) {
                    mEZPlayer.openSound();
                } else {
                    mEZPlayer.closeSound();
                }
            }
        }
    }

    private void onRealPlaySvClick() {
        if (mCameraInfo != null && mEZPlayer != null && mDeviceInfo != null) {
            if (mDeviceInfo.getStatus() != 1) {
                return;
            }
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                setRealPlayControlRlVisibility();
            } else {
                setRealPlayFullOperateBarVisibility();
            }
        } else if (mRtspUrl != null) {
            setRealPlayControlRlVisibility();
        }
    }
    private void setRealPlayControlRlVisibility() {
        if (mLandscapeTitleBar.getVisibility() == View.VISIBLE){
//                || mRealPlayControlRl.getVisibility() == View.VISIBLE) {
            //            mRealPlayControlRl.setVisibility(View.GONE);
            mLandscapeTitleBar.setVisibility(GONE);
            closeQualityPopupWindow();
        } else {
//            mRealPlayControlRl.setVisibility(View.VISIBLE);
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!mIsOnTalk && !mIsOnPtz) {
                    mLandscapeTitleBar.setVisibility(View.VISIBLE);
                }
            } else {
                mLandscapeTitleBar.setVisibility(GONE);
            }
            mControlDisplaySec = 0;
        }
    }
    private void setRealPlayFullOperateBarVisibility() {
        if (mLandscapeTitleBar.getVisibility() == View.VISIBLE) {
//            mRealPlayFullOperateBar.setVisibility(View.GONE);
//            if (!mIsOnTalk && !mIsOnPtz) {
//                mFullscreenFullButton.setVisibility(View.GONE);
//            }
            mLandscapeTitleBar.setVisibility(GONE);
        } else {
            if (!mIsOnTalk && !mIsOnPtz) {
                //mj mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
                //                mFullscreenFullButton.setVisibility(View.VISIBLE);
                mLandscapeTitleBar.setVisibility(View.VISIBLE);
            }
            mControlDisplaySec = 0;
        }
    }

    private void startFullBtnAnim(final View animView, final int[] startXy, final int[] endXy,
                                  final Animation.AnimationListener animationListener) {
        animView.setVisibility(View.VISIBLE);
        TranslateAnimation anim = new TranslateAnimation(startXy[0], endXy[0], startXy[1], endXy[1]);
        anim.setAnimationListener(animationListener);
        anim.setDuration(ANIMATION_DURING_TIME);
        animView.startAnimation(anim);
    }
    private boolean mIsOnPtz = false;
    private PopupWindow mPtzPopupWindow = null;
    private PopupWindow mSpitPopupWindow = null;
    private LinearLayout mPtzControlLy = null;

    public void setForceOrientation(int orientation) {
        if (mForceOrientation == orientation) {
            LogUtil.debugLog(TAG, "setForceOrientation no change");
            return;
        }
        mForceOrientation = orientation;
        if (mForceOrientation != 0) {
            if (mForceOrientation != mOrientation) {
                if (mForceOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    mScreenOrientationHelper.portrait();
                } else {
                    mScreenOrientationHelper.landscape();
                }
            }
            mScreenOrientationHelper.disableSensorOrientation();
        } else {
            updateOrientation();
        }
    }
    private void dismissPopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && !isFinishing()) {
            try {
                popupWindow.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
    private void getRealPlaySquareInfo() {
        if (TextUtils.isEmpty(mRtspUrl)) {
            return;
        }

        Uri uri = Uri.parse(mRtspUrl.replaceFirst("&", "?"));
        try {
            mRealPlaySquareInfo.mSquareId = Integer.parseInt(uri.getQueryParameter("squareid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            mRealPlaySquareInfo.mChannelNo = Integer.parseInt(Utils.getUrlValue(mRtspUrl, "channelno=", "&"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mRealPlaySquareInfo.mCameraName = uri.getQueryParameter("cameraname");
        try {
            mRealPlaySquareInfo.mSoundType = Integer.parseInt(uri.getQueryParameter("soundtype"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        mRealPlaySquareInfo.mCoverUrl = uri.getQueryParameter("md5Serial");
        if (!TextUtils.isEmpty(mRealPlaySquareInfo.mCoverUrl)) {
            mRealPlaySquareInfo.mCoverUrl = mLocalInfo.getServAddr() + mRealPlaySquareInfo.mCoverUrl + "_mobile.jpeg";
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        // LogUtil.infoLog(TAG, "handleMessage:" + msg.what);
        if (this.isFinishing()) {
            return false;
        }
        switch (msg.what) {
            case EZConstants.EZRealPlayConstants.MSG_GET_CAMERA_INFO_SUCCESS:
                updateLoadingProgress(20);
                handleGetCameraInfoSuccess();
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_START:
                updateLoadingProgress(40);
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_CONNECTION_START:
                updateLoadingProgress(60);
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_CONNECTION_SUCCESS:
                updateLoadingProgress(80);
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                handlePlaySuccess(msg);
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                handlePlayFail(msg.obj);
                break;
            case EZConstants.EZRealPlayConstants.MSG_SET_VEDIOMODE_SUCCESS:
                handleSetVedioModeSuccess();
                break;
            case EZConstants.EZRealPlayConstants.MSG_SET_VEDIOMODE_FAIL:
                handleSetVedioModeFail(msg.arg1);
                break;
            case EZConstants.EZRealPlayConstants.MSG_PTZ_SET_FAIL:
                handlePtzControlFail(msg);
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_VOICETALK_SUCCESS:
                handleVoiceTalkSucceed();
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_VOICETALK_STOP:
                handleVoiceTalkStoped();
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_VOICETALK_FAIL:
                ErrorInfo errorInfo = (ErrorInfo) msg.obj;
                handleVoiceTalkFailed(errorInfo);
                break;
            case MSG_PLAY_UI_UPDATE:
                updateRealPlayUI();
                break;
            case MSG_AUTO_START_PLAY:
                startRealPlay();
                break;
            case MSG_CLOSE_PTZ_PROMPT:
                mRealPlayFullPtzPromptIv.setVisibility(GONE);
                break;
            case MSG_HIDE_PTZ_DIRECTION:
                handleHidePtzDirection(msg);
                break;
            case MSG_HIDE_PAGE_ANIM:
                hidePageAnim();
                break;
            case MSG_PLAY_UI_REFRESH:
                initUI();
                break;
            case MSG_PREVIEW_START_PLAY:
                mPageAnimIv.setVisibility(GONE);
//                mRealPlayPreviewTv.setVisibility(View.GONE);
                mStatus = RealPlayStatus.STATUS_INIT;
                startRealPlay();
                break;
            default:
                break;
        }
        return false;
    }
    private void handleHidePtzDirection(Message msg) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        if (msg.arg1 > 2) {
            mRealPlayPtzDirectionIv.setVisibility(GONE);
        } else {
            mRealPlayPtzDirectionIv.setVisibility(msg.arg1 == 1 ? GONE : View.VISIBLE);
            Message message = new Message();
            message.what = MSG_HIDE_PTZ_DIRECTION;
            message.arg1 = msg.arg1 + 1;
            mHandler.sendMessageDelayed(message, 500);
        }
    }
    private void updateRealPlayUI() {
        if (mControlDisplaySec == 5) {
            mControlDisplaySec = 0;
            hideControlRlAndFullOperateBar(false);
        }
//        checkRealPlayFlow();
        updateCaptureUI();

        if (mIsRecording) {
            updateRecordTime();
        }
    }

    /**
     * 更新录像时间
     *
     * @see
     * @since V1.0
     */
    /**
     * 录像时间
     */
    private int mRecordSecond = 0;
//    /**
//     * 更新流量统计
//     *
//     * @see
//     * @since V1.0
//     */
//    private void checkRealPlayFlow() {
//        if ((mEZPlayer != null && mRealPlayFlowTv.getVisibility() == View.VISIBLE)) {
//            // 更新流量数据
//            long streamFlow = mEZPlayer.getStreamFlow();
//            updateRealPlayFlowTv(streamFlow);
//        }
//    }
    private void handleVoiceTalkFailed(ErrorInfo errorInfo) {
        LogUtil.debugLog(TAG, "Talkback failed. " + errorInfo.toString());

        closeTalkPopupWindow(true, false);

        switch (errorInfo.errorCode) {
            case ErrorCode.ERROR_TRANSF_DEVICE_TALKING:
                Utils.showToast(VideoShowActivity.this, R.string.realplay_play_talkback_fail_ison);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_PRIVACYON:
                Utils.showToast(VideoShowActivity.this, R.string.realplay_play_talkback_fail_privacy);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_OFFLINE:
                Utils.showToast(VideoShowActivity.this, R.string.realplay_fail_device_not_exist);
                break;
            case ErrorCode.ERROR_TTS_MSG_REQ_TIMEOUT:
            case ErrorCode.ERROR_TTS_MSG_SVR_HANDLE_TIMEOUT:
            case ErrorCode.ERROR_TTS_WAIT_TIMEOUT:
            case ErrorCode.ERROR_TTS_HNADLE_TIMEOUT:
                Utils.showToast(VideoShowActivity.this, R.string.realplay_play_talkback_request_timeout, errorInfo.errorCode);
                break;
            case ErrorCode.ERROR_CAS_AUDIO_SOCKET_ERROR:
            case ErrorCode.ERROR_CAS_AUDIO_RECV_ERROR:
            case ErrorCode.ERROR_CAS_AUDIO_SEND_ERROR:
                Utils.showToast(VideoShowActivity.this, R.string.realplay_play_talkback_network_exception, errorInfo.errorCode);
                break;
            default:
                Utils.showToast(VideoShowActivity.this, R.string.realplay_play_talkback_fail, errorInfo.errorCode);
                break;
        }
    }
    private void handleVoiceTalkSucceed() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            openTalkPopupWindow(true);
        } else {
//            mRealPlayFullTalkAnimBtn.setVisibility(View.VISIBLE);
            //            mFullscreenFullButton.setVisibility(View.VISIBLE);
//            ((AnimationDrawable) mRealPlayFullTalkAnimBtn.getBackground()).start();
        }

        mLLTalkBack.setEnabled(true);
//        mRealPlayFullTalkBtn.setEnabled(true);
//        mRealPlayFullTalkAnimBtn.setEnabled(true);
    }
    private void handlePtzControlFail(Message msg) {
        LogUtil.debugLog(TAG, "handlePtzControlFail:" + msg.arg1);
        switch (msg.arg1) {
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_CALLING_PRESET_FAILED:// 正在调用预置点，键控动作无效
                Utils.showToast(VideoShowActivity.this, R.string.camera_lens_too_busy, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_PRESETING_FAILE:// 当前正在调用预置点
                Utils.showToast(VideoShowActivity.this, R.string.ptz_is_preseting, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_TIMEOUT_SOUND_LACALIZATION_FAILED:// 当前正在声源定位
                break;
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_TIMEOUT_CRUISE_TRACK_FAILED:// 键控动作超时(当前正在轨迹巡航)
                Utils.showToast(VideoShowActivity.this, R.string.ptz_control_timeout_cruise_track_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_INVALID_POSITION_FAILED:// 当前预置点信息无效
                Utils.showToast(VideoShowActivity.this, R.string.ptz_preset_invalid_position_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_CURRENT_POSITION_FAILED:// 该预置点已是当前位置
                Utils.showToast(VideoShowActivity.this, R.string.ptz_preset_current_position_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_SOUND_LOCALIZATION_FAILED:// 设备正在响应本次声源定位
                Utils.showToast(VideoShowActivity.this, R.string.ptz_preset_sound_localization_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_OPENING_PRIVACY_FAILED:// 当前正在开启隐私遮蔽
            case ErrorCode.ERROR_CAS_PTZ_CLOSING_PRIVACY_FAILED:// 当前正在关闭隐私遮蔽
            case ErrorCode.ERROR_CAS_PTZ_MIRRORING_FAILED:// 设备正在镜像操作（设备镜像要几秒钟，防止频繁镜像操作）
                Utils.showToast(VideoShowActivity.this, R.string.ptz_operation_too_frequently, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_CONTROLING_FAILED:// 设备正在键控动作（上下左右）(一个客户端在上下左右控制，另外一个在开其它东西)
                break;
            case ErrorCode.ERROR_CAS_PTZ_FAILED:// 云台当前操作失败
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_EXCEED_MAXNUM_FAILED:// 当前预置点超过最大个数
                Utils.showToast(VideoShowActivity.this, R.string.ptz_preset_exceed_maxnum_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRIVACYING_FAILED:// 设备处于隐私遮蔽状态（关闭了镜头，再去操作云台相关）
                Utils.showToast(VideoShowActivity.this, R.string.ptz_privacying_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_TTSING_FAILED:// 设备处于语音对讲状态(区别以前的语音对讲错误码，云台单独列一个）
                Utils.showToast(VideoShowActivity.this, R.string.ptz_mirroring_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_UP_LIMIT_FAILED:// 设备云台旋转到达上限位
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_DOWN_LIMIT_FAILED:// 设备云台旋转到达下限位
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_LEFT_LIMIT_FAILED:// 设备云台旋转到达左限位
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_RIGHT_LIMIT_FAILED:// 设备云台旋转到达右限位
                setPtzDirectionIv(-1, msg.arg1);
                break;
            default:
                Utils.showToast(VideoShowActivity.this, R.string.ptz_operation_failed, msg.arg1);
                break;
        }
    }
    private void setPtzDirectionIv(int command) {
        setPtzDirectionIv(command, 0);
    }

    private void setPtzDirectionIv(int command, int errorCode) {
        if (command != -1 && errorCode == 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            switch (command) {
                case RealPlayStatus.PTZ_LEFT:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.left_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_RIGHT:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.right_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_UP:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.up_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_DOWN:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.down_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.realplay_sv);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mRealPlayPtzDirectionIv.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);
        } else if (errorCode != 0) {
            RelativeLayout.LayoutParams svParams = (RelativeLayout.LayoutParams) mRealPlaySv.getLayoutParams();
            RelativeLayout.LayoutParams params = null;
            switch (errorCode) {
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_LEFT_LIMIT_FAILED:
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, svParams.height);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_left_limit);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_RIGHT_LIMIT_FAILED:
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, svParams.height);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_right_limit);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_UP_LIMIT_FAILED:
                    params = new RelativeLayout.LayoutParams(svParams.width, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_top_limit);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_DOWN_LIMIT_FAILED:
                    params = new RelativeLayout.LayoutParams(svParams.width, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_bottom_limit);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.realplay_sv);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mRealPlayPtzDirectionIv.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);
        } else {
            mRealPlayPtzDirectionIv.setVisibility(GONE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        }
    }

    private void handleSetVedioModeFail(int errorCode) {
        closeQualityPopupWindow();
        setVideoLevel();
        try {
            mWaitDialog.setWaitText(null);
            mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.showToast(VideoShowActivity.this, R.string.realplay_set_vediomode_fail, errorCode);
    }
    private void handleSetVedioModeSuccess() {
        closeQualityPopupWindow();
        setVideoLevel();
        try {
            mWaitDialog.setWaitText(null);
            mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            // 停止播放
            stopRealPlay();
            //下面语句防止stopRealPlay线程还没释放surface, startRealPlay线程已经开始使用surface
            //因此需要等待500ms
            SystemClock.sleep(500);
            // 开始播放
            startRealPlay();
        }
    }
    /**
     * 处理播放失败的情况
     *
     * @param obj - 错误码
     * @see
     * @since V1.0
     */
    private void handlePlayFail(Object obj) {
        int errorCode = 0;
        if (obj != null) {
            ErrorInfo errorInfo = (ErrorInfo) obj;
            errorCode = errorInfo.errorCode;
            LogUtil.debugLog(TAG, "handlePlayFail:" + errorInfo.errorCode);
        }


        hidePageAnim();

        stopRealPlay();

        updateRealPlayFailUI(errorCode);
    }
    private void updateRealPlayFailUI(int errorCode) {
        String txt = null;
        LogUtil.i(TAG, "updateRealPlayFailUI: errorCode:" + errorCode);
        // 判断返回的错误码
        switch (errorCode) {
            case ErrorCode.ERROR_TRANSF_ACCESSTOKEN_ERROR:
                startActivity(LoginActivity.class);
                showToast("请重新登录");
//                ActivityUtils.goToLoginAgain(EZRealPlayActivity.this);
                return;
            case ErrorCode.ERROR_CAS_MSG_PU_NO_RESOURCE:
                txt = getString(R.string.remoteplayback_over_link);
                break;
            case ErrorCode.ERROR_TRANSF_DEVICE_OFFLINE:
                if (mCameraInfo != null) {
                    mCameraInfo.setIsShared(0);
                }
                txt = getString(R.string.realplay_fail_device_not_exist);
                break;
            case ErrorCode.ERROR_INNER_STREAM_TIMEOUT:
                txt = getString(R.string.realplay_fail_connect_device);
                break;
            case ErrorCode.ERROR_WEB_CODE_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_LOGIN, this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;
            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_OP_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_HARDWARE, this, null);
//                SecureValidate.secureValidateDialog(this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;
            case ErrorCode.ERROR_TRANSF_TERMINAL_BINDING:
                txt = "请在萤石客户端关闭终端绑定";
                break;
            // 收到这两个错误码，可以弹出对话框，让用户输入密码后，重新取流预览
            case ErrorCode.ERROR_INNER_VERIFYCODE_NEED:
            case ErrorCode.ERROR_INNER_VERIFYCODE_ERROR: {
                DataManager.getInstance().setDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial(), null);
                VerifyCodeInput.VerifyCodeInputDialog(this, this).show();
            }
            break;
            case ErrorCode.ERROR_EXTRA_SQUARE_NO_SHARING:
            default:
                txt = Utils.getErrorTip(this, R.string.realplay_play_fail, errorCode);
                break;
        }

        if (!TextUtils.isEmpty(txt)) {
            setRealPlayFailUI(txt);
        } else {
            setRealPlayStopUI();
        }
    }
    private void hidePageAnim() {
        if (mHandler != null)
            mHandler.removeMessages(MSG_HIDE_PAGE_ANIM);
//        if (mPageAnimDrawable != null) {
//            if (mPageAnimDrawable.isRunning()) {
//                mPageAnimDrawable.stop();
//            }
//            mPageAnimDrawable = null;
//            mPageAnimIv.setBackgroundDrawable(null);
//            mPageAnimIv.setVisibility(View.GONE);
//        }
        if (mPageAnimIv != null) {
            mPageAnimIv.setBackgroundDrawable(null);
            mPageAnimIv.setVisibility(GONE);
        }
    }
    /**
     * 处理播放成功的情况
     *
     * @see
     * @since V1.0
     */
    private void handlePlaySuccess(Message msg) {
        LogUtil.d(TAG,"handlePlaySuccess");
        mStatus = RealPlayStatus.STATUS_PLAY;

        // 声音处理
        setRealPlaySound();

        // temp solution for OPENSDK-92
        // Android 预览3Q10的时候切到流畅之后 视频播放窗口变大了
        //        if (msg.arg1 != 0) {
        //            mRealRatio = (float) msg.arg2 / msg.arg1;
        //        } else {
        //            mRealRatio = Constant.LIVE_VIEW_RATIO;
        //        }
        mRealRatio = Constant.LIVE_VIEW_RATIO;

        boolean bSupport = true;//(float) mLocalInfo.getScreenHeight() / mLocalInfo.getScreenWidth() >= BIG_SCREEN_RATIO;
        if (bSupport) {
            initOperateBarUI(mRealRatio <= Constant.LIVE_VIEW_RATIO);
            initUI();
            if (mRealRatio <= Constant.LIVE_VIEW_RATIO) {
//                setBigScreenOperateBtnLayout();
            }
        }
        setRealPlaySvLayout();
        setRealPlaySuccessUI();
        updatePtzUI();
        //        startPrivacyAnim();
        updateTalkUI();
        if (mDeviceInfo != null && mDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport) {
            mLLTalkBack.setEnabled(true);
        }else{
            mLLTalkBack.setEnabled(false);
        }
    }
    private void updateTalkUI() {
        if (!mIsOnTalk) {
            return;
        }
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    openTalkPopupWindow(false);
                }
            });
        } else {
            closeTalkPopupWindow(false, false);
        }
    }
    private View.OnClickListener mOnPopWndClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ptz_close_btn:
                    closePtzPopupWindow();
                    break;
                case R.id.ptz_flip_btn:
                    break;
                case R.id.talkback_close_btn:
                    closeTalkPopupWindow(true, false);
                    break;
                default:
                    break;
            }
        }
    };
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            int action = motionevent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    switch (view.getId()) {
                        case R.id.talkback_control_btn:
                            mTalkRingView.setVisibility(View.VISIBLE);
                            mEZPlayer.setVoiceTalkStatus(true);
                            break;
                        case R.id.ptz_top_btn:
                            setPtzDirectionIv(RealPlayStatus.PTZ_UP);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_bottom_btn:
                            setPtzDirectionIv(RealPlayStatus.PTZ_DOWN);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_left_btn:
                            setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_right_btn:
                            setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.rpw_ib_multiple_del:
                            //变小
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomOut, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.rpw_ib_multiple_add:
                            //变大
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomIn, EZConstants.EZPTZAction.EZPTZActionSTART);
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
                        case R.id.talkback_control_btn:
                            mEZPlayer.setVoiceTalkStatus(false);
                            mTalkRingView.setVisibility(GONE);
                            break;
                        case R.id.ptz_top_btn:
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_bottom_btn:
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_left_btn:
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.ptz_right_btn:
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.rpw_ib_multiple_del:
                            //变小
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomOut, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.rpw_ib_multiple_add:
                            //变大

                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandZoomIn, EZConstants.EZPTZAction.EZPTZActionSTOP);
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
    /**
     * 云台操作
     *
     * @param command ptz控制命令
     * @param action  控制启动/停止
     */
    private void ptzOption(final EZConstants.EZPTZCommand command, final EZConstants.EZPTZAction action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ptz_result = false;
                try {
                    ptz_result = getOpenSDK().controlPTZ(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo(), command,
                            action, EZConstants.PTZ_SPEED_DEFAULT);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                LogUtil.i(TAG, "controlPTZ ptzCtrl result: " + ptz_result);
            }
        }).start();
    }
    /**
     * 打开对讲控制窗口
     *
     * @see
     * @since V1.8.3
     */
    private void openTalkPopupWindow(boolean showAnimation) {
        showToast("对讲中…");
    }
    private void initRealPlayPageLy() {
        /** 测量状态栏高度 **/
        ViewTreeObserver viewTreeObserver = mLlRootContainer.getViewTreeObserver();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mOrientation = newConfig.orientation;

        onOrientationChanged();
        super.onConfigurationChanged(newConfig);
    }

    private void onOrientationChanged() {
        updateOperatorUI();
        setRealPlaySvLayout();
        updateCaptureUI();
        updateTalkUI();
        updatePtzUI();
    }
    private void updatePtzUI() {
        if (!mIsOnPtz) {
            return;
        }
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setFullPtzStopUI(false);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    openPtzPopupWindow(mFlDisplayContainer);
                }
            });
        } else {
            closePtzPopupWindow();
            setFullPtzStartUI(false);
        }
    }

    private void setFullPtzStartUI(boolean startAnim) {
        mIsOnPtz = true;
        setPlayScaleUI(1, null, null);
        if (mLocalInfo.getPtzPromptCount() < 3) {
            mRealPlayFullPtzPromptIv.setBackgroundResource(R.drawable.ptz_prompt);
            mRealPlayFullPtzPromptIv.setVisibility(View.VISIBLE);
            mLocalInfo.setPtzPromptCount(mLocalInfo.getPtzPromptCount() + 1);
            mHandler.removeMessages(MSG_CLOSE_PTZ_PROMPT);
            mHandler.sendEmptyMessageDelayed(MSG_CLOSE_PTZ_PROMPT, 2000);
        }

    }

    @Bind(R.id.av_rl_titlelayout)
    RelativeLayout mRlTitleLayout;

    private Boolean m4BoxOr9Box = null; //4-true 9-false

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
                show4SpitLayout();
            }
        });
        vrs_ll_9box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //9分
                show9SpitLayout();
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
                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                mSpitPopupWindow = null;
                mSpitPopupWindow = null;
                closeSpitPopupWindow();
            }
        });
        mSpitPopupWindow.update();
    }

    private void show9SpitLayout(){
        if (mEzDeviceInfo == null || mEzDeviceInfo.size() == 0)
            return;
        stopCurPlayer();
        m4BoxOr9Box = false;
        removeSpitPlayer();
        mFlDisplayContainer.removeView(mRealPlayPlayRl);
        if (mFlDisplayContainer.findViewById(R.id.av_viewpager_spitscreen) == null)
            mFlDisplayContainer.addView(mViewPagerSpit);
        showSpitLayout();
        closeSpitPopupWindow();
    }

    private void show4SpitLayout(){
        if (mEzDeviceInfo == null || mEzDeviceInfo.size() == 0)
            return;
        stopCurPlayer();
        m4BoxOr9Box = true;
        removeSpitPlayer();
        mFlDisplayContainer.removeView(mRealPlayPlayRl);
        if (mFlDisplayContainer.findViewById(R.id.av_viewpager_spitscreen) == null)
            mFlDisplayContainer.addView(mViewPagerSpit);
        showSpitLayout();
        closeSpitPopupWindow();
    }


    //显示分屏视图
    private void showSpitLayout(){
        initSpitViewPager();
        mViewPagerSpit.setVisibility(View.VISIBLE);
//        测试
//        av_ezplayer_1.setLoadingView(initProgressBar());
//        findViewById(R.id.test).setVisibility(View.VISIBLE);
//        av_ezplayer_1.setZOrderMediaOverlay(true);
//        av_ezplayer_1.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {
//            @Override
//            public void onPlaySuccess() {
//                Log.e("ezuiplayer","onPlaySuccess");
//            }
//
//            @Override
//            public void onPlayFail(EZUIError ezuiError) {
//                if (ezuiError.getErrorString().equals(EZUIError.UE_ERROR_INNER_VERIFYCODE_ERROR)){
//
//                }else if(ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_NOT_FOUND_RECORD_FILES)){
//                    // TODO: 2017/5/12
//                    //未发现录像文件
//                    showToast("未找到录像文件");
//                }
//            }
//
//            @Override
//            public void onVideoSizeChange(int width, int height) {
//                Log.d(TAG,"onVideoSizeChange  width = "+width+"   height = "+height);
//            }
//
//            @Override
//            public void onPrepared() {
//                av_ezplayer_1.startPlay();
//            }
//
//            @Override
//            public void onPlayTime(Calendar calendar) {
//
//            }
//
//            @Override
//            public void onPlayFinish() {
//                Log.e("ezuiplayer","onPlayFinish");
//            }
//        });
//        String ezopenUrl = "ezopen://open.ys7.com/" + mEzDeviceInfo.get(1).getDeviceSerial() + "/1.live";
//        av_ezplayer_1.setUrl(ezopenUrl);

        //测试
//        av_ezplayer_2.setLoadingView(initProgressBar());
//        av_ezplayer_2.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {
//            @Override
//            public void onPlaySuccess() {
//                Log.e("ezuiplayer","onPlaySuccess");
//            }
//
//            @Override
//            public void onPlayFail(EZUIError ezuiError) {
//                if (ezuiError.getErrorString().equals(EZUIError.UE_ERROR_INNER_VERIFYCODE_ERROR)){
//
//                }else if(ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_NOT_FOUND_RECORD_FILES)){
//                    // TODO: 2017/5/12
//                    //未发现录像文件
//                    showToast("未找到录像文件");
//                }
//            }
//
//            @Override
//            public void onVideoSizeChange(int width, int height) {
//                Log.d(TAG,"onVideoSizeChange  width = "+width+"   height = "+height);
//            }
//
//            @Override
//            public void onPrepared() {
//                av_ezplayer_2.startPlay();
//            }
//
//            @Override
//            public void onPlayTime(Calendar calendar) {
//
//            }
//
//            @Override
//            public void onPlayFinish() {
//                Log.e("ezuiplayer","onPlayFinish");
//            }
//        });
//        String ezopenUrl2 = "ezopen://open.ys7.com/" + mEzDeviceInfo.get(1).getDeviceSerial() + "/1.live";
//        av_ezplayer_2.setUrl(ezopenUrl2);
    }

    //暂停播放
    private void stopCurPlayer(){
        stopRealPlay();
        setRealPlayStopUI();
    }

    private SpitFragmentAdapter mSpitAdapter;
    private SparseArray<EZUIPlayer> mSpitPlayerArr = new SparseArray<>();
    private SpitVideoFragment[] mSpitFragments;

    private void removeSpitPlayer(){
        if (mSpitFragments==null) return;
        for (int i=0; i<mSpitFragments.length; i++){
            mSpitFragments[i].releasePlay();
        }
        mSpitFragments = null;
    }
    private void initSpitViewPager(){
        int tag = 0;
        if (m4BoxOr9Box) {
            tag = 4;
        }else{
            tag = 9;
        }
        int pagerSize = (int)Math.ceil(mEzDeviceInfo.size() / (tag+0.0f));
        mSpitFragments = new SpitVideoFragment[pagerSize];
        for (int i=0; i < pagerSize; i++){
            mSpitFragments[i] = SpitVideoFragment.getInstance();
            int type;
            boolean autoPlay = false;
//            Bundle bundle = mSpitFragments[i].getArguments();
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
        mViewPagerSpit.setOffscreenPageLimit(mSpitFragments.length);
        mSpitAdapter = new SpitFragmentAdapter(getSupportFragmentManager(),mSpitFragments);
        mViewPagerSpit.setAdapter(mSpitAdapter);
        mSpitAdapter.notifyDataSetChanged();
    }
    /**
     * 打开云台控制窗口
     *
     * @see
     * @since V1.8.3
     */
    private void openPtzPopupWindow(View parent) {
        closePtzPopupWindow();
        mIsOnPtz = true;
        setPlayScaleUI(1, null, null);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_ptz_wnd, null, true);

        mPtzControlLy = (LinearLayout) layoutView.findViewById(R.id.ptz_control_ly);
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


        int height = mLocalInfo.getScreenHeight() - mRealPlayPlayRl.getHeight()
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
                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                mPtzPopupWindow = null;
                mPtzControlLy = null;
                closePtzPopupWindow();
            }
        });
        mPtzPopupWindow.update();
    }

    /**
     * 设备对讲
     *
     * @see
     * @since V2.0
     */
    @Bind(R.id.av_img_talkback)
    ImageView mImgTalkBack;
    private void startVoiceTalk() {
        LogUtil.debugLog(TAG, "startVoiceTalk");
        if (mEZPlayer == null) {
            LogUtil.debugLog(TAG, "EZPlaer is null");
            return;
        }
        if (mCameraInfo == null) {
            return;
        }
        mIsOnTalk = true;

        updateOrientation();

        Utils.showToast(this, R.string.start_voice_talk);
        mLLTalkBack.setEnabled(false);
        mImgTalkBack.setImageResource(R.mipmap.ic_nor_novoice);

        if (mEZPlayer != null) {
            mEZPlayer.closeSound();
        }
        mEZPlayer.startVoiceTalk();
    }

    /**
     * 停止对讲
     *
     * @see
     * @since V2.0
     */
    private void stopVoiceTalk() {
        if (mCameraInfo == null || mEZPlayer == null) {
            return;
        }
        LogUtil.debugLog(TAG, "stopVoiceTalk");

        mEZPlayer.stopVoiceTalk();
        handleVoiceTalkStoped();
    }

    private void closeSpitPopupWindow() {
        if (mSpitPopupWindow != null) {
            dismissPopWindow(mSpitPopupWindow);
            mSpitPopupWindow = null;
        }
    }

    private void closePtzPopupWindow() {
        mIsOnPtz = false;
        if (mPtzPopupWindow != null) {
            dismissPopWindow(mPtzPopupWindow);
            mPtzPopupWindow = null;
            mPtzControlLy = null;
            setForceOrientation(0);
        }
    }

    private void setRealPlaySuccessUI() {
        mStopTime = System.currentTimeMillis();
        showType();

        updateOrientation();
        setLoadingSuccess();

        if (mCameraInfo != null && mDeviceInfo != null) {
            mRealPlayCaptureBtn.setEnabled(true);
            mRealPlayRecordBtn.setEnabled(true);
            mLLVoice.setEnabled(true);
            mLLStream.setEnabled(true);
            mLLTalkBack.setEnabled(true);
            mLLControl.setEnabled(true);
        }


        startUpdateTimer();
    }

    private void setLoadingSuccess() {
        mRealPlayLoadingRl.setVisibility(View.INVISIBLE);
        mRealPlayTipTv.setVisibility(GONE);
        mRealPlayPlayLoading.setVisibility(GONE);
        mRealPlayPlayIv.setVisibility(GONE);
    }
    private void setRealPlaySound() {
        if (mEZPlayer != null) {
            if (mRtspUrl == null) {
                if (mLocalInfo.isSoundOpen()) {
                    mEZPlayer.openSound();
                } else {
                    mEZPlayer.closeSound();
                }
            } else {
                if (mRealPlaySquareInfo.mSoundType == 0) {
                    mEZPlayer.closeSound();
                } else {
                    mEZPlayer.openSound();
                }
            }
        }
    }
    /**
     * 获取设备信息成功
     *
     * @see
     * @since V1.0
     */
    private void handleGetCameraInfoSuccess() {
        LogUtil.infoLog(TAG, "handleGetCameraInfoSuccess");

        //通过能力级设置
        updateUI();

    }
    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "EZRealPlay Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.videogo.open/http/host/path")
//        );
//  AppIndex.AppIndexApi.end(client, viewAction);

        for (int i= 0;i < mSpitPlayerArr.size(); i++){
            EZUIPlayer ezuiPlayer = mSpitPlayerArr.get(i);
            if (ezuiPlayer!=null)
                ezuiPlayer.stopPlay();
        }

        if (mScreenOrientationHelper != null) {
            mScreenOrientationHelper.postOnStop();
        }

        mHandler.removeMessages(MSG_AUTO_START_PLAY);
        hidePageAnim();

        if (mCameraInfo == null && mRtspUrl == null) {
            return;
        }

        closePtzPopupWindow();
        closeTalkPopupWindow(true, false);
        if (mStatus != RealPlayStatus.STATUS_STOP) {
            mIsOnStop = true;
            stopRealPlay();
            mStatus = RealPlayStatus.STATUS_PAUSE;
            setRealPlayStopUI();
        } else {
            setStopLoading();
        }
        mRealPlaySv.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void bindEvent() {
        super.bindEvent();
        mLLPlayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回放
                DateTimeDialog dateTimeDialog = new DateTimeDialog(VideoShowActivity.this, null, VideoShowActivity.this, 0);
                dateTimeDialog.show();
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(VideoShowActivity.this);
            }
        });
        mImgSpit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分屏窗口
                openSpitPopWindow(v);
            }
        });
        mRealPlayRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //录像
                onRecordBtnClick();
            }
        });
        mLLTalkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启设备对讲

                if (m4BoxOr9Box != null){
                    showToast("请选择具体设备");
                    return;
                }

                if (!mIsOnTalk)
                    startVoiceTalk();
                else
                    stopVoiceTalk();
            }
        });
        mLLStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //码流

                if (m4BoxOr9Box != null){
                    showToast("请选择具体设备");
                    return;
                }


                new AlertView("选择码流", null,"取消", null,
                        new String[]{"高清","标清","流畅"},
                        VideoShowActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (mEZPlayer == null) {
                            return;
                        }
                        int tag = -1;
                        // 视频质量，2-高清，1-标清，0-流畅
                        if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET) {
                            tag = 2;
                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED) {
                            tag = 1;
                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_HD) {
                            tag = 0;
                        }
                        if (tag == position)
                            return;
                        switch (position){
                            case 0:
                                //高清
                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_HD);
                                break;
                            case 1:
                                //标清
                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED);
                                break;
                            case 2:
                                //流畅
                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET);
                                break;
                        }
                    }
                }).show();
            }
        });
        mLLVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //声音
                if (m4BoxOr9Box == null)
                    onSoundBtnClick();
                else
                    showToast("请选择具体设备");
            }
        });
        mRealPlayCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                if (m4BoxOr9Box == null)
                    onCapturePicBtnClick();
                else
                    showToast("请选择具体设备");
            }
        });
        mLLControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开控制台
                if (m4BoxOr9Box == null)
                    openPtzPopupWindow(mFlDisplayContainer);
                else
                    showToast("请选择具体设备");
            }
        });
        mRealPlayPlayIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    setRealPlayStopUI();
                } else {
                    startRealPlay();
                }
            }
        });
    }
    /**
     * 开始录像
     *
     * @see
     * @since V1.0
     */
    private boolean mIsRecording = false;
    private String mRecordTime = null;
    private void onRecordBtnClick() {
        mControlDisplaySec = 0;
        if (mIsRecording) {
            stopRealPlayRecord();
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(VideoShowActivity.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(VideoShowActivity.this, R.string.remoteplayback_record_fail_for_memory);
            return;
        }

        if (mEZPlayer != null) {
            mCaptureDisplaySec = 4;
            updateCaptureUI();

            mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);

            // 可以采用deviceSerial+时间作为文件命名，demo中简化，只用时间命名
            java.util.Date date = new java.util.Date();
            String strRecordFile = Environment.getExternalStorageDirectory().getPath() + "/EZOpenSDK/Records/" + String.format("%tY", date)
                    + String.format("%tm", date) + String.format("%td", date) + "/"
                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) + ".mp4";

            if (mEZPlayer.startLocalRecordWithFile(strRecordFile))
            {
                handleRecordSuccess(strRecordFile);
            } else {
                handleRecordFail();
            }
        }
    }
    /**
     * 开始录像成功
     *
     * @param recordFilePath
     * @see
     * @since V2.0
     */
    @Bind(R.id.av_fl_videocontainer)
    FrameLayout mRealPlayRecordContainer;
    @Bind(R.id.av_img_videostart)
    ImageView mImgVideoStart;
    @Bind(R.id.av_img_videostop)
    ImageView mImgVideoStop;
    private void handleRecordSuccess(String recordFilePath) {
        if (mCameraInfo == null) {
            return;
        }

        // 设置录像按钮为check状态
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStart,
                        mImgVideoStop, 0, 90);
            } else {
                mImgVideoStart.setVisibility(GONE);
                mImgVideoStop.setVisibility(View.VISIBLE);
            }
        } else {
            mImgVideoStart.setVisibility(GONE);
            mImgVideoStop.setVisibility(View.VISIBLE);
        }
        mIsRecording = true;
        // 计时按钮可见
        mRlPrompt.setVisibility(View.VISIBLE);
        mRealPlayRecordLy.setVisibility(View.VISIBLE);
        mRealPlayRecordTv.setText("00:00");
        mRecordSecond = 0;
    }

    /**
     * 启动定时器
     *
     * @see
     * @since V1.0
     */
    private void startUpdateTimer() {
        stopUpdateTimer();
        // 开始录像计时
        mUpdateTimer = new Timer();
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mLandscapeTitleBar != null && (mLandscapeTitleBar.getVisibility() == View.VISIBLE )
                        && mControlDisplaySec < 5) {
                    mControlDisplaySec++;
                }
                if (mRealPlayCaptureRl != null && mRealPlayCaptureRl.getVisibility() == View.VISIBLE
                        && mCaptureDisplaySec < 4) {
                    mCaptureDisplaySec++;
                }

                // 更新录像时间
                if (mEZPlayer != null && mIsRecording) {
                    // 更新录像时间
                    Calendar OSDTime = mEZPlayer.getOSDTime();
                    if (OSDTime != null) {
                        String playtime = Utils.OSD2Time(OSDTime);
                        if (!TextUtils.equals(playtime, mRecordTime)) {
                            mRecordSecond++;
                            mRecordTime = playtime;
                        }
                    }
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(MSG_PLAY_UI_UPDATE);
                }
            }
        };
        // 延时1000ms后执行，1000ms执行一次
        mUpdateTimer.schedule(mUpdateTimerTask, 0, 1000);
    }

    /**
     * 更新录像时间
     *
     * @see
     * @since V1.0
     */
    private void updateRecordTime() {
        if (mRealPlayRecordIv.getVisibility() == View.VISIBLE) {
            mRealPlayRecordIv.setVisibility(View.INVISIBLE);
        } else {
            mRealPlayRecordIv.setVisibility(View.VISIBLE);
        }
        // 计算分秒
        int leftSecond = mRecordSecond % 3600;
        int minitue = leftSecond / 60;
        int second = leftSecond % 60;

        // 显示录像时间
        String recordTime = String.format("%02d:%02d", minitue, second);
        mRealPlayRecordTv.setText(recordTime);
    }
    private void handleRecordFail() {
        Utils.showToast(VideoShowActivity.this, R.string.remoteplayback_record_fail);
        if (mIsRecording) {
            stopRealPlayRecord();
        }
    }
    /**
     * 停止录像
     *
     * @see
     * @since V1.0
     */
    private void stopRealPlayRecord() {
        if (mEZPlayer == null || !mIsRecording) {
            return;
        }
        Toast.makeText(VideoShowActivity.this, getResources().getString(R.string.already_saved_to_volume), Toast.LENGTH_SHORT).show();

        // 设置录像按钮为check状态
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStop,
                        mImgVideoStart, 0, 90);
            } else {
                mImgVideoStop.setVisibility(GONE);
                mImgVideoStart.setVisibility(View.VISIBLE);
            }
        } else {
            mImgVideoStop.setVisibility(GONE);
            mImgVideoStart.setVisibility(View.VISIBLE);
        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
        mEZPlayer.stopLocalRecord();

        // 计时按钮不可见
        mRlPrompt.setVisibility(GONE);
        mRealPlayRecordLy.setVisibility(GONE);
        mCaptureDisplaySec = 0;
        mIsRecording = false;
        updateCaptureUI();
    }
    /**
     * 码流配置 清晰度 2-高清，1-标清，0-流畅
     *
     * @see
     * @since V2.0
     */
    private void setQualityMode(final EZConstants.EZVideoLevel mode) {
        // 检查网络是否可用
        if (!ConnectionDetector.isNetworkAvailable(VideoShowActivity.this)) {
            // 提示没有连接网络
            Utils.showToast(VideoShowActivity.this, R.string.realplay_set_fail_network);
            return;
        }

        if (mEZPlayer != null) {
            mWaitDialog.setWaitText(this.getString(R.string.setting_video_level));
            mWaitDialog.show();

            Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // need to modify by yudan at 08-11
                        getOpenSDK().setVideoLevel(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo(), mode.getVideoLevel());
                        mCurrentQulityMode = mode;
                        Message msg = Message.obtain();
                        msg.what = MSG_SET_VEDIOMODE_SUCCESS;
                        mHandler.sendMessage(msg);
                        LogUtil.i(TAG, "setQualityMode success");
                    } catch (BaseException e) {
                        mCurrentQulityMode = EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET;
                        e.printStackTrace();
                        Message msg = Message.obtain();
                        msg.what = MSG_SET_VEDIOMODE_FAIL;
                        mHandler.sendMessage(msg);
                        LogUtil.i(TAG, "setQualityMode fail");
                    }

                }
            });
            thr.start();
        }
    }

    //声音控制
    private void onSoundBtnClick() {
        if (mLocalInfo.isSoundOpen()) {
            mLocalInfo.setSoundOpen(false);
            mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_nohorn);
        } else {
            mLocalInfo.setSoundOpen(true);
            mRealPlaySoundBtn.setImageResource(R.mipmap.ic_nor_greenhorn);
        }

        setRealPlaySound();
    }
    /**
     * 抓拍按钮响应函数
     *
     * @since V1.0
     */
    private void onCapturePicBtnClick() {

        mControlDisplaySec = 0;
        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(VideoShowActivity.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(VideoShowActivity.this, R.string.remoteplayback_capture_fail_for_memory);
            return;
        }

        if (mEZPlayer != null) {
            mCaptureDisplaySec = 4;
            updateCaptureUI();

            Thread thr = new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = mEZPlayer.capturePicture();
                    if (bmp != null) {
                        try {
                            mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);

                            // 可以采用deviceSerial+时间作为文件命名，demo中简化，只用时间命名
                            java.util.Date date = new java.util.Date();
                            final String path = Environment.getExternalStorageDirectory().getPath() + "/EZOpenSDK/CapturePicture/" + String.format("%tY", date)
                                    + String.format("%tm", date) + String.format("%td", date) + "/"
                                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) +".jpg";

                            if (TextUtils.isEmpty(path)) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                            EZUtils.saveCapturePictrue(path, bmp);


                            MediaScanner mMediaScanner = new MediaScanner(VideoShowActivity.this);
                            mMediaScanner.scanFile(path, "jpg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(VideoShowActivity.this, getResources().getString(R.string.already_saved_to_volume)+path, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (InnerException e) {
                            e.printStackTrace();
                        } finally {
                            if (bmp != null) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                        }
                    }
                    super.run();
                }
            };
            thr.start();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onInputVerifyCode(String verifyCode) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

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
            if (VideoShowActivity.this.isFinishing()) {
                return null;
            }

            if (!ConnectionDetector.isNetworkAvailable(VideoShowActivity.this)) {
                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
                return null;
            }

            try {
                List<EZDeviceInfo> result = null;
                result = getOpenSDK().getDeviceList(0, 20);
                return result;

            } catch (BaseException e) {
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                LogUtil.debugLog(TAG, errorInfo.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<EZDeviceInfo> result) {
            super.onPostExecute(result);
            if (VideoShowActivity.this.isFinishing()) {
                return;
            }

            mEzDeviceInfo = result;

            if (mEzDeviceInfo != null && mEzDeviceInfo.size() > 0){
                onGetDeviceInfoSucess();
            }else{
                showToast("暂无设备信息");
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
                    startActivity(new Intent(VideoShowActivity.this,LoginActivity.class));
                    break;
                default:
                    showToast("获取摄像头设备列表失败");
                    break;
            }
        }
    }


    //*******************
    class RealPlaySquareInfo {
        public int mSquareId;
        public String mCoverUrl = null;
        public String mDeviceSerial = null;
        public int mChannelNo = 1;
        public String mCameraName = null;
        public int mSoundType = 0;
    }
}
