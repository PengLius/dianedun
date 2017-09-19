package cn.dianedun.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.DisposeJBBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HisDisposeJbActivity extends BaseTitlActivity {

    @Bind(R.id.tv_hisdisposejb_adress)
    TextView tv_hisdisposejb_adress;

    @Bind(R.id.tv_hisdisposejb_timer)
    TextView tv_hisdisposejb_timer;

    @Bind(R.id.tv_hisdisposejb_cause)
    TextView tv_hisdisposejb_cause;

    @Bind(R.id.tv_hisdisposejb_xxadress)
    TextView tv_hisdisposejb_xxadress;

    @Bind(R.id.tv_hisdisposejb_reult)
    TextView tv_hisdisposejb_reult;

    @Bind(R.id.tv_hisdisposejb_type)
    TextView tv_hisdisposejb_type;


    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DisposeJBBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisdisposejb);
        setTvTitleText("警报详情");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        initData();
    }

    private void initData() {
        hashMap = new HashMap<>();
        hashMap.put("id", getIntent().getStringExtra("id"));
        myAsyncTast = new MyAsyncTast(HisDisposeJbActivity.this, hashMap, AppConfig.FINDALARMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DisposeJBBean.class);
                tv_hisdisposejb_adress.setText(bean.getData().getDepartName());
                tv_hisdisposejb_timer.setText(bean.getData().getCreateTime());
                tv_hisdisposejb_cause.setText(bean.getData().getAlertDetails());
                tv_hisdisposejb_xxadress.setText(bean.getData().getAddress());
                tv_hisdisposejb_reult.setText(bean.getData().getResult());
                if (bean.getData().getType() == 1) {
                    tv_hisdisposejb_type.setText("普通");
                } else {
                    tv_hisdisposejb_type.setText("严重");
                }

            }
        });
        myAsyncTast.execute();
    }

}
