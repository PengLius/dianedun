package cn.dianedun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;
import cn.dianedun.activity.GdCeCharActivity;
import cn.dianedun.activity.TempChartActivity;
import cn.dianedun.bean.DetactionXBean;
import cn.dianedun.tools.AppConfig;

/**
 * Created by Administrator on 2017/8/8.
 */

public class TemperatureFragment extends Fragment {

    @Bind(R.id.lv_temperature)
    ListView lv_temperature;


    View view;
    private IndentCusAdapter adapter;
    private DetactionXBean bean;
    private List<HashMap<String, String>> allList;
    private HashMap<String, String> hashMap;
    private String RoomId, depart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_temperature, null);
        ButterKnife.bind(this, view);
        allList = new ArrayList<>();
        adapter = new IndentCusAdapter();
        lv_temperature.setAdapter(adapter);
        return view;
    }

    public void setData(DetactionXBean xBean, String RoomId, String depart) {
        bean = xBean;
        this.RoomId = RoomId;
        this.depart = depart;
        allList = new ArrayList<>();
        for (int i = 0; i < bean.getData().getTemp().getTransformer().size(); i++) {
            hashMap = new HashMap<>();
            hashMap.put("level", bean.getData().getTemp().getTransformer().get(i).getLevel());
            hashMap.put("number", bean.getData().getTemp().getTransformer().get(i).getNumber());
            hashMap.put("val", bean.getData().getTemp().getTransformer().get(i).getVal() + "");
            allList.add(hashMap);
        }
        for (int i = 0; i < bean.getData().getTemp().getBusbar().size(); i++) {
            hashMap = new HashMap<>();
            hashMap.put("level", bean.getData().getTemp().getBusbar().get(i).getLevel());
            hashMap.put("number", bean.getData().getTemp().getBusbar().get(i).getNumber());
            hashMap.put("val", bean.getData().getTemp().getBusbar().get(i).getVal() + "");
            allList.add(hashMap);
        }
        adapter.notifyDataSetChanged();
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_temperature, null);
                cache = new Cache();
                cache.ll_temperature = (LinearLayout) convertView.findViewById(R.id.ll_temperature);
                cache.tv_item_temname = (TextView) convertView.findViewById(R.id.tv_item_temname);
                cache.tv_wd_name = (TextView) convertView.findViewById(R.id.tv_wd_name);
                cache.tv_wd_wd = (TextView) convertView.findViewById(R.id.tv_wd_wd);
                cache.img_wd_type = (ImageView) convertView.findViewById(R.id.img_wd_type);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }

            if (position == 0) {
                cache.ll_temperature.setVisibility(View.VISIBLE);
                cache.tv_item_temname.setText("变压器温度");
            } else if (position == bean.getData().getTemp().getTransformer().size()) {
                cache.ll_temperature.setVisibility(View.VISIBLE);
                cache.tv_item_temname.setText("母排温度");
            } else {
                cache.ll_temperature.setVisibility(View.GONE);
            }
            cache.tv_wd_name.setText(allList.get(position).get("number"));
            cache.tv_wd_wd.setText(allList.get(position).get("val") + "℃");
            if (allList.get(position).get("level").equals("0")) {
                cache.img_wd_type.setImageResource(R.mipmap.jc_green);
            } else if (allList.get(position).get("level").equals("1")) {
                cache.img_wd_type.setImageResource(R.mipmap.jc_ye);
            } else if (allList.get(position).get("level").equals("2")) {
                cache.img_wd_type.setImageResource(R.mipmap.jc_red);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TempChartActivity.class);
                    intent.putExtra("RoomId", RoomId);
                    intent.putExtra("depart", depart);
                    intent.putExtra("deviceNum", allList.get(position).get("number") + "");
                    if (position < bean.getData().getTemp().getTransformer().size()) {
                        intent.putExtra("type", "0");
                    } else {
                        intent.putExtra("type", "1");
                    }
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    class Cache {
        LinearLayout ll_temperature;
        TextView tv_item_temname, tv_wd_name, tv_wd_wd;
        ImageView img_wd_type;
    }

}
