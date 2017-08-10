package cn.dianedun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import cn.dianedun.activity.DetailsingActivity;
import cn.dianedun.activity.DisposeJbActivity;
import cn.dianedun.activity.MessageActivity;

/**
 * Created by Administrator on 2017/8/8.
 */

public class GaoYaFragment extends Fragment {

    private IndentCusAdapter adapter;

    View view;
    String result;

    @Bind(R.id.lv_gaoya)
    ListView lv_gaoya;


    public GaoYaFragment(String result) {
        this.result = result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gaoya, null);
        ButterKnife.bind(this, view);
        adapter = new IndentCusAdapter();
        lv_gaoya.setAdapter(adapter);
        return view;
    }


    private class IndentCusAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_gaoya, null);
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
