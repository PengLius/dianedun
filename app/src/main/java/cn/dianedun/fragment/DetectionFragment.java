package cn.dianedun.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
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

    @Bind(R.id.lv_detection)
    ListView lv_detection;

    @Bind(R.id.rl_detection)
    RelativeLayout rl_detection;

    @Bind(R.id.map)
    MapView mapView;

    AMap aMap;

    List<HashMap<String, Double>> list;
    List<String> markList;

    private TextView title, snippet;

    private IndentCusAdapter adapter;
    private boolean rightState = false;

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
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }


        setTvTitleText("监测");
        setTitleBack(R.mipmap.home_backg_leftnull);
        setImgRightVisibility(View.VISIBLE);
        setImgRight(R.mipmap.jc_topright);
        setImgRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightState) {
                    rl_detection.setVisibility(View.GONE);
                    rightState = false;
                } else {
                    rl_detection.setVisibility(View.VISIBLE);
                    rightState = true;
                }
                adapter = new IndentCusAdapter();
                lv_detection.setAdapter(adapter);
            }
        });
        rl_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_detection.setVisibility(View.GONE);
                rightState = false;
            }
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mList.add(new GaoCeFragment());
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
        markList = new ArrayList<>();
        list = new ArrayList<>();
        HashMap<String, Double> hashMap = new HashMap<>();
        hashMap.put("jd", 39.906901);
        hashMap.put("wd", 116.397);
        list.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("jd", 39.906901);
        hashMap.put("wd", 116.396);
        list.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("jd", 39.906901);
        hashMap.put("wd", 116.395);
        list.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("jd", 39.906901);
        hashMap.put("wd", 116.394);
        list.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("jd", 39.906901);
        hashMap.put("wd", 116.393);
        list.add(hashMap);
        iniDt();

    }

    /**
     * 地图设置点
     */
    private void iniDt() {
        AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                if (infoWindow == null) {
                    infoWindow = LayoutInflater.from(getActivity()).inflate(
                            R.layout.custom_info_window, null);
                }
                render(marker, infoWindow);
                return infoWindow;
            }

            View infoWindow = null;

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }

            public void render(Marker marker, View view) {
                snippet = (TextView) view.findViewById(R.id.snippet);
                title = (TextView) view.findViewById(R.id.title);
                for (int i = 0; i < markList.size(); i++) {
                    if (markList.get(i).equals(marker.getId())) {
                        title.setText(list.get(i).get("jd") + "");
                        snippet.setText(list.get(i).get("wd") + "");
                    }
                }
            }
        };

        for (int i = 0; i < list.size(); i++) {
            LatLng latLng = new LatLng(list.get(i).get("jd"), list.get(i).get("wd"));
            Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("").snippet(""));
            markList.add(marker.getId());
        }
        aMap.setInfoWindowAdapter(infoWindowAdapter);
        AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (int i = 0; i < markList.size(); i++) {
                    if (markList.get(i).equals(marker.getId())) {
                        showToast(i + "");
                    }
                }
            }
        };
        aMap.setOnInfoWindowClickListener(listener);
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

    private class IndentCusAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Cache cache;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_detection, null);
                cache = new Cache();
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            return convertView;
        }
    }

    class Cache {
        TextView tv_grouponall_name, tv_grouponall_from, tv_grouponall_offered;
        ImageView img_grouponall_head;
    }
}
