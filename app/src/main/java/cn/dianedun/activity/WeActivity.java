package cn.dianedun.activity;

import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/8.
 */

public class WeActivity extends BaseTitlActivity {
    private MyAsyncTast myAsyncTast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we);
        setTvTitleText("关于我们");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        getDate();

    }

    public void getDate() {
        myAsyncTast = new MyAsyncTast(WeActivity.this, new HashMap<String, String>(), "", App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                
            }

            @Override
            public void send(String result) {

            }
        });
        myAsyncTast.execute();

    }
}
