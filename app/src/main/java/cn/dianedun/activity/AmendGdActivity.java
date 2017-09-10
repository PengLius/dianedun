package cn.dianedun.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.ApplyAdreesBean;
import cn.dianedun.bean.ApplyPresonBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import cn.dianedun.view.DateTimeDialog;
import cn.dianedun.view.DateTimeDialogOnlyTime;
import cn.dianedun.view.DateTimeDialogOnlyYMD;

/**
 * Created by Administrator on 2017/8/8.
 */

public class AmendGdActivity extends BaseTitlActivity implements View.OnClickListener,
        DateTimeDialogOnlyYMD.MyOnDateSetListener, DateTimeDialogOnlyTime.MyOnDateSetListener, DateTimeDialog.MyOnDateSetListener {
    @Bind(R.id.tv_amendgd_startime)
    TextView tv_amendgd_startime;

    @Bind(R.id.tv_amendgd_endtime)
    TextView tv_amendgd_endtime;

    @Bind(R.id.tv_amendgd_pt)
    TextView tv_amendgd_pt;

    @Bind(R.id.tv_amendgd_jj)
    TextView tv_amendgd_jj;

    @Bind(R.id.img_amendgd_add)
    ImageView img_amendgd_add;

    @Bind(R.id.tv_amendgd_headView)
    TextView tv_amendgd_headView;

    @Bind(R.id.tv_amendgd_name)
    TextView tv_amendgd_name;

    @Bind(R.id.ll_amendgd_adress)
    LinearLayout ll_amendgd_adress;

    @Bind(R.id.tv_amendgd_adress)
    TextView tv_amendgd_adress;

    @Bind(R.id.img_amendgd_yuyin)
    ImageView img_amendgd_yuyin;


    private GridView gv_amendgd;
    private TextView tv_sqr_qd, tv_sqr_cz;
    private List<String> alllist;
    int STARTTIME = 0;
    int ENDTTIME = 1;
    String level = "1";
    private View view, view2;
    private PopupWindow pop, pop2;
    private GirdAdapter adapter;
    private ListViewAdapter listAdapters;
    private ListView lv_itemadress;
    private MyAsyncTast myAsyncTast;
    private ApplyAdreesBean applyBean;
    private ApplyPresonBean applyPresonBean;
    private String departId;
    private DateTimeDialog dateTimeDialog;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private List<String> nameList;
    private boolean includs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amendgd);
        setTvTitleText("工单修改");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);

        LayoutInflater inflaters = LayoutInflater.from(this);
        view = inflaters.inflate(R.layout.popupwindow_sqr, null);
        view2 = inflaters.inflate(R.layout.popupwindow_adress, null);

        gv_amendgd = (GridView) view.findViewById(R.id.gv_amendgd);
        tv_sqr_qd = (TextView) view.findViewById(R.id.tv_sqr_qd);
        tv_sqr_cz = (TextView) view.findViewById(R.id.tv_sqr_cz);
        lv_itemadress = (ListView) view2.findViewById(R.id.lv_itemadress);

        tv_amendgd_startime.setOnClickListener(this);
        tv_amendgd_endtime.setOnClickListener(this);
        tv_sqr_qd.setOnClickListener(this);
        tv_sqr_cz.setOnClickListener(this);
        tv_amendgd_pt.setOnClickListener(this);
        tv_amendgd_jj.setOnClickListener(this);
        img_amendgd_add.setOnClickListener(this);
        ll_amendgd_adress.setOnClickListener(this);
        img_amendgd_yuyin.setOnClickListener(this);

        view.setFocusableInTouchMode(true);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pop.setAnimationStyle(R.style.MenuAnimationFade);
        pop.setBackgroundDrawable(new BitmapDrawable());

        view2.setFocusableInTouchMode(true);
        pop2 = new PopupWindow(view2, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pop2.setAnimationStyle(R.style.MenuAnimationFade);
        pop2.setBackgroundDrawable(new BitmapDrawable());
        nameList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        Drawable drawable = this.getResources().getDrawable(R.mipmap.amendgd_yellow);
        Drawable drawable1 = this.getResources().getDrawable(R.mipmap.amendgd_red);
        Drawable drawable2 = this.getResources().getDrawable(R.mipmap.amendgd_null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable2.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (v.getId()) {
            case R.id.tv_amendgd_startime:
                //选择开始时间
                dateTimeDialog = new DateTimeDialog(this, null, this, STARTTIME);
                showAll();
                break;
            case R.id.tv_amendgd_endtime:
                //选择结束时间
                dateTimeDialog = new DateTimeDialog(this, null, this, ENDTTIME);
                showAll();
                break;
            case R.id.tv_amendgd_pt:
                //普通
                level = "1";
                tv_amendgd_pt.setCompoundDrawables(drawable, null, null, null);
                tv_amendgd_jj.setCompoundDrawables(drawable2, null, null, null);

                break;
            case R.id.tv_amendgd_jj:
                //紧急
                level = "2";
                tv_amendgd_jj.setCompoundDrawables(drawable1, null, null, null);
                tv_amendgd_pt.setCompoundDrawables(drawable2, null, null, null);
                break;
            case R.id.img_amendgd_add:
                //申请人
                if (departId != null && !departId.equals("")) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("departId", departId);
                    myAsyncTast = new MyAsyncTast(AmendGdActivity.this, hashMap, AppConfig.GETUSERBYDEPARTID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                        @Override
                        public void send(String result) {
                            showDialog();
                            applyPresonBean = GsonUtil.parseJsonWithGson(result, ApplyPresonBean.class);
                            adapter = new GirdAdapter();
                            gv_amendgd.setAdapter(adapter);
                        }
                    });
                    myAsyncTast.execute();
                } else {
                    showToast("请先选择地点");
                }
                break;
            case R.id.tv_sqr_cz:
                //重置申请人
                nameList = new ArrayList<>();
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_sqr_qd:
                //确认申请人
                pop.dismiss();
                String name = "";
                if (nameList.size() > 0) {
                    for (int i = 0; i < nameList.size(); i++) {
                        name = name + nameList.get(i) + ",";
                    }
                    tv_amendgd_name.setText(name);
                } else {
                    tv_amendgd_name.setText("");
                }
                break;
            case R.id.ll_amendgd_adress:
                //地点
                Log.e("token", App.getInstance().getToken());
                HashMap hashMaps = new HashMap();
                myAsyncTast = new MyAsyncTast(AmendGdActivity.this, hashMaps, AppConfig.GETDEPARTBYUSER, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                    @Override
                    public void send(String result) {
                        showDialog2();
                        applyBean = GsonUtil.parseJsonWithGson(result, ApplyAdreesBean.class);
                        listAdapters = new ListViewAdapter();
                        lv_itemadress.setAdapter(listAdapters);
                    }
                });
                myAsyncTast.execute();
                break;
            case R.id.img_amendgd_yuyin:
                //语音

                break;
            default:
                break;
        }
    }

    private void showAll() {
        dateTimeDialog.hideOrShow();
    }


    @Override
    public void onDateSet(Date date, int type) {
        if (type == STARTTIME) {
            tv_amendgd_startime.setText(mFormatter.format(date) + "");
        } else if (type == ENDTTIME) {
            tv_amendgd_endtime.setText(mFormatter.format(date) + "");
        }
    }

    @Override
    public void onDateSet(Date date) {

    }

    /**
     * 申请人弹窗
     */
    private void showDialog() {
        // TODO Auto-generated method stub
        if (pop.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
            pop.dismiss();
        } else {
            // 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
            pop.showAtLocation(tv_amendgd_headView, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 地址弹窗
     */
    private void showDialog2() {
        // TODO Auto-generated method stub
        if (pop2.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
            pop2.dismiss();
        } else {
            // 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
            pop2.showAtLocation(tv_amendgd_headView, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 申请人适配器
     */
    class GirdAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return applyPresonBean.getData().getResult().size();
        }

        @Override
        public Object getItem(int position) {
            return applyPresonBean.getData().getResult().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Cache cache;
            if (convertView == null) {
                cache = new Cache();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_sqr, null);
                cache.img_itemsqr_type = (ImageView) convertView.findViewById(R.id.img_itemsqr_type);
                cache.ll_itemsqr = (LinearLayout) convertView.findViewById(R.id.ll_itemsqr);
                cache.tv_itemsqr_name = (TextView) convertView.findViewById(R.id.tv_itemsqr_name);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.tv_itemsqr_name.setText(applyPresonBean.getData().getResult().get(position).getRealname());
            if (nameList.size() > 0) {
                for (int i = 0; i < nameList.size(); i++) {
                    if (nameList.get(i).equals(applyPresonBean.getData().getResult().get(position).getRealname())) {
                        cache.img_itemsqr_type.setImageResource(R.mipmap.gou_green);
                        break;
                    } else {
                        cache.img_itemsqr_type.setImageResource(R.mipmap.dot_black);
                    }
                }
            } else {
                cache.img_itemsqr_type.setImageResource(R.mipmap.dot_black);
            }

            cache.ll_itemsqr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nameList.size() > 0) {
                        for (int i = 0; i < nameList.size(); i++) {
                            if (applyPresonBean.getData().getResult().get(position).getRealname().equals(nameList.get(i))) {
                                nameList.remove(i);
                                cache.img_itemsqr_type.setImageResource(R.mipmap.dot_black);
                                includs = false;
                                break;
                            } else {
                                includs = true;
                            }
                        }
                        if (includs) {
                            nameList.add(applyPresonBean.getData().getResult().get(position).getRealname());
                            cache.img_itemsqr_type.setImageResource(R.mipmap.gou_green);
                        }
                    } else {
                        nameList.add(applyPresonBean.getData().getResult().get(position).getRealname());
                        cache.img_itemsqr_type.setImageResource(R.mipmap.gou_green);
                    }
                }
            });
            return convertView;
        }
    }

    /**
     * 地址适配器
     */
    class ListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return applyBean.getData().getResult().size();
        }

        @Override
        public Object getItem(int position) {
            return applyBean.getData().getResult().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ListViewCache caches;
            if (convertView == null) {
                caches = new ListViewCache();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_adress, null);
                caches.tv_item_adress = (TextView) convertView.findViewById(R.id.tv_item_adress);
                convertView.setTag(caches);
            } else {
                caches = (ListViewCache) convertView.getTag();
            }
            caches.tv_item_adress.setText(applyBean.getData().getResult().get(position).getDepartname());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_amendgd_adress.setText(applyBean.getData().getResult().get(position).getDepartname());
                    departId = applyBean.getData().getResult().get(position).getId();
                    pop2.dismiss();
                    nameList = new ArrayList<String>();
                    tv_amendgd_name.setText("");
                }
            });


            return convertView;
        }
    }

    class Cache {
        TextView tv_itemsqr_name;
        ImageView img_itemsqr_type;
        LinearLayout ll_itemsqr;
    }

    class ListViewCache {
        TextView tv_item_adress;
    }
}
