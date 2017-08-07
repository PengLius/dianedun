package cn.dianedun.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import cn.dianedun.R;
import cn.dianedun.base.BaseTitlFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MineFragment extends BaseTitlFragment {

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
    protected void initView(View contentView) {
        super.initView(contentView);
        setTvTitleText("我的");
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
