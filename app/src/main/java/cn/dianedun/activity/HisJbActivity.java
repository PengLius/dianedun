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

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.HisJbBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HisJbActivity extends BaseTitlActivity {

    @Bind(R.id.lv_hisjb)
    ListView lv_hisjb;

    private IndentCusAdapter adapter;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private HisJbBean bean;
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


    }

    private void initData() {
        hashMap = new HashMap<>();
        hashMap.put("currentPage", page + "");
        myAsyncTast = new MyAsyncTast(HisJbActivity.this, hashMap, AppConfig.HISTORYALARM, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, HisJbBean.class);
                adapter = new IndentCusAdapter();
                lv_hisjb.setAdapter(adapter);
            }
        });
        myAsyncTast.execute();
    }


    private class IndentCusAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bean.getData().getList().size();
        }

        @Override
        public Object getItem(int position) {
            return bean.getData().getList().get(position);
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
                cache.tv_hisjbitem_adress = (TextView) convertView.findViewById(R.id.tv_hisjbitem_type);
                cache.tv_hisjbitem_type = (TextView) convertView.findViewById(R.id.tv_hisjbitem_type);
                cache.tv_hisjbitem_time = (TextView) convertView.findViewById(R.id.tv_hisjbitem_time);
                cache.tv_hisjbitem_cause = (TextView) convertView.findViewById(R.id.tv_hisjbitem_cause);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.tv_hisjbitem_adress.setText(bean.getData().getList().get(position).getDepartName());
            if (bean.getData().getList().get(position).getType() == 1) {
                cache.tv_hisjbitem_type.setText("普通");
            } else {
                cache.tv_hisjbitem_type.setText("严重");
            }
            cache.tv_hisjbitem_time.setText(bean.getData().getList().get(position).getCreateTime() + "");
            cache.tv_hisjbitem_cause.setText(bean.getData().getList().get(position).getAlertDetails());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HisDisposeJbActivity.class);
                    intent.putExtra("id", bean.getData().getList().get(position).getId() + "");
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
