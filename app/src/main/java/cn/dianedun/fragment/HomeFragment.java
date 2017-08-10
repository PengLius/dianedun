package cn.dianedun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.ApplyGdActivity;
import cn.dianedun.activity.DetailsingActivity;
import cn.dianedun.activity.DisposeJbActivity;
import cn.dianedun.activity.HisDetailsActivity;
import cn.dianedun.activity.HisDisposeJbActivity;
import cn.dianedun.activity.HisJbActivity;
import cn.dianedun.activity.HisWorkOrderActivity;
import cn.dianedun.base.BaseFragment;
import cn.dianedun.base.BaseTitlFragment;

/**
 * Created by Administrator on 2017/8/3.
 */

public class HomeFragment extends BaseTitlFragment {
    private IndentCusAdapter adapter;
    private List<Integer> alllist;
    private Intent intent;


    @Bind(R.id.lv_home)
    ListView lv_home;

    @Bind(R.id.rl_home)
    RelativeLayout rl_home;

    @Bind(R.id.tv_home_hisjb)
    TextView tv_home_hisjb;

    @Bind(R.id.tv_home_hisgd)
    TextView tv_home_hisgd;

    private boolean rightState = false;


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
        setImgLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ApplyGdActivity.class);
                startActivity(intent);
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
                intent = new Intent(getActivity(), HisJbActivity.class);
                startActivity(intent);
                rl_home.setVisibility(View.GONE);
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


        alllist = new ArrayList();
        alllist.add(1);
        alllist.add(2);
        alllist.add(3);
        alllist.add(4);
        alllist.add(5);
        alllist.add(6);
        alllist.add(7);
        alllist.add(8);
        alllist.add(9);
        alllist.add(10);
        adapter = new IndentCusAdapter();
        lv_home.setAdapter(adapter);
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
            return alllist.size();
        }

        @Override
        public Object getItem(int position) {
            return alllist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (alllist.get(position) == 1) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_jb, null);
            } else {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_gd, null);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast(alllist.get(position) + "");
                }
            });
            if (alllist.get(position) == 1) {
                convertView.findViewById(R.id.item_tv_dcljb).setVisibility(View.VISIBLE);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getActivity(), DisposeJbActivity.class);
                        startActivity(intent);
                    }
                });
            }
            if (alllist.get(position) != 1) {
                ((TextView) convertView.findViewById(R.id.item_hometv_code)).setText(alllist.get(position) + "");
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getActivity(), DetailsingActivity.class);
                        startActivity(intent);
                    }
                });
            }
            return convertView;
        }
    }

    class Cache {
        TextView tv_grouponall_name, tv_grouponall_from, tv_grouponall_offered;
        ImageView img_grouponall_head;
    }
}