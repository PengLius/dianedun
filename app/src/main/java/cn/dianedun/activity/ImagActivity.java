package cn.dianedun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;

/**
 * Created by Administrator on 2017/9/22.
 */

public class ImagActivity extends BaseTitlActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.view_pager)
    ViewPager view_pager;

    private List<String> imgUriList;
    private List<ImageView> imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imag);
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        initData();
    }

    private void initData() {
        imgUriList = getIntent().getStringArrayListExtra("imgList");
        imgList = new ArrayList<>();
        ImageView iv;
        for (int i = 0; i < imgUriList.size(); i++) {
            iv = new ImageView(this);
            Glide.with(ImagActivity.this).load(imgUriList.get(i)).into(iv);
            imgList.add(iv);
        }
        MyPagerAdapter adapter = new MyPagerAdapter();
        view_pager.setAdapter(adapter);
        view_pager.setOnPageChangeListener(this);
        int pos = Integer.parseInt(getIntent().getStringExtra("pos"));
        view_pager.setCurrentItem(pos);
        setTvTitleText(pos + 1 + "/" + imgList.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTvTitleText(position + 1 + "/" + imgList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imgList.get(position % imgList.size());
            view_pager.addView(iv);
            // 把当前添加ImageView返回回去.
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 把ImageView从ViewPager中移除掉
            view_pager.removeView(imgList.get(position % imgList.size()));
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("图片大图");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图片大图");
        MobclickAgent.onPause(this);
    }
}
