package cn.dianedun.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.bean.EZRecordFile;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.LocalInfo;
import com.videogo.util.MediaScanner;
import com.videogo.util.RotateViewUtil;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.widget.CheckTextButton;
import com.videogo.widget.TitleBar;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AudioPlayUtil;
import cn.dianedun.tools.EZUtils;
import cn.dianedun.tools.WindowSizeChangeNotifier;
import cn.dianedun.view.EZUIPlayer.EZUIPlayer;
import cn.dianedun.view.timeshaftbar.TimerShaftBar;
import cn.dianedun.view.timeshaftbar.TimerShaftRegionItem;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/10/14.
 */

public class PlayerBackActiviaty extends BaseActivity implements EZUIPlayer.EZUIPlayerCallBack {

    @Bind(R.id.av_img_back)
    ImageView mImgBack;

    @Bind(R.id.av_img_selectdate)
    ImageView mImgSelectDate;

    @Bind(R.id.ap_tv_date)
    TextView mTvTitleDate;

    /**
     * //回放相关---------------
     */
    @Bind(R.id.ap_img_play)
    ImageView mImgPlay;

    @Bind(R.id.av_ll_playbackcontainer)
    LinearLayout mLlPlayBackContainer;

    @Bind(R.id.av_rl_playback_1_)
    RelativeLayout mRlPlayback_1_;

    @Bind(R.id.av_rl_playback_2_)
    RelativeLayout mRlPlayback_2_;

    @Bind(R.id.av_rl_playback_1)
    RelativeLayout mRlPlayback_1;

    @Bind(R.id.av_rl_playback_2)
    RelativeLayout mRlPlayback_2;

    @Bind(R.id.av_rl_playback_go)
    RelativeLayout mRlPlayback_go;

    @Bind(R.id.av_rl_playback_voice)
    RelativeLayout mRlPlayBackVoice;

    @Bind(R.id.av_rl_playback_takephoto)
    RelativeLayout mRlPlayBackTakePhoto;

    @Bind(R.id.av_rl_playback_video)
    RelativeLayout mRlPlayBackVideo;

    @Bind(R.id.av_rl_playback_stream)
    RelativeLayout mRlPlayBackStream;

    @Bind(R.id.av_timershaftbar)
    TimerShaftBar mTimerShaftBar;

    @Bind(R.id.av_ezuiplayerback)
    EZUIPlayer mEZUiPlayBack;

    @Bind(R.id.realplay_err_rl)
    RelativeLayout mRlErr;

    @Bind(R.id.realplay_tip_tv)
    TextView mTvErrTip;

    /**
     * 拍照
     */

    @Bind(R.id.realplay_capture_rl)
    RelativeLayout mRealPlayCaptureRl;

    @Bind(R.id.realplay_capture_iv)
    ImageView mRealPlayCaptureIv;

    @Bind(R.id.realplay_capture_watermark_iv)
    ImageView mRealPlayCaptureWatermarkIv;

    /**
     * 地点
     */
    @Bind(R.id.av_rl_bottomlayout)
    RelativeLayout mRlBottomLayout;

    @Bind(R.id.av_tv_places)
    TextView mTvPlaces;

    @Bind(R.id.av_ctb_fullscreen)
    CheckTextButton mCtbFullscreen;

    /**
     * 录像相关
     */

    @Bind(R.id.realplay_prompt_rl)
    RelativeLayout mRlPrompt;
    @Bind(R.id.title_bar_landscape)
    TitleBar mLandscapeTitleBar;
    @Bind(R.id.realplay_ratio_tv)
    TextView mRealPlayRatioTv;
    @Bind(R.id.realplay_record_ly)
    LinearLayout mRealPlayRecordLy;
    @Bind(R.id.realplay_record_iv)
    ImageView mRealPlayRecordIv;
    @Bind(R.id.realplay_record_tv)
    TextView mRealPlayRecordTv;
    @Bind(R.id.realplay_full_ptz_prompt_iv)
    ImageView mRealPlayFullPtzPromptIv;

    @Bind(R.id.ap_img_sound)
    ImageView mRealPlaySoundBtn;

    private Handler mHandler = null;

    private ArrayList<TimerShaftRegionItem> mTimeShaftItems;
    private static final String TAG = "PlayBackActivity";
//    private MyOrientationDetector mOrientationDetector;
    public static final String APPKEY = "AppKey";
    public static final String AccessToekn = "AccessToekn";
    public static final String PLAY_URL = "play_url";
    /**
     * onresume时是否恢复播放
     */
    private boolean isResumePlay = false;

    /**
     * 开发者申请的Appkey
     */
    private String mAppkey;
    /**
     * 授权accesstoken
     */
    private String mAccesstoken;
    /**
     * 播放url：ezopen协议
     */
    private String mPlayUrl;

    /**
     * 开启回放
     *
     * @param context
     * @param accesstoken 开发者登录授权的accesstoken
     * @param url         预览url
     */
    public static void startPlayBackActivity(Context context,String accesstoken, String url) {
        Intent intent = new Intent(context, PlayerBackActiviaty.class);
        intent.putExtra(AccessToekn, accesstoken);
        intent.putExtra(PLAY_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
    }

    LocalInfo mLocalInfo = null;

    @Override
    protected void initView() {
        super.initView();
        mAppkey = getIntent().getStringExtra(APPKEY);
        mAccesstoken = getIntent().getStringExtra(AccessToekn);
        mPlayUrl = getIntent().getStringExtra(PLAY_URL);
        mLocalInfo = LocalInfo.getInstance();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mLocalInfo.setScreenWidthHeight(metric.widthPixels, metric.heightPixels);
        mLocalInfo.setNavigationBarHeight((int) Math.ceil(25 * getResources().getDisplayMetrics().density));
        mLocalInfo.setSoundOpen(false);
        //默认开启播放
        mImgPlay.setImageResource(R.mipmap.ic_nor_stop);
        //设置加载需要显示的view
        mEZUiPlayBack.setLoadingView(initProgressBar());
        preparePlay();
//        setSurfaceSize();
    }

    /**
     * 创建加载view
     *
     * @return
     */
    private ProgressBar initProgressBar() {
        ProgressBar mProgressBar = new ProgressBar(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(lp);
        return mProgressBar;
    }
    /**
     * 准备播放资源参数
     */
    private void preparePlay() {
        //设置授权accesstoken
        EZUIKit.setAccessToken(mAccesstoken);
        mEZUiPlayBack.setCallBack(this);
        //设置播放资源参数
        mEZUiPlayBack.setUrl(mPlayUrl);
    }
    //  //声音控制
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
    private void setRealPlaySound() {
        if (mEZUiPlayBack.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_PLAY) {
            if (mLocalInfo.isSoundOpen()) {
                mEZUiPlayBack.setOpenSound(true);
            } else {
                mEZUiPlayBack.setOpenSound(false);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        mOrientationDetector.enable();
        Log.d(TAG, "onResume");
        //界面stop时，如果在播放，那isResumePlay标志位置为true，resume时恢复播放
        if (isResumePlay) {
            isResumePlay = false;
            mImgPlay.setImageResource(R.mipmap.ic_nor_greenplay);
            mEZUiPlayBack.startPlay();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
//        mOrientationDetector.disable();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop + " + mEZUiPlayBack.getStatus());
        //界面stop时，如果在不是暂停和停止状态，那isResumePlay标志位置为true，以便resume时恢复播放
        if (mEZUiPlayBack.getStatus() != com.ezvizuikit.open.EZUIPlayer.STATUS_STOP && mEZUiPlayBack.getStatus() != com.ezvizuikit.open.EZUIPlayer.STATUS_PAUSE) {
            isResumePlay = true;
        }
        //停止播放
        mEZUiPlayBack.stopPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        //释放资源
        mEZUiPlayBack.releasePlayer();
    }

//    /**
//     * 屏幕旋转时调用此方法
//     */
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.d(TAG, "onConfigurationChanged");
//        setSurfaceSize();
//    }

//    @Override
//    public void onWindowSizeChanged(int w, int h, int oldW, int oldH) {
//        if (mEZUiPlayBack != null) {
//            setSurfaceSize();
//        }
//    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRlPlayBackStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("选择码流", null,"取消", null,
                        new String[]{"高清","标清","流畅"},
                        PlayerBackActiviaty.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (mEZUiPlayBack == null) {
                            return;
                        }
//                        int tag = -1;
//                        // 视频质量，2-高清，1-标清，0-流畅
//                        if (mEZUiPlayBack.getEzPlayer().getStreamFlow().getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET) {
//                            tag = 2;
//                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED) {
//                            tag = 1;
//                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_HD) {
//                            tag = 0;
//                        }
//                        if (tag == position)
//                            return;
//                        switch (position){
//                            case 0:
//                                //高清
//                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_HD);
//                                break;
//                            case 1:
//                                //标清
//                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED);
//                                break;
//                            case 2:
//                                //流畅
//                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET);
//                                break;
//                        }
                    }
                }).show();
            }
        });
        mRlPlayBackVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //声音
                onSoundBtnClick();
            }
        });
        mRlPlayBackTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                onCapturePicBtnClick();
            }
        });
        mTvTitleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择日期
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTimerShaftBar.setTimerShaftLayoutListener(new TimerShaftBar.TimerShaftBarListener() {
            @Override
            public void onTimerShaftBarPosChanged(Calendar calendar) {
                Log.d(TAG, "onTimerShaftBarPosChanged " + calendar.getTime().toString());
                mEZUiPlayBack.seekPlayback(calendar);
            }

            @Override
            public void onTimerShaftBarDown() {

            }
        });

        mRlPlayback_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEZUiPlayBack.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_PLAY) {
                    //播放状态，点击停止播放
                    mImgPlay.setImageResource(R.mipmap.ic_nor_greenplay);
                    mEZUiPlayBack.pausePlay();
                } else if (mEZUiPlayBack.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_PAUSE) {
                    //停止状态，点击播放
                    mImgPlay.setImageResource(R.mipmap.ic_nor_stop);
                    mEZUiPlayBack.resumePlay();
                } else if (mEZUiPlayBack.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_STOP
                        || mEZUiPlayBack.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_INIT) {
                    mImgPlay.setImageResource(R.mipmap.ic_nor_stop);
                    mEZUiPlayBack.startPlay();
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
        if (mIsRecording) {
            stopRealPlayRecord();
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(PlayerBackActiviaty.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(PlayerBackActiviaty.this, R.string.remoteplayback_record_fail_for_memory);
            return;
        }

        if (mEZUiPlayBack.getEzPlayer() != null) {
//            updateCaptureUI();
            mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);

            // 可以采用deviceSerial+时间作为文件命名，demo中简化，只用时间命名
            java.util.Date date = new java.util.Date();
            String strRecordFile = Environment.getExternalStorageDirectory().getPath() + "/EZOpenSDK/Records/" + String.format("%tY", date)
                    + String.format("%tm", date) + String.format("%td", date) + "/"
                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) + ".mp4";

            if (mEZUiPlayBack.getEzPlayer().startLocalRecordWithFile(strRecordFile))
            {
                handleRecordSuccess(strRecordFile);
            } else {
                handleRecordFail();
            }
        }
    }
    private void handleRecordFail() {
        Utils.showToast(PlayerBackActiviaty.this, R.string.remoteplayback_record_fail);
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
    @Bind(R.id.av_fl_videocontainer)
    FrameLayout mRealPlayRecordContainer;
    @Bind(R.id.av_img_videostart)
    ImageView mImgVideoStart;
    @Bind(R.id.av_img_videostop)
    ImageView mImgVideoStop;
    protected RotateViewUtil mRecordRotateViewUtil;
    private void handleRecordSuccess(String recordFilePath) {
        // 设置录像按钮为check状态
//        if (!mIsOnStop) {
//            mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStart,
//                    mImgVideoStop, 0, 90);
//        } else {
//            mImgVideoStart.setVisibility(GONE);
//            mImgVideoStop.setVisibility(View.VISIBLE);
//        }
        mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStart,
                mImgVideoStop, 0, 90);
        mIsRecording = true;
        // 计时按钮可见
        mRlPrompt.setVisibility(View.VISIBLE);
        mRealPlayRecordLy.setVisibility(View.VISIBLE);
        mRealPlayRecordTv.setText("00:00");
        mRecordSecond = 0;
    }
    /**
     * 停止录像
     *
     * @see
     * @since V1.0
     */
    private void stopRealPlayRecord() {
        if (mEZUiPlayBack.getEzPlayer() == null || !mIsRecording) {
            return;
        }
        Toast.makeText(PlayerBackActiviaty.this, getResources().getString(R.string.already_saved_to_volume), Toast.LENGTH_SHORT).show();

        // 设置录像按钮为check状态
//        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
//            if (!mIsOnStop) {
//                mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStop,
//                        mImgVideoStart, 0, 90);
//            } else {
//                mImgVideoStop.setVisibility(GONE);
//                mImgVideoStart.setVisibility(View.VISIBLE);
//            }
//        } else {
//            mImgVideoStop.setVisibility(GONE);
//            mImgVideoStart.setVisibility(View.VISIBLE);
//        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
        mEZUiPlayBack.getEzPlayer().stopLocalRecord();

        // 计时按钮不可见
        mRlPrompt.setVisibility(GONE);
        mRealPlayRecordLy.setVisibility(GONE);
        mIsRecording = false;
//        updateCaptureUI();
    }
    /**
     * 抓拍按钮响应函数
     *
     * @since V1.0
     */
    private AudioPlayUtil mAudioPlayUtil = null;
    private void onCapturePicBtnClick() {

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(PlayerBackActiviaty.this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(PlayerBackActiviaty.this, R.string.remoteplayback_capture_fail_for_memory);
            return;
        }

        if (mEZUiPlayBack.getEzPlayer() != null && mEZUiPlayBack.getStatus() == EZUIPlayer.STATUS_PLAY) {
            Thread thr = new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = mEZUiPlayBack.getEzPlayer().capturePicture();
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


                            MediaScanner mMediaScanner = new MediaScanner(PlayerBackActiviaty.this);
                            mMediaScanner.scanFile(path, "jpg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PlayerBackActiviaty.this, getResources().getString(R.string.already_saved_to_volume)+path, Toast.LENGTH_SHORT).show();
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
    protected void initData() {
        super.initData();
        // 获取本地信息
        mAudioPlayUtil = AudioPlayUtil.getInstance(App.getInstance());
        mRecordRotateViewUtil = new RotateViewUtil();

//        mHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if (this.isFinishing()) {
//                    return false;
//                }
//                switch (msg.what) {
//                    case EZConstants.EZRealPlayConstants.MSG_GET_CAMERA_INFO_SUCCESS:
//                        updateLoadingProgress(20);
//                        handleGetCameraInfoSuccess();
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_START:
//                        updateLoadingProgress(40);
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_CONNECTION_START:
//                        updateLoadingProgress(60);
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_CONNECTION_SUCCESS:
//                        updateLoadingProgress(80);
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
//                        handlePlaySuccess(msg);
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
//                        handlePlayFail(msg.obj);
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_SET_VEDIOMODE_SUCCESS:
//                        handleSetVedioModeSuccess();
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_SET_VEDIOMODE_FAIL:
//                        handleSetVedioModeFail(msg.arg1);
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_PTZ_SET_FAIL:
//                        handlePtzControlFail(msg);
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_VOICETALK_SUCCESS:
//                        handleVoiceTalkSucceed();
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_VOICETALK_STOP:
//                        handleVoiceTalkStoped();
//                        break;
//                    case EZConstants.EZRealPlayConstants.MSG_REALPLAY_VOICETALK_FAIL:
//                        ErrorInfo errorInfo = (ErrorInfo) msg.obj;
//                        handleVoiceTalkFailed(errorInfo);
//                        break;
//                    case MSG_PLAY_UI_UPDATE:
//                        updateRealPlayUI();
//                        break;
//                    case MSG_AUTO_START_PLAY:
//                        startRealPlay();
//                        break;
//                    case MSG_CLOSE_PTZ_PROMPT:
//                        mRealPlayFullPtzPromptIv.setVisibility(GONE);
//                        break;
//                    case MSG_HIDE_PTZ_DIRECTION:
//                        handleHidePtzDirection(msg);
//                        break;
//                    case MSG_HIDE_PAGE_ANIM:
//                        hidePageAnim();
//                        break;
//                    case MSG_PLAY_UI_REFRESH:
//                        initUI();
//                        break;
//                    case MSG_PREVIEW_START_PLAY:
//                        mPageAnimIv.setVisibility(GONE);
////                mRealPlayPreviewTv.setVisibility(View.GONE);
//                        mStatus = RealPlayStatus.STATUS_INIT;
//                        startRealPlay();
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
    }


    @Override
    public void onPlayFail(EZUIError var1) {
        showToast(var1.getErrorString());
        mImgPlay.setImageResource(R.mipmap.ic_nor_greenplay);
        if (var1.getInternalErrorCode() == 0){
            mRlErr.setVisibility(View.VISIBLE);
            mTvErrTip.setText(var1.getErrorString());
        }
    }

    @Override
    public void onPlayFinish() {

    }

    @Override
    public void onPlaySuccess() {
        Log.d(TAG,"onPlaySuccess");
        mRlErr.setVisibility(GONE);
        mTimerShaftBar.setRefereshPlayTimeWithPlayer();
        mImgPlay.setImageResource(R.mipmap.ic_nor_stop);
    }

    @Override
    public void onPlayTime(Calendar calendar) {
        Log.d(TAG,"onPlayTime calendar");
        if (calendar != null) {
            // TODO: 2017/2/16 当前播放时间
            Log.d(TAG,"onPlayTime calendar = "+calendar.getTime().toString());
            mTimerShaftBar.setPlayCalendar(calendar);
        }
    }

    @Override
    public void onPrepared() {
        Log.d(TAG,"onPrepared");
        ArrayList<EZRecordFile> mlist = (ArrayList) mEZUiPlayBack.getPlayList();
        if (mlist != null && mlist.size() > 0) {
            mTimeShaftItems = new ArrayList<TimerShaftRegionItem>();
            for (int i = 0; i < mlist.size(); i++) {
                TimerShaftRegionItem timeShaftItem = new TimerShaftRegionItem(mlist.get(i).getStartTime(),  mlist.get(i).getEndTime(), mlist.get(i).getRecType());
                mTimeShaftItems.add(timeShaftItem);
            }
            mTimerShaftBar.setTimeShaftItems(mTimeShaftItems);
        }
        mEZUiPlayBack.startPlay();
    }

    @Override
    public void onShowLoading() {

    }

    @Override
    public void onVideoSizeChange(int var1, int var2) {

    }
}
