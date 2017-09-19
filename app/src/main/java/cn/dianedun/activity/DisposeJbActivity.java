package cn.dianedun.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class DisposeJbActivity extends BaseTitlActivity {

    @Bind(R.id.tv_disposejb_adress)
    TextView tv_disposejb_adress;

    @Bind(R.id.tv_disposejb_cause)
    TextView tv_disposejb_cause;

    @Bind(R.id.tv_disposejb_xxadress)
    TextView tv_disposejb_xxadress;

    @Bind(R.id.img_disposejb_type)
    ImageView img_disposejb_type;

    @Bind(R.id.rl_disposejb_ok)
    RelativeLayout rl_disposejb_ok;

    @Bind(R.id.tv_disposejb_time)
    TextView tv_disposejb_time;

    @Bind(R.id.ed_disposejb_cause)
    EditText ed_disposejb_cause;

    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DisposeJBBean bean;
    private String obj2 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposejb);
        setTvTitleText("警报处理");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        initData();
    }

    private void initData() {
        ed_disposejb_cause.setText("");
        hashMap = new HashMap<>();
        hashMap.put("id", getIntent().getStringExtra("id"));

        myAsyncTast = new MyAsyncTast(DisposeJbActivity.this, hashMap, AppConfig.FINDALARMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DisposeJBBean.class);
                tv_disposejb_adress.setText(bean.getData().getDepartName() + "");
                tv_disposejb_cause.setText(bean.getData().getAlertDetails() + "");
                tv_disposejb_xxadress.setText(bean.getData().getAddress() + "");
                tv_disposejb_time.setText(bean.getData().getCreateTime() + "");
                if (bean.getData().getType() == 1) {
                    img_disposejb_type.setImageResource(R.mipmap.home_jg_yellow);
                } else {
                    img_disposejb_type.setImageResource(R.mipmap.home_jg_red);
                }
                rl_disposejb_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subData();
                    }
                });

            }
        });
        myAsyncTast.execute();
    }

    private void subData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String confirmTime = formatter.format(curDate);
        hashMap = new HashMap<>();
        hashMap.put("result", ed_disposejb_cause.getText().toString());
        if (confirmTime != null) {
            hashMap.put("confirmTime", confirmTime + ":00");
        }
        if (!obj2.equals("")) {
            hashMap.put("jsonStr", obj2);
        }
        hashMap.put("id",getIntent().getStringExtra("id"));
        myAsyncTast = new MyAsyncTast(DisposeJbActivity.this, hashMap, AppConfig.UPDATEALARMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                showToast("提交成功");
                finish();
            }
        });
        myAsyncTast.execute();
    }
}
