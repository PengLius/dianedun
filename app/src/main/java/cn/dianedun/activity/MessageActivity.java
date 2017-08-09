package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MessageActivity extends BaseTitlActivity {

    private IndentCusAdapter adapter;

    @Bind(R.id.lv_message)
    ListView lv_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTvTitleText("我的消息");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        adapter = new IndentCusAdapter();
        lv_message.setAdapter(adapter);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final Cache cache;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_message, null);
                cache = new Cache();
                cache.img_itmemessage = (ImageView) convertView.findViewById(R.id.img_itmemessage);
                cache.ll_itemmassge = (LinearLayout) convertView.findViewById(R.id.ll_itemmassge);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            if (position == 5) {
                cache.img_itmemessage.setImageResource(R.mipmap.msg);
                cache.ll_itemmassge.setBackgroundColor(Color.parseColor("#60D9D9D9"));
            } else {
                cache.img_itmemessage.setImageResource(R.mipmap.msg1);
                cache.ll_itemmassge.setBackgroundColor(Color.parseColor("#6019246B"));
            }

            return convertView;
        }
    }

    class Cache {
        TextView tv_grouponall_name, tv_grouponall_from, tv_grouponall_offered;
        ImageView img_itmemessage;
        LinearLayout ll_itemmassge;
    }


}
