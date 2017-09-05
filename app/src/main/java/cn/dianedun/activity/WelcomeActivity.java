package cn.dianedun.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.tools.App;


/**
 * Created by Administrator on 2017/8/8.
 */

public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //创建Timer对象
        Timer timer = new Timer();
        //创建TimerTask对象
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("user",
                        Activity.MODE_PRIVATE);
                String zddl = sharedPreferences.getString("zddl", "");
                if (zddl.equals("y")) {
                    String token = sharedPreferences.getString("token", "");
                    String isAdmin = sharedPreferences.getString("isAdmin", "");
                    App.getInstance().setToken(token);
                    App.getInstance().setIsAdmin(isAdmin);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("token");
                    editor.remove("zddl");
                    editor.remove("isAdmin");
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        //使用timer.schedule（）方法调用timerTask，定时3秒后执行run
        timer.schedule(timerTask, 3000);


    }
}
