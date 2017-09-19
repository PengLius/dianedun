package cn.dianedun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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


    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DetailsBean bean;

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
                tv_detailsing_startime.setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm"));
                tv_detailsing_endtime.setText(DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm"));
                tv_detailsing_cause.setText(bean.getData().getCause() + "");
                tv_detailsing_adress.setText(bean.getData().getAddress() + "");
                tv_detailsing_xxadress.setText(bean.getData().getDepartName() + "");

            }
        });
        myAsyncTast.execute();
    }
}
