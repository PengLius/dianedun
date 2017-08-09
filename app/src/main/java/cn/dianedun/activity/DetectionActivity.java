package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseActivity;
import cn.dianedun.fragment.DiYaFragment;
import cn.dianedun.fragment.GaoYaFragment;
import cn.dianedun.fragment.HomeFragment;
import cn.dianedun.fragment.HumidityFragment;
import cn.dianedun.fragment.TemperatureFragment;
import cn.dianedun.tools.CommonUtil;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/8.
 */

public class DetectionActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.view_title)
    View view_title;

    @Bind(R.id.vp_detection)
    ViewPager vp_detection;

    @Bind(R.id.tv_detection_gy)
    TextView tv_detection_gy;

    @Bind(R.id.tv_detection_dy)
    TextView tv_detection_dy;

    @Bind(R.id.tv_detection_wd)
    TextView tv_detection_wd;

    @Bind(R.id.tv_detection_sd)
    TextView tv_detection_sd;


    private List<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            view_title.setVisibility(GONE);
        }
        ViewGroup.LayoutParams params = view_title.getLayoutParams();
        params.height = CommonUtil.getStatusBarHeight(DetectionActivity.this);
        view_title.setLayoutParams(params);
    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mList.add(new GaoYaFragment("aaa"));
        mList.add(new DiYaFragment());
        mList.add(new TemperatureFragment());
        mList.add(new HumidityFragment());
        tv_detection_gy.setOnClickListener(this);
        tv_detection_dy.setOnClickListener(this);
        tv_detection_wd.setOnClickListener(this);
        tv_detection_sd.setOnClickListener(this);
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), mList);
        vp_detection.setAdapter(myAdapter);
        vp_detection.setCurrentItem(0);
        vp_detection.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_detection_gy:
                vp_detection.setCurrentItem(0);
                tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab1);
                tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab);
                break;
            case R.id.tv_detection_dy:
                vp_detection.setCurrentItem(1);
                tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab1);
                tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab);

                break;
            case R.id.tv_detection_wd:
                vp_detection.setCurrentItem(2);
                tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab1);
                tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab);

                break;
            case R.id.tv_detection_sd:
                vp_detection.setCurrentItem(3);
                tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab);
                tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab1);
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
                    tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab1);
                    tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab);
                    break;
                case 1:
                    tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab1);
                    tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab);
                    break;
                case 2:
                    tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab1);
                    tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab);
                    break;
                case 3:
                    tv_detection_gy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_dy.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_wd.setBackgroundResource(R.mipmap.jc_tab);
                    tv_detection_sd.setBackgroundResource(R.mipmap.jc_tab1);
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
