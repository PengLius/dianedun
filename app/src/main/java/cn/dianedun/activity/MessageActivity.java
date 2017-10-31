package cn.dianedun.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.MessageBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MessageActivity extends BaseTitlActivity {

    private IndentCusAdapter adapter;
    MyAsyncTast myAsyncTast;

    @Bind(R.id.lv_message)
    ListView lv_message;

    @Bind(R.id.tv_message_bj)
    TextView tv_message_bj;

    @Bind(R.id.srl_message)
    SmartRefreshLayout srl_message;

    @Bind(R.id.cf_message)
    ClassicsFooter cf_message;

    private MessageBean bean;
    private List<MessageBean.DataBean.ResultBean> allList;
    private Dialog dialog;
    private int type = 0;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTvTitleText("我的消息");
        setTitleBack(R.mipmap.home_backg_has);
        setImgLeftVisibility(View.VISIBLE);
        setImgRightVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        setImgRight(R.mipmap.home_look);
        setImgRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    tv_message_bj.setVisibility(View.VISIBLE);
                    type = 1;
                } else {
                    tv_message_bj.setVisibility(View.GONE);
                    type = 0;
                }
            }
        });
        initRefreshLayout();
        srl_message.autoRefresh();
    }

    private void initRefreshLayout() {
        srl_message.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDate();
            }
        });
        srl_message.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("pageSize", "10");
                hashMap.put("startIndex", page + "");
                myAsyncTast = new MyAsyncTast(MessageActivity.this, hashMap, AppConfig.GETMESSAGELIST, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
                    @Override
                    public void onError(String result) {
                        srl_message.finishLoadmore();
                        showToast(result);
                    }

                    @Override
                    public void send(String result) {
                        srl_message.finishLoadmore();
                        bean = GsonUtil.parseJsonWithGson(result, MessageBean.class);
                        for (int i = 0; i < bean.getData().getResult().size(); i++) {
                            allList.add(bean.getData().getResult().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        if (bean.getData().getResult().size() < 10) {
                            srl_message.setLoadmoreFinished(true);
                        }
                        page++;
                    }
                });
                myAsyncTast.execute();
                tv_message_bj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAsyncTast = new MyAsyncTast(MessageActivity.this, new HashMap<String, String>(), AppConfig.MODIFYMESSAGESTATSALL, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                            @Override
                            public void onError(String result) {
                                showToast(result);
                                tv_message_bj.setVisibility(View.GONE);
                                type = 0;
                            }

                            @Override
                            public void send(String result) {
                                tv_message_bj.setVisibility(View.GONE);
                                type = 0;
                                getDate();
                            }
                        });
                        myAsyncTast.execute();
                    }
                });
            }
        });
    }

    public void getDate() {
        page = 1;
        allList = new ArrayList<>();
        srl_message.setLoadmoreFinished(false);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pageSize", "10");
        hashMap.put("startIndex", page + "");
        myAsyncTast = new MyAsyncTast(MessageActivity.this, hashMap, AppConfig.GETMESSAGELIST, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                srl_message.finishRefresh();
                showToast(result);
            }

            @Override
            public void send(String result) {
                srl_message.finishRefresh();
                page++;
                bean = GsonUtil.parseJsonWithGson(result, MessageBean.class);
                for (int i = 0; i < bean.getData().getResult().size(); i++) {
                    allList.add(bean.getData().getResult().get(i));
                }
                if (bean.getData().getResult().size() < 10) {
                    srl_message.setLoadmoreFinished(true);
                }
                adapter = new IndentCusAdapter();
                lv_message.setAdapter(adapter);
            }
        });
        myAsyncTast.execute();
        tv_message_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAsyncTast = new MyAsyncTast(MessageActivity.this, new HashMap<String, String>(), AppConfig.MODIFYMESSAGESTATSALL, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                    @Override
                    public void onError(String result) {
                        showToast(result);
                        tv_message_bj.setVisibility(View.GONE);
                        type = 0;

                    }

                    @Override
                    public void send(String result) {
                        tv_message_bj.setVisibility(View.GONE);
                        type = 0;
                        getDate();
                    }
                });
                myAsyncTast.execute();
            }
        });
    }

    private class IndentCusAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return allList.size();
        }


        @Override
        public Object getItem(int position) {
            return allList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Cache cache;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_message, null);
                cache = new Cache();
                cache.img_itmemessage = (ImageView) convertView.findViewById(R.id.img_itmemessage);
                cache.ll_itemmassge = (LinearLayout) convertView.findViewById(R.id.ll_itemmassge);
                cache.tv_message_con = (TextView) convertView.findViewById(R.id.tv_message_con);
                cache.tv_message_time = (TextView) convertView.findViewById(R.id.tv_message_time);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.tv_message_con.setText(allList.get(position).getContents());
            cache.tv_message_time.setText(allList.get(position).getCreateTime());
            if (allList.get(position).getStatus() == 0) {
                cache.img_itmemessage.setImageResource(R.mipmap.msg);
                cache.ll_itemmassge.setBackgroundColor(Color.parseColor("#60D9D9D9"));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = createLoadingDialog(MessageActivity.this, allList.get(position).getContents(), allList.get(position).getCreateTime());
                        dialog.show();
                    }
                });
            } else if (allList.get(position).getStatus() == 1) {
                cache.img_itmemessage.setImageResource(R.mipmap.msg1);
                cache.ll_itemmassge.setBackgroundColor(Color.parseColor("#6019246B"));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("msgId", allList.get(position).getId() + "");
                        myAsyncTast = new MyAsyncTast(MessageActivity.this, hashMap, AppConfig.MODIFYMESSAGESTATS, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                            @Override
                            public void send(String result) {
                                cache.img_itmemessage.setImageResource(R.mipmap.msg);
                                cache.ll_itemmassge.setBackgroundColor(Color.parseColor("#60D9D9D9"));
                                dialog = createLoadingDialog(MessageActivity.this, allList.get(position).getContents(), allList.get(position).getCreateTime());
                                dialog.show();
                            }

                            @Override
                            public void onError(String result) {
                                showToast(result);
                            }
                        });
                        myAsyncTast.execute();

                    }
                });
            }
            return convertView;
        }
    }

    class Cache {
        TextView tv_message_con, tv_message_time;
        ImageView img_itmemessage;
        LinearLayout ll_itemmassge;
    }

    private Dialog createLoadingDialog(Context context, String con, String time) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_message, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tv_dialog_con = (TextView) v.findViewById(R.id.tv_dialog_con);
        TextView tv_dialog_time = (TextView) v.findViewById(R.id.tv_dialog_time);
        TextView tv_dialog_close = (TextView) v.findViewById(R.id.tv_dialog_close);
        tv_dialog_con.setText(con);
        tv_dialog_time.setText(time);
        tv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialogs);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout);// 设置布局
        return loadingDialog;
    }

}
