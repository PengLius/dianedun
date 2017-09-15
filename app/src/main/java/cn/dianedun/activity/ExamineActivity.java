package cn.dianedun.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.ResultBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
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

    private String applyStatus = "0";
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);
        setTvTitleText("工单审批");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);

        tv_examine_pass.setOnClickListener(this);
        tv_examine_reject.setOnClickListener(this);
        rl_examine_sub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Drawable drawable = this.getResources().getDrawable(R.mipmap.dot_null);
        Drawable drawable1 = this.getResources().getDrawable(R.mipmap.dot_bule);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (v.getId()) {
            case R.id.tv_examine_pass:
                //通过
                tv_examine_pass.setCompoundDrawables(drawable1, null, null, null);
                tv_examine_reject.setCompoundDrawables(drawable, null, null, null);
                applyStatus = "1";
                break;

            case R.id.tv_examine_reject:
                //驳回
                tv_examine_pass.setCompoundDrawables(drawable, null, null, null);
                tv_examine_reject.setCompoundDrawables(drawable1, null, null, null);
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
