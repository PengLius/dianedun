package cn.dianedun.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
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
import cn.dianedun.bean.DetailsBean;
import cn.dianedun.bean.JbUpBean;
import cn.dianedun.bean.ResultBean;
import cn.dianedun.bean.ToJsonBean;
import cn.dianedun.bean.UpdataBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.DataUtil;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import cn.dianedun.view.DateTimeDialog;
import cn.dianedun.view.DateTimeDialogOnlyTime;
import cn.dianedun.view.DateTimeDialogOnlyYMD;
import cn.dianedun.view.MyEditText;
import dev.xesam.android.toolbox.timer.CountDownTimer;
import dev.xesam.android.toolbox.timer.CountTimer;

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

    @Bind(R.id.ll_amendgd_star)
    LinearLayout ll_amendgd_star;

    @Bind(R.id.img_amendgd_del)
    ImageView img_amendgd_del;

    @Bind(R.id.rl_amendgd_tj)
    RelativeLayout rl_amendgd_tj;

    @Bind(R.id.img_amendgd_lyic)
    ImageView img_amendgd_lyic;

    @Bind(R.id.rl_amendgd_ly)
    RelativeLayout rl_amendgd_ly;

    @Bind(R.id.tv_amendgd_lytime)
    TextView tv_amendgd_lytime;

    @Bind(R.id.tv_amendgd_xxadress)
    TextView tv_amendgd_xxadress;

    @Bind(R.id.ed_amendgd_sqyy)
    MyEditText ed_amendgd_sqyy;

    @Bind(R.id.tv_amendgd_numb)
    TextView tv_amendgd_numb;

    @Bind(R.id.ll_amendgd)
    LinearLayout ll_amendgd;

    @Bind(R.id.tv_amendgd_gdh)
    TextView tv_amendgd_gdh;


    private LinearLayout ll_view;
    private String beginTime, endTime;
    private GridView gv_amendgd;
    private LinearLayout ll_adress_close;
    private TextView tv_sqr_qd, tv_sqr_cz;
    private List<String> alllist;
    int STARTTIME = 0;
    int ENDTTIME = 1;
    private String level = "1";
    private View view, view2, view3;
    private PopupWindow pop, pop2, pop3;
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
    private Drawable drawable, drawable1, drawable2;
    private RelativeLayout rl_luyin_type;
    private ImageView img_luyin_uploading, img_yuyin_close, img_luyin;
    private TextView tv_luyin_timer;
    private MediaRecorder recorder;
    private String fileName = "/sdcard/audiorecordtest.mp3";
    private CountTimer countTimer;
    private boolean offs = false;
    private int type = 0;
    private int playTyp = 0;
    private MediaPlayer player;
    private CountDownTimer countDownTimer;
    private AnimationDrawable animationDrawable;
    private String obj2 = "";
    private Dialog diaglog;
    private JbUpBean updataBean;
    private ToJsonBean toJsonBean;
    private Date startTimer, endTimer;
    private HashMap<String, String> hashMap;
    private DetailsBean bean;
    private String handlePersion = "";
    private String xxAdress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amendgd);
        setTvTitleText("工单修改");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        ll_amendgd.setVisibility(View.VISIBLE);

        LayoutInflater inflaters = LayoutInflater.from(this);
        view = inflaters.inflate(R.layout.popupwindow_sqr, null);
        view2 = inflaters.inflate(R.layout.popupwindow_adress, null);
        view3 = inflaters.inflate(R.layout.popupwindow_luyin, null);

        gv_amendgd = (GridView) view.findViewById(R.id.gv_amendgd);
        tv_sqr_qd = (TextView) view.findViewById(R.id.tv_sqr_qd);
        tv_sqr_cz = (TextView) view.findViewById(R.id.tv_sqr_cz);
        ll_view = (LinearLayout) view.findViewById(R.id.ll_view);
        ll_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                nameList.clear();
                tv_amendgd_name.setText("");
            }
        });
        lv_itemadress = (ListView) view2.findViewById(R.id.lv_itemadress);
        ll_adress_close = (LinearLayout) view2.findViewById(R.id.ll_adress_close);
        rl_luyin_type = (RelativeLayout) view3.findViewById(R.id.rl_luyin_type);
        img_luyin_uploading = (ImageView) view3.findViewById(R.id.img_luyin_uploading);
        img_yuyin_close = (ImageView) view3.findViewById(R.id.img_yuyin_close);
        img_luyin = (ImageView) view3.findViewById(R.id.img_luyin);
        tv_luyin_timer = (TextView) view3.findViewById(R.id.tv_luyin_timer);

        tv_amendgd_startime.setOnClickListener(this);
        tv_amendgd_endtime.setOnClickListener(this);
        ll_amendgd_star.setOnClickListener(this);
        img_amendgd_del.setOnClickListener(this);
        tv_sqr_qd.setOnClickListener(this);
        tv_sqr_cz.setOnClickListener(this);
        tv_amendgd_pt.setOnClickListener(this);
        tv_amendgd_jj.setOnClickListener(this);
        img_amendgd_add.setOnClickListener(this);
        ll_amendgd_adress.setOnClickListener(this);
        img_amendgd_yuyin.setOnClickListener(this);
        rl_amendgd_tj.setOnClickListener(this);
        rl_luyin_type.setOnClickListener(this);
        img_luyin_uploading.setOnClickListener(this);
        ll_adress_close.setOnClickListener(this);
        img_yuyin_close.setOnClickListener(this);

        nameList = new ArrayList<>();
        ed_amendgd_sqyy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 500) {
//                    showToast("字数不超过500字");
                } else {
                    tv_amendgd_numb.setText(s.length() + "/500");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        invitData();

    }

    /**
     * 初始数据
     */
    public void invitData() {
        drawable = this.getResources().getDrawable(R.mipmap.amendgd_yellow);
        drawable1 = this.getResources().getDrawable(R.mipmap.amendgd_red);
        drawable2 = this.getResources().getDrawable(R.mipmap.amendgd_null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable2.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        countTimer = new CountTimer(1000) {
            @Override
            public void onTick(long millisFly) {
                long js = millisFly / 1000;
                long minute = millisFly / 1000 / 60;
                long second = (millisFly - (minute * 1000 * 60)) / 1000;
                if (minute < 10) {
                    if (second < 10) {
                        tv_luyin_timer.setText("0" + minute + ":" + "0" + second);
                    } else {
                        tv_luyin_timer.setText("0" + minute + ":" + second);
                    }
                } else {
                    if (second < 10) {
                        tv_luyin_timer.setText(minute + ":" + "0" + second);
                    } else {
                        tv_luyin_timer.setText(minute + ":" + second);
                    }
                }
                if (js == 600 || js > 600) {
                    countTimer.cancel();
                    img_luyin.setImageResource(R.mipmap.bofang);
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    type = 3;
                    showToast("录音时间到");
                    offs = false;
                }
            }
        };
        hashMap = new HashMap<>();
        hashMap.put("orderNum", getIntent().getStringExtra("orderNum"));
        myAsyncTast = new MyAsyncTast(AmendGdActivity.this, hashMap, AppConfig.GETHANDLEORDERBYNUM, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
            }

            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DetailsBean.class);
                if (bean.getData().getUrgency() == 0) {
                    tv_amendgd_pt.setCompoundDrawables(drawable, null, null, null);
                    tv_amendgd_jj.setCompoundDrawables(drawable2, null, null, null);
                    level = "0";
                } else if (bean.getData().getUrgency() == 1) {
                    tv_amendgd_jj.setCompoundDrawables(drawable1, null, null, null);
                    tv_amendgd_pt.setCompoundDrawables(drawable2, null, null, null);
                    level = "1";
                }
                beginTime = bean.getData().getBeginTime() / 1000 + "";
                endTime = bean.getData().getEndTime() / 1000 + "";
                startTimer = DataUtil.getDate(bean.getData().getBeginTime() + "");
                endTimer = DataUtil.getDate(bean.getData().getEndTime() + "");

                beginTime = DataUtil.timeStamp2Date(beginTime, "yyyy-MM-dd HH:mm");
                endTime = DataUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm");
                tv_amendgd_gdh.setText(bean.getData().getOrderNum() + "");
                tv_amendgd_startime.setText(beginTime);
                tv_amendgd_endtime.setText(endTime);
                departId = bean.getData().getDepartId();
                tv_amendgd_adress.setText(bean.getData().getDepartName());
                handlePersion = bean.getData().getHandlePersion();
                tv_amendgd_name.setText(handlePersion);
                ed_amendgd_sqyy.setText(bean.getData().getCause());
                xxAdress = bean.getData().getAddress() + "";
                tv_amendgd_xxadress.setText(xxAdress);
                if (bean.getData().getAlertOptionsArray().size() > 0) {
                    toJsonBean = new ToJsonBean();
                    toJsonBean.setOptionType(1);
                    toJsonBean.setContents(bean.getData().getAlertOptionsArray().get(0).getContents());
                    toJsonBean.setType(4);
                    Gson gson = new Gson();
                    obj2 = "[" + gson.toJson(toJsonBean) + "]";
                    rl_amendgd_ly.setVisibility(View.VISIBLE);
                    player = MediaPlayer.create(getApplicationContext(), Uri.parse(bean.getData().getAlertOptionsArray().get(0).getContents()));
                    long a = player.getDuration() / 1000 / 60;
                    long b = (player.getDuration() - (a * 1000 * 60)) / 1000;
                    if (a < 10) {
                        if (b < 10) {
                            tv_amendgd_lytime.setText("0" + a + "'" + "0" + b + "\"");
                        } else {
                            tv_amendgd_lytime.setText("0" + a + "'" + b + "\"");
                        }
                    } else {
                        if (b < 10) {
                            tv_amendgd_lytime.setText(a + "'" + "0" + b + "\"");
                        } else {
                            tv_amendgd_lytime.setText(a + "'" + b + "\"");
                        }
                    }
                    countDownTimer = new CountDownTimer(player.getDuration(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) { // millisUntilFinished is the left time at *Running State*
                            long f = millisUntilFinished / 1000 / 60;
                            long m = (millisUntilFinished - (f * 1000 * 60)) / 1000;
                            if (f < 10) {
                                if (m < 10) {
                                    tv_amendgd_lytime.setText("0" + f + "'" + "0" + m + "\"");
                                } else {
                                    tv_amendgd_lytime.setText("0" + f + "'" + m + "\"");
                                }
                            } else {
                                if (m < 10) {
                                    tv_amendgd_lytime.setText(f + "'" + "0" + m + "\"");
                                } else {
                                    tv_amendgd_lytime.setText(f + "'" + m + "\"");
                                }
                            }
                        }

                        @Override
                        public void onCancel(long millisUntilFinished) {
                        }

                        @Override
                        public void onPause(long millisUntilFinished) {

                        }

                        @Override
                        public void onResume(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            long a = player.getDuration() / 1000 / 60;
                            long b = (player.getDuration() - (a * 1000 * 60)) / 1000;
                            if (a < 10) {
                                if (b < 10) {
                                    tv_amendgd_lytime.setText("0" + a + "'" + "0" + b + "\"");
                                } else {
                                    tv_amendgd_lytime.setText("0" + a + "'" + b + "\"");
                                }
                            } else {
                                if (b < 10) {
                                    tv_amendgd_lytime.setText(a + "'" + "0" + b + "\"");
                                } else {
                                    tv_amendgd_lytime.setText(a + "'" + b + "\"");
                                }
                            }
                            if (animationDrawable != null) {
                                animationDrawable.stop();
                            }
                            img_amendgd_lyic.setImageResource(R.mipmap.yp_bf);
                            playTyp = 0;
                        }
                    };
                } else {
                    rl_amendgd_ly.setVisibility(View.GONE);

                }

            }
        });
        myAsyncTast.execute();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_amendgd_startime:
                //选择开始时间
                dateTimeDialog = new DateTimeDialog(this, null, this, STARTTIME, true);
                showAll();
                break;
            case R.id.tv_amendgd_endtime:
                //选择结束时间
                dateTimeDialog = new DateTimeDialog(this, null, this, ENDTTIME, true);
                showAll();
                break;
            case R.id.tv_amendgd_pt:
                //普通
                level = "0";
                tv_amendgd_pt.setCompoundDrawables(drawable, null, null, null);
                tv_amendgd_jj.setCompoundDrawables(drawable2, null, null, null);
                break;
            case R.id.tv_amendgd_jj:
                //紧急
                level = "1";
                tv_amendgd_jj.setCompoundDrawables(drawable1, null, null, null);
                tv_amendgd_pt.setCompoundDrawables(drawable2, null, null, null);
                break;
            case R.id.ll_adress_close:
                if (pop2.isShowing()) {
                    pop2.dismiss();
                }
                break;

            case R.id.img_amendgd_add:
                //申请人
                if (departId != null && !departId.equals("")) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("departId", departId);
                    myAsyncTast = new MyAsyncTast(AmendGdActivity.this, hashMap, AppConfig.GETUSERBYDEPARTID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                        @Override
                        public void onError(String result) {
                            showToast(result);
                        }

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
                    public void onError(String result) {
                        showToast(result);
                    }

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
                if (obj2.equals("")) {
                    showDialog3();
                } else {
                    showToast("已存在录音文件");
                }
                break;
            case R.id.rl_amendgd_tj:
                //提交
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String applyTime = formatter.format(curDate);
                if (nameList.size() > 0) {
                    handlePersion = "";
                    for (int i = 0; i < nameList.size(); i++) {
                        if ((i + 1) != nameList.size()) {
                            handlePersion = handlePersion + nameList.get(i) + ",";
                        } else {
                            handlePersion = handlePersion + nameList.get(i);
                        }
                    }
                }
                HashMap hashMap = new HashMap();
                if (beginTime == null) {
                    showToast("请选择开始时间");
                } else if (endTime == null) {
                    showToast("请选择结束时间");
                } else if (departId == null) {
                    showToast("请选择地点");
                } else if (handlePersion == null) {
                    showToast("请选择申请人");
                } else if (ed_amendgd_sqyy.getText() == null || ed_amendgd_sqyy.getText().toString().equals("")) {
                    showToast("请填写申请原因");
                } else {
                    hashMap.put("departId", departId);
                    hashMap.put("urgency", level);
                    hashMap.put("id", bean.getData().getId());
                    hashMap.put("applyTime", applyTime + ":00");
                    hashMap.put("handlePersion", handlePersion);
                    hashMap.put("address", xxAdress);
                    hashMap.put("beginTime", beginTime + ":00");
                    hashMap.put("endTime", endTime + ":00");
                    hashMap.put("cause", ed_amendgd_sqyy.getText().toString());
                    hashMap.put("jsonStr", obj2);
                    myAsyncTast = new MyAsyncTast(AmendGdActivity.this, hashMap, AppConfig.MODIFYHANDLEORDER, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                        @Override
                        public void onError(String result) {
                            showToast(result);
                        }

                        @Override
                        public void send(String result) {
                            ResultBean bean = GsonUtil.parseJsonWithGson(result, ResultBean.class);
                            if (bean.getCode() == 0) {
                                showToast("工单修改成功");
                                finish();
                            } else {
                                showToast(bean.getMsg());
                            }
                        }
                    });
                    myAsyncTast.execute();
                }
                break;
            case R.id.rl_luyin_type:
                //开始录音
                if (type == 0) {
                    type = 1;
                    recorder.start();
                    offs = true;
                    img_luyin.setImageResource(R.mipmap.zanting);
                    countTimer.start();
                } else if (type == 1) {
                    img_luyin.setImageResource(R.mipmap.bofang);
                    recorder.pause();
                    countTimer.pause();
                    type = 4;
                } else if (type == 3) {
                    showToast("录音时间到");
                } else if (type == 4) {
                    type = 1;
                    offs = true;
                    img_luyin.setImageResource(R.mipmap.zanting);
                    recorder.start();
                    countTimer.resume();
                }
                break;
            case R.id.img_luyin_uploading:
                //上传录音
                MyAsync myAsync = new MyAsync();
                myAsync.execute();
                break;
            case R.id.img_yuyin_close:
                //关闭录音弹窗
                pop3.dismiss();
                break;
            case R.id.ll_amendgd_star:
                //播放
                if (playTyp == 0) {
                    player.start();
                    countDownTimer.start();
                    playTyp = 1;
                    img_amendgd_lyic.setImageResource(R.drawable.animation1);
                    animationDrawable = (AnimationDrawable) img_amendgd_lyic.getDrawable();
                    animationDrawable.start();
                } else if (playTyp == 1) {
                    img_amendgd_lyic.setImageResource(R.mipmap.yp_bf);
                    player.pause();
                    countDownTimer.pause();
                    playTyp = 2;
                    animationDrawable.stop();
                } else {
                    img_amendgd_lyic.setImageResource(R.drawable.animation1);
                    animationDrawable = (AnimationDrawable) img_amendgd_lyic.getDrawable();
                    player.start();
                    countDownTimer.resume();
                    playTyp = 1;
                    animationDrawable.start();
                }
                break;
            case R.id.img_amendgd_del:
                //隐藏录音
                rl_amendgd_ly.setVisibility(View.GONE);
                obj2 = "";
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
            if (endTimer.getTime() < date.getTime()) {
                showToast("开始时间不能大于结束时间");
            } else {
                tv_amendgd_startime.setText(mFormatter.format(date) + "");
                beginTime = mFormatter.format(date) + "";
                startTimer = date;
            }
        } else if (type == ENDTTIME) {
            if (startTimer.getTime() > date.getTime()) {
                showToast("开始时间不能大于结束时间");
            } else {
                tv_amendgd_endtime.setText(mFormatter.format(date) + "");
                endTime = mFormatter.format(date) + "";
                endTimer = date;
            }
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
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置好参数之后再show
        pop.showAsDropDown(tv_amendgd_headView);
    }

    /**
     * 地址弹窗
     */
    private void showDialog2() {
        // TODO Auto-generated method stub
        pop2 = new PopupWindow(view2, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        pop2.setTouchable(true);
        pop2.setFocusable(true);
        pop2.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置好参数之后再show
        pop2.showAsDropDown(tv_amendgd_headView);
    }

    /**
     * 录音弹窗
     */
    private void showDialog3() {
        // TODO Auto-generated method stub
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(fileName);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pop3 = new PopupWindow(view3, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        pop3.setTouchable(true);
        pop3.setFocusable(true);
        pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDismiss() {
                img_luyin.setImageResource(R.mipmap.yuyin);
                tv_luyin_timer.setText("00:00");
                countTimer.cancel();
                if (recorder != null && offs) {
                    recorder.stop();
                    recorder.release();
                }
                recorder = null;
                offs = false;
                type = 0;
            }
        });
        pop3.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置好参数之后再show
        pop3.showAsDropDown(tv_amendgd_headView);

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
                cache.ll_itemsqr = (RelativeLayout) convertView.findViewById(R.id.ll_itemsqr);
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
                    xxAdress = applyBean.getData().getResult().get(position).getAddress();
                    tv_amendgd_xxadress.setText(xxAdress);
                    tv_amendgd_adress.setText(applyBean.getData().getResult().get(position).getDepartname());
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
        RelativeLayout ll_itemsqr;
    }

    class ListViewCache {
        TextView tv_item_adress;
    }

    private class MyAsync extends AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diaglog = createLoadingDialog(AmendGdActivity.this, "上传文件中请稍后...");
            diaglog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            RequestParams param = new RequestParams(AppConfig.UPLOADFILE);
            param.addBodyParameter("file", new File(fileName));
            param.addBodyParameter("optionType", "1");
            param.addHeader("token", App.getInstance().getToken());
            x.http().post(param, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    updataBean = GsonUtil.parseJsonWithGson(result, JbUpBean.class);
                    if (updataBean.getCode() == 0) {
                        player = MediaPlayer.create(getApplicationContext(), Uri.parse(updataBean.getData().get(0)));
                        showToast("上传成功");
                        rl_amendgd_ly.setVisibility(View.VISIBLE);
                        pop3.dismiss();
                        toJsonBean = new ToJsonBean();
                        toJsonBean.setOptionType(1);
                        toJsonBean.setContents(updataBean.getData().get(0));
                        toJsonBean.setType(4);
                        Gson gson = new Gson();
                        obj2 = "[" + gson.toJson(toJsonBean) + "]";
                        long a = player.getDuration() / 1000 / 60;
                        long b = (player.getDuration() - (a * 1000 * 60)) / 1000;
                        if (a < 10) {
                            if (b < 10) {
                                tv_amendgd_lytime.setText("0" + a + "'" + "0" + b + "\"");
                            } else {
                                tv_amendgd_lytime.setText("0" + a + "'" + b + "\"");
                            }
                        } else {
                            if (b < 10) {
                                tv_amendgd_lytime.setText(a + "'" + "0" + b + "\"");
                            } else {
                                tv_amendgd_lytime.setText(a + "'" + b + "\"");
                            }
                        }
                        countDownTimer = new CountDownTimer(player.getDuration(), 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) { // millisUntilFinished is the left time at *Running State*
                                long f = millisUntilFinished / 1000 / 60;
                                long m = (millisUntilFinished - (f * 1000 * 60)) / 1000;
                                if (f < 10) {
                                    if (m < 10) {
                                        tv_amendgd_lytime.setText("0" + f + "'" + "0" + m + "\"");
                                    } else {
                                        tv_amendgd_lytime.setText("0" + f + "'" + m + "\"");
                                    }
                                } else {
                                    if (m < 10) {
                                        tv_amendgd_lytime.setText(f + "'" + "0" + m + "\"");
                                    } else {
                                        tv_amendgd_lytime.setText(f + "'" + m + "\"");
                                    }
                                }
                            }

                            @Override
                            public void onCancel(long millisUntilFinished) {
                            }

                            @Override
                            public void onPause(long millisUntilFinished) {

                            }

                            @Override
                            public void onResume(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                long a = player.getDuration() / 1000 / 60;
                                long b = (player.getDuration() - (a * 1000 * 60)) / 1000;
                                if (a < 10) {
                                    if (b < 10) {
                                        tv_amendgd_lytime.setText("0" + a + "'" + "0" + b + "\"");
                                    } else {
                                        tv_amendgd_lytime.setText("0" + a + "'" + b + "\"");
                                    }
                                } else {
                                    if (b < 10) {
                                        tv_amendgd_lytime.setText(a + "'" + "0" + b + "\"");
                                    } else {
                                        tv_amendgd_lytime.setText(a + "'" + b + "\"");
                                    }
                                }
                                if (animationDrawable != null) {
                                    animationDrawable.stop();
                                }
                                img_amendgd_lyic.setImageResource(R.mipmap.yp_bf);
                                playTyp = 0;
                            }
                        };
                    }
                    diaglog.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    showToast("上传失败");
                    diaglog.dismiss();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
            return null;
        }

        public Dialog createLoadingDialog(Context context, String msg) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.dialog_loadings, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
            // main.xml中的ImageView
            ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
            TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.load_animation);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            tipTextView.setText(msg);// 设置加载信息
            Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
            loadingDialog.setCancelable(false);// 不可以用“返回键”取消
            loadingDialog.setContentView(layout);// 设置布局
            return loadingDialog;
        }
    }

}
