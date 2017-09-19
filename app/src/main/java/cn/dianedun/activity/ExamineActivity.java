package cn.dianedun.activity;

import android.os.Bundle;
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


    private String applyStatus = "0";
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DetailsBean bean;

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

        initData();

    }

    private void initData() {
        hashMap = new HashMap<>();
        hashMap.put("orderNum", getIntent().getStringExtra("orderNum"));
        myAsyncTast = new MyAsyncTast(ExamineActivity.this, hashMap, AppConfig.GETHANDLEORDERBYNUM, App.getInstance().getToken(), new MyAsyncTast.Callback() {
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
                hashMap.put("Num", "");
                hashMap.put("applyStatus", applyStatus);
                if (ed_examine_cause.getText() != null) {
                    hashMap.put("rejectCause", ed_examine_cause.getText().toString());
                }
                myAsyncTast = new MyAsyncTast(ExamineActivity.this, hashMap, AppConfig.APPROVALHANDLEORDER, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                    @Override
                    public void send(String result) {
                        ResultBean bean = GsonUtil.parseJsonWithGson(result, ResultBean.class);
                        if (bean.getCode() == 0) {
                            showToast("审批成功");
                            finish();
                        }
                    }
                });
                break;

            default:
                break;

        }
    }
}
