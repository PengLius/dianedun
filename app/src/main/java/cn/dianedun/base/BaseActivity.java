package cn.dianedun.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

import static cn.dianedun.base.Constant.ACTIVITY_BUNDLE;

/**
 * Created by Administrator on 2017/6/19.
 */

public class BaseActivity extends com.vise.xsnow.ui.BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 设置透明主题
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            StatusBarUtil.transparencyBar(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        }

    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(this,cls);
        intent.putExtra(ACTIVITY_BUNDLE,bundle);
        startActivity(intent);
    }
    @Override
    protected void initView() {

    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {

    }

    protected void startActivity(Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(this,cls);
        startActivity(intent);
    }

    Toast mToast;
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    private boolean mInitData = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mInitData){
            mInitData = false;
            onActivityLoadFinish();
        }
    }

    protected void onActivityLoadFinish(){}
}
