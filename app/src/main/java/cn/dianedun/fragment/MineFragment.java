package cn.dianedun.fragment;

import android.os.Bundle;

import cn.dianedun.R;
import cn.dianedun.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MineFragment extends BaseFragment {

    public static MineFragment getInstance() {
        MineFragment fragment = new MineFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

}
