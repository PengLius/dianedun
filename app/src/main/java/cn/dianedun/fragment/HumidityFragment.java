package cn.dianedun.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;
import cn.dianedun.bean.DetactionXBean;

/**
 * Created by Administrator on 2017/8/8.
 */

public class HumidityFragment extends Fragment {

    @Bind(R.id.img_humidity_sd)
    ImageView img_humidity_sd;

    @Bind(R.id.tv_humidity_sd)
    TextView tv_humidity_sd;

    @Bind(R.id.img_humidity_sj)
    ImageView img_humidity_sj;

    @Bind(R.id.tv_humidity_sj)
    TextView tv_humidity_sj;

    @Bind(R.id.ll_humidity)
    LinearLayout ll_humidity;

    View view;
    private DetactionXBean bean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_humidity, null);
        ButterKnife.bind(this, view);
        if (bean != null) {
            ll_humidity.setVisibility(View.VISIBLE);
            tv_humidity_sd.setText(bean.getData().getHumidityAndWater().getHumidity().getVal() + "%");
            if (bean.getData().getHumidityAndWater().getWater().getVal().equals("0")) {
                tv_humidity_sj.setText("无");
            } else {
                tv_humidity_sj.setText("有");
            }

            if (bean.getData().getHumidityAndWater().getWater().getLevel().equals("0")) {
                img_humidity_sj.setImageResource(R.mipmap.jc_green);
            } else if (bean.getData().getHumidityAndWater().getWater().getLevel().equals("1")) {
                img_humidity_sj.setImageResource(R.mipmap.jc_ye);
            } else if (bean.getData().getHumidityAndWater().getWater().getLevel().equals("2")) {
                img_humidity_sj.setImageResource(R.mipmap.jc_red);
            }
            if (bean.getData().getHumidityAndWater().getHumidity().getLevel().equals("0")) {
                img_humidity_sd.setImageResource(R.mipmap.jc_green);
            } else if (bean.getData().getHumidityAndWater().getHumidity().getLevel().equals("1")) {
                img_humidity_sd.setImageResource(R.mipmap.jc_ye);
            } else if (bean.getData().getHumidityAndWater().getHumidity().getLevel().equals("2")) {
                img_humidity_sd.setImageResource(R.mipmap.jc_red);
            }
        }
        return view;
    }

    public void setData(DetactionXBean xBean) {
        bean = xBean;
        if (tv_humidity_sd != null) {
            ll_humidity.setVisibility(View.VISIBLE);
            tv_humidity_sd.setText(bean.getData().getHumidityAndWater().getHumidity().getVal() + "%");
            tv_humidity_sj.setText(bean.getData().getHumidityAndWater().getWater().getVal() + "");
            if (bean.getData().getHumidityAndWater().getWater().getLevel().equals("0")) {
                img_humidity_sj.setImageResource(R.mipmap.jc_green);
            } else if (bean.getData().getHumidityAndWater().getWater().getLevel().equals("1")) {
                img_humidity_sj.setImageResource(R.mipmap.jc_ye);
            } else if (bean.getData().getHumidityAndWater().getWater().getLevel().equals("2")) {
                img_humidity_sj.setImageResource(R.mipmap.jc_red);
            }
            if (bean.getData().getHumidityAndWater().getHumidity().getLevel().equals("0")) {
                img_humidity_sd.setImageResource(R.mipmap.jc_green);
            } else if (bean.getData().getHumidityAndWater().getHumidity().getLevel().equals("1")) {
                img_humidity_sd.setImageResource(R.mipmap.jc_ye);
            } else if (bean.getData().getHumidityAndWater().getHumidity().getLevel().equals("2")) {
                img_humidity_sd.setImageResource(R.mipmap.jc_red);
            }
        }

    }
}
