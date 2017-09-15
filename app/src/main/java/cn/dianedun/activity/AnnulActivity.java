package cn.dianedun.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.MyAsyncTast;

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

    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annul);
        setTvTitleText("工单撤销");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);

        rl_annul_sub.setOnClickListener(this);
        img_annul_ly.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_annul_ly:

                break;

            case R.id.rl_annul_sub:
                //提交
                hashMap = new HashMap<>();
                hashMap.put("workOrderId", "");
                if (ed_annul_cause.getText() != null) {
                    hashMap.put("remark", ed_annul_cause.getText().toString());
                }
                myAsyncTast = new MyAsyncTast(AnnulActivity.this, hashMap, "", App.getInstance().getToken(), new MyAsyncTast.Callback() {
                    @Override
                    public void send(String result) {

                    }
                });

                break;

            default:
                break;
        }
    }
}
