package cn.dianedun.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.videogo.exception.InnerException;
import com.videogo.openapi.bean.EZRecordFile;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.MediaScanner;
import com.videogo.util.RotateViewUtil;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.widget.CheckTextButton;
import com.videogo.widget.TitleBar;
import com.vise.xsnow.manager.AppManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AudioPlayUtil;
import cn.dianedun.tools.EZUtils;
import cn.dianedun.tools.ScreenOrientationHelper;
import cn.dianedun.tools.WindowSizeChangeNotifier;
import cn.dianedun.view.DateTimeDialogOnlyYMD;
import cn.dianedun.view.EZUIPlayer.EZUIPlayer;
import cn.dianedun.view.timeshaftbar.TimerShaftBar;
import cn.dianedun.view.timeshaftbar.TimerShaftRegionItem;

import static android.view.View.GONE;
import static cn.dianedun.activity.VideoShowActivity.MSG_PLAY_UI_UPDATE;

/**
 * Created by Administrator on 2017/10/14.
 */

public class PlayerBackActiviaty extends BaseActivity implements EZUIPlayer.EZUIPlayerCallBack ,
        WindowSizeChangeNotifier.OnWindowSizeChangedListener {

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

//    @Bind(R.id.av_rl_playback_stream)
//    RelativeLayout mRlPlayBackStream;

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

    private ArrayList<TimerShaftRegionItem> mTimeShaftItems;
    private static final String TAG = "PlayBackActivity";
//    private MyOrientationDetector mOrientationDetector;
    public static final String APPKEY = "AppKey";
    public static final String AccessToekn = "AccessToekn";
    public static final String PLAY_URL = "play_url";
    public static final String PLAY_PRE_URL = "PLAY_PRE_URL";
    public static final String PLAY_DATE = "play_date";
    public static final String PLACENAME = "playname";
    /**
     * onresume时是否恢复播放
     */
    private boolean isResumePlay = false;

    /**
     * 授权accesstoken
     */
    private String mAccesstoken;
    /**
     * 播放url：ezopen协议
     */
    private String mPlayUrl,mPlayPreUrl;

    /**
     * 开启回放
     *
     * @param context
     * @param accesstoken 开发者登录授权的accesstoken
     * @param url         预览url
     */
    public static void startPlayBackActivity(Context context,String accesstoken, String url,String prefixStr,Calendar date,String placesName) {
        Intent intent = new Intent(context, PlayerBackActiviaty.class);
        intent.putExtra(AccessToekn, accesstoken);
        intent.putExtra(PLAY_URL, url);
        intent.putExtra(PLAY_PRE_URL, prefixStr);
        intent.putExtra(PLAY_DATE, date);
        intent.putExtra(PLACENAME, placesName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
    }

    LocalInfo mLocalInfo = null;

    private String getMonth(Calendar calendar){
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10){
            return "0" + month;
        }
        return String.valueOf(month);
    }

//    private Date mCurDate;
    private Calendar mCurCalendar;
    @Override
    protected void initView() {
        super.initView();
        mAccesstoken = getIntent().getStringExtra(AccessToekn);
        mPlayUrl = getIntent().getStringExtra(PLAY_URL);
        mPlayPreUrl = getIntent().getStringExtra(PLAY_PRE_URL);
        mCurCalendar = (Calendar) getIntent().getSerializableExtra(PLAY_DATE);
        mTvPlaces.setText(getIntent().getStringExtra(PLACENAME));
        mTimerShaftBar.setRefereshPlayTimeWithPlayer();
        mCurCalendar.set(Calendar.HOUR,0);
        mCurCalendar.set(Calendar.MINUTE,0);
        mCurCalendar.set(Calendar.SECOND,0);
        mTimerShaftBar.setPlayCalendar(mCurCalendar);
        int month = mCurCalendar.get(Calendar.MONTH) + 1;
        mTvTitleDate.setText(mCurCalendar.get(Calendar.YEAR) + "年" +  month + "月" + mCurCalendar.get(Calendar.DAY_OF_MONTH) + "日");
        mLocalInfo = LocalInfo.getInstance();
        mEZUiPlayBack.setbPlayBackUse(true);
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
        mScreenOrientationHelper = null;
    }

    @Override
    public void onBackPressedSupport() {
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            mScreenOrientationHelper.portrait();
            return;
        }
        if (mLocalInfo!=null)
            mLocalInfo.setSoundOpen(false);
        super.onBackPressedSupport();
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
//        mRlPlayback_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        mCtbFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrientation();
            }
        });
        mRlPlayBackVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //录像
                if (!TextUtils.isEmpty(mPlayErrStr)) {
                    showToast(mPlayErrStr);
                    return;
                }
                onRecordBtnClick();
            }
        });
//        mRlPlayBackStream.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(mPlayErrStr)) {
//                    showToast(mPlayErrStr);
//                    return;
//                }
//                new AlertView("选择码流", null,"取消", null,
//                        new String[]{"高清","标清","流畅"},
//                        PlayerBackActiviaty.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        if (mEZUiPlayBack == null) {
//                            return;
//                        }
////                        int tag = -1;
////                        // 视频质量，2-高清，1-标清，0-流畅
////                        if (mEZUiPlayBack.getEzPlayer().getStreamFlow().getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET) {
////                            tag = 2;
////                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED) {
////                            tag = 1;
////                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_HD) {
////                            tag = 0;
////                        }
////                        if (tag == position)
////                            return;
////                        switch (position){
////                            case 0:
////                                //高清
////                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_HD);
////                                break;
////                            case 1:
////                                //标清
////                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED);
////                                break;
////                            case 2:
////                                //流畅
////                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET);
////                                break;
////                        }
//                    }
//                }).show();
//            }
//        });
        mRlPlayBackVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mPlayErrStr)) {
                    showToast(mPlayErrStr);
                    return;
                }
                //声音
                onSoundBtnClick();
            }
        });
        mRlPlayBackTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mPlayErrStr)) {
                    showToast(mPlayErrStr);
                    return;
                }
                //拍照
                onCapturePicBtnClick();
            }
        });
        mTvTitleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择日期
                if (mIsRecording) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerBackActiviaty.this);
                    builder.setTitle("提示");
                    builder.setMessage("当前正在录像，是否选择回放日期？选择日期后录像将停止并自动保存");
                    builder.setPositiveButton("取消", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopRealPlayRecord();
                            new DateTimeDialogOnlyYMD(PlayerBackActiviaty.this, new DateTimeDialogOnlyYMD.MyOnDateSetListener() {
                                @Override
                                public void onDateSet(Date date) {
                                    mCurCalendar.setTime(date);
                                    final Calendar tempCalenar = (Calendar)mCurCalendar.clone();
                                    mCurCalendar.set(Calendar.HOUR,0);
                                    mCurCalendar.set(Calendar.MINUTE,0);
                                    mCurCalendar.set(Calendar.SECOND,0);
                                    mTimerShaftBar.setPlayCalendar(mCurCalendar);
                                    int m = mCurCalendar.get(Calendar.MONTH) + 1;
                                    final String url = mPlayPreUrl + mCurCalendar.get(Calendar.YEAR) + m + mCurCalendar.get(Calendar.DAY_OF_MONTH);
                                    mTvTitleDate.setText(mCurCalendar.get(Calendar.YEAR) + "年" +  m + "月" + mCurCalendar.get(Calendar.DAY_OF_MONTH) + "日");

                                    mEZUiPlayBack.stopPlay();
                                    mEZUiPlayBack.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tempCalenar.setTimeInMillis(0);
                                            mEZUiPlayBack.seekPlayback(tempCalenar);
                                            mEZUiPlayBack.setUrl(url);
                                        }
                                    },500);
                                }
                            },true,true,true).show();
//                            new DatePickerDialog(PlayerBackActiviaty.this, new DatePickerDialog.OnDateSetListener() {
//                                @Override
//                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                    mCurCalendar.set(year,month,dayOfMonth);
//                                    final Calendar tempCalenar = (Calendar)mCurCalendar.clone();
//                                    mTimerShaftBar.setPlayCalendar(mCurCalendar);
//                                    int m = month + 1;
//                                    final String url = mPlayPreUrl + year + m + dayOfMonth;
//                                    mTvTitleDate.setText(year + "年" +  m + "月" + dayOfMonth + "日");
//
//                                    mEZUiPlayBack.stopPlay();
//                                    mEZUiPlayBack.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            tempCalenar.setTimeInMillis(0);
//                                            mEZUiPlayBack.seekPlayback(tempCalenar);
//                                            mEZUiPlayBack.setUrl(url);
//                                        }
//                                    },500);
//                                }
//                            },mCurCalendar.get(Calendar.YEAR),mCurCalendar.get(Calendar.MONTH),mCurCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    builder.show();
                    return;
                }else{
                    new DateTimeDialogOnlyYMD(PlayerBackActiviaty.this, new DateTimeDialogOnlyYMD.MyOnDateSetListener() {
                        @Override
                        public void onDateSet(Date date) {
                            mCurCalendar.setTime(date);
                            final Calendar tempCalenar = (Calendar)mCurCalendar.clone();
                            mCurCalendar.set(Calendar.HOUR,0);
                            mCurCalendar.set(Calendar.MINUTE,0);
                            mCurCalendar.set(Calendar.SECOND,0);
                            mTimerShaftBar.setPlayCalendar(mCurCalendar);
                            int m = mCurCalendar.get(Calendar.MONTH) + 1;
                            final String url = mPlayPreUrl + mCurCalendar.get(Calendar.YEAR) + m + mCurCalendar.get(Calendar.DAY_OF_MONTH);
                            mTvTitleDate.setText(mCurCalendar.get(Calendar.YEAR) + "年" +  m + "月" + mCurCalendar.get(Calendar.DAY_OF_MONTH) + "日");

                            mEZUiPlayBack.stopPlay();
                            mEZUiPlayBack.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tempCalenar.setTimeInMillis(0);
                                    mEZUiPlayBack.seekPlayback(tempCalenar);
                                    mEZUiPlayBack.setUrl(url);
                                }
                            },500);
                        }
                    },true,true,true).show();
//                    new DatePickerDialog(PlayerBackActiviaty.this, new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                            mCurCalendar.set(year,month,dayOfMonth);
//                            final Calendar tempCalenar = (Calendar)mCurCalendar.clone();
//                            mTimerShaftBar.setPlayCalendar(mCurCalendar);
//                            int m = month + 1;
//                            final String url = mPlayPreUrl + year + m + dayOfMonth;
//                            mTvTitleDate.setText(year + "年" +  m + "月" + dayOfMonth + "日");
//
//                            mEZUiPlayBack.stopPlay();
//                            mEZUiPlayBack.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    tempCalenar.setTimeInMillis(0);
//                                    mEZUiPlayBack.seekPlayback(tempCalenar);
//                                    mEZUiPlayBack.setUrl(url);
//                                }
//                            },500);
//                        }
//                    },mCurCalendar.get(Calendar.YEAR),mCurCalendar.get(Calendar.MONTH),mCurCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
//                DateTimeDialog dateTimeDialog = new DateTimeDialog(PlayerBackActiviaty.this, mCurDate, PlayerBackActiviaty.this, 0);
//                dateTimeDialog.show();
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocalInfo!=null)
                    mLocalInfo.setSoundOpen(false);
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
                if (!TextUtils.isEmpty(mPlayErrStr)) {
                    showToast(mPlayErrStr);
                    return;
                }
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
     * 屏幕当前方向
     */
    @Bind(R.id.av_rl_titlelayout)
    RelativeLayout mRlTitleLayout;
    @Bind(R.id.av_fl_displaycontainer)
    FrameLayout mFlDisplayContainer;
    ViewGroup.LayoutParams mSvLayoutParams;
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    private void updateOperatorUI() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            // 显示状态栏
            fullScreen(false);
//            updateOrientation();
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
    }
    private void updateOrientation() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private int mForceOrientation = 0;
    private ScreenOrientationHelper mScreenOrientationHelper;
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
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_PLAY_UI_UPDATE:
                    updateRealPlayUI();
                    break;
            }
            return false;
        }
    });
    private void updateRealPlayUI() {
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
    /**
     * 启动定时器
     *
     * @see
     * @since V1.0
     */
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
//                if (mLandscapeTitleBar != null && (mLandscapeTitleBar.getVisibility() == View.VISIBLE )
//                        && mControlDisplaySec < 5) {
//                    mControlDisplaySec++;
//                }
//                if (mRealPlayCaptureRl != null && mRealPlayCaptureRl.getVisibility() == View.VISIBLE
//                        && mCaptureDisplaySec < 4) {
//                    mCaptureDisplaySec++;
//                }

                // 更新录像时间
                if (mEZUiPlayBack != null && mIsRecording) {
                    // 更新录像时间
                    Calendar OSDTime = mEZUiPlayBack.getOSDTime();
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
     * 开始录像
     *
     * @see
     * @since V1.0
     */
    private boolean mIsRecording = false;
    private String mRecordTime = null;
    private String mCurRecordName;
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
    private boolean mIsOnStop = false;
    private void handleRecordSuccess(String recordFilePath) {
        EZUtils.updateVideo(this,recordFilePath);
        // 设置录像按钮为check状态
        if (!mIsOnStop) {
            mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStart,
                    mImgVideoStop, 0, 90);
        } else {
            mImgVideoStart.setVisibility(GONE);
            mImgVideoStop.setVisibility(View.VISIBLE);
        }
        mCurRecordName = "已将录像保存至目录：" + recordFilePath;
        mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStart,
                mImgVideoStop, 0, 90);
        mIsRecording = true;
        // 计时按钮可见
        mRlPrompt.setVisibility(View.VISIBLE);
        mRealPlayRecordLy.setVisibility(View.VISIBLE);
        mRealPlayRecordTv.setText("00:00");
        mRecordSecond = 0;
        startUpdateTimer();
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
//        Toast.makeText(PlayerBackActiviaty.this, getResources().getString(R.string.already_saved_to_volume), Toast.LENGTH_SHORT).show();
        Toast.makeText(PlayerBackActiviaty.this, mCurRecordName, Toast.LENGTH_SHORT).show();
        mCurRecordName = "";
        // 设置录像按钮为check状态
        if (!mIsOnStop) {
            mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mImgVideoStop,
                    mImgVideoStart, 0, 90);
        } else {
            mImgVideoStop.setVisibility(GONE);
            mImgVideoStart.setVisibility(View.VISIBLE);
        }
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
        mOrientationDetector = new MyOrientationDetector(this);
        new WindowSizeChangeNotifier(this, this);
        mSvLayoutParams = mFlDisplayContainer.getLayoutParams();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mOrientation = newConfig.orientation;
        updateOperatorUI();
        setSurfaceSize();
    }
    private MyOrientationDetector mOrientationDetector;

    private void setSurfaceSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        boolean isWideScrren = mOrientationDetector.isWideScrren();
        //竖屏
        if (!isWideScrren) {
            //竖屏调整播放区域大小，宽全屏，高根据视频分辨率自适应
//            mEZUiPlayBack.setSurfaceSize(dm.widthPixels, 0);
        } else {
            //横屏屏调整播放区域大小，宽、高均全屏，播放区域根据视频分辨率自适应
            mEZUiPlayBack.setSurfaceSize(dm.widthPixels, dm.heightPixels);
        }
    }

    @Override
    public void onWindowSizeChanged(int w, int h, int oldW, int oldH) {
//        if (mEZUiPlayBack != null) {
//            setSurfaceSize();
//        }
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
    @Override
    public void onPlayFail(EZUIError var1) {
        showToast(var1.getErrorString());
        mImgPlay.setImageResource(R.mipmap.ic_nor_greenplay);
        if (var1.getInternalErrorCode() == 0){
            mRlErr.setVisibility(View.VISIBLE);
            mTvErrTip.setText(var1.getErrorString());
        }
        mPlayErrStr = var1.getErrorString();
    }

    private String mPlayErrStr = "正在加载，请稍后";

    @Override
    public void onPlayFinish() {

    }

    @Override
    public void onPlaySuccess() {
        Log.d(TAG,"onPlaySuccess");
        mPlayErrStr = null;
        mRlErr.setVisibility(GONE);
        mTimerShaftBar.setRefereshPlayTimeWithPlayer();
        mImgPlay.setImageResource(R.mipmap.ic_nor_stop);
//        updateOrientation();
    }

    @Override
    public void onPlayTime(Calendar calendar) {
        Log.d(TAG,"onPlayTime calendar");
        if (calendar != null) {
            Log.d(TAG,"onPlayTime calendar = "+calendar.getTime().toString());
            mTimerShaftBar.setPlayCalendar(calendar);
        }
    }

    @Override
    public void onPrepared() {
        Log.d(TAG,"onPrepared");
        ArrayList<EZRecordFile> mlist = (ArrayList) mEZUiPlayBack.getPlayList();
        if (mlist != null && mlist.size() > 0) {
            mTimeShaftItems = new ArrayList<>();
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

