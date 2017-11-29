package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/8.
 */

public class WeActivity extends BaseTitlActivity {
    @Bind(R.id.web_we)
    WebView web_we;
    private MyAsyncTast myAsyncTast;
    private String text;

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
        myAsyncTast = new MyAsyncTast(WeActivity.this, new HashMap<String, String>(), AppConfig.FINDABOUTUS, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
            }

            @Override
            public void send(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    text = jsonObject1.getString("text");
                    web_we.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
                    web_we.setBackgroundColor(Color.parseColor("#6019246B"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        myAsyncTast.execute();

    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关于我们");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于我们");
        MobclickAgent.onPause(this);
    }
}
