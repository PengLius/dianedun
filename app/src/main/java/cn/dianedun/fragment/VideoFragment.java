package cn.dianedun.fragment;

import android.os.Bundle;

import cn.dianedun.R;
import cn.dianedun.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class VideoFragment extends BaseFragment {

    public static VideoFragment getInstance() {
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_video;
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

}
