package cn.dianedun.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.HumidityCharBean;
import cn.dianedun.bean.WaterChartBean;
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

public class WaterOutActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.lcv_sj)
    LineChartView lcv_sj;

    @Bind(R.id.tv_waterout_adress)
    TextView tv_waterout_adress;

    @Bind(R.id.tv_waterout_1)
    TextView tv_waterout_1;

    @Bind(R.id.tv_waterout_2)
    TextView tv_waterout_2;

    @Bind(R.id.tv_waterout_3)
    TextView tv_waterout_3;

    @Bind(R.id.tv_waterout_4)
    TextView tv_waterout_4;

    @Bind(R.id.srl_waterout)
    SmartRefreshLayout srl_waterout;

    private int type = 1;
    private String RoomId;
    private WaterChartBean bean;
    List<AxisValue> values;
    private MyAsyncTast.Callback callback = new MyAsyncTast.Callback() {
        @Override
        public void send(String result) {
            bean = GsonUtil.parseJsonWithGson(result, WaterChartBean.class);
            List<String> xList = new ArrayList<>();
            for (int i = 0; i < bean.getData().getXList().size(); i++) {
                xList.add(bean.getData().getXList().get(i).substring(5, bean.getData().getXList().get(i).length()));
            }
            initLineChart(lcv_sj, bean.getData().getWaterList(), xList);
//            new Thread(sendable).start();
            srl_waterout.finishRefresh();
        }

        @Override
        public void onError(String result) {
            srl_waterout.finishRefresh();
        }
    };
    private Boolean treadoff = true;
    private String flag = "day";
    private String num = "1";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0) {
                getData(flag, num);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wateroutchart);
        setTvTitleText("实时监测");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        tv_waterout_1.setOnClickListener(this);
        tv_waterout_2.setOnClickListener(this);
        tv_waterout_3.setOnClickListener(this);
        tv_waterout_4.setOnClickListener(this);
        tv_waterout_adress.setText(getIntent().getStringExtra("depart"));
        RoomId = getIntent().getStringExtra("RoomId");
        initRefreshLayout();
        srl_waterout.autoRefresh();
    }

    private void initRefreshLayout() {
        srl_waterout.setLoadmoreFinished(true);
        srl_waterout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(flag, num);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_waterout_1:
                tv_waterout_1.setBackgroundResource(R.mipmap.jc_tab1);
                tv_waterout_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 1) {
                    flag = "day";
                    num = "1";
                    srl_waterout.autoRefresh();
                    type = 1;
                }
                break;
            case R.id.tv_waterout_2:
                tv_waterout_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_2.setBackgroundResource(R.mipmap.jc_tab1);
                tv_waterout_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 2) {
                    flag = "week";
                    num = "1";
                    srl_waterout.autoRefresh();
                    type = 2;
                }
                break;
            case R.id.tv_waterout_3:
                tv_waterout_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_3.setBackgroundResource(R.mipmap.jc_tab1);
                tv_waterout_4.setBackgroundResource(R.mipmap.jc_tab);
                if (type != 3) {
                    flag = "month";
                    num = "1";
                    srl_waterout.autoRefresh();
                    type = 3;
                }
                break;
            case R.id.tv_waterout_4:
                tv_waterout_1.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_2.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_3.setBackgroundResource(R.mipmap.jc_tab);
                tv_waterout_4.setBackgroundResource(R.mipmap.jc_tab1);
                if (type != 4) {
                    flag = "month";
                    num = "6";
                    srl_waterout.autoRefresh();
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
        MyAsyncTast myAsyncTast = new MyAsyncTast(WaterOutActivity.this, hashMap, AppConfig.STATSWATER, App.getInstance().getToken(), false, callback);
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
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
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

        axisY.setHasLines(true); //y轴分割线

        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.WHITE);

        AxisValue value = new AxisValue(0);
        String label = "0";
        value.setLabel(label);
        values.add(value);

        value = new AxisValue(1);
        label = "1";
        value.setLabel(label);
        values.add(value);

        axisY.setValues(values).setLineColor(Color.parseColor("#323944"));

        datas.setAxisYLeft(axisY);  //Y轴设置在左边
        chart.setInteractive(true);
        chart.setScrollEnabled(true);
        chart.setZoomEnabled(false);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setLineChartData(datas);
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.top = 1.3f;
        v.bottom = 0;
        chart.setMaximumViewport(v);
        v.left = 0;
        v.right = 4;
        chart.setCurrentViewport(v);
    }

    /**
     * 定时刷新
     */
//    Runnable sendable = new Runnable() {
//        @Override
//        public void run() {
//            int a = 10;
//            while (-1 < a && treadoff) {
//                try {
//                    Thread.sleep(1000);
//                    Message message = new Message();
//                    message.arg1 = a;
//                    handler.sendMessage(message);
//                    a--;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    };
    @Override
    protected void onDestroy() {
        treadoff = false;
        super.onDestroy();
    }
}
