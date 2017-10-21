package cn.dianedun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.HisJbBean;
import cn.dianedun.bean.HisOrderListBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.DataUtil;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HisWorkOrderActivity extends BaseTitlActivity {

    @Bind(R.id.lv_hisworkorder)
    ListView lv_hisworkorder;

    @Bind(R.id.srl_hisworkorder)
    SmartRefreshLayout srl_hisworkorder;

    private IndentCusAdapter adapter;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private int page = 1;
    private HisOrderListBean bean;
    private Intent intent;
    private List<HisOrderListBean.DataBean.ResultBean> allList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisworkorder);
        setTvTitleText("历史工单");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        getData();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        srl_hisworkorder.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
            }
        });
        srl_hisworkorder.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                hashMap = new HashMap<>();
                hashMap.put("stats", "0,4");
                hashMap.put("startIndex", page + "");
                hashMap.put("pageSize", "10");
                myAsyncTast = new MyAsyncTast(HisWorkOrderActivity.this, hashMap, AppConfig.GETHANDLEORDERBYSTATS, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
                    @Override
                    public void onError(String result) {
                        srl_hisworkorder.finishLoadmore();
                    }

                    @Override
                    public void send(String result) {
                        srl_hisworkorder.finishLoadmore();
                        page++;
                        bean = GsonUtil.parseJsonWithGson(result, HisOrderListBean.class);
                        for (int i = 0; i < bean.getData().getResult().size(); i++) {
                            allList.add(bean.getData().getResult().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        if (bean.getData().getResult().size() < 10) {
                            srl_hisworkorder.setLoadmoreFinished(true);
                        }
                    }
                });
                myAsyncTast.execute();


            }
        });
    }


    public void getData() {
        page = 1;
        hashMap = new HashMap<>();
        hashMap.put("stats", "0,4");
        hashMap.put("startIndex", page + "");
        hashMap.put("pageSize", "10");
        myAsyncTast = new MyAsyncTast(HisWorkOrderActivity.this, hashMap, AppConfig.GETHANDLEORDERBYSTATS, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                srl_hisworkorder.finishRefresh();
            }

            @Override
            public void send(String result) {
                srl_hisworkorder.finishRefresh();
                srl_hisworkorder.setLoadmoreFinished(false);
                bean = GsonUtil.parseJsonWithGson(result, HisOrderListBean.class);
                allList = new ArrayList<>();
                for (int i = 0; i < bean.getData().getResult().size(); i++) {
                    allList.add(bean.getData().getResult().get(i));
                }
                page++;
                adapter = new IndentCusAdapter();
                lv_hisworkorder.setAdapter(adapter);
            }
        });
        myAsyncTast.execute();
    }

    private class IndentCusAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return allList.size();
        }

        @Override
        public Object getItem(int position) {
            return allList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Cache cache;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_hisworkorder, null);
                cache = new Cache();
                cache.item_hometv_code = (TextView) convertView.findViewById(R.id.item_hometv_code);
                cache.item_hometv_startime = (TextView) convertView.findViewById(R.id.item_hometv_startime);
                cache.item_hometv_endtime = (TextView) convertView.findViewById(R.id.item_hometv_endtime);
                cache.item_hometv_adress = (TextView) convertView.findViewById(R.id.item_hometv_adress);
                cache.item_homeitv_jg = (TextView) convertView.findViewById(R.id.item_homeitv_jg);
                cache.item_homeitv_sta = (TextView) convertView.findViewById(R.id.item_homeitv_sta);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.item_hometv_code.setText(allList.get(position).getOrderNum() + "");
            if (allList.get(position).getStatus() == 0) {
                cache.item_homeitv_sta.setText("已撤销");
            } else if (allList.get(position).getStatus() == 4) {
                cache.item_homeitv_sta.setText("已完成");
            }

            String begin = allList.get(position).getBeginTime();
            String end = allList.get(position).getEndTime();
            if (begin != null) {
                long beginTimes = Long.parseLong(begin);
                String beginTime = beginTimes / 1000 + "";
                cache.item_hometv_startime.setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm") + ":00");
            } else {
                cache.item_hometv_startime.setText("");
            }
            if (end != null) {
                long endTimes = Long.parseLong(end);
                String endTime = endTimes / 1000 + "";
                cache.item_hometv_endtime.setText(DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm") + ":00");
            } else {
                cache.item_hometv_endtime.setText("");
            }
            cache.item_hometv_adress.setText(allList.get(position).getDepartName() + "");
            if (allList.get(position).getUrgency() == 0) {
                cache.item_homeitv_jg.setText("普通");
            } else {
                cache.item_homeitv_jg.setText("紧急");
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(getApplicationContext(), HisDetailsActivity.class);
                    intent.putExtra("orderNum", allList.get(position).getOrderNum() + "");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class Cache {
        TextView item_hometv_code, item_hometv_startime, item_hometv_endtime, item_hometv_adress, item_homeitv_jg, item_homeitv_sta;
    }

}
