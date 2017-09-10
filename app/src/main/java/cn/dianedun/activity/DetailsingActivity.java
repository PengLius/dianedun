package cn.dianedun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;

/**
 * Created by Administrator on 2017/8/7.
 */

public class DetailsingActivity extends BaseTitlActivity {

    private Intent intent;

    @Bind(R.id.rl_detail_cx)
    RelativeLayout rl_detail_cx;

    @Bind(R.id.rl_detail_xg)
    RelativeLayout rl_detail_xg;

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
                startActivity(intent);
            }
        });
        rl_detail_xg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //工单修改
                intent = new Intent(getApplicationContext(), AmendGdActivity.class);
                startActivity(intent);
            }
        });

    }
}
