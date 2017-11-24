package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
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

public class GdCeCharActivity extends BaseTitlActivity implements View.OnClickListener, View.OnTouchListener {

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

    @Bind(R.id.tv_tempchart_adress)
    TextView tv_tempchart_adress;

    @Bind(R.id.tv_gdchar_xg)
    TextView tv_gdchar_xg;

    @Bind(R.id.tv_gdchar_xn)
    TextView tv_gdchar_xn;

    @Bind(R.id.srl_gdce)
    SmartRefreshLayout srl_gdce;

    @Bind(R.id.tv_gdxg_amax)
    TextView tv_gdxg_amax;

    @Bind(R.id.tv_gdxg_amean)
    TextView tv_gdxg_amean;

    @Bind(R.id.tv_gdxg_bmax)
    TextView tv_gdxg_bmax;

    @Bind(R.id.tv_gdxg_bmean)
    TextView tv_gdxg_bmean;

    @Bind(R.id.tv_gdxg_cmax)
    TextView tv_gdxg_cmax;

    @Bind(R.id.tv_gdxg_cmean)
    TextView tv_gdxg_cmean;

    @Bind(R.id.tv_gdxn_abmax)
    TextView tv_gdxn_abmax;

    @Bind(R.id.tv_gdxn_abmean)
    TextView tv_gdxn_abmean;

    @Bind(R.id.tv_gdxn_bcmax)
    TextView tv_gdxn_bcmax;

    @Bind(R.id.tv_gdxn_bcmean)
    TextView tv_gdxn_bcmean;

    @Bind(R.id.tv_gdxn_acmax)
    TextView tv_gdxn_acmax;

    @Bind(R.id.tv_gdxn_acmean)
    TextView tv_gdxn_acmean;

    @Bind(R.id.tv_gdil_amax)
    TextView tv_gdil_amax;

    @Bind(R.id.tv_gdil_amean)
    TextView tv_gdil_amean;

    @Bind(R.id.tv_gdil_bmax)
    TextView tv_gdil_bmax;

    @Bind(R.id.tv_gdil_bmean)
    TextView tv_gdil_bmean;

    @Bind(R.id.tv_gdil_cmax)
    TextView tv_gdil_cmax;

    @Bind(R.id.tv_gdil_cmean)
    TextView tv_gdil_cmean;

    @Bind(R.id.tv_tempchart_name)
    TextView tv_tempchart_name;

    @Bind(R.id.sv_gdce)
    ScrollView sv_gdce;

    private RealTimeConBean bean;
    private String RoomId;
    private String url;
    private String deviceNum;
    private int type = 1;
    private Float maxXg, maxXn, maxIl;
    private MyAsyncTast.Callback callback = new MyAsyncTast.Callback() {
        @Override
        public void send(String result) {
            bean = GsonUtil.parseJsonWithGson(result, RealTimeConBean.class);
            maxXg = getMaxNumb(bean.getData().getVaList(), bean.getData().getVbList(), bean.getData().getVcList());
            maxXn = getMaxNumb(bean.getData().getVabList(), bean.getData().getVbcList(), bean.getData().getVcaList());
            maxIl = getMaxNumb(bean.getData().getIaList(), bean.getData().getIbList(), bean.getData().getIcList());
            if (getIntent().getStringExtra("types").equals("0")) {
                tv_gdxg_amax.setText(getmMax1Numb(bean.getData().getVaList()) + "KV");
                tv_gdxg_bmax.setText(getmMax1Numb(bean.getData().getVbList()) + "KV");
                tv_gdxg_cmax.setText(getmMax1Numb(bean.getData().getVcList()) + "KV");
                tv_gdxn_abmax.setText(getmMax1Numb(bean.getData().getVabList()) + "KV");
                tv_gdxn_bcmax.setText(getmMax1Numb(bean.getData().getVbcList()) + "KV");
                tv_gdxn_acmax.setText(getmMax1Numb(bean.getData().getVcaList()) + "KV");

                tv_gdxg_amean.setText(getmMeanNumb(bean.getData().getVaList()) + "KV");
                tv_gdxg_bmean.setText(getmMeanNumb(bean.getData().getVbList()) + "KV");
                tv_gdxg_cmean.setText(getmMeanNumb(bean.getData().getVcList()) + "KV");
                tv_gdxn_abmean.setText(getmMeanNumb(bean.getData().getVabList()) + "KV");
                tv_gdxn_bcmean.setText(getmMeanNumb(bean.getData().getVbcList()) + "KV");
                tv_gdxn_acmean.setText(getmMeanNumb(bean.getData().getVcaList()) + "KV");
            } else {
                tv_gdxg_amax.setText(getmMax1Numb(bean.getData().getVaList()) + "V");
                tv_gdxg_bmax.setText(getmMax1Numb(bean.getData().getVbList()) + "V");
                tv_gdxg_cmax.setText(getmMax1Numb(bean.getData().getVcList()) + "V");
                tv_gdxn_abmax.setText(getmMax1Numb(bean.getData().getVabList()) + "V");
                tv_gdxn_bcmax.setText(getmMax1Numb(bean.getData().getVbcList()) + "V");
                tv_gdxn_acmax.setText(getmMax1Numb(bean.getData().getVcaList()) + "V");

                tv_gdxg_amean.setText(getmMeanNumb(bean.getData().getVaList()) + "V");
                tv_gdxg_bmean.setText(getmMeanNumb(bean.getData().getVbList()) + "V");
                tv_gdxg_cmean.setText(getmMeanNumb(bean.getData().getVcList()) + "V");
                tv_gdxn_abmean.setText(getmMeanNumb(bean.getData().getVabList()) + "V");
                tv_gdxn_bcmean.setText(getmMeanNumb(bean.getData().getVbcList()) + "V");
                tv_gdxn_acmean.setText(getmMeanNumb(bean.getData().getVcaList()) + "V");

            }

            tv_gdil_amax.setText(getmMax1Numb(bean.getData().getIaList()) + "A");
            tv_gdil_bmax.setText(getmMax1Numb(bean.getData().getIbList()) + "A");
            tv_gdil_cmax.setText(getmMax1Numb(bean.getData().getIcList()) + "A");

            tv_gdil_amean.setText(getmMeanNumb(bean.getData().getIaList()) + "A");
            tv_gdil_bmean.setText(getmMeanNumb(bean.getData().getIbList()) + "A");
            tv_gdil_cmean.setText(getmMeanNumb(bean.getData().getIcList()) + "A");

            List<String> xList = new ArrayList<>();
            for (int i = 0; i < bean.getData().getXList().size(); i++) {
                xList.add(bean.getData().getXList().get(i).substring(5, bean.getData().getXList().get(i).length()));
            }
            initLineChart(lcv_xgtension, bean.getData().getVaList(), bean.getData().getVbList(), bean.getData().getVcList(), xList, maxXg);
            initLineChart(lcv_xntension, bean.getData().getVabList(), bean.getData().getVbcList(), bean.getData().getVcaList(), xList, maxXn);
            initLineChart(lcv_electricity, bean.getData().getIaList(), bean.getData().getIbList(), bean.getData().getIcList(), xList, maxIl);
            srl_gdce.finishRefresh();
        }

        @Override
        public void onError(String result) {
            srl_gdce.finishRefresh();
            showToast(result);
        }
    };
    private Boolean treadoff = true;
    private String flag = "day";
    private String num = "1";


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
        if (getIntent().getStringExtra("types").equals("0")) {
            tv_gdchar_xg.setText("相电压（KV）");
            tv_gdchar_xn.setText("线电压（KV）");
            tv_tempchart_name.setText("高压侧" + deviceNum);
        } else {
            tv_gdchar_xg.setText("相电压（V）");
            tv_gdchar_xn.setText("线电压（V）");
            tv_tempchart_name.setText("低压侧" + deviceNum);
        }
        initRefreshLayout();
        srl_gdce.autoRefresh();
        lcv_xgtension.setOnTouchListener(this);
        lcv_xntension.setOnTouchListener(this);
        lcv_electricity.setOnTouchListener(this);
    }

    private void initRefreshLayout() {
        srl_gdce.setLoadmoreFinished(true);
        srl_gdce.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(flag, num);
            }
        });
    }


    /**
     * 初始化图表
     */
    private void initLineChart(LineChartView chart, List<String> data, List<String> data1, List<String> data2, List<String> Ybz, Float max) {
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

        axisY.setHasLines(true).setLineColor(Color.parseColor("#323944")); //y轴分割线

        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.WHITE);
        datas.setAxisYLeft(axisY);  //Y轴设置在左边
        chart.setInteractive(true);
        chart.setScrollEnabled(true);
        chart.setZoomEnabled(false);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setLineChartData(datas);
        Viewport v = new Viewport(chart.getMaximumViewport());

        if (max < 5.0f) {
            v.top = max + 5;
        } else if (max < 50) {
            v.top = max + 10;
        } else if (max < 100) {
            v.top = max + 20;
        } else if (max < 150) {
            v.top = max + 30;
        } else {
            v.top = max / 5 + max;
        }
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
            case R.id.tv_gdchart_1:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab1);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 1) {
                    flag = "day";
                    num = "1";
                    srl_gdce.autoRefresh();
                    type = 1;
                }
                break;
            case R.id.tv_gdchart_2:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab1);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 2) {
                    flag = "week";
                    num = "1";
                    srl_gdce.autoRefresh();
                    type = 2;
                }
                break;
            case R.id.tv_gdchart_3:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab1);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 3) {
                    flag = "month";
                    num = "1";
                    srl_gdce.autoRefresh();
                    type = 3;
                }
                break;
            case R.id.tv_gdchart_4:
                tv_gdchart_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_gdchart_4.setBackgroundResource(R.mipmap.jc_tab1);
                if (type != 4) {
                    flag = "month";
                    num = "6";
                    srl_gdce.autoRefresh();
                    type = 4;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据
     *
     * @param flag
     * @param num
     */
    public void getData(String flag, String num) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("RoomId", RoomId);
        hashMap.put("flag", flag);
        hashMap.put("num", num);
        hashMap.put("deviceNum", deviceNum);
        MyAsyncTast myAsyncTast = new MyAsyncTast(GdCeCharActivity.this, hashMap, url, App.getInstance().getToken(), false, callback);
        myAsyncTast.execute();
    }

    public Float getMaxNumb(List<String> numbList, List<String> numbList1, List<String> numbList2) {
        Float a = Float.valueOf(0);
        for (int i = 0; i < numbList.size(); i++) {
            Float numb = Float.parseFloat(numbList.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        for (int i = 0; i < numbList1.size(); i++) {
            Float numb = Float.parseFloat(numbList1.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        for (int i = 0; i < numbList2.size(); i++) {
            Float numb = Float.parseFloat(numbList2.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        return a;
    }

    public String getmMeanNumb(List<String> numbList) {
        if (numbList.size() > 0) {
            Float a = Float.valueOf(0);
            for (int i = 0; i < numbList.size(); i++) {
                Float numb = Float.parseFloat(numbList.get(i));
                a = numb + a;
            }
            String b = new DecimalFormat("0.00").format(a / numbList.size());
            return b;
        } else {
            return "0";
        }
    }

    public String getmMax1Numb(List<String> numbList) {
        Float a = Float.valueOf(0);
        for (int i = 0; i < numbList.size(); i++) {
            Float numb = Float.parseFloat(numbList.get(i));
            if (a < numb) {
                a = numb;
            }
        }
        String b = new DecimalFormat("0.00").format(a);
        return b;
    }

    @Override
    protected void onDestroy() {
        treadoff = false;
        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float mLastX = 0;
        if (action == MotionEvent.ACTION_DOWN) {
            // 记录点击到ViewPager时候，手指的X坐标
            mLastX = event.getX();
        }
        if (action == MotionEvent.ACTION_MOVE) {
            // 超过阈值
            if (Math.abs(event.getX() - mLastX) > 600f) {
                sv_gdce.requestDisallowInterceptTouchEvent(true);
            }
        }
        if (action == MotionEvent.ACTION_UP) {
            // 用户抬起手指，恢复父布局状态
            sv_gdce.requestDisallowInterceptTouchEvent(false);
        }
        return false;
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
