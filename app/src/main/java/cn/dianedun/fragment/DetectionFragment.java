package cn.dianedun.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.DetectionActivity;
import cn.dianedun.base.BaseFragment;
import cn.dianedun.base.BaseTitlFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class DetectionFragment extends BaseTitlFragment implements View.OnClickListener {

    @Bind(R.id.vp_fdetection)
    ViewPager vp_fdetection;

    @Bind(R.id.tv_fdetection_gy)
    TextView tv_fdetection_gy;

    @Bind(R.id.tv_fdetection_dy)
    TextView tv_fdetection_dy;

    @Bind(R.id.tv_fdetection_wd)
    TextView tv_fdetection_wd;

    @Bind(R.id.tv_fdetection_sd)
    TextView tv_fdetection_sd;


    private List<Fragment> mList;


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
        setTitleBack(R.mipmap.home_backg_leftnull);
        setImgRightVisibility(View.VISIBLE);
        setImgRight(R.mipmap.home_look);
        setImgRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mList.add(new GaoYaFragment("aaa"));
        mList.add(new DiYaFragment());
        mList.add(new TemperatureFragment());
        mList.add(new HumidityFragment());
        tv_fdetection_gy.setOnClickListener(this);
        tv_fdetection_dy.setOnClickListener(this);
        tv_fdetection_wd.setOnClickListener(this);
        tv_fdetection_sd.setOnClickListener(this);
        MyAdapter myAdapter = new MyAdapter(getChildFragmentManager(), mList);
        vp_fdetection.setAdapter(myAdapter);
        vp_fdetection.setCurrentItem(0);
        vp_fdetection.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fdetection_gy:
                vp_fdetection.setCurrentItem(0);
                tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab1);
                tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab);
                break;
            case R.id.tv_fdetection_dy:
                vp_fdetection.setCurrentItem(1);
                tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab1);
                tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab);

                break;
            case R.id.tv_fdetection_wd:
                vp_fdetection.setCurrentItem(2);
                tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab1);
                tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab);

                break;
            case R.id.tv_fdetection_sd:
                vp_fdetection.setCurrentItem(3);
                tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab);
                tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab1);
                break;
            default:
                break;
        }
    }

    class MyAdapter extends FragmentPagerAdapter {
        private List<Fragment> list = new ArrayList<>();
        private FragmentManager fm;

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) list.get(position);
        }

        public int getCount() {
            return list != null ? list.size() : 0;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab1);
                    tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab);
                    break;
                case 1:
                    tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab1);
                    tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab);
                    break;
                case 2:
                    tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab1);
                    tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab);
                    break;
                case 3:
                    tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab);
                    tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab1);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
