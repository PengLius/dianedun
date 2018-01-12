package cn.dianedun.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.DetailsBean;
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

public class DetailsingActivity extends BaseTitlActivity {

    private Intent intent;

    @Bind(R.id.rl_detail_cx)
    RelativeLayout rl_detail_cx;

    @Bind(R.id.rl_detail_xg)
    RelativeLayout rl_detail_xg;

    @Bind(R.id.tv_detailsing_numb)
    TextView tv_detailsing_numb;

    @Bind(R.id.tv_detailsing_jjd)
    TextView tv_detailsing_jjd;

    @Bind(R.id.tv_detailsing_name)
    TextView tv_detailsing_name;

    @Bind(R.id.tv_detailsing_startime)
    TextView tv_detailsing_startime;

    @Bind(R.id.tv_detailsing_endtime)
    TextView tv_detailsing_endtime;

    @Bind(R.id.tv_detailsing_cause)
    TextView tv_detailsing_cause;

    @Bind(R.id.tv_detailsing_adress)
    TextView tv_detailsing_adress;

    @Bind(R.id.tv_detailsing_xxadress)
    TextView tv_detailsing_xxadress;

    @Bind(R.id.ll_detailsing_bh)
    LinearLayout ll_detailsing_bh;

    @Bind(R.id.tv_detailsing_bhcause)
    TextView tv_detailsing_bhcause;

    @Bind(R.id.rl_detail)
    RelativeLayout rl_detail;

    @Bind(R.id.img_detail_yy)
    ImageView img_detail_yy;

    @Bind(R.id.tv_detail_time)
    TextView tv_detail_time;


    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DetailsBean bean;
    private int types = 0;
    private MediaPlayer player;
    private CountDownTimer countDownTimer;
    private AnimationDrawable animationDrawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsing);
        setTvTitleText("工单详情");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        rl_detail_cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //工单撤销
                intent = new Intent(getApplicationContext(), AnnulActivity.class);
                intent.putExtra("orderNum", getIntent().getStringExtra("orderNum"));
                startActivity(intent);
            }
        });
        rl_detail_xg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //工单修改
                intent = new Intent(getApplicationContext(), AmendGdActivity.class);
                intent.putExtra("orderNum", getIntent().getStringExtra("orderNum"));
                startActivity(intent);
            }
        });
        initData();

    }

    private void initData() {
        hashMap = new HashMap<>();
        hashMap.put("orderNum", getIntent().getStringExtra("orderNum"));
        myAsyncTast = new MyAsyncTast(DetailsingActivity.this, hashMap, AppConfig.GETHANDLEORDERBYNUM, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
            }

            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DetailsBean.class);
                tv_detailsing_numb.setText(bean.getData().getOrderNum() + "");
                if (bean.getData().getUrgency() == 0) {
                    tv_detailsing_jjd.setText("普通");
                } else if (bean.getData().getUrgency() == 1) {
                    tv_detailsing_jjd.setText("紧急");
                }
                tv_detailsing_name.setText(bean.getData().getHandlePersion() + "");
                String beginTime = bean.getData().getBeginTime() / 1000 + "";
                String endTime = bean.getData().getEndTime() / 1000 + "";
                tv_detailsing_startime.setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm:ss"));
                tv_detailsing_endtime.setText(DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm:ss"));
                tv_detailsing_cause.setText(bean.getData().getCause() + "");
                tv_detailsing_xxadress.setText(bean.getData().getAddress() + "");
                tv_detailsing_adress.setText(bean.getData().getDepartName() + "");
                if (bean.getData().getStatus() == 3) {
                    ll_detailsing_bh.setVisibility(View.VISIBLE);
                    tv_detailsing_bhcause.setText(bean.getData().getRejectCause());
                }
                if (bean.getData().getAlertOptionsArray().size() > 0) {
                    rl_detail.setVisibility(View.VISIBLE);
                    String contents = bean.getData().getAlertOptionsArray().get(0).getContents();
                    player = MediaPlayer.create(getApplicationContext(), Uri.parse(contents));
                    long f = player.getDuration() / 1000 / 60;
                    long m = (player.getDuration() - (f * 1000 * 60)) / 1000;
                    if (f < 10) {
                        if (m < 10) {
                            tv_detail_time.setText("0" + f + "'" + "0" + m + "\"");
                        } else {
                            tv_detail_time.setText("0" + f + "'" + m + "\"");
                        }
                    } else {
                        if (m < 10) {
                            tv_detail_time.setText(f + "'" + "0" + m + "\"");
                        } else {
                            tv_detail_time.setText(f + "'" + m + "\"");
                        }
                    }
                    countDownTimer = new CountDownTimer(player.getDuration(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) { // millisUntilFinished is the left time at *Running State*
                            long f = millisUntilFinished / 1000 / 60;
                            long m = (millisUntilFinished - (f * 1000 * 60)) / 1000;
                            if (f < 10) {
                                if (m < 10) {
                                    tv_detail_time.setText("0" + f + "'" + "0" + m + "\"");
                                } else {
                                    tv_detail_time.setText("0" + f + "'" + m + "\"");
                                }
                            } else {
                                if (m < 10) {
                                    tv_detail_time.setText(f + "'" + "0" + m + "\"");
                                } else {
                                    tv_detail_time.setText(f + "'" + m + "\"");
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
                                    tv_detail_time.setText("0" + a + "'" + "0" + b + "\"");
                                } else {
                                    tv_detail_time.setText("0" + a + "'" + b + "\"");
                                }
                            } else {
                                if (b < 10) {
                                    tv_detail_time.setText(a + "'" + "0" + b + "\"");
                                } else {
                                    tv_detail_time.setText(a + "'" + b + "\"");
                                }
                            }
                            animationDrawables.stop();
                            img_detail_yy.setImageResource(R.mipmap.yp_bf);
                            types = 0;
                        }
                    };
                    rl_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (types == 0) {
                                player.start();
                                countDownTimer.start();
                                img_detail_yy.setImageResource(R.drawable.animation1);
                                animationDrawables = (AnimationDrawable) img_detail_yy.getDrawable();
                                animationDrawables.start();
                                types = 1;
                            } else if (types == 1) {
                                img_detail_yy.setImageResource(R.mipmap.yp_bf);
                                player.pause();
                                countDownTimer.pause();
                                types = 2;
                                animationDrawables.stop();
                            } else {
                                player.start();
                                countDownTimer.resume();
                                img_detail_yy.setImageResource(R.drawable.animation1);
                                animationDrawables = (AnimationDrawable) img_detail_yy.getDrawable();
                                animationDrawables.start();
                                types = 1;
                            }
                        }
                    });

                }

            }
        });
        myAsyncTast.execute();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("工单详情");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("工单详情");
        MobclickAgent.onPause(this);
    }
}
