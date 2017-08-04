package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;

import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;

/**
 * Created by Administrator on 2017/8/3.
 */

public class LoginActivity extends BaseTitlActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTvTitleText("用户登录");
        setStatusColor(Color.parseColor("#000000"));
        setTitleBack(R.mipmap.ic_launcher);
    }
}
