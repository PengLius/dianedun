package cn.dianedun.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.ezvizuikit.open.EZUIKit;
import com.umeng.analytics.MobclickAgent;
import com.videogo.openapi.EZOpenSDK;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.xutils.BuildConfig;
import org.xutils.x;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.dianedun.R;
import cn.dianedun.tools.App;

import static cn.dianedun.tools.App.AppKey;
import static cn.dianedun.tools.App.getInstance;

import static cn.dianedun.tools.App.AppKey;
import static cn.dianedun.tools.App.getInstance;


/**
 * Created by Administrator on 2017/8/8.
 */

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initSDK();
    }

    private void initSDK() {
        if (AndPermission.hasPermission(this,Manifest.permission.READ_PHONE_STATE)){
            EZUIKit.initWithAppKey(getInstance(),AppKey);
            {
                /**
                 * sdk日志开关，正式发布需要去掉
                 */
                EZOpenSDK.showSDKLog(true);

                EZOpenSDK.enableP2P(false);

                EZUIKit.setDebug(true);

                EZOpenSDK.initLib(getInstance(), AppKey, "");
            }
            x.Ext.init(getInstance());
            x.Ext.setDebug(BuildConfig.DEBUG);

            jump();
        }else{
            AndPermission.with(this).permission(Manifest.permission.READ_PHONE_STATE).requestCode(0).send();
        }
    }

    private void jump(){
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantPermissions) {
                if (requestCode == 0){
                    EZUIKit.initWithAppKey(getInstance(),AppKey);
                    {
                        /**
                         * sdk日志开关，正式发布需要去掉
                         */
                        EZOpenSDK.showSDKLog(true);

                        EZOpenSDK.enableP2P(false);

                        EZUIKit.setDebug(true);

                        EZOpenSDK.initLib(getInstance(), AppKey, "");
                    }
                    x.Ext.init(getInstance());
                    x.Ext.setDebug(BuildConfig.DEBUG);

                    jump();
                }
            }

            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                if (requestCode == 0){
                    jump();
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("启动页");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("启动页");
        MobclickAgent.onPause(this);
    }
}
