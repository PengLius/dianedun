package cn.dianedun.tools;

import android.support.multidex.MultiDexApplication;


import com.ezvizuikit.open.EZUIKit;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.videogo.openapi.EZOpenSDK;

import org.xutils.BuildConfig;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/9/2.
 */

public class App extends MultiDexApplication {
    private static App mInstance;

    private String token;
    private String isAdmin;

    public static String AppKey = "0f74e3ed04794788a1b2ac9e45109031";
    public static String AppSecret = "53f55b1e13a13452dbb078fd2ea6fcae";

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(mInstance);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.init(this, "598bf5bc310c93481f0012c3", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
    }
  
    public static App getInstance() {
        return mInstance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }


}

