package cn.dianedun.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseActivity;
import cn.dianedun.bean.DetactionXBean;
import cn.dianedun.bean.PeiDSBean;
import cn.dianedun.fragment.DiYaFragment;
import cn.dianedun.fragment.GaoCeFragment;
import cn.dianedun.fragment.HumidityFragment;
import cn.dianedun.fragment.TemperatureFragment;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.CommonUtil;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

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

    @Bind(R.id.img_detection_close)
    ImageView img_detection_close;

    @Bind(R.id.vt_img_right)
    ImageView vt_img_right;

    @Bind(R.id.rl_detection)
    RelativeLayout rl_detection;

    @Bind(R.id.lv_detection)
    ListView lv_detection;

    @Bind(R.id.tv_detection_adress)
    TextView tv_detection_adress;


    private List<Fragment> mList;
    private boolean rightState = false;
    private HashMap<String, String> hashMap;
    private MyAsyncTast myAsyncTast;
    private PeiDSBean bean;
    private IndentCusAdapter adapter;
    private DetactionXBean xBean;
    private GaoCeFragment gaoCeFragment;
    private DiYaFragment diYaFragment;
    private TemperatureFragment temperatureFragment;
    private HumidityFragment humidityFragment;
    private String pdsId, depart;
    private Boolean treadoff = true;
    private Thread thread;
    private Boolean firstCome = true;


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
        gaoCeFragment = new GaoCeFragment();
        diYaFragment = new DiYaFragment();
        temperatureFragment = new TemperatureFragment();
        humidityFragment = new HumidityFragment();
        mList.add(gaoCeFragment);
        mList.add(diYaFragment);
        mList.add(temperatureFragment);
        mList.add(humidityFragment);
        tv_detection_gy.setOnClickListener(this);
        tv_detection_dy.setOnClickListener(this);
        tv_detection_wd.setOnClickListener(this);
        tv_detection_sd.setOnClickListener(this);
        img_detection_close.setOnClickListener(this);
        vt_img_right.setOnClickListener(this);
        rl_detection.setOnClickListener(this);
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), mList);
        vp_detection.setAdapter(myAdapter);
        vp_detection.setCurrentItem(0);
        vp_detection.setOffscreenPageLimit(3);
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
            case R.id.img_detection_close:
                finish();
                break;
            case R.id.rl_detection:
                rl_detection.setVisibility(GONE);
                break;

            case R.id.vt_img_right:
                if (rightState) {
                    rl_detection.setVisibility(View.GONE);
                    rightState = false;
                } else {
                    hashMap = new HashMap<>();
                    hashMap.put("id", getIntent().getStringExtra("id"));
                    myAsyncTast = new MyAsyncTast(DetectionActivity.this, hashMap, AppConfig.FINDSWITCHROOMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                        @Override
                        public void onError(String result) {
                            showToast(result);
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
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_detection, null);
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
                    depart = bean.getData().getSwitchRoomList().get(position).getDepartname();
                    pdsId = bean.getData().getSwitchRoomList().get(position).getId();
                    tv_detection_adress.setText(bean.getData().getSwitchRoomList().get(position).getDepartname());
                    rl_detection.setVisibility(View.GONE);
                    rightState = false;
                    getPDSAll(pdsId, true, false);
                }
            });
            return convertView;
        }
    }

    class Cache {
        TextView tv_item_detecticon;
    }

    /**
     * 配电室数据
     *
     * @param RoomId 配电室Id
     */
    private void getPDSAll(final String RoomId, boolean laodoff, final boolean isStarThread) {
        hashMap = new HashMap<>();
        hashMap.put("RoomId", RoomId);
        myAsyncTast = new MyAsyncTast(DetectionActivity.this, hashMap, AppConfig.FINDALLLATEST, App.getInstance().getToken(), laodoff, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
                if (isStarThread) {
                    new Thread(sendable).start();
                }
            }

            @Override
            public void send(String result) {
                xBean = GsonUtil.parseJsonWithGson(result, DetactionXBean.class);
                gaoCeFragment.setData(xBean, RoomId, depart);
                diYaFragment.setData(xBean, RoomId, depart);
                temperatureFragment.setData(xBean, RoomId, depart);
                humidityFragment.setData(xBean, RoomId, depart);
                if (isStarThread) {
                    new Thread(sendable).start();
                }
            }
        });
        myAsyncTast.execute();
    }

    private void firstData() {
        hashMap = new HashMap<>();
        hashMap.put("id", getIntent().getStringExtra("id"));
        myAsyncTast = new MyAsyncTast(DetectionActivity.this, hashMap, AppConfig.FINDSWITCHROOMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
                new Thread(sendable).start();
            }

            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, PeiDSBean.class);
                pdsId = bean.getData().getSwitchRoomList().get(0).getId();
                getPDSAll(pdsId, false, true);
                depart = bean.getData().getSwitchRoomList().get(0).getDepartname();
                tv_detection_adress.setText(bean.getData().getSwitchRoomList().get(0).getDepartname());
            }
        });
        myAsyncTast.execute();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("监测Activity");
        MobclickAgent.onResume(this);
        treadoff = true;
        if (firstCome) {
            firstData();
            firstCome = false;
        } else {
            if (pdsId != null) {
                new Thread(sendable).start();
            } else {
                firstData();
            }
        }
    }

    public void onPause() {
        super.onPause();
        treadoff = false;
        MobclickAgent.onPageEnd("监测Activity");
        MobclickAgent.onPause(this);
    }

    /**
     * 定时刷新
     */
    private Runnable sendable = new Runnable() {
        @Override
        public void run() {
            int a = 12;
            while (-1 < a && treadoff) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.arg1 = a;
                    handler.sendMessage(message);
                    a--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0) {
                if (pdsId != null) {
                    getPDSAll(pdsId, false, true);
                } else {
                    firstData();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        treadoff = false;
        super.onDestroy();
    }
}
