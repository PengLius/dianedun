package cn.dianedun.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.DetectionActivity;
import cn.dianedun.base.BaseTitlFragment;
import cn.dianedun.bean.DetactionXBean;
import cn.dianedun.bean.MapBean;
import cn.dianedun.bean.PeiDSBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

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

    @Bind(R.id.ll_detection_all)
    LinearLayout ll_detection_all;

    @Bind(R.id.tv_detection_adress)
    TextView tv_detection_adress;

    @Bind(R.id.srl_detection)
    SmartRefreshLayout srl_detection;

    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    AMap aMap;
    List<String> markList;
    private TextView title, snippet;
    private IndentCusAdapter adapter;
    private boolean rightState = false;
    private List<Fragment> mList;
    private PeiDSBean bean;
    private GaoCeFragment gaoCeFragment;
    private DiYaFragment diYaFragment;
    private TemperatureFragment temperatureFragment;
    private HumidityFragment humidityFragment;
    private DetactionXBean xBean;
    private MapBean mapBean;
    private String pdsId, depart;

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
        if (App.getInstance().getIsAdmin().equals("2")) {
            setTvTitleText("概览");
            mapView.setVisibility(View.VISIBLE);
            ll_detection_all.setVisibility(View.GONE);
            mapView.onCreate(savedInstanceState);
            if (aMap == null) {
                aMap = mapView.getMap();
            }
            aMap.clear();
            setImgRightVisibility(View.GONE);
            setTitleBack(R.mipmap.home_backg_null);
            initMap();
        } else {
            initListV();
            setTvTitleText("监测");
            mapView.setVisibility(View.GONE);
            ll_detection_all.setVisibility(View.VISIBLE);
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
                        hashMap = new HashMap<>();
                        hashMap.put("id", "");
                        myAsyncTast = new MyAsyncTast(getActivity(), hashMap, AppConfig.FINDSWITCHROOMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                            @Override
                            public void onError(String result) {

                            }

                            @Override
                            public void send(String result) {
                                bean = GsonUtil.parseJsonWithGson(result, PeiDSBean.class);
                                adapter = new IndentCusAdapter();
                                lv_detection.setAdapter(adapter);
                                rl_detection.setVisibility(View.VISIBLE);
                                rightState = true;
                            }
                        });
                        myAsyncTast.execute();
                    }

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

    }

    private void initMap() {
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(38.867814, 115.49159), 11, 0, 0));
        aMap.moveCamera(mCameraUpdate);
        markList = new ArrayList<>();
        myAsyncTast = new MyAsyncTast(getActivity(), new HashMap<String, String>(), AppConfig.INDEX, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
            }

            @Override
            public void send(String result) {
                mapBean = GsonUtil.parseJsonWithGson(result, MapBean.class);
                iniDt();
            }
        });
        myAsyncTast.execute();
    }

    private void initListV() {
        mList = new ArrayList<>();
        gaoCeFragment = new GaoCeFragment();
        diYaFragment = new DiYaFragment();
        temperatureFragment = new TemperatureFragment();
        humidityFragment = new HumidityFragment();
        tv_fdetection_gy.setBackgroundResource(R.mipmap.jc_tab1);
        tv_fdetection_dy.setBackgroundResource(R.mipmap.jc_tab);
        tv_fdetection_wd.setBackgroundResource(R.mipmap.jc_tab);
        tv_fdetection_sd.setBackgroundResource(R.mipmap.jc_tab);
        mList.add(gaoCeFragment);
        mList.add(diYaFragment);
        mList.add(temperatureFragment);
        mList.add(humidityFragment);
        tv_fdetection_gy.setOnClickListener(this);
        tv_fdetection_dy.setOnClickListener(this);
        tv_fdetection_wd.setOnClickListener(this);
        tv_fdetection_sd.setOnClickListener(this);
        MyAdapter myAdapter = new MyAdapter(getChildFragmentManager(), mList);
        vp_fdetection.setAdapter(myAdapter);
        vp_fdetection.setCurrentItem(0);
        vp_fdetection.setOnPageChangeListener(new MyOnPageChangeListener());
        vp_fdetection.setOffscreenPageLimit(3);
        initRefreshLayout();
    }


    private void initRefreshLayout() {
        srl_detection.setLoadmoreFinished(true);
        srl_detection.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (pdsId != null) {
                    getPDSAll(pdsId, false);
                } else {
                    fristData();
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 获取所有配电室id
     */
    private void fristData() {
        hashMap = new HashMap<>();
        hashMap.put("id", "");
        myAsyncTast = new MyAsyncTast(getActivity(), hashMap, AppConfig.FINDSWITCHROOMBYID, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                srl_detection.finishRefresh();
                showToast(result);
            }

            @Override
            public void send(String result) {
                srl_detection.finishRefresh();
                bean = GsonUtil.parseJsonWithGson(result, PeiDSBean.class);
                pdsId = bean.getData().getSwitchRoomList().get(0).getId();
                getPDSAll(pdsId, true);
                tv_detection_adress.setText(bean.getData().getSwitchRoomList().get(0).getDepartname());
                depart = bean.getData().getSwitchRoomList().get(0).getDepartname();
            }
        });
        myAsyncTast.execute();
    }

    /**
     * 配电室数据
     *
     * @param RoomId 配电室Id
     */
    private void getPDSAll(final String RoomId, boolean loading) {

        hashMap = new HashMap<>();
        hashMap.put("RoomId", RoomId);
        myAsyncTast = new MyAsyncTast(getActivity(), hashMap, AppConfig.FINDALLLATEST, App.getInstance().getToken(), loading, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                srl_detection.finishRefresh();
                showToast(result);
            }

            @Override
            public void send(String result) {
                xBean = GsonUtil.parseJsonWithGson(result, DetactionXBean.class);
                gaoCeFragment.setData(xBean, RoomId, depart);
                diYaFragment.setData(xBean, RoomId, depart);
                temperatureFragment.setData(xBean, RoomId, depart);
                humidityFragment.setData(xBean, RoomId, depart);
                srl_detection.finishRefresh();
            }
        });
        myAsyncTast.execute();
    }

    /**
     * 地图设置点
     */
    private void iniDt() {
        for (int i = 0; i < mapBean.getData().getDepartList().size(); i++) {
            Double lat = Double.parseDouble(mapBean.getData().getDepartList().get(i).getLat());
            Double lng = Double.parseDouble(mapBean.getData().getDepartList().get(i).getLng());
            LatLng latLng = new LatLng(lat, lng);
            Marker marker = null;
            if (mapBean.getData().getDepartList().get(i).getAlarmLevel().equals("0")) {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.title("").snippet("");
                markerOption.draggable(false);//设置Marker可拖动
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.map_green)));
                markerOption.position(latLng);
                marker = aMap.addMarker(markerOption);
            } else if (mapBean.getData().getDepartList().get(i).getAlarmLevel().equals("1")) {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.title("").snippet("");
                markerOption.draggable(false);//设置Marker可拖动
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.map_ye)));
                markerOption.position(latLng);
                marker = aMap.addMarker(markerOption);

            } else if (mapBean.getData().getDepartList().get(i).getAlarmLevel().equals("2")) {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.title("").snippet("");
                markerOption.draggable(false);//设置Marker可拖动
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.map_red)));
                markerOption.position(latLng);
                marker = aMap.addMarker(markerOption);
            }
            markList.add(marker.getId());
        }
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
                        title.setText(mapBean.getData().getDepartList().get(i).getDepartname());
                        snippet.setText("点击查看配电室");
                    }
                }
            }
        };

        aMap.setInfoWindowAdapter(infoWindowAdapter);
        AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (int i = 0; i < markList.size(); i++) {
                    if (markList.get(i).equals(marker.getId())) {
                        Intent intent = new Intent(getActivity(), DetectionActivity.class);
                        intent.putExtra("id", mapBean.getData().getDepartList().get(i).getId() + "");
                        startActivity(intent);
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

    public void getData() {

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
            return bean.getData().getSwitchRoomList().size();
        }

        @Override
        public Object getItem(int position) {
            return bean.getData().getSwitchRoomList().get(position);
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
                cache.tv_item_detecticon = (TextView) convertView.findViewById(R.id.tv_item_detecticon);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.tv_item_detecticon.setText(bean.getData().getSwitchRoomList().get(position).getDepartname());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pdsId = bean.getData().getSwitchRoomList().get(position).getId() + "";
                    depart = bean.getData().getSwitchRoomList().get(position).getDepartname();
                    tv_detection_adress.setText(bean.getData().getSwitchRoomList().get(position).getDepartname());
                    depart = bean.getData().getSwitchRoomList().get(position).getDepartname();
                    rl_detection.setVisibility(View.GONE);
                    rightState = false;
                    srl_detection.autoRefresh();
                }
            });
            return convertView;
        }
    }

    class Cache {
        TextView tv_item_detecticon;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        srl_detection.autoRefresh();
    }
}
