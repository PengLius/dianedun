package cn.dianedun.fragment;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezvizuikit.open.EZUIError;
import com.videogo.constant.Constant;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.MediaScanner;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.widget.CustomRect;
import com.videogo.widget.CustomTouchListener;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.dianedun.R;
import cn.dianedun.activity.VideoPlayActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AudioPlayUtil;
import cn.dianedun.tools.EZUtils;
import cn.dianedun.view.EZUIPlayer.EZUIPlayer;
import cn.dianedun.view.PagesGallery;
import me.yokeyword.fragmentation.SupportFragment;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/12/16.
 */

public class VideoEntiryFragment extends SupportFragment implements View.OnTouchListener {

    private static final String TAG = "VideoEntiryFragment";

    public static VideoEntiryFragment getInstance(int pos, boolean bShow, EZDeviceInfo bean) {
        VideoEntiryFragment fragment = new VideoEntiryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        bundle.putBoolean("bshow",bShow);
        bundle.putParcelable("data",bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EZUIPlayer mEzUiPlayer;
    private RelativeLayout mRlContainer;
    private TextView mTvTip;
    private ImageView mImgDirection;
    private LinearLayout mLLRecordView;
    private ImageView mImgRecordTag,mImgBg;
    private TextView mTvRecordSec;
    private VideoPlayActivity.OnVideoSelect mVideoParent;

    private Bitmap mCurBitmap;
//    protected ProgressBar mProgress;

    public void setCurBitmap(Bitmap curBitmap){
        if (mCurBitmap!=null){
            mCurBitmap.recycle();
            mCurBitmap = null;
        }
        mCurBitmap = curBitmap;
        mImgBg.setImageBitmap(mCurBitmap);
    }

    public int getVideoStatus(){
        if (mEzUiPlayer==null)
            return -1;
        return mEzUiPlayer.getStatus();
    }

    private boolean mInTop = false;

    public VideoEntiryFragment setVideoParent(VideoPlayActivity.OnVideoSelect videoParent) {
        this.mVideoParent = videoParent;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_video,container,false);
        mRlContainer = (RelativeLayout)view;
        init(view);
        return view;
    }

    private PagesGallery mPageGallery;
    private void init(View view){
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mVideoParent!=null)
//                    mVideoParent.onVideoClick();
//            }
//        });
        mPageGallery = (PagesGallery)view.findViewById(R.id.realplay_pages_gallery);
        mPageGallery.setOnTouchListener(this);
        mEzUiPlayer = (EZUIPlayer) view.findViewById(R.id.vv_ezuiplayer);
        mTvTip = (TextView)view.findViewById(R.id.vv_tv_tip);
        mLLRecordView = (LinearLayout)view.findViewById(R.id.vv_ll_record);
        mImgRecordTag = (ImageView)view.findViewById(R.id.vv_img_realplay);
        mTvRecordSec = (TextView)view.findViewById(R.id.vv_tv_realplay);
        mImgDirection = (ImageView)view.findViewById(R.id.vv_img_direction);
        mImgBg = (ImageView)view.findViewById(R.id.vv_img_bg);
//        mProgress = (ProgressBar)view.findViewById(R.id.vv_progress);
        mDataBean = getArguments().getParcelable("data");
        mRealPlayTouchListener = new CustomTouchListener() {

            @Override
            public boolean canZoom(float scale) {
                if (mEzUiPlayer.getStatus() == RealPlayStatus.STATUS_PLAY){
//                    if (mPlayScale <= 1){
//                        mPageGallery.setSwitch(true);
//                    }else{
//                        mPageGallery.setSwitch(false);
//                    }
                    return true;
                } else {
//                    Log.e("zoom false",scale+"");
                    return false;
                }
            }

            @Override
            public boolean canDrag(int direction) {
                if (mEzUiPlayer.getStatus() != RealPlayStatus.STATUS_PLAY) {
                    return false;
                }
                if (mEzUiPlayer.getEzPlayer() != null && mDataBean != null) {
                    // 出界判断
                    if (DRAG_LEFT == direction || DRAG_RIGHT == direction) {
                        // 左移/右移出界判断
                        if (mDataBean.isSupportPTZ()) {
                            return true;
                        }
                    } else if (DRAG_UP == direction || DRAG_DOWN == direction) {
                        // 上移/下移出界判断
                        if (mDataBean.isSupportPTZ()) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void onSingleClick() {
                if (mVideoParent!=null){
                    mVideoParent.onVideoClick();
                }
            }

            @Override
            public void onDoubleClick(MotionEvent e) {
            }

            @Override
            public void onZoom(float scale) {
//                LogUtil.debugLog(TAG, "onZoom:" + scale);
//                if (mEzUiPlayer.getEzPlayer() != null && mDataBean != null &&  mDataBean.isSupportZoom()) {
//                    startZoom(scale);
//                }
            }

            @Override
            public void onDrag(int direction, float distance, float rate) {
//                LogUtil.debugLog(TAG, "onDrag:" + direction);
                if (mEzUiPlayer.getEzPlayer() != null) {
                    //Utils.showLog(RealPlayActivity.this, "onDrag rate:" + rate);
                    startDrag(direction, distance, rate);
                }
            }

            @Override
            public void onEnd(int mode) {
//                LogUtil.debugLog(TAG, "onEnd:" + mode);
                if (mEzUiPlayer.getEzPlayer() != null) {
                    stopDrag(false);
                }
                if (mEzUiPlayer.getEzPlayer() != null && mDataBean != null && mDataBean.isSupportZoom()) {
//                    stopZoom();
                }
            }

            @Override
            public void onZoomChange(float scale, CustomRect oRect, CustomRect curRect) {
//                LogUtil.debugLog(TAG, "onZoomChange:" + scale);
//                if (mEzUiPlayer.getEzPlayer() != null && mDataBean != null && mDataBean.isSupportZoom()) {
                    //采用云台调焦
//                    return;
//                }
                if (mEzUiPlayer.getStatus() == RealPlayStatus.STATUS_PLAY) {
                    if (scale > 1.0f && scale < 1.1f) {
                        scale = 1.1f;
                    }
                    setPlayScaleUI(scale, oRect, curRect);
                }
            }
        };
    }

    private String mPreUrl,mErrStr;
    private EZDeviceInfo mDataBean;

    public EZDeviceInfo getDataBean(){
        return mDataBean;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initUi();
    }
    private LocalInfo mLocalInfo;
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

    public int getPos(){
        return mPos;
    }

    private int mPos;
    private boolean mRetryPlay = false;
    private CustomTouchListener mRealPlayTouchListener = null;
    private void initUi(){
//        if (mDataBean.getStatus() != 1){
//            mRlContainer.removeView(mEzUiPlayer);
//            mErrStr = "设备不在线";
//            mTvTip.setText(mErrStr);
//            mTvTip.setVisibility(View.VISIBLE);
//            return;
//        }
        mLocalInfo = getLocalInfo();
        mAudioPlayUtil = AudioPlayUtil.getInstance(App.getInstance());
        final DisplayMetrics dm = new DisplayMetrics();
        _mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        mEzUiPlayer.setSurfaceSize(dm.widthPixels, 0);
        mEzUiPlayer.setOpenSound(false);
        mPos = getArguments().getInt("pos",0);
        //getArguments().getInt("pos",0) == 0 ? true : false
        mEzUiPlayer.setAutoPlay(getArguments().getBoolean("bshow"));
        mEzUiPlayer.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {

            @Override
            public void onTalkBackState(boolean state, ErrorInfo errorInfo) {
                if (!state){
//                    mIsOnTalk = false;
                }else{
                    mIsOnTalk = true;
                    if (mVideoParent!=null)
                        mVideoParent.onVideoTalkBackState(state,errorInfo);
                }
            }

            @Override
            public boolean onRetryLoad() {
                if (!mRetryPlay) {
                    mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
                    return mRetryPlay = true;
                }else{
                    return mRetryPlay = false;
                }
            }

            @Override
            public void onShowLoading() {
//                mTvTip.setVisibility(GONE);
//                laodingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPlaySuccess() {
                Log.e("ezuiplayer","onPlaySuccess");
//                            ezUIPlayer.setZOrderOnTop(true);
//                laodingView.setVisibility(View.INVISIBLE);

                setCurBitmap(mEzUiPlayer.getEzPlayer().capturePicture());
                mPageGallery.setVisibility(View.VISIBLE);
                if (mInTop && mVideoParent!=null) {
                    mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_PLAY,null);
                    if (getSoundOpenStatus()) {
                        mEzUiPlayer.setOpenSound(true);
                        mVideoParent.onVideoVoiceControl(true);
                    }else {
                        mEzUiPlayer.setOpenSound(false);
                        mVideoParent.onVideoVoiceControl(false);
                    }
                }
                mErrStr = null;
            }

            @Override
            public void onPlayFail(EZUIError ezuiError) {
                if (mIsRecording){
                    stopRealPlayRecord();
                }
                mPageGallery.setVisibility(GONE);
                if (mInTop && mVideoParent!=null) {
                    mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT,ezuiError);
                    mEzUiPlayer.setOpenSound(false);
                    mVideoParent.onVideoVoiceControl(false);
                }
                mErrStr = ezuiError.getErrorString();
                Toast.makeText(_mActivity, mErrStr, Toast.LENGTH_SHORT).show();
//                laodingView.setVisibility(View.INVISIBLE);
//                mTvTip.setText(mErrStr);
//                mTvTip.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoSizeChange(int width, int height) {
            }

            @Override
            public void onPrepared() {
                Log.e("onPrepared","onPrepared");
                mEzUiPlayer.startPlay();
            }

            @Override
            public void onPlayTime(Calendar calendar) {

            }

            @Override
            public void onPlayFinish() {
                Log.e("ezuiplayer","onPlayFinish");
            }
        });

        mEzUiPlayer.setSufaceViewTouchListener(mRealPlayTouchListener);
        setSurfaceSize(dm.widthPixels);
        mPreUrl = "ezopen://open.ys7.com/" + mDataBean.getDeviceSerial();
        mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
    }
    // 播放比例
    private float mPlayScale = 1;
    private void setPlayScaleUI(float scale, CustomRect oRect, CustomRect curRect) {
        if (scale == 1) {
            if (mPlayScale == scale) {
                return;
            }
//            mRealPlayRatioTv.setVisibility(View.GONE);
            try {
                if (mEzUiPlayer.getEzPlayer() != null) {
                    mEzUiPlayer.getEzPlayer().setDisplayRegion(false, null, null);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (mPlayScale == scale) {
                try {
                    if (mEzUiPlayer.getEzPlayer() != null) {
                        mEzUiPlayer.getEzPlayer().setDisplayRegion(true, oRect, curRect);
                    }
                } catch (BaseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
//            RelativeLayout.LayoutParams realPlayRatioTvLp = (RelativeLayout.LayoutParams) mRealPlayRatioTv
//                    .getLayoutParams();
//            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
//                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 10), Utils.dip2px(this, 10), 0, 0);
//            } else {
//                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 70), Utils.dip2px(this, 20), 0, 0);
//            }
//            mRealPlayRatioTv.setLayoutParams(realPlayRatioTvLp);
//            String sacleStr = String.valueOf(scale);
//            mRealPlayRatioTv.setText(sacleStr.subSequence(0, Math.min(3, sacleStr.length())) + "X");
//            //mj mRealPlayRatioTv.setVisibility(View.VISIBLE);
//            mRealPlayRatioTv.setVisibility(View.GONE);
//            hideControlRlAndFullOperateBar(false);
            try {
                if (mEzUiPlayer.getEzPlayer() != null) {
                    mEzUiPlayer.getEzPlayer().setDisplayRegion(true, oRect, curRect);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mPlayScale = scale;
        if (mPlayScale <= 1){
            mPageGallery.setSwitch(true);
        }else{
            mPageGallery.setSwitch(false);
        }
    }
    public void startDrag(int direction, float distance, float rate) {
    }

    public void stopDrag(boolean control) {
    }
    private float mZoomScale = 0;
    private void startZoom(float scale) {
        if (mEzUiPlayer.getEzPlayer() == null) {
            return;
        }

//        hideControlRlAndFullOperateBar(false);
        boolean preZoomIn = mZoomScale > 1.01 ? true : false;
        boolean zoomIn = scale > 1.01 ? true : false;
        if (mZoomScale != 0 && preZoomIn != zoomIn) {
            LogUtil.debugLog(TAG, "startZoom stop:" + mZoomScale);

//            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
//                                : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_STOP);

            ptzOption(mZoomScale > 1.01 ? EZConstants.EZPTZCommand.EZPTZCommandZoomIn
                    : EZConstants.EZPTZCommand.EZPTZCommandZoomOut, EZConstants.EZPTZAction.EZPTZActionSTOP);
            mZoomScale = 0;
        }
        if (scale != 0 && (mZoomScale == 0 || preZoomIn != zoomIn)) {
            mZoomScale = scale;
            LogUtil.debugLog(TAG, "startZoom start:" + mZoomScale);
//            mEZOpenSDK.controlPTZ(mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
//                                : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, EZPlayer.PTZ_COMMAND_START);

            ptzOption(mZoomScale > 1.01 ? EZConstants.EZPTZCommand.EZPTZCommandZoomIn
                    : EZConstants.EZPTZCommand.EZPTZCommandZoomOut, EZConstants.EZPTZAction.EZPTZActionSTART);
        }
    }
    private void stopZoom() {
        if (mEzUiPlayer.getEzPlayer() == null) {
            return;
        }
        if (mZoomScale != 0) {
            LogUtil.debugLog(TAG, "stopZoom stop:" + mZoomScale);
            ptzOption(mZoomScale > 1.01 ? EZConstants.EZPTZCommand.EZPTZCommandZoomIn
                    : EZConstants.EZPTZCommand.EZPTZCommandZoomOut, EZConstants.EZPTZAction.EZPTZActionSTOP);
            mZoomScale = 0;
        }
    }
    
    public final static String QA_HD = "/1.hd.live?mute=true";
    public final static String QA_BA = "/1.live?mute=true";
    private String mVideoQa = QA_HD;

    public void setSurfaceSize(int width){
        if (mEzUiPlayer!=null) {
            Point p = mEzUiPlayer.setSurfaceSize(width, 0);
            mRealPlayTouchListener.setSacaleRect(Constant.MAX_SCALE, 0, 0, p.x, p.y);
            setPlayScaleUI(1, null, null);
        }
    }

    public void changeShowState(boolean bShow){
        if (bShow){
            mImgBg.setVisibility(GONE);
            mEzUiPlayer.setVisibility(View.VISIBLE);
        }else{
//            滑动
            if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY){
                Bitmap bmp = mEzUiPlayer.getEzPlayer().capturePicture();
                mImgBg.setImageBitmap(bmp);
            }
            mImgBg.setVisibility(View.VISIBLE);
            mEzUiPlayer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.realplay_pages_gallery:
                mRealPlayTouchListener.touch(event);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        mImgBg.setVisibility(View.GONE);
//        mEzUiPlayer.setVisibility(View.VISIBLE);
        mInTop = true;
        if (mVideoParent!=null)
            mVideoParent.onVideoSelect(mPos,mDataBean);

        if (mDataBean.getStatus() == 1 && mEzUiPlayer.getStatus() != EZUIPlayer.STATUS_PLAY){
            mEzUiPlayer.setVisibility(View.VISIBLE);
            mEzUiPlayer.setAutoPlay(true);
            mEzUiPlayer.startPlay();
        }else {
            if (mVideoParent!=null){
                mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT,null);
                if (getSoundOpenStatus()) {
                    mEzUiPlayer.setOpenSound(true);
                    mVideoParent.onVideoVoiceControl(true);
                }else {
                    LocalInfo localInfo = getLocalInfo();
                    localInfo.setSoundOpen(false);
                    mEzUiPlayer.setOpenSound(false);
                    mVideoParent.onVideoVoiceControl(false);
                }
            }
        }
    }

    @Override
    public void onSupportInvisible(){
        super.onSupportInvisible();
//        mImgBg.setVisibility(View.VISIBLE);
//        mEzUiPlayer.setVisibility(View.GONE);
        mInTop = false;
        if (mIsRecording){
            stopRealPlayRecord();
        }
        if (mIsOnTalk){
            stopVoiceTalk();
        }
        if (mDataBean.getStatus() == 1)
            stopPlay();

        mEzUiPlayer.setVisibility(View.VISIBLE);
    }

    public void stopPlay() {
        if (mEzUiPlayer!=null)
            mEzUiPlayer.stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        mHandler = null;
        releasePlay();
        if (mCurBitmap!=null){
            mCurBitmap.recycle();
            mCurBitmap = null;
        }
    }

    //待用
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void releasePlay(){
        mEzUiPlayer.stopPlay();
        mEzUiPlayer.releasePlayer();
    }

    public boolean isInTop(){
        return mInTop;
    }

    public boolean isVideoNormal(){
        if (!mInTop) return false;

        if (!TextUtils.isEmpty(mErrStr)){
            Toast.makeText(_mActivity, mErrStr, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 开始录像
     *
     * @see
     * @since V1.0
     */
    private boolean mIsRecording = false;
    private String mRecordTime = null;
    private String mCurRecordName;
    public void onRecordBtnClick() {
        if (!isVideoNormal()){
            return ;
        }

        if (mEzUiPlayer.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_STOP
                || mEzUiPlayer.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_INIT) {
//            if (mVideoParent!=null){
//                mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_PLAY);
//            }
//            mEzUiPlayer.startPlay();
            Utils.showToast(_mActivity,"请先获取直播，再录像");
            return;
        }

        if (mIsRecording) {
            stopRealPlayRecord();
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(_mActivity,"存储卡不可用");
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(_mActivity, "录像中断,存储空间已满");
            return;
        }

        if (mEzUiPlayer.getEzPlayer() != null) {
            mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
            java.util.Date date = new java.util.Date();

            String entityPath = "DianEDun/Records/" + String.format("%tY", date)
                    + String.format("%tm", date) + String.format("%td", date) + "/"
                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) + ".mp4";
            String strRecordFile = Environment.getExternalStorageDirectory().getPath() + "/" +  entityPath;
            if (mEzUiPlayer.getEzPlayer().startLocalRecordWithFile(strRecordFile))
            {
                handleRecordSuccess(strRecordFile,entityPath);
            } else {
                handleRecordFail();
            }
        }
    }

    public boolean getIsRecord(){
        return mIsRecording;
    }

    private void handleRecordFail() {
        Utils.showToast(_mActivity,"录像失败");
        if (mIsRecording) {
            stopRealPlayRecord();
        }
    }
    /**
     * 开始录像成功
     *
     * @param recordFilePath
     * @see
     * @since V2.0
     */
    /**
     * 录像时间
     */
    private int mRecordSecond = 0;
    private void handleRecordSuccess(String recordFilePath,String entityPath) {
        EZUtils.updateVideo(_mActivity,recordFilePath);

        mCurRecordName = "已将录像保存至目录：" + entityPath;
        if (mVideoParent!=null){
            mVideoParent.onVideoRecordState(true);
        }

        mIsRecording = true;
        // 计时按钮可见
        mLLRecordView.setVisibility(View.VISIBLE);
        mTvRecordSec.setText("00:00");
        mRecordSecond = 0;
        startUpdateTimer();
    }

    /**
     * 定时器
     */
    private Timer mUpdateTimer = null;
    /**
     * 定时器执行的任务
     */
    private TimerTask mUpdateTimerTask = null;
    private void startUpdateTimer() {
        stopUpdateTimer();
        // 开始录像计时
        mUpdateTimer = new Timer();
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                // 更新录像时间
                if (mEzUiPlayer != null && mIsRecording) {
                    // 更新录像时间
                    Calendar OSDTime = mEzUiPlayer.getOSDTime();
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
    // UI消息
    public static final int MSG_PLAY_UI_UPDATE = 200;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_PLAY_UI_UPDATE:
                    updateRealPlayUI();
                    break;
                case MSG_HIDE_PTZ_DIRECTION:
                    handleHidePtzDirection(msg);
                    break;
            }
            return false;
        }
    });

    private void updateRealPlayUI() {
        if (mIsRecording){
            updateRecordTime();
        }
    }
    /**
     * 更新录像时间
     *
     * @see
     * @since V1.0
     */
    private void updateRecordTime() {
        if (mImgRecordTag.getVisibility() == View.VISIBLE) {
            mImgRecordTag.setVisibility(View.INVISIBLE);
        } else {
            mImgRecordTag.setVisibility(View.VISIBLE);
        }
        // 计算分秒
        int leftSecond = mRecordSecond % 3600;
        int minitue = leftSecond / 60;
        int second = leftSecond % 60;

        // 显示录像时间
        String recordTime = String.format("%02d:%02d", minitue, second);
        mTvRecordSec.setText(recordTime);
    }
    /**
     * 停止定时器
     *
     * @see
     * @since V1.0
     */
    private void stopUpdateTimer() {
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
    /**
     * 停止录像
     *
     * @see
     * @since V1.0
     */
    public void stopRealPlayRecord() {
        if (mEzUiPlayer.getEzPlayer() == null || !mIsRecording) {
            return;
        }
        Toast.makeText(_mActivity, mCurRecordName, Toast.LENGTH_SHORT).show();
        mCurRecordName = "";
        if (mVideoParent!=null){
            mVideoParent.onVideoRecordState(false);
        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
        mEzUiPlayer.getEzPlayer().stopLocalRecord();
        // 计时按钮不可见
        mLLRecordView.setVisibility(GONE);
        mIsRecording = false;
    }

    public Boolean onSoundBtnClick(){
        if (!isVideoNormal()){
            return null;
        }
        if (mEzUiPlayer.getStatus() != EZUIPlayer.STATUS_PLAY) {
            return null;
        }

        LocalInfo localInfo = getLocalInfo();

        if (localInfo.isSoundOpen()) {
            localInfo.setSoundOpen(false);
            mEzUiPlayer.setOpenSound(false);
            return false;
        } else {
            localInfo.setSoundOpen(true);
            mEzUiPlayer.setOpenSound(true);
            return true;
        }
    }

    private boolean getSoundOpenStatus(){
        LocalInfo localInfo = getLocalInfo();
        if (localInfo == null)
            return false;
        return localInfo.isSoundOpen();
    }

    public Boolean setVideoQa(final String qa){
        if (!isVideoNormal()){
            return null;
        }

        if (mIsRecording){
            AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
            builder.setTitle("提示");
            builder.setMessage("选择码流会终止录像，是否选择码流？");
            builder.setPositiveButton("取消", null);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopRealPlayRecord();
                    mVideoQa = qa;
                    mEzUiPlayer.stopPlay();
                    mEzUiPlayer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
                        }
                    },500);
                }
            });
            builder.show();
            return null;
        }

        mVideoQa = qa;
        mEzUiPlayer.stopPlay();
        mEzUiPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
            }
        },500);

        return null;
    }
    private AudioPlayUtil mAudioPlayUtil = null;
    public void onCapturePicBtnClick() {

        if (!isVideoNormal()){
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(_mActivity, "存储卡不可用");
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(_mActivity, "抓图失败,存储空间已满");
            return;
        }

        if (mEzUiPlayer.getEzPlayer() != null && mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
            Thread thr = new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = mEzUiPlayer.getEzPlayer().capturePicture();
                    if (bmp != null) {
                        try {
                            mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);
                            // 可以采用deviceSerial+时间作为文件命名，demo中简化，只用时间命名
                            java.util.Date date = new java.util.Date();
                            final String path = Environment.getExternalStorageDirectory().getPath() + "/DianEDun/CapturePicture/" + String.format("%tY", date)
                                    + String.format("%tm", date) + String.format("%td", date) + "/"
                                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) +".jpg";

                            if (TextUtils.isEmpty(path)) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                            EZUtils.saveCapturePictrue(path, bmp);


                            MediaScanner mMediaScanner = new MediaScanner(_mActivity);
                            mMediaScanner.scanFile(path, "jpg");
                            _mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(_mActivity, "已保存至相册"+path, Toast.LENGTH_SHORT).show();
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

//    public Boolean startPlay(){
//        if (!isVideoNormal()){
//            return null;
//        }
//
//        if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
//            //播放状态，点击停止播放
//            if (mIsRecording){
//                AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
//                builder.setTitle("提示");
//                builder.setMessage("暂停观看直播会终止录像，是否暂停观看？");
//                builder.setPositiveButton("取消", null);
//                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        stopRealPlayRecord();
//                        if (mVideoParent!=null){
//                            mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT,null);
//                        }
//                        if (mEzUiPlayer.getEzPlayer() != null && mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    final WeakReference<Bitmap> bmp = new WeakReference(mEzUiPlayer.getEzPlayer().capturePicture());
//                                    if (bmp==null || bmp.get()==null)
//                                        return;
//                                    try {
//                                        _mActivity.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                mImgPic.setImageBitmap(bmp.get());
//                                                mImgPic.setVisibility(View.VISIBLE);
//                                                mEzUiPlayer.stopPlay();
//                                            }
//                                        });
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    } finally {
//                                    }
//                                }
//                            }).start();
//                        }
//                    }
//                });
//                builder.show();
//                return null;
//            }
//
//            if (mEzUiPlayer.getEzPlayer() != null && mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final WeakReference<Bitmap> bmp = new WeakReference(mEzUiPlayer.getEzPlayer().capturePicture());
//                        if (bmp==null || bmp.get()==null)
//                            return;
//                        try {
//                            _mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mImgPic.setImageBitmap(bmp.get());
//                                    mImgPic.setVisibility(View.VISIBLE);
//                                    mEzUiPlayer.stopPlay();
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
////                            if (bmp != null && bmp.get()!=null) {
////                                bmp.get().recycle();
////                            }
//                        }
//                    }
//                }).start();
//                return false;
//            }
//        }  else if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_STOP
//                || mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_INIT) {
//            mImgPic.setVisibility(View.GONE);
//            mEzUiPlayer.startPlay();
//            return true;
//        }
//        return null;
//    }

    /**
     * 云台操作
     *
     * @param command ptz控制命令
     * @param action  控制启动/停止
     */
    public void ptzOption(final EZConstants.EZPTZCommand command, final EZConstants.EZPTZAction action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ptz_result = false;
                try {
                    ptz_result = EZOpenSDK.getInstance().controlPTZ(mDataBean.getDeviceSerial(), mEzUiPlayer.getCameraId(), command,
                            action, EZConstants.PTZ_SPEED_DEFAULT);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                LogUtil.i("VideoFragment", "controlPTZ ptzCtrl result: " + ptz_result);
            }
        }).start();
    }

    public static final int MSG_HIDE_PTZ_DIRECTION = 204;
    private void handleHidePtzDirection(Message msg) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        if (msg.arg1 > 2) {
            mImgDirection.setVisibility(GONE);
        } else {
            mImgDirection.setVisibility(msg.arg1 == 1 ? GONE : View.VISIBLE);
            Message message = new Message();
            message.what = MSG_HIDE_PTZ_DIRECTION;
            message.arg1 = msg.arg1 + 1;
            mHandler.sendMessageDelayed(message, 500);
        }
    }


    public void setPtzDirectionIv(int command) {
        if (command != -1 ) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            switch (command) {
                case RealPlayStatus.PTZ_LEFT:
                    mImgDirection.setBackgroundResource(R.drawable.left_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mImgDirection.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_RIGHT:
                    mImgDirection.setBackgroundResource(R.drawable.right_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mImgDirection.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_UP:
                    mImgDirection.setBackgroundResource(R.drawable.up_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mImgDirection.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_DOWN:
                    mImgDirection.setBackgroundResource(R.drawable.down_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.vv_ezuiplayer);
                    mImgDirection.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mImgDirection.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);
        } else {
            mImgDirection.setVisibility(GONE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        }
    }
    private boolean mIsOnTalk = false;

    public boolean getIsOnTalk(){
        return mIsOnTalk;
    }


    public void startVoiceTalk() {
        if (mEzUiPlayer == null) {
            LogUtil.debugLog("VideoEntiryFragment", "EZPlaer is null");
            return;
        }

        if (mIsOnTalk){
            stopVoiceTalk();
            return;
        }

        Utils.showToast(_mActivity, R.string.start_voice_talk);

        if (mEzUiPlayer != null) {
            mEzUiPlayer.closeSound();
        }
        mEzUiPlayer.startVoiceTalk();
        mLLRecordView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEzUiPlayer.setVoiceTalkStatus(true);
            }
        },200);
    }

    /**
     * 停止对讲
     *
     * @see
     * @since V2.0
     */
    public void stopVoiceTalk() {
        if (mEzUiPlayer!=null){
            mEzUiPlayer.stopVoiceTalk();
            mIsOnTalk = false;
            if (mVideoParent!=null)
                mVideoParent.onVideoTalkBackState(false,null);
        }
    }
}
