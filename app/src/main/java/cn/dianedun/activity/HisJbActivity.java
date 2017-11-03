package cn.dianedun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import lecho.lib.hellocharts.model.Line;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HisJbActivity extends BaseTitlActivity {

    @Bind(R.id.lv_hisjb)
    ListView lv_hisjb;

    @Bind(R.id.srl_hisjb)
    SmartRefreshLayout srl_hisjb;

    @Bind(R.id.ll_hisjb_null)
    LinearLayout ll_hisjb_null;

    private IndentCusAdapter adapter;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private HisJbBean bean;
    private HisJbBean.DataBean.ListBean listBean;
    private List<HisJbBean.DataBean.ListBean> allList;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisjb);
        setTvTitleText("历史警报");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        initData();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        srl_hisjb.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });
        srl_hisjb.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                hashMap = new HashMap<>();
                hashMap.put("currentPage", page + "");
                myAsyncTast = new MyAsyncTast(HisJbActivity.this, hashMap, AppConfig.HISTORYALARM, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
                    @Override
                    public void onError(String result) {
                        srl_hisjb.finishLoadmore();
                        showToast(result);
                    }

                    @Override
                    public void send(String result) {
                        srl_hisjb.finishLoadmore();
                        page++;
                        bean = GsonUtil.parseJsonWithGson(result, HisJbBean.class);
                        for (int i = 0; i < bean.getData().getList().size(); i++) {
                            allList.add(bean.getData().getList().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        if (bean.getData().getList().size() < 10) {
                            srl_hisjb.setLoadmoreFinished(true);
                        }
                    }
                });
                myAsyncTast.execute();
            }
        });
    }

    private void initData() {
        page = 1;
        srl_hisjb.setLoadmoreFinished(false);
        hashMap = new HashMap<>();
        hashMap.put("currentPage", page + "");
        myAsyncTast = new MyAsyncTast(HisJbActivity.this, hashMap, AppConfig.HISTORYALARM, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                srl_hisjb.finishRefresh();
                if (result.equals("无记录")) {
                    ll_hisjb_null.setVisibility(View.VISIBLE);
                    srl_hisjb.setVisibility(View.GONE);
                } else {
                    showToast(result);
                }
            }

            @Override
            public void send(String result) {
                srl_hisjb.finishRefresh();
                bean = GsonUtil.parseJsonWithGson(result, HisJbBean.class);
                allList = new ArrayList<>();
                if (bean.getData().getList().size() > 0) {
                    for (int i = 0; i < bean.getData().getList().size(); i++) {
                        allList.add(bean.getData().getList().get(i));
                    }
                    adapter = new IndentCusAdapter();
                    lv_hisjb.setAdapter(adapter);
                    page++;
                } else {
                    ll_hisjb_null.setVisibility(View.VISIBLE);
                    srl_hisjb.setVisibility(View.GONE);
                }
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
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_hisjb, null);
                cache = new Cache();
                cache.tv_hisjbitem_adress = (TextView) convertView.findViewById(R.id.tv_hisjbitem_adress);
                cache.tv_hisjbitem_type = (TextView) convertView.findViewById(R.id.tv_hisjbitem_type);
                cache.tv_hisjbitem_time = (TextView) convertView.findViewById(R.id.tv_hisjbitem_time);
                cache.tv_hisjbitem_cause = (TextView) convertView.findViewById(R.id.tv_hisjbitem_cause);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.tv_hisjbitem_adress.setText(allList.get(position).getDepartName());
            if (allList.get(position).getType() == 1) {
                cache.tv_hisjbitem_type.setText("一般");
            } else {
                cache.tv_hisjbitem_type.setText("严重");
            }
            cache.tv_hisjbitem_time.setText(allList.get(position).getCreateTime() + "");
            cache.tv_hisjbitem_cause.setText(allList.get(position).getAlertDetails());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HisDisposeJbActivity.class);
                    intent.putExtra("id", allList.get(position).getId() + "");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class Cache {
        TextView tv_hisjbitem_type, tv_hisjbitem_adress, tv_hisjbitem_time, tv_hisjbitem_cause;
    }
}
