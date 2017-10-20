package cn.dianedun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.ApplyGdActivity;
import cn.dianedun.activity.DetailsingActivity;
import cn.dianedun.activity.DisposeJbActivity;
import cn.dianedun.activity.ExamineActivity;
import cn.dianedun.activity.HisJbActivity;
import cn.dianedun.activity.HisWorkOrderActivity;
import cn.dianedun.activity.RetroactionActivity;
import cn.dianedun.base.BaseTitlFragment;
import cn.dianedun.bean.HomeListBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.DataUtil;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import cn.dianedun.view.HeaderView;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/3.
 */

public class HomeFragment extends BaseTitlFragment {

    @Bind(R.id.lv_home)
    ListView lv_home;

    @Bind(R.id.rl_home)
    RelativeLayout rl_home;

    @Bind(R.id.tv_home_hisjb)
    TextView tv_home_hisjb;

    @Bind(R.id.tv_home_hisgd)
    TextView tv_home_hisgd;

    @Bind(R.id.ll_home_null)
    LinearLayout ll_home_null;

    @Bind(R.id.srl_home)
    SmartRefreshLayout srl_home;


    private IndentCusAdapter adapter;
    private Intent intent;
    private MyAsyncTast myAsyncTast;
    private boolean rightState = false;
    private HomeListBean bean;
    private List<HashMap<String, String>> itemList;
    private HashMap<String, String> itemHash;
    private int jg;
    private int jbNum = 0;
    private int gdNum = 0;


    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setTvTitleText("首页");
        setTitleBack(R.mipmap.home_backg_has);
        setImgLeftVisibility(View.VISIBLE);
        setImgRightVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.home_green_add);
        setImgRight(R.mipmap.home_look);
        initRefreshLayout();
        setImgLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().getIsAdmin().equals("1")) {
                    showToast("您没有权限");
                } else {
                    intent = new Intent(getActivity(), ApplyGdActivity.class);
                    startActivity(intent);
                }

            }
        });
        setImgRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightState) {
                    rl_home.setVisibility(View.GONE);
                    rightState = false;
                } else {
                    rl_home.setVisibility(View.VISIBLE);
                    rightState = true;
                }
            }
        });
        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_home.setVisibility(View.GONE);
                rightState = false;
            }
        });
        tv_home_hisjb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().getIsAdmin().equals("1")) {
                    showToast("您没有权限");
                } else {
                    intent = new Intent(getActivity(), HisJbActivity.class);
                    startActivity(intent);
                    rl_home.setVisibility(View.GONE);
                }
            }
        });

        tv_home_hisgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), HisWorkOrderActivity.class);
                startActivity(intent);
                rl_home.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        srl_home.autoRefresh();
    }

    private void initRefreshLayout() {
        srl_home.setLoadmoreFinished(true);
        srl_home.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
            }
        });
    }


    private void getData() {
        myAsyncTast = new MyAsyncTast(getActivity(), new HashMap<String, String>(), AppConfig.SHOWINDEX, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
                ll_home_null.setVisibility(View.VISIBLE);
                srl_home.finishRefresh();
            }

            @Override
            public void send(String result) {
                ll_home_null.setVisibility(View.GONE);
                srl_home.finishRefresh();
                bean = GsonUtil.parseJsonWithGson(result, HomeListBean.class);
                itemList = new ArrayList<>();
                if (bean.getData().getAlarmList() != null) {
                    for (int i = 0; i < bean.getData().getAlarmList().size(); i++) {
                        itemHash = new HashMap<>();
                        itemHash.put("id", bean.getData().getAlarmList().get(i).getId() + "");
                        itemHash.put("createTime", bean.getData().getAlarmList().get(i).getCreateTime() + "");
                        itemHash.put("status", bean.getData().getAlarmList().get(i).getStatus() + "");
                        itemHash.put("delayDay", bean.getData().getAlarmList().get(i).getDelayDay() + "");
                        itemHash.put("type", bean.getData().getAlarmList().get(i).getType() + "");
                        itemHash.put("alert_details", bean.getData().getAlarmList().get(i).getAlert_details() + "");
                        itemHash.put("depart_name", bean.getData().getAlarmList().get(i).getDepart_name() + "");
                        itemHash.put("itemType", "0");
                        itemList.add(itemHash);
                    }
                }
                jg = itemList.size();
                if (bean.getData().getOrderList() != null) {
                    for (int i = 0; i < bean.getData().getOrderList().size(); i++) {
                        itemHash = new HashMap<>();
                        itemHash.put("id", bean.getData().getOrderList().get(i).getId() + "");
                        itemHash.put("orderNum", bean.getData().getOrderList().get(i).getOrderNum() + "");
                        itemHash.put("urgency", bean.getData().getOrderList().get(i).getUrgency() + "");
                        itemHash.put("applyTime", bean.getData().getOrderList().get(i).getApplyTime() + "");
                        itemHash.put("address", bean.getData().getOrderList().get(i).getAddress() + "");
                        itemHash.put("beginTime", bean.getData().getOrderList().get(i).getBeginTime() + "");
                        itemHash.put("endTime", bean.getData().getOrderList().get(i).getEndTime() + "");
                        itemHash.put("delayTime", bean.getData().getOrderList().get(i).getDelayTime() + "");
                        itemHash.put("status", bean.getData().getOrderList().get(i).getStatus() + "");
                        itemHash.put("applyStatus", bean.getData().getOrderList().get(i).getApplyStatus() + "");
                        itemHash.put("delayTime", bean.getData().getOrderList().get(i).getDelayTime() + "");
                        itemHash.put("departName", bean.getData().getOrderList().get(i).getDepartName() + "");
                        itemHash.put("itemType", "1");
                        itemList.add(itemHash);
                    }
                }
                if (itemList.size() > 0) {
                    ll_home_null.setVisibility(View.GONE);
                    lv_home.setVisibility(View.VISIBLE);
                    jbNum = 0;
                    gdNum = 0;
                    for (int i = 0; i < itemList.size(); i++) {
                        if (itemList.get(i).get("itemType").equals("0")) {
                            jbNum++;
                        }
                        if (itemList.get(i).get("itemType").equals("1")) {
                            gdNum++;
                        }
                    }
                    adapter = new IndentCusAdapter();
                    lv_home.setAdapter(adapter);
                } else {
                    ll_home_null.setVisibility(View.VISIBLE);
                    lv_home.setVisibility(View.GONE);
                }
            }
        });
        myAsyncTast.execute();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }


    private class IndentCusAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (itemList.get(position).get("itemType").equals("0")) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_jb, null);
                if (position == 0) {
                    convertView.findViewById(R.id.item_tv_dcljb).setVisibility(View.VISIBLE);
                    ((TextView) convertView.findViewById(R.id.item_tv_dcljb)).setText("待处理警报(" + jbNum + ")");
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getActivity(), DisposeJbActivity.class);
                        intent.putExtra("id", itemList.get(position).get("id"));
                        startActivity(intent);
                    }
                });
                ((TextView) (convertView.findViewById(R.id.tv_itemjb_adress))).setText(itemList.get(position).get("depart_name"));
                ((TextView) (convertView.findViewById(R.id.tv_itemjb_creattime))).setText(itemList.get(position).get("createTime"));
                ((TextView) (convertView.findViewById(R.id.tv_itemjb_con))).setText(itemList.get(position).get("alert_details"));
                if (itemList.get(position).get("type").equals("1")) {
                    ((ImageView) convertView.findViewById(R.id.item_homeimg_jbsta)).setImageResource(R.mipmap.home_jg_yellow);
                } else {
                    ((ImageView) convertView.findViewById(R.id.item_homeimg_jbsta)).setImageResource(R.mipmap.home_jg_red);
                }


            } else {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_gd, null);
                if (position == jg) {
                    convertView.findViewById(R.id.item_tv_dclgd).setVisibility(View.VISIBLE);
                    ((TextView) convertView.findViewById(R.id.item_tv_dclgd)).setText("待处理工单(" + gdNum + ")");
                }
                ((TextView) convertView.findViewById(R.id.item_hometv_code)).setText(itemList.get(position).get("orderNum"));
                String beginTime = Long.parseLong(itemList.get(position).get("beginTime")) / 1000 + "";
                String endTime = Long.parseLong(itemList.get(position).get("endTime")) / 1000 + "";
                ((TextView) convertView.findViewById(R.id.item_hometv_starti)).setText(DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm:ss"));
                ((TextView) convertView.findViewById(R.id.item_hometv_endti)).setText(DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm:ss"));
                ((TextView) convertView.findViewById(R.id.item_hometv_adres)).setText(itemList.get(position).get("departName"));
                if (itemList.get(position).get("urgency").equals("0")) {
                    ((ImageView) convertView.findViewById(R.id.item_hoemimg_sta)).setImageResource(R.mipmap.home_jg_yellow);
                } else {
                    ((ImageView) convertView.findViewById(R.id.item_hoemimg_sta)).setImageResource(R.mipmap.home_jg_red);
                }
                int a = Integer.parseInt(itemList.get(position).get("delayTime"));

                if (a > 0) {
                    ((TextView) convertView.findViewById(R.id.item_hoemtv_yw)).setVisibility(View.VISIBLE);
                    ((TextView) convertView.findViewById(R.id.item_hoemtv_yw)).setText("已延误    " + itemList.get(position).get("delayTime") + "天");
                } else {
                    ((TextView) convertView.findViewById(R.id.item_hoemtv_yw)).setVisibility(GONE);
                }

                switch (itemList.get(position).get("status")) {
                    case "0":
                        ((TextView) convertView.findViewById(R.id.item_hometv_sta)).setText("已删除");
                        break;
                    case "1":
                        ((TextView) convertView.findViewById(R.id.item_hometv_sta)).setText("待审批");
                        break;
                    case "2":
                        ((TextView) convertView.findViewById(R.id.item_hometv_sta)).setText("待反馈");
                        break;
                    case "3":
                        ((TextView) convertView.findViewById(R.id.item_hometv_sta)).setText("被驳回");
                        break;
                    default:
                        break;
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (itemList.get(position).get("status")) {
                            case "0":

                                break;
                            case "1":
                                if (App.getInstance().getIsAdmin().equals("1")) {
                                    intent = new Intent(getActivity(), ExamineActivity.class);
                                    intent.putExtra("orderNum", itemList.get(position).get("orderNum"));
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getActivity(), DetailsingActivity.class);
                                    intent.putExtra("orderNum", itemList.get(position).get("orderNum"));
                                    startActivity(intent);
                                }
                                break;
                            case "2":
                                if (!(App.getInstance().getIsAdmin().equals("1"))) {
                                    intent = new Intent(getActivity(), RetroactionActivity.class);
                                    intent.putExtra("orderNum", itemList.get(position).get("orderNum"));
                                    startActivity(intent);
                                }
                                break;
                            case "3":
                                if (!(App.getInstance().getIsAdmin().equals("1"))) {
                                    intent = new Intent(getActivity(), DetailsingActivity.class);
                                    intent.putExtra("orderNum", itemList.get(position).get("orderNum"));
                                    startActivity(intent);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });

            }
            return convertView;
        }
    }
}
