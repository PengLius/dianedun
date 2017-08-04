package cn.dianedun.fragment;

import android.os.Bundle;

import cn.dianedun.R;
import cn.dianedun.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class HomeFragment extends BaseFragment {

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

}
