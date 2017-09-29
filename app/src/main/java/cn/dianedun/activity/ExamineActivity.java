package cn.dianedun.activity;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.DetailsBean;
import cn.dianedun.bean.ResultBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.DataUtil;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import dev.xesam.android.toolbox.timer.CountDownTimer;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ExamineActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.tv_examine_pass)
    TextView tv_examine_pass;

    @Bind(R.id.tv_examine_reject)
    TextView tv_examine_reject;

    @Bind(R.id.ed_examine_cause)
    EditText ed_examine_cause;

    @Bind(R.id.rl_examine_sub)
    RelativeLayout rl_examine_sub;

    @Bind(R.id.ll_examine_pass)
    LinearLayout ll_examine_pass;

    @Bind(R.id.img_examine_pass)
    ImageView img_examine_pass;

    @Bind(R.id.ll_examine_reject)
    LinearLayout ll_examine_reject;

    @Bind(R.id.img_examine_reject)
    ImageView img_examine_reject;

    @Bind(R.id.tv_examine_gdh)
    TextView tv_examine_gdh;

    @Bind(R.id.tv_examine_sqr)
    TextView tv_examine_sqr;

    @Bind(R.id.tv_examine_starttime)
    TextView tv_examine_starttime;

    @Bind(R.id.tv_examine_endtime)
    TextView tv_examine_endtime;

    @Bind(R.id.tv_examine_cause)
    TextView tv_examine_cause;

    @Bind(R.id.tv_annul_adr)
    TextView tv_annul_adr;

    @Bind(R.id.img_examine_sta)
    ImageView img_examine_sta;

    @Bind(R.id.tv_examine_numb)
    TextView tv_examine_numb;

    @Bind(R.id.rl_examine)
    RelativeLayout rl_examine;

    @Bind(R.id.img_examine_yy)
    ImageView img_examine_yy;

    @Bind(R.id.tv_examine_time)
    TextView tv_examine_time;


    private String applyStatus = "0";
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DetailsBean bean;
    private String contents;
    private MediaPlayer player;
    private CountDownTimer countDownTimer;
    private AnimationDrawable animationDrawable;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);
        setTvTitleText("工单审批");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);

        ll_examine_reject.setOnClickListener(this);
        ll_examine_pass.setOnClickListener(this);
        rl_examine_sub.setOnClickListener(this);
        rl_examine.setOnClickListener(this);

        initData();
        ed_examine_cause.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 500) {
//                    showToast("字数不超过500字");
                } else {
                    tv_examine_numb.setText(s.length() + "/500");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initData() {
        hashMap = new HashMap<>();
        hashMap.put("orderNum", getIntent().getStringExtra("orderNum"));
        myAsyncTast = new MyAsyncTast(ExamineActivity.this, hashMap, AppConfig.GETHANDLEORDERBYNUM, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {

            }

            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DetailsBean.class);
                tv_examine_gdh.setText(bean.getData().getOrderNum() + "");
                tv_examine_sqr.setText(bean.getData().getHandlePersion() + "");
                String beginTime = bean.getData().getBeginTime() / 1000 + "";
                String endTime = bean.getData().getEndTime() / 1000 + "";
                tv_examine_starttime.setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm"));
                tv_examine_endtime.setText(DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm"));
                tv_examine_cause.setText(bean.getData().getCause() + "");
                tv_annul_adr.setText(bean.getData().getAddress() + "");
                if (bean.getData().getUrgency() == 0) {
                    img_examine_sta.setImageResource(R.mipmap.home_jg_yellow);
                } else {
                    img_examine_sta.setImageResource(R.mipmap.home_jg_red);
                }
                if (bean.getData().getAlertOptionsArray().size() > 0) {
                    for (int i = 0; i < bean.getData().getAlertOptionsArray().size(); i++) {
                        if (bean.getData().getAlertOptionsArray().get(i).getOptionType() == 1) {
                            rl_examine.setVisibility(View.VISIBLE);
                            contents = bean.getData().getAlertOptionsArray().get(i).getContents();
                        }
                    }
                    player = MediaPlayer.create(getApplicationContext(), Uri.parse(contents));
                    long f = player.getDuration() / 1000 / 60;
                    long m = (player.getDuration() - (f * 1000 * 60)) / 1000;
                    if (f < 10) {
                        if (m < 10) {
                            tv_examine_time.setText("0" + f + "'" + "0" + m + "\"");
                        } else {
                            tv_examine_time.setText("0" + f + "'" + m + "\"");
                        }
                    } else {
                        if (m < 10) {
                            tv_examine_time.setText(f + "'" + "0" + m + "\"");
                        } else {
                            tv_examine_time.setText(f + "'" + m + "\"");
                        }
                    }
                    countDownTimer = new CountDownTimer(player.getDuration(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) { // millisUntilFinished is the left time at *Running State*
                            long f = millisUntilFinished / 1000 / 60;
                            long m = (millisUntilFinished - (f * 1000 * 60)) / 1000;
                            if (f < 10) {
                                if (m < 10) {
                                    tv_examine_time.setText("0" + f + "'" + "0" + m + "\"");
                                } else {
                                    tv_examine_time.setText("0" + f + "'" + m + "\"");
                                }
                            } else {
                                if (m < 10) {
                                    tv_examine_time.setText(f + "'" + "0" + m + "\"");
                                } else {
                                    tv_examine_time.setText(f + "'" + m + "\"");
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
                                    tv_examine_time.setText("0" + a + "'" + "0" + b + "\"");
                                } else {
                                    tv_examine_time.setText("0" + a + "'" + b + "\"");
                                }
                            } else {
                                if (b < 10) {
                                    tv_examine_time.setText(a + "'" + "0" + b + "\"");
                                } else {
                                    tv_examine_time.setText(a + "'" + b + "\"");
                                }
                            }
                            if(animationDrawable!=null){
                                animationDrawable.stop();
                            }
                            img_examine_yy.setImageResource(R.mipmap.yp_bf);
                            type = 0;
                        }
                    };


                }
            }
        });
        myAsyncTast.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_examine_pass:
                //通过
                img_examine_pass.setImageResource(R.mipmap.dot_bule);
                img_examine_reject.setImageResource(R.mipmap.dot_null);
                applyStatus = "1";
                break;

            case R.id.ll_examine_reject:
                //驳回
                img_examine_reject.setImageResource(R.mipmap.dot_bule);
                img_examine_pass.setImageResource(R.mipmap.dot_null);
                applyStatus = "0";
                break;

            case R.id.rl_examine_sub:
                //提交
                hashMap = new HashMap<>();
                hashMap.put("Num", getIntent().getStringExtra("orderNum"));
                hashMap.put("applyStatus", applyStatus);
                if (ed_examine_cause.getText() != null) {
                    hashMap.put("rejectCause", ed_examine_cause.getText().toString());
                }
                myAsyncTast = new MyAsyncTast(ExamineActivity.this, hashMap, AppConfig.APPROVALHANDLEORDER, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                    @Override
                    public void onError(String result) {

                    }

                    @Override
                    public void send(String result) {
                        ResultBean bean = GsonUtil.parseJsonWithGson(result, ResultBean.class);
                        if (bean.getCode() == 0) {
                            showToast("审批成功");
                            finish();
                        }
                    }
                });
                myAsyncTast.execute();
                break;
            case R.id.rl_examine:
                if (type == 0) {
                    player.start();
                    countDownTimer.start();
                    img_examine_yy.setImageResource(R.drawable.animation1);
                    animationDrawable = (AnimationDrawable) img_examine_yy.getDrawable();
                    animationDrawable.start();
                    type = 1;
                } else if (type == 1) {
                    img_examine_yy.setImageResource(R.mipmap.yp_bf);
                    player.pause();
                    countDownTimer.pause();
                    type = 2;
                    animationDrawable.stop();
                } else {
                    player.start();
                    countDownTimer.resume();
                    img_examine_yy.setImageResource(R.drawable.animation1);
                    animationDrawable = (AnimationDrawable) img_examine_yy.getDrawable();
                    animationDrawable.start();
                    type = 1;
                }
                break;
            default:
                break;

        }
    }
}
