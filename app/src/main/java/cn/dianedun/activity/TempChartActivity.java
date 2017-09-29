package cn.dianedun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
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

public class TempChartActivity extends BaseTitlActivity {


    @Bind(R.id.tv_tempchart_1)
    TextView tv_tempchart_1;

    @Bind(R.id.cash_chart)
    LineChartView lineChart;


    String[] date = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//X轴的标注
    int[] weather = {12, 22, 1, 20, 20, 21, 21, 12, 21, 22};
    List<int[]> dataList;
    List<Integer> colorList;

    private List<PointValue> mPointValues = new ArrayList<PointValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempchart);
        setTvTitleText("实时监测");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        tv_tempchart_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getAxisPoints();
        initLineChart();

    }


    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < weather.length; i++) {
            mPointValues.add(new PointValue(i, weather[i]));
        }
    }

    /**
     * 初始化图表
     */
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.BLUE);  //折线的颜色
        List<Line> lines = new ArrayList<Line>();//折线的集合
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setStrokeWidth(1);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);//加入图表中

        Axis axisX = initX();
        data.setAxisXBottom(axisX); //x 轴在底部

        Axis axisY = initY();
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        lineChart.setInteractive(false);
        lineChart.setLineChartData(data);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.top =1000;
        v.bottom = 0;
        lineChart.setMaximumViewport(v);
        v.left = 0;
        v.right = 10;
        lineChart.setCurrentViewport(v);

    }

    /**
     * x轴
     */
    private Axis initX() {
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setMaxLabelChars(1); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(null);
        axisX.setHasLines(false); //x 轴分割线
        return axisX;
    }

    /**
     * Y轴
     */
    private Axis initY() {
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(12);//设置字体大小
        axisY.setTextColor(Color.WHITE);
        return axisY;
    }


}
