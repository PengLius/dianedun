package cn.dianedun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;
import cn.dianedun.activity.GdCeCharActivity;
import cn.dianedun.bean.DetactionXBean;
import cn.dianedun.tools.AppConfig;

/**
 * Created by Administrator on 2017/8/8.
 */

public class DiYaFragment extends Fragment {

    private IndentCusAdapter adapter = new IndentCusAdapter();
    private DetactionXBean bean;
    View view;
    private String RoomId, depart;

    @Bind(R.id.lv_diya)
    ListView lv_diya;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diya, null);
        ButterKnife.bind(this, view);
        lv_diya.setAdapter(adapter);
        return view;
    }

    public void setData(DetactionXBean xBean,String RoomId, String depart) {
        bean = xBean;
        this.RoomId = RoomId;
        this.depart = depart;
        adapter.notifyDataSetChanged();
    }

    private class IndentCusAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (bean != null) {
                return bean.getData().getLdevice().size();
            } else {
                return 0;
            }

        }

        @Override
        public Object getItem(int position) {
            return bean.getData().getLdevice().get(position);
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
                cache.tv_gyc_name = (TextView) convertView.findViewById(R.id.tv_gyc_name);
                cache.tv_gyc_GA = (TextView) convertView.findViewById(R.id.tv_gyc_GA);
                cache.tv_gyc_GB = (TextView) convertView.findViewById(R.id.tv_gyc_GB);
                cache.tv_gyc_GC = (TextView) convertView.findViewById(R.id.tv_gyc_GC);
                cache.tv_gyc_XAB = (TextView) convertView.findViewById(R.id.tv_gyc_XAB);
                cache.tv_gyc_XBC = (TextView) convertView.findViewById(R.id.tv_gyc_XBC);
                cache.tv_gyc_XCA = (TextView) convertView.findViewById(R.id.tv_gyc_XCA);
                cache.tv_gyc_DA = (TextView) convertView.findViewById(R.id.tv_gyc_DA);
                cache.tv_gyc_DB = (TextView) convertView.findViewById(R.id.tv_gyc_DB);
                cache.tv_gyc_DC = (TextView) convertView.findViewById(R.id.tv_gyc_DC);

                cache.img_gyc_GA = (ImageView) convertView.findViewById(R.id.img_gyc_GA);
                cache.img_gyc_GB = (ImageView) convertView.findViewById(R.id.img_gyc_GB);
                cache.img_gyc_GC = (ImageView) convertView.findViewById(R.id.img_gyc_GC);
                cache.img_gyc_XAB = (ImageView) convertView.findViewById(R.id.img_gyc_XAB);
                cache.img_gyc_XBC = (ImageView) convertView.findViewById(R.id.img_gyc_XBC);
                cache.img_gyc_XCA = (ImageView) convertView.findViewById(R.id.img_gyc_XCA);
                cache.img_gyc_DA = (ImageView) convertView.findViewById(R.id.img_gyc_DA);
                cache.img_gyc_DB = (ImageView) convertView.findViewById(R.id.img_gyc_DB);
                cache.img_gyc_DC = (ImageView) convertView.findViewById(R.id.img_gyc_DC);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.tv_gyc_name.setText(bean.getData().getLdevice().get(position).getDeviceno());
            cache.tv_gyc_GA.setText(bean.getData().getLdevice().get(position).getVa().getVal() + "V");
            cache.tv_gyc_GB.setText(bean.getData().getLdevice().get(position).getVb().getVal() + "V");
            cache.tv_gyc_GC.setText(bean.getData().getLdevice().get(position).getVc().getVal() + "V");
            cache.tv_gyc_XAB.setText(bean.getData().getLdevice().get(position).getVab().getVal() + "V");
            cache.tv_gyc_XBC.setText(bean.getData().getLdevice().get(position).getVbc().getVal() + "V");
            cache.tv_gyc_XCA.setText(bean.getData().getLdevice().get(position).getVca().getVal() + "V");
            cache.tv_gyc_DA.setText(bean.getData().getLdevice().get(position).getIa().getVal() + "A");
            cache.tv_gyc_DB.setText(bean.getData().getLdevice().get(position).getIb().getVal() + "A");
            cache.tv_gyc_DC.setText(bean.getData().getLdevice().get(position).getIc().getVal() + "A");

            setImag(cache.img_gyc_GA, bean.getData().getLdevice().get(position).getVa().getLevel());
            setImag(cache.img_gyc_GB, bean.getData().getLdevice().get(position).getVb().getLevel());
            setImag(cache.img_gyc_GC, bean.getData().getLdevice().get(position).getVc().getLevel());
            setImag(cache.img_gyc_XAB, bean.getData().getLdevice().get(position).getVab().getLevel());
            setImag(cache.img_gyc_XBC, bean.getData().getLdevice().get(position).getVbc().getLevel());
            setImag(cache.img_gyc_XCA, bean.getData().getLdevice().get(position).getVca().getLevel());
            setImag(cache.img_gyc_DA, bean.getData().getLdevice().get(position).getIa().getLevel());
            setImag(cache.img_gyc_DB, bean.getData().getLdevice().get(position).getIb().getLevel());
            setImag(cache.img_gyc_DC, bean.getData().getLdevice().get(position).getIc().getLevel());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GdCeCharActivity.class);
                    intent.putExtra("RoomId", RoomId);
                    intent.putExtra("depart", depart);
                    intent.putExtra("url", AppConfig.STATSLDEVICE);
                    intent.putExtra("deviceNum", bean.getData().getLdevice().get(position).getDeviceno());
                    intent.putExtra("types", "1");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }


    private void setImag(ImageView imag, String type) {
        if (type.equals("0")) {
            imag.setImageResource(R.mipmap.jc_green);
        } else if (type.equals("1")) {
            imag.setImageResource(R.mipmap.jc_ye);
        } else if (type.equals("2")) {
            imag.setImageResource(R.mipmap.jc_red);
        }

    }

    class Cache {
        TextView tv_gyc_name, tv_gyc_GA, tv_gyc_GB, tv_gyc_GC, tv_gyc_XAB, tv_gyc_XBC, tv_gyc_XCA, tv_gyc_DA, tv_gyc_DB, tv_gyc_DC;
        ImageView img_gyc_GA, img_gyc_GB, img_gyc_GC, img_gyc_XAB, img_gyc_XBC, img_gyc_XCA, img_gyc_DA, img_gyc_DB, img_gyc_DC;
    }
}
