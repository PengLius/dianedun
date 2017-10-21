package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.RealTimeConBean;
import cn.dianedun.tools.App;
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
 * Created by Administrator on 2017/10/20.
 */

public class GdCeCharActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.lcv_xgtension)
    LineChartView lcv_xgtension;

    @Bind(R.id.lcv_xntension)
    LineChartView lcv_xntension;

    @Bind(R.id.lcv_electricity)
    LineChartView lcv_electricity;

    @Bind(R.id.tv_gdchart_1)
    TextView tv_gdchart_1;

    @Bind(R.id.tv_gdchart_2)
    TextView tv_gdchart_2;

    @Bind(R.id.tv_gdchart_3)
    TextView tv_gdchart_3;

    @Bind(R.id.tv_gdchart_4)
    TextView tv_gdchart_4;

    @Bind(R.id.tv_xg_mean)
    TextView tv_xg_mean;

    @Bind(R.id.tv_xg_max)
    TextView tv_xg_max;

    @Bind(R.id.tv_xn_max)
    TextView tv_xn_max;

    @Bind(R.id.tv_xn_mean)
    TextView tv_xn_mean;

    @Bind(R.id.tv_il_max)
    TextView tv_il_max;

    @Bind(R.id.tv_il_mean)
    TextView tv_il_mean;

    @Bind(R.id.tv_tempchart_adress)
    TextView tv_tempchart_adress;

    private RealTimeConBean bean;
    private String RoomId;
    private String url;
    private String deviceNum;
    private int type = 1;
    private int maxXg, maxXn, maxIl, meanXg, meanXn, meanIl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdcechar);
        setTvTitleText("实时监测");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        tv_gdchart_1.setOnClickListener(this);
        tv_gdchart_2.setOnClickListener(this);
        tv_gdchart_3.setOnClickListener(this);
        tv_gdchart_4.setOnClickListener(this);
        RoomId = getIntent().getStringExtra("RoomId");
        url = getIntent().getStringExtra("url");
        deviceNum = getIntent().getStringExtra("deviceNum");
        tv_tempchart_adress.setText(getIntent().getStringExtra("depart"));
        getData("day", "1");

    }

    /**
     * 初始化图表
     */
    private void initLineChart(LineChartView chart, List<String> data, List<String> data1, List<String> data2, List<String> Ybz, int max) {
        List<Line> lines = new ArrayList<Line>();//折线的集合
        Line line = new Line(getAxisPoints(data)).setColor(Color.parseColor("#e84b06"));  //折线的颜色
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setStrokeWidth(1);
        lines.add(line);
        Line line1 = new Line(getAxisPoints(data1)).setColor(Color.parseColor("#1179ce"));  //折线的颜色
        line1.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line1.setFilled(false);//是否填充曲线的面积
        line1.setHasLabels(false);//曲线的数据坐标是否加上备注
        line1.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line1.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line1.setStrokeWidth(1);
        lines.add(line1);
        Line line2 = new Line(getAxisPoints(data2)).setColor(Color.parseColor("#3dc281"));  //折线的颜色
        line2.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line2.setFilled(false);//是否填充曲线的面积
        line2.setHasLabels(false);//曲线的数据坐标是否加上备注
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line2.setStrokeWidth(1);
        lines.add(line2);
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
            mPointValues.add(new PointValue(i, Integer.parseInt(data.get(i))));
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
            case R.id.tv_gdchart_1:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab1);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 1) {
                    getData("day", "1");
                    type = 1;
                }
                break;
            case R.id.tv_gdchart_2:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab1);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 2) {
                    getData("week", "1");
                    type = 2;
                }
                break;
            case R.id.tv_gdchart_3:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab1);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 3) {
                    getData("month", "1");
                    type = 3;
                }
                break;
            case R.id.tv_gdchart_4:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab1);
                if (type != 4) {
                    getData("custon", "1");
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
        MyAsyncTast myAsyncTast = new MyAsyncTast(GdCeCharActivity.this, hashMap, url, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, RealTimeConBean.class);
                maxXg = getMaxNumb(bean.getData().getVaList(), bean.getData().getVbList(), bean.getData().getVcList());
                maxXn = getMaxNumb(bean.getData().getVabList(), bean.getData().getVbcList(), bean.getData().getVcaList());
                maxIl = getMaxNumb(bean.getData().getIaList(), bean.getData().getIbList(), bean.getData().getIcList());
                meanXg = getmMeanNumb(bean.getData().getVaList(), bean.getData().getVbList(), bean.getData().getVcList());
                meanXn = getmMeanNumb(bean.getData().getVabList(), bean.getData().getVbcList(), bean.getData().getVcaList());
                meanIl = getmMeanNumb(bean.getData().getIaList(), bean.getData().getIbList(), bean.getData().getIcList());
                tv_xg_max.setText(maxXg + "V");
                tv_xg_mean.setText(meanXg + "V");
                tv_xn_max.setText(maxXn + "V");
                tv_xn_mean.setText(meanXn + "V");
                tv_il_max.setText(maxIl + "A");
                tv_il_mean.setText(meanIl + "A");
                List<String> xList = new ArrayList<>();
                for (int i = 0; i < bean.getData().getXList().size(); i++) {
                    xList.add(bean.getData().getXList().get(i).substring(5, bean.getData().getXList().get(i).length()));
                }
                initLineChart(lcv_xgtension, bean.getData().getVaList(), bean.getData().getVbList(), bean.getData().getVcList(), xList, maxXg);
                initLineChart(lcv_xntension, bean.getData().getVabList(), bean.getData().getVbcList(), bean.getData().getVcaList(), xList, maxXn);
                initLineChart(lcv_electricity, bean.getData().getIaList(), bean.getData().getIbList(), bean.getData().getIcList(), xList, maxIl);
            }

            @Override
            public void onError(String result) {

            }
        });
        myAsyncTast.execute();
    }

    public int getMaxNumb(List<String> numbList, List<String> numbList1, List<String> numbList2) {
        int a = 0;
        for (int i = 0; i < numbList.size(); i++) {
            int numb = Integer.parseInt(numbList.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        for (int i = 0; i < numbList1.size(); i++) {
            int numb = Integer.parseInt(numbList1.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        for (int i = 0; i < numbList2.size(); i++) {
            int numb = Integer.parseInt(numbList2.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        return a;
    }

    public int getmMeanNumb(List<String> numbList, List<String> numbList1, List<String> numbList2) {
        int a = 0;
        for (int i = 0; i < numbList.size(); i++) {
            int numb = Integer.parseInt(numbList.get(i));
            a = numb + a;
        }
        for (int i = 0; i < numbList1.size(); i++) {
            int numb = Integer.parseInt(numbList1.get(i));
            a = numb + a;
        }
        for (int i = 0; i < numbList2.size(); i++) {
            int numb = Integer.parseInt(numbList2.get(i));
            a = numb + a;
        }
        return a / (numbList.size() + numbList1.size() + numbList2.size());
    }
}
