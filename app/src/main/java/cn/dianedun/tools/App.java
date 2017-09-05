package cn.dianedun.tools;

import android.app.Application;

/**
 * Created by Administrator on 2017/9/2.
 */

public class App extends Application {
    private static App mInstance;

    private String token;
    private String isAdmin;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
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
