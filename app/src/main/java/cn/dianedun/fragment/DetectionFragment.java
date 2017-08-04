package cn.dianedun.fragment;

import android.os.Bundle;

import cn.dianedun.R;
import cn.dianedun.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class DetectionFragment extends BaseFragment {

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
    protected void bindEvent() {
        super.bindEvent();
    }

}
