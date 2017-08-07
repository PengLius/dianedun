package cn.dianedun.fragment;

import android.os.Bundle;
import android.view.View;

import cn.dianedun.R;
import cn.dianedun.base.BaseFragment;
import cn.dianedun.base.BaseTitlFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class DetectionFragment extends BaseTitlFragment {

    public static DetectionFragment getInstance() {
        DetectionFragment fragment = new DetectionFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_detection;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setTvTitleText("监测");
        setTitleBack(R.mipmap.home_backg_null);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

}
