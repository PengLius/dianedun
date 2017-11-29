package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.HumidityCharBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/10/21.
 */

public class HumidityCharActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.tv_humiditychar_1)
    TextView tv_humiditychar_1;

    @Bind(R.id.tv_humiditychar_2)
    TextView tv_humiditychar_2;

    @Bind(R.id.tv_humiditychar_3)
    TextView tv_humiditychar_3;

    @Bind(R.id.tv_humiditychar_4)
    TextView tv_humiditychar_4;

    @Bind(R.id.tv_humiditychar_adress)
    TextView tv_humiditychar_adress;

    @Bind(R.id.lcv_sd)
    LineChartView lcv_sd;

    @Bind(R.id.tv_sd_mean)
    TextView tv_sd_mean;

    @Bind(R.id.tv_sd_max)
    TextView tv_sd_max;

    @Bind(R.id.srl_humidity)
    SmartRefreshLayout srl_humidity;

    private String RoomId;
    private String url;
    private int type = 1;
    private HumidityCharBean bean;
    List<AxisValue> values;
    private Boolean treadoff = true;
    private String flag = "day";
    private String num = "1";
    private MyAsyncTast.Callback callback = new MyAsyncTast.Callback() {
        @Override
        public void send(String result) {
            bean = GsonUtil.parseJsonWithGson(result, HumidityCharBean.class);
            List<String> xList = new ArrayList<>();
            for (int i = 0; i < bean.getData().getXList().size(); i++) {
                xList.add(bean.getData().getXList().get(i).substring(5, bean.getData().getXList().get(i).length()));
            }
            tv_sd_max.setText(getMaxNumb(bean.getData().getHumidityList()) + "%");
            tv_sd_mean.setText(getmMeanNumb(bean.getData().getHumidityList()) + "%");
            initLineChart(lcv_sd, bean.getData().getHumidityList(), xList);
            srl_humidity.finishRefresh();
        }

        @Override
        public void onError(String result) {
            srl_humidity.finishRefresh();
            showToast(result);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humiditychar);
        setTvTitleText("实时监测");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        tv_humiditychar_1.setOnClickListener(this);
        tv_humiditychar_2.setOnClickListener(this);
        tv_humiditychar_3.setOnClickListener(this);
        tv_humiditychar_4.setOnClickListener(this);
        tv_humiditychar_adress.setText(getIntent().getStringExtra("depart"));
        RoomId = getIntent().getStringExtra("RoomId");
        initRefreshLayout();
        srl_humidity.autoRefresh();
    }

    private void initRefreshLayout() {
        srl_humidity.setLoadmoreFinished(true);
        srl_humidity.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(flag, num);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_humiditychar_1:
                tv_humiditychar_1.setBackgroundResource(R.mipmap.jc_tab1);
                tv_humiditychar_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 1) {
                    flag = "day";
                    num = "1";
                    srl_humidity.autoRefresh();
                    type = 1;
                }
                break;
            case R.id.tv_humiditychar_2:
                tv_humiditychar_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_2.setBackgroundResource(R.mipmap.jc_tab1);
                tv_humiditychar_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 2) {
                    flag = "week";
                    num = "1";
                    srl_humidity.autoRefresh();
                    type = 2;
                }
                break;
            case R.id.tv_humiditychar_3:
                tv_humiditychar_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_3.setBackgroundResource(R.mipmap.jc_tab1);
                tv_humiditychar_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 3) {
                    flag = "month";
                    num = "1";
                    srl_humidity.autoRefresh();
                    type = 3;
                }
                break;
            case R.id.tv_humiditychar_4:
                tv_humiditychar_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_humiditychar_4.setBackgroundResource(R.mipmap.jc_tab1);
                if (type != 4) {
                    flag = "month";
                    num = "6";
                    srl_humidity.autoRefresh();
                    type = 4;
                }
                break;
            default:
                break;
        }
    }

    public void getData(String flag, String num) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("RoomId", RoomId);
        hashMap.put("flag", flag);
        hashMap.put("num", num);
        MyAsyncTast myAsyncTast = new MyAsyncTast(HumidityCharActivity.this, hashMap, AppConfig.STATSHUMIDITY, App.getInstance().getToken(), false, callback);
        myAsyncTast.execute();
    }

    /**
     * 数据点
     *
     * @param data
     * @return
     */
    private List<PointValue> getAxisPoints(List<String> data) {
        List<PointValue> mPointValues = new ArrayList<PointValue>();
        for (int i = 0; i < data.size(); i++) {
            mPointValues.add(new PointValue(i, Float.parseFloat(data.get(i))));
        }
        return mPointValues;
    }

    /**
     * X轴标注
     */
    private List<AxisValue> getAxisXLables(List<String> Ybz) {
        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
        for (int i = 0; i < Ybz.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(Ybz.get(i)));
        }
        return mAxisXValues;
    }

    /**
     * 初始化图表
     */
    private void initLineChart(LineChartView chart, List<String> data, List<String> Ybz) {
        values = new ArrayList<>();
        List<Line> lines = new ArrayList<Line>();//折线的集合
        Line line = new Line(getAxisPoints(data)).setColor(Color.parseColor("#e84b06"));  //折线的颜色
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setStrokeWidth(1);
        lines.add(line);

        LineChartData datas = new LineChartData();
        datas.setLines(lines);//加入图表中
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextSize(8);
        //axisX.setName("date");  //表格名称
        axisX.setMaxLabelChars(1); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(getAxisXLables(Ybz));


        datas.setAxisXBottom(axisX); //x 轴在底部
        Axis axisY = new Axis();  //Y轴

        axisY.setHasLines(true).setLineColor(Color.parseColor("#323944")); //y轴分割线

        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.WHITE);
        for (int i = 0; i < 101; i += 10) {
            AxisValue value = new AxisValue(i);
            String label = i + "";
            value.setLabel(label);
            values.add(value);
        }
        axisY.setValues(values);

        datas.setAxisYLeft(axisY);  //Y轴设置在左边
        chart.setInteractive(true);
        chart.setScrollEnabled(true);
        chart.setZoomEnabled(false);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setLineChartData(datas);
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.top = 110;
        v.bottom = 0;
        chart.setMaximumViewport(v);
        v.left = 0;
        v.right = 4;
        chart.setCurrentViewport(v);
    }

    public int getMaxNumb(List<String> numbList) {
        int a = 0;
        for (int i = 0; i < numbList.size(); i++) {
            int numb = Integer.parseInt(numbList.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        return a;
    }

    public int getmMeanNumb(List<String> numbList) {
        if(numbList.size()>0){
            int a = 0;
            for (int i = 0; i < numbList.size(); i++) {
                int numb = Integer.parseInt(numbList.get(i));
                a = numb + a;
            }
            return a / (numbList.size());
        }else{
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        treadoff = false;
        super.onDestroy();
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("实时监测");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("实时监测");
        MobclickAgent.onPause(this);
    }
}
