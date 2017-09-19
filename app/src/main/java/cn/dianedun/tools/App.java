package cn.dianedun.tools;

import android.app.Application;

import com.videogo.openapi.EZOpenSDK;

/**
 * Created by Administrator on 2017/9/2.
 */

public class App extends Application {
    private static App mInstance;

    private String token;
    private String isAdmin;

    public static String AppKey = "e41e8e905bad4fb8a5788892d639e0a2";

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initSDK();
    }

    private void initSDK() {
        {
            /**
             * sdk日志开关，正式发布需要去掉
             */
            EZOpenSDK.showSDKLog(true);

            EZOpenSDK.enableP2P(false);

            EZOpenSDK.initLib(this, AppKey, "");
        }
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
