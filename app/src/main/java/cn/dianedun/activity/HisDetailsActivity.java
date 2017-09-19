package cn.dianedun.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.DetailsBean;
import cn.dianedun.bean.HisOrderListBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.DataUtil;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import cn.dianedun.view.NoScrollGridview;
import dev.xesam.android.toolbox.timer.CountDownTimer;

import static cn.dianedun.R.id.tv_amendgd_lytime;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HisDetailsActivity extends BaseTitlActivity {

    @Bind(R.id.tv_hisdetails_urgency)
    TextView tv_hisdetails_urgency;

    @Bind(R.id.tv_hisdetails_name)
    TextView tv_hisdetails_name;

    @Bind(R.id.tv_hisdetails_startime)
    TextView tv_hisdetails_startime;

    @Bind(R.id.tv_hisdetails_endtime)
    TextView tv_hisdetails_endtime;

    @Bind(R.id.tv_hisdetails_cause)
    TextView tv_hisdetails_cause;

    @Bind(R.id.tv_hisdetails_adress)
    TextView tv_hisdetails_adress;

    @Bind(R.id.tv_hisdetails_xxadress)
    TextView tv_hisdetails_xxadress;

    @Bind(R.id.tv_hisdetails_cl)
    TextView tv_hisdetails_cl;

    @Bind(R.id.tv_hisdetails_nmb)
    TextView tv_hisdetails_nmb;

    @Bind(R.id.gv_hisdetauls)
    NoScrollGridview gv_hisdetauls;


    private Intent intent;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DetailsBean bean;
    private List<String> imgList;
    private int type = 0;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisdetails);
        setTvTitleText("工单详情");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        getData();
    }

    public void getData() {
        hashMap = new HashMap<>();
        hashMap.put("orderNum", getIntent().getStringExtra("orderNum"));
        myAsyncTast = new MyAsyncTast(HisDetailsActivity.this, hashMap, AppConfig.GETHANDLEORDERBYNUM, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DetailsBean.class);
                if (bean.getData().getUrgency() == 0) {
                    tv_hisdetails_urgency.setText("普通");
                } else {
                    tv_hisdetails_urgency.setText("紧急");
                }
                tv_hisdetails_name.setText(bean.getData().getHandlePersion() + "");
                String beginTime = bean.getData().getBeginTime() / 1000 + "";
                String endTime = bean.getData().getEndTime() / 1000 + "";
                tv_hisdetails_startime.setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm"));
                tv_hisdetails_endtime.setText(DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm"));
                tv_hisdetails_cause.setText(bean.getData().getCause() + "");
                tv_hisdetails_adress.setText(bean.getData().getDepartName() + "");
                tv_hisdetails_xxadress.setText(bean.getData().getAddress() + "");
                tv_hisdetails_nmb.setText(bean.getData().getOrderNum() + "");
                tv_hisdetails_cl.setText(bean.getData().getFeedCause() + "");
                if (bean.getData().getAlertOptionsArray().size() > 0) {
                    GirdAdapter adapter = new GirdAdapter();
                    gv_hisdetauls.setAdapter(adapter);
                }
            }
        });
        myAsyncTast.execute();
        imgList = new ArrayList<String>();
    }

    class GirdAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return bean.getData().getAlertOptionsArray().size();
        }

        @Override
        public Object getItem(int position) {
            return bean.getData().getAlertOptionsArray().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (bean.getData().getAlertOptionsArray().get(position).getOptionType() == 0) {
                //图片
                imgList.add(bean.getData().getAlertOptionsArray().get(position).getContents());
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_fjimg, null);
                Glide.with(HisDetailsActivity.this).load(bean.getData().getAlertOptionsArray().get(position).getContents()).into(((ImageView) convertView.findViewById(R.id.img_fjimg_img)));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putStringArrayListExtra("imgList", (ArrayList<String>) imgList);
                        startActivity(intent);
                    }
                });

            } else {
                //音频
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_fjyp, null);
                final TextView tv_fjyp_time = (TextView) convertView.findViewById(R.id.tv_fjyp_time);
                final ImageView img_fjyp_yy = (ImageView) convertView.findViewById(R.id.img_fjyp_yy);
                final MediaPlayer player = MediaPlayer.create(getApplicationContext(), Uri.parse(bean.getData().getAlertOptionsArray().get(position).getContents()));

                long f = player.getDuration() / 1000 / 60;
                long m = (player.getDuration() - (f * 1000 * 60)) / 1000;
                if (f < 10) {
                    if (m < 10) {
                        tv_fjyp_time.setText("0" + f + "'" + "0" + m + "\"");
                    } else {
                        tv_fjyp_time.setText("0" + f + "'" + m + "\"");
                    }
                } else {
                    if (m < 10) {
                        tv_fjyp_time.setText(f + "'" + "0" + m + "\"");
                    } else {
                        tv_fjyp_time.setText(f + "'" + m + "\"");
                    }
                }
                final CountDownTimer countDownTimer = new CountDownTimer(player.getDuration(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) { // millisUntilFinished is the left time at *Running State*
                        long f = millisUntilFinished / 1000 / 60;
                        long m = (millisUntilFinished - (f * 1000 * 60)) / 1000;
                        if (f < 10) {
                            if (m < 10) {
                                tv_fjyp_time.setText("0" + f + "'" + "0" + m + "\"");
                            } else {
                                tv_fjyp_time.setText("0" + f + "'" + m + "\"");
                            }
                        } else {
                            if (m < 10) {
                                tv_fjyp_time.setText(f + "'" + "0" + m + "\"");
                            } else {
                                tv_fjyp_time.setText(f + "'" + m + "\"");
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
                                tv_fjyp_time.setText("0" + a + "'" + "0" + b + "\"");
                            } else {
                                tv_fjyp_time.setText("0" + a + "'" + b + "\"");
                            }
                        } else {
                            if (b < 10) {
                                tv_fjyp_time.setText(a + "'" + "0" + b + "\"");
                            } else {
                                tv_fjyp_time.setText(a + "'" + b + "\"");
                            }
                        }
                        animationDrawable.stop();
                        img_fjyp_yy.setImageResource(R.mipmap.yp_bf);
                        type = 0;
                    }
                };
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 0) {
                            player.start();
                            countDownTimer.start();
                            img_fjyp_yy.setImageResource(R.drawable.animation1);
                            animationDrawable = (AnimationDrawable) img_fjyp_yy.getDrawable();
                            animationDrawable.start();
                            type = 1;
                        } else if (type == 1) {
                            img_fjyp_yy.setImageResource(R.mipmap.yp_bf);
                            player.pause();
                            countDownTimer.pause();
                            type = 2;
                            animationDrawable.stop();
                        } else {
                            player.start();
                            countDownTimer.resume();
                            img_fjyp_yy.setImageResource(R.drawable.animation1);
                            animationDrawable = (AnimationDrawable) img_fjyp_yy.getDrawable();
                            animationDrawable.start();
                            type = 1;
                        }
                    }
                });
            }
            return convertView;
        }
    }
}
