package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.RealTimeConBean;
import cn.dianedun.bean.TempCharBean;
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
 * Created by Administrator on 2017/9/26.
 */

public class TempChartActivity extends BaseTitlActivity implements View.OnClickListener {


    @Bind(R.id.tv_tempchart_1)
    TextView tv_tempchart_1;

    @Bind(R.id.tv_tempchart_2)
    TextView tv_tempchart_2;

    @Bind(R.id.tv_tempchart_3)
    TextView tv_tempchart_3;

    @Bind(R.id.tv_tempchart_4)
    TextView tv_tempchart_4;

    @Bind(R.id.cash_chart)
    LineChartView lineChart;

    @Bind(R.id.tv_tempchart_adress)
    TextView tv_tempchart_adress;

    @Bind(R.id.srl_tempchart)
    SmartRefreshLayout srl_tempchart;

    @Bind(R.id.tv_tempchart_name)
    TextView tv_tempchart_name;

    @Bind(R.id.tv_tempchart_mean)
    TextView tv_tempchart_mean;

    @Bind(R.id.tv_tempchart_max)
    TextView tv_tempchart_max;

    private String RoomId;
    private String deviceNum;
    private int type = 1;
    private TempCharBean bean;
    private float maxXg;
    private Boolean treadoff = true;
    private String flag = "day";
    private String num = "1";
    private MyAsyncTast.Callback callback = new MyAsyncTast.Callback() {
        @Override
        public void send(String result) {
            bean = GsonUtil.parseJsonWithGson(result, TempCharBean.class);
            maxXg = getMaxNumb(bean.getData().getTempList());
            tv_tempchart_max.setText(new DecimalFormat("0.00").format(maxXg) + "℃");
            tv_tempchart_mean.setText(getmMeanNumb(bean.getData().getTempList()) + "℃");
            List<String> xList = new ArrayList<>();
            for (int i = 0; i < bean.getData().getXList().size(); i++) {
                xList.add(bean.getData().getXList().get(i).substring(5, bean.getData().getXList().get(i).length()));
            }
            initLineChart(lineChart, bean.getData().getTempList(), xList, maxXg);
            srl_tempchart.finishRefresh();
        }

        @Override
        public void onError(String result) {
            showToast(result);
            srl_tempchart.finishRefresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempchart);
        setTvTitleText("实时监测");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        tv_tempchart_1.setOnClickListener(this);
        tv_tempchart_2.setOnClickListener(this);
        tv_tempchart_3.setOnClickListener(this);
        tv_tempchart_4.setOnClickListener(this);
        tv_tempchart_adress.setText(getIntent().getStringExtra("depart"));
        RoomId = getIntent().getStringExtra("RoomId");
        deviceNum = getIntent().getStringExtra("deviceNum");
        if (getIntent().getStringExtra("type").equals("0")) {
            tv_tempchart_name.setText("变压器温度" + deviceNum);
        } else {
            tv_tempchart_name.setText("母排温度" + deviceNum);
        }
        initRefreshLayout();
        srl_tempchart.autoRefresh();
    }

    private void initRefreshLayout() {
        srl_tempchart.setLoadmoreFinished(true);
        srl_tempchart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(flag, num);
            }
        });
    }

    /**
     * 初始化图表
     */
    private void initLineChart(LineChartView chart, List<String> data, List<String> Ybz, float max) {
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
        axisX.setHasLines(false); //x 轴分割线
        datas.setAxisXBottom(axisX); //x 轴在底部
        Axis axisY = new Axis();  //Y轴

        axisY.setHasLines(true).setLineColor(Color.parseColor("#323944")); //y轴分割线

        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.WHITE);
        datas.setAxisYLeft(axisY);  //Y轴设置在左边
        chart.setInteractive(true);
        chart.setScrollEnabled(true);
        chart.setZoomEnabled(false);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setLineChartData(datas);
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.top = max + 10;
        v.bottom = 0;
        chart.setMaximumViewport(v);
        v.left = 0;
        v.right = 4;
        chart.setCurrentViewport(v);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tempchart_1:
                tv_tempchart_1.setBackgroundResource(R.mipmap.jc_tab1);
                tv_tempchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 1) {
                    flag = "day";
                    num = "1";
                    srl_tempchart.autoRefresh();
                    type = 1;
                }
                break;
            case R.id.tv_tempchart_2:
                tv_tempchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_2.setBackgroundResource(R.mipmap.jc_tab1);
                tv_tempchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 2) {
                    flag = "week";
                    num = "1";
                    srl_tempchart.autoRefresh();
                    type = 2;
                }
                break;
            case R.id.tv_tempchart_3:
                tv_tempchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_3.setBackgroundResource(R.mipmap.jc_tab1);
                tv_tempchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 3) {
                    flag = "month";
                    num = "1";
                    srl_tempchart.autoRefresh();
                    type = 3;
                }
                break;
            case R.id.tv_tempchart_4:
                tv_tempchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_tempchart_4.setBackgroundResource(R.mipmap.jc_tab1);
                if (type != 4) {
                    flag = "month";
                    num = "6";
                    srl_tempchart.autoRefresh();
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
        hashMap.put("deviceNum", deviceNum);
        MyAsyncTast myAsyncTast = new MyAsyncTast(TempChartActivity.this, hashMap, AppConfig.STATSTEMP, App.getInstance().getToken(), false, callback);
        myAsyncTast.execute();
    }

    public Float getMaxNumb(List<String> numbList) {
        float a = 0;
        for (int i = 0; i < numbList.size(); i++) {
            float numb = Float.parseFloat(numbList.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        return a;
    }

    public String getmMeanNumb(List<String> numbList) {
        Float a = Float.valueOf(0);
        for (int i = 0; i < numbList.size(); i++) {
            Float numb = Float.parseFloat(numbList.get(i));
            a = numb + a;
        }
        String b = new DecimalFormat("0.00").format(a / numbList.size());
        return b;
    }

    @Override
    protected void onDestroy() {
        treadoff = false;
        super.onDestroy();
    }
}
