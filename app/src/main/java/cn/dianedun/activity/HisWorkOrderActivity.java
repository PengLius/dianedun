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

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
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

    private IndentCusAdapter adapter;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private int page = 1;
    private HisOrderListBean bean;
    private String results;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisworkorder);
        setTvTitleText("历史工单");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        getData();

    }

    public void getData() {
        hashMap = new HashMap<>();
        hashMap.put("stats", "0,4");
        hashMap.put("startIndex", page + "");
        hashMap.put("pageSize", "10");
        myAsyncTast = new MyAsyncTast(HisWorkOrderActivity.this, hashMap, AppConfig.GETHANDLEORDERBYSTATS, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                results = result;
                bean = GsonUtil.parseJsonWithGson(result, HisOrderListBean.class);
                adapter = new IndentCusAdapter();
                lv_hisworkorder.setAdapter(adapter);
            }
        });
        myAsyncTast.execute();
    }

    private class IndentCusAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bean.getData().getResult().size();
        }

        @Override
        public Object getItem(int position) {
            return bean.getData().getResult().get(position);
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
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.item_hometv_code.setText(bean.getData().getResult().get(position).getOrderNum() + "");
            String beginTime = bean.getData().getResult().get(position).getBeginTime() / 1000 + "";
            String endTiem = bean.getData().getResult().get(position).getEndTime() / 1000 + "";
            cache.item_hometv_startime.setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm"));
            cache.item_hometv_endtime.setText(DataUtil.timeStamp2Date(endTiem, "yyyy-MM-dd HH:mm"));
            cache.item_hometv_adress.setText(bean.getData().getResult().get(position).getAddress() + "");
            if (bean.getData().getResult().get(position).getUrgency() == 0) {
                cache.item_homeitv_jg.setText("普通");
            } else {
                cache.item_homeitv_jg.setText("紧急");
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    HisOrderListBean.DataBean.ResultBean hisOrderListBean = bean.getData().getResult().get(position);
                    intent = new Intent(getApplicationContext(), HisDetailsActivity.class);
//                    Bundle mBundle = new Bundle();
//                    mBundle.putSerializable("bean", hisOrderListBean);
//                    intent.putExtras(mBundle);
                    intent.putExtra("orderNum", bean.getData().getResult().get(position).getOrderNum() + "");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class Cache {
        TextView item_hometv_code, item_hometv_startime, item_hometv_endtime, item_hometv_adress, item_homeitv_jg;
    }

}
