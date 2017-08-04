package cn.dianedun.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static cn.dianedun.base.Constant.ACTIVITY_BUNDLE;
import static cn.dianedun.base.Constant.TOOLBAR_TYPE;

/**
 * Created by Administrator on 2017/6/19.
 */

public class BaseFragment extends com.vise.xsnow.ui.BaseFragment {


    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void initView(View contentView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {

    }
    protected void startActivity(Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(_mActivity,cls);
        startActivity(intent);
    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(_mActivity,cls);
        intent.putExtra(ACTIVITY_BUNDLE,bundle);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, int  type){
        Intent intent = new Intent();
        intent.setClass(_mActivity,cls);
        intent.putExtra(TOOLBAR_TYPE.TYPE,type);
        startActivity(intent);
    }

    private Toast mToast;
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(_mActivity, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
