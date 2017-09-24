package cn.dianedun.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import cn.dianedun.fragment.SpitVideoFragment;

/**
 * Created by Administrator on 2017/9/22.
 */

public class SpitFragmentAdapter extends FragmentPagerAdapter {

    private SpitVideoFragment[] mFragment;

    public SpitFragmentAdapter(FragmentManager fm,SpitVideoFragment ... fragments){
        super(fm);
        mFragment = fragments;
    }

    public void setData(SpitVideoFragment ... fragments){
        mFragment = fragments;
    }

    @Override
    public long getItemId(int position) {
        return mFragment[position].hashCode();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment[position];
    }

    @Override
    public int getCount() {
        return mFragment.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
