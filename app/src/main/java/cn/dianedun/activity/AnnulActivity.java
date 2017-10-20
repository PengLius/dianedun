package cn.dianedun.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.DetailsBean;
import cn.dianedun.bean.JbUpBean;
import cn.dianedun.bean.ToJsonBean;
import cn.dianedun.bean.UpdataBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.DataUtil;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import dev.xesam.android.toolbox.timer.CountDownTimer;
import dev.xesam.android.toolbox.timer.CountTimer;

/**
 * Created by Administrator on 2017/8/7.
 */

public class AnnulActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.rl_annul_sub)
    RelativeLayout rl_annul_sub;

    @Bind(R.id.ed_annul_cause)
    EditText ed_annul_cause;

    @Bind(R.id.img_annul_ly)
    ImageView img_annul_ly;

    @Bind(R.id.tv_annul_gdh)
    TextView tv_annul_gdh;

    @Bind(R.id.tv_annul_sqr)
    TextView tv_annul_sqr;

    @Bind(R.id.tv_annul_starttime)
    TextView tv_annul_starttime;

    @Bind(R.id.tv_annul_endtime)
    TextView tv_annul_endtime;

    @Bind(R.id.tv_annul_adr)
    TextView tv_annul_adr;

    @Bind(R.id.tv_annul_xxadr)
    TextView tv_annul_xxadr;

    @Bind(R.id.img_annul_jb)
    ImageView img_annul_jb;

    @Bind(R.id.tv_annul_headView)
    TextView tv_annul_headView;

    @Bind(R.id.rl_annul_ly)
    RelativeLayout rl_annul_ly;

    @Bind(R.id.tv_annul_lytime)
    TextView tv_annul_lytime;

    @Bind(R.id.img_annul_lyic)
    ImageView img_annul_lyic;

    @Bind(R.id.ll_annul_star)
    LinearLayout ll_annul_star;

    @Bind(R.id.img_annul_del)
    ImageView img_annul_del;

    @Bind(R.id.tv_annul_num)
    TextView tv_annul_num;


    private RelativeLayout rl_luyin_type;
    private ImageView img_luyin_uploading, img_yuyin_close, img_luyin;
    private TextView tv_luyin_timer;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DetailsBean bean;
    private MediaRecorder recorder;
    private String fileName = "/sdcard/audiorecordtest.mp3";
    private View view3;
    private PopupWindow pop3;
    private boolean offs = false;
    private CountTimer countTimer;
    private int type = 0;
    private Dialog diaglog;
    private JbUpBean updataBean;
    private MediaPlayer player;
    private ToJsonBean toJsonBean;
    private String obj2 = "";
    private CountDownTimer countDownTimer;
    private AnimationDrawable animationDrawable;
    private int playTyp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annul);
        setTvTitleText("工单撤销");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        LayoutInflater inflaters = LayoutInflater.from(this);
        view3 = inflaters.inflate(R.layout.popupwindow_luyin, null);
        rl_luyin_type = (RelativeLayout) view3.findViewById(R.id.rl_luyin_type);
        img_luyin_uploading = (ImageView) view3.findViewById(R.id.img_luyin_uploading);
        img_yuyin_close = (ImageView) view3.findViewById(R.id.img_yuyin_close);
        img_luyin = (ImageView) view3.findViewById(R.id.img_luyin);
        tv_luyin_timer = (TextView) view3.findViewById(R.id.tv_luyin_timer);

        rl_annul_sub.setOnClickListener(this);
        img_annul_ly.setOnClickListener(this);
        ll_annul_star.setOnClickListener(this);
        img_annul_del.setOnClickListener(this);
        rl_luyin_type.setOnClickListener(this);
        img_luyin_uploading.setOnClickListener(this);
        img_yuyin_close.setOnClickListener(this);

        initData();
        ed_annul_cause.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 500) {
//                    showToast("字数不超过500字");
                } else {
                    tv_annul_num.setText(s.length() + "/500");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initData() {
        ed_annul_cause.setText("");
        hashMap = new HashMap<>();
        hashMap.put("orderNum", getIntent().getStringExtra("orderNum"));
        myAsyncTast = new MyAsyncTast(AnnulActivity.this, hashMap, AppConfig.GETHANDLEORDERBYNUM, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {

            }

            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DetailsBean.class);
                tv_annul_gdh.setText(bean.getData().getOrderNum() + "");
                tv_annul_sqr.setText(bean.getData().getHandlePersion() + "");
                String beginTime = bean.getData().getBeginTime() / 1000 + "";
                String endTime = bean.getData().getEndTime() / 1000 + "";
                tv_annul_starttime.setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm") + ":00");
                tv_annul_endtime.setText(DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm") + ":00");
                tv_annul_xxadr.setText(bean.getData().getAddress() + "");
                tv_annul_adr.setText(bean.getData().getDepartName());
                if (bean.getData().getUrgency() == 0) {
                    img_annul_jb.setImageResource(R.mipmap.home_jg_yellow);
                } else {
                    img_annul_jb.setImageResource(R.mipmap.home_jg_red);
                }
            }
        });
        myAsyncTast.execute();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_annul_ly:
                if (obj2.equals("")) {
                    showDialog3();
                } else {
                    showToast("已存在录音文件");
                }
                break;
            case R.id.rl_annul_sub:
                //提交
                hashMap = new HashMap<>();
                hashMap.put("Num", bean.getData().getOrderNum() + "");
                hashMap.put("type", "6");
                if (obj2.equals("") || obj2 == null) {
                    if (ed_annul_cause.getText() == null || ed_annul_cause.getText().toString().equals("")) {
                        showToast("请填写撤销原因");
                    } else {
                        hashMap.put("remark", ed_annul_cause.getText().toString());
                        myAsyncTast = new MyAsyncTast(AnnulActivity.this, hashMap, AppConfig.REVOKEDHANDLERORDER, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                            @Override
                            public void onError(String result) {
                            }

                            @Override
                            public void send(String result) {
                                showToast("撤销成功");
                                finish();
                            }
                        });
                        myAsyncTast.execute();
                    }
                } else {
                    hashMap.put("jsonStr", obj2);
                    if (ed_annul_cause.getText() != null) {
                        hashMap.put("remark", ed_annul_cause.getText().toString());
                    }
                    myAsyncTast = new MyAsyncTast(AnnulActivity.this, hashMap, AppConfig.REVOKEDHANDLERORDER, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                        @Override
                        public void onError(String result) {
                        }

                        @Override
                        public void send(String result) {
                            showToast("撤销成功");
                            finish();
                        }
                    });
                    myAsyncTast.execute();
                }
                break;
            case R.id.rl_luyin_type:
                //开始录音
                if (type == 0) {
                    type = 1;
                    recorder.start();
                    offs = true;
                    img_luyin.setImageResource(R.mipmap.zanting);
                    countTimer.start();
                } else if (type == 1) {
                    img_luyin.setImageResource(R.mipmap.bofang);
                    recorder.pause();
                    countTimer.pause();
                    type = 4;
                } else if (type == 3) {
                    showToast("录音时间到");
                } else if (type == 4) {
                    type = 1;
                    offs = true;
                    img_luyin.setImageResource(R.mipmap.zanting);
                    recorder.start();
                    countTimer.resume();
                }
                break;
            case R.id.img_luyin_uploading:
                //上传录音
                MyAsync myAsync = new MyAsync();
                myAsync.execute();
                break;
            case R.id.img_yuyin_close:
                //关闭录音弹窗
                pop3.dismiss();
                break;
            case R.id.ll_annul_star:
                //播放
                if (playTyp == 0) {
                    player.start();
                    countDownTimer.start();
                    playTyp = 1;
                    img_annul_lyic.setImageResource(R.drawable.animation1);
                    animationDrawable = (AnimationDrawable) img_annul_lyic.getDrawable();
                    animationDrawable.start();
                } else if (playTyp == 1) {
                    img_annul_lyic.setImageResource(R.mipmap.yp_bf);
                    player.pause();
                    countDownTimer.pause();
                    playTyp = 2;
                    animationDrawable.stop();
                } else {
                    img_annul_lyic.setImageResource(R.drawable.animation1);
                    animationDrawable = (AnimationDrawable) img_annul_lyic.getDrawable();
                    player.start();
                    countDownTimer.resume();
                    playTyp = 1;
                    animationDrawable.start();
                }
                break;
            case R.id.img_annul_del:
                //隐藏录音
                rl_annul_ly.setVisibility(View.GONE);
                obj2 = "";
                break;

            default:
                break;
        }
    }

    /**
     * 录音弹窗
     */
    private void showDialog3() {
        // TODO Auto-generated method stub
        countTimer = new CountTimer(1000) {
            @Override
            public void onTick(long millisFly) {
                long js = millisFly / 1000;
                long minute = millisFly / 1000 / 60;
                long second = (millisFly - (minute * 1000 * 60)) / 1000;
                if (minute < 10) {
                    if (second < 10) {
                        tv_luyin_timer.setText("0" + minute + ":" + "0" + second);
                    } else {
                        tv_luyin_timer.setText("0" + minute + ":" + second);
                    }
                } else {
                    if (second < 10) {
                        tv_luyin_timer.setText(minute + ":" + "0" + second);
                    } else {
                        tv_luyin_timer.setText(minute + ":" + second);
                    }
                }

                if (js == 600 || js > 600) {
                    countTimer.cancel();
                    img_luyin.setImageResource(R.mipmap.bofang);
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    type = 3;
                    showToast("录音时间到");
                    offs = false;
                }
            }
        };

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(fileName);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pop3 = new PopupWindow(view3, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        pop3.setTouchable(true);
        pop3.setFocusable(true);
        pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDismiss() {
                img_luyin.setImageResource(R.mipmap.yuyin);
                tv_luyin_timer.setText("00:00");
                countTimer.cancel();
                if (recorder != null && offs) {
                    recorder.stop();
                    recorder.release();
                }
                recorder = null;
                offs = false;
                type = 0;
            }
        });
        pop3.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置好参数之后再show
        pop3.showAsDropDown(tv_annul_headView);
    }

    private class MyAsync extends AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diaglog = createLoadingDialog(AnnulActivity.this, "上传文件中请稍后...");
            diaglog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            RequestParams param = new RequestParams(AppConfig.UPLOADFILE);
            param.addBodyParameter("file", new File(fileName));
            param.addBodyParameter("optionType", "1");
            param.addHeader("token", App.getInstance().getToken());
            x.http().post(param,
                    new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            updataBean = GsonUtil.parseJsonWithGson(result, JbUpBean.class);
                            if (updataBean.getCode() == 0) {
                                player = MediaPlayer.create(getApplicationContext(), Uri.parse(updataBean.getData().get(0)));
                                showToast("上传成功");
                                rl_annul_ly.setVisibility(View.VISIBLE);
                                pop3.dismiss();
                                toJsonBean = new ToJsonBean();
                                toJsonBean.setOptionType(1);
                                toJsonBean.setContents(updataBean.getData().get(0));
                                toJsonBean.setType(6);
                                Gson gson = new Gson();
                                obj2 = "[" + gson.toJson(toJsonBean) + "]";
                                long a = player.getDuration() / 1000 / 60;
                                long b = (player.getDuration() - (a * 1000 * 60)) / 1000;
                                if (a < 10) {
                                    if (b < 10) {
                                        tv_annul_lytime.setText("0" + a + "'" + "0" + b + "\"");
                                    } else {
                                        tv_annul_lytime.setText("0" + a + "'" + b + "\"");
                                    }
                                } else {
                                    if (b < 10) {
                                        tv_annul_lytime.setText(a + "'" + "0" + b + "\"");
                                    } else {
                                        tv_annul_lytime.setText(a + "'" + b + "\"");
                                    }
                                }
                                countDownTimer = new CountDownTimer(player.getDuration(), 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) { // millisUntilFinished is the left time at *Running State*
                                        long f = millisUntilFinished / 1000 / 60;
                                        long m = (millisUntilFinished - (f * 1000 * 60)) / 1000;
                                        if (f < 10) {
                                            if (m < 10) {
                                                tv_annul_lytime.setText("0" + f + "'" + "0" + m + "\"");
                                            } else {
                                                tv_annul_lytime.setText("0" + f + "'" + m + "\"");
                                            }
                                        } else {
                                            if (m < 10) {
                                                tv_annul_lytime.setText(f + "'" + "0" + m + "\"");
                                            } else {
                                                tv_annul_lytime.setText(f + "'" + m + "\"");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancel(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onPause(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onResume(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {
                                        long a = player.getDuration() / 1000 / 60;
                                        long b = (player.getDuration() - (a * 1000 * 60)) / 1000;
                                        if (a < 10) {
                                            if (b < 10) {
                                                tv_annul_lytime.setText("0" + a + "'" + "0" + b + "\"");
                                            } else {
                                                tv_annul_lytime.setText("0" + a + "'" + b + "\"");
                                            }
                                        } else {
                                            if (b < 10) {
                                                tv_annul_lytime.setText(a + "'" + "0" + b + "\"");
                                            } else {
                                                tv_annul_lytime.setText(a + "'" + b + "\"");
                                            }
                                        }
                                        if (animationDrawable != null) {
                                            animationDrawable.stop();
                                        }
                                        img_annul_lyic.setImageResource(R.mipmap.yp_bf);
                                        playTyp = 0;
                                    }
                                };
                            }
                            diaglog.dismiss();
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            showToast("上传失败");
                            diaglog.dismiss();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }

                    });
            return null;
        }

        public Dialog createLoadingDialog(Context context, String msg) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.dialog_loadings, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
            // main.xml中的ImageView
            ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
            TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.load_animation);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            tipTextView.setText(msg);// 设置加载信息
            Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
            loadingDialog.setCancelable(false);// 不可以用“返回键”取消
            loadingDialog.setContentView(layout);// 设置布局
            return loadingDialog;
        }
    }

}
