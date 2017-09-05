package cn.dianedun.fragment;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;

/**
 * Created by Administrator on 2017/8/8.
 */

public class TemperatureFragment extends Fragment {

    private IndentCusAdapter adapter;

    @Bind(R.id.lv_temperature)
    ListView lv_temperature;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_temperature, null);
        ButterKnife.bind(this, view);
        adapter = new IndentCusAdapter();
        lv_temperature.setAdapter(adapter);
        return view;
    }

    private class IndentCusAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_temperature, null);
                cache = new Cache();
                cache.ll_temperature = (LinearLayout) convertView.findViewById(R.id.ll_temperature);
                cache.tv_item_temname = (TextView) convertView.findViewById(R.id.tv_item_temname);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            if (position == 0) {
                cache.ll_temperature.setVisibility(View.VISIBLE);
                cache.tv_item_temname.setText("变压器温度");
            } else if (position == 4) {
                cache.ll_temperature.setVisibility(View.VISIBLE);
                cache.tv_item_temname.setText("母排温度");
            } else {
                cache.ll_temperature.setVisibility(View.GONE);
            }


            return convertView;
        }
    }

    class Cache {
        LinearLayout ll_temperature;
        TextView tv_item_temname;
    }

}
