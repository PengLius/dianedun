package cn.dianedun.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nanchen.compresshelper.CompressHelper;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
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
import cn.dianedun.bean.DisposeJBBean;
import cn.dianedun.bean.ImagBean;
import cn.dianedun.bean.JbUpBean;
import cn.dianedun.bean.ToJsonBean;
import cn.dianedun.bean.UpdataBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;
import cn.dianedun.view.MyEditText;
import cn.dianedun.view.NoScrollGridview;
import dev.xesam.android.toolbox.timer.CountDownTimer;
import dev.xesam.android.toolbox.timer.CountTimer;

/**
 * Created by Administrator on 2017/8/7.
 */

public class DisposeJbActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.tv_disposejb_adress)
    TextView tv_disposejb_adress;

    @Bind(R.id.tv_disposejb_cause)
    TextView tv_disposejb_cause;

    @Bind(R.id.tv_disposejb_xxadress)
    TextView tv_disposejb_xxadress;

    @Bind(R.id.img_disposejb_type)
    ImageView img_disposejb_type;

    @Bind(R.id.rl_disposejb_ok)
    RelativeLayout rl_disposejb_ok;

    @Bind(R.id.tv_disposejb_time)
    TextView tv_disposejb_time;

    @Bind(R.id.ed_disposejb_cause)
    MyEditText ed_disposejb_cause;

    @Bind(R.id.img_disposejb_add)
    ImageView img_disposejb_add;

    @Bind(R.id.gv_disposejb)
    NoScrollGridview gv_disposejb;

    @Bind(R.id.tv_amendgd_headView)
    TextView tv_amendgd_headView;

    @Bind(R.id.tv_disposejb_num)
    TextView tv_disposejb_num;

    private ImagBean imgBean;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private DisposeJBBean bean;
    private String obj2 = "";
    private List<LocalMedia> selectList;
    private List<File> dzList;
    private int num = 9;
    Dialog diaglog;
    private static int HASLY = 0;
    private static int NOLY = 1;
    private int lyType = 1;
    private int type = 0;
    private AnimationDrawable animationDrawable;
    private GirdAdapter adapter;
    private String ypUri = "";
    private MediaRecorder recorder;
    private String fileName = "/sdcard/audiorecordtest.mp3";
    private PopupWindow pop3;
    private View view3;
    private RelativeLayout rl_luyin_type;
    private ImageView img_luyin_uploading, img_yuyin_close, img_luyin;
    private TextView tv_luyin_timer;
    private boolean offs = false;
    private CountTimer countTimer;
    private JbUpBean updataBean;
    private ToJsonBean toJsonBean;
    private List<String> imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposejb);
        setTvTitleText("警报处理");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        LayoutInflater inflaters = LayoutInflater.from(this);
        view3 = inflaters.inflate(R.layout.popupwindow_luyin, null);
        rl_luyin_type = (RelativeLayout) view3.findViewById(R.id.rl_luyin_type);
        img_luyin_uploading = (ImageView) view3.findViewById(R.id.img_luyin_uploading);
        img_yuyin_close = (ImageView) view3.findViewById(R.id.img_yuyin_close);
        img_luyin = (ImageView) view3.findViewById(R.id.img_luyin);
        tv_luyin_timer = (TextView) view3.findViewById(R.id.tv_luyin_timer);
        initData();
        rl_luyin_type.setOnClickListener(this);
        img_luyin_uploading.setOnClickListener(this);
        img_yuyin_close.setOnClickListener(this);
        img_disposejb_add.setOnClickListener(this);
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
        adapter = new GirdAdapter();
        gv_disposejb.setAdapter(adapter);
        imgList = new ArrayList<>();
        ed_disposejb_cause.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 500) {
//                    showToast("字数不超过500字");
                } else {
                    tv_disposejb_num.setText(s.length() + "/500");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setupDialog() {
        final String[] items = {"拍照", "相册", "录音"};
        final AlertDialog.Builder listDialog = new AlertDialog.Builder(DisposeJbActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (num == 0) {
                            showToast("最多上传9张图片");
                        } else {
                            PictureSelector.create(DisposeJbActivity.this)
                                    .openCamera(PictureMimeType.ofImage())
                                    .forResult(PictureConfig.CHOOSE_REQUEST);
                        }
                        break;
                    case 1:
                        if (num == 0) {
                            showToast("最多上传9张图片");
                        } else {
                            PictureSelector.create(DisposeJbActivity.this)
                                    .openGallery(PictureMimeType.ofImage())
                                    .maxSelectNum(num)
                                    .imageSpanCount(4)
                                    .selectionMode(PictureConfig.MULTIPLE)
                                    .isCamera(false)
                                    .forResult(PictureConfig.CHOOSE_REQUEST);
                        }
                        break;
                    case 2:
                        if (ypUri.equals("")) {
                            showDialog3();
                        } else {
                            showToast("已存在录音文件");
                        }
                        break;
                    default:
                        break;
                }


            }
        });
        listDialog.show();

    }

    private void initData() {
        ed_disposejb_cause.setText("");
        hashMap = new HashMap<>();
        hashMap.put("id", getIntent().getStringExtra("id"));
        myAsyncTast = new MyAsyncTast(DisposeJbActivity.this, hashMap, AppConfig.FINDALARMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                showToast(result);
            }

            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, DisposeJBBean.class);
                tv_disposejb_adress.setText(bean.getData().getDepartName() + "");
                tv_disposejb_cause.setText(bean.getData().getAlertDetails() + "");
                tv_disposejb_xxadress.setText(bean.getData().getAddress() + "");
                tv_disposejb_time.setText(bean.getData().getCreateTime() + "");
                if (bean.getData().getType() == 1) {
                    img_disposejb_type.setImageResource(R.mipmap.home_jg_yellow);
                } else {
                    img_disposejb_type.setImageResource(R.mipmap.home_jg_red);
                }
                rl_disposejb_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subData();
                    }
                });

            }
        });
        myAsyncTast.execute();
    }

    private void subData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String confirmTime = formatter.format(curDate);
        hashMap = new HashMap<>();
        if (confirmTime != null) {
            hashMap.put("confirmTime", confirmTime + ":00");
        }
        Gson gson = new Gson();
        String obS = "";
        if (!(ypUri.equals(""))) {
            toJsonBean = new ToJsonBean();
            toJsonBean.setOptionType(1);
            toJsonBean.setContents(ypUri);
            toJsonBean.setType(2);
            obS = gson.toJson(toJsonBean) + ",";
        }
        if (imgList != null) {
            for (int i = 0; i < imgList.size(); i++) {
                toJsonBean = new ToJsonBean();
                toJsonBean.setOptionType(0);
                toJsonBean.setContents(imgList.get(i));
                toJsonBean.setType(2);
                obS = obS + gson.toJson(toJsonBean) + ",";
            }
            if (obS.length() > 1) {
                obj2 = "[" + obS.substring(0, obS.length() - 1) + "]";
            }
        }
        if (!obj2.equals("")) {
            hashMap.put("jsonStr", obj2);
            if (ed_disposejb_cause.getText() != null) {
                hashMap.put("result", ed_disposejb_cause.getText().toString());
            }
            myAsyncTast = new MyAsyncTast(DisposeJbActivity.this, hashMap, AppConfig.UPDATEALARMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                @Override
                public void onError(String result) {
                    showToast(result);
                }

                @Override
                public void send(String result) {
                    showToast("提交成功");
                    finish();
                }
            });
            myAsyncTast.execute();
        } else {
            if (ed_disposejb_cause.getText() == null || ed_disposejb_cause.getText().toString().equals("")) {
                showToast("请填写反馈详情");
            } else {
                if (ed_disposejb_cause.getText() != null) {
                    hashMap.put("result", ed_disposejb_cause.getText().toString());
                }
                myAsyncTast = new MyAsyncTast(DisposeJbActivity.this, hashMap, AppConfig.UPDATEALARMBYID, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                    @Override
                    public void onError(String result) {
                        showToast(result);
                    }

                    @Override
                    public void send(String result) {
                        showToast("提交成功");
                        finish();
                    }
                });
                myAsyncTast.execute();
            }
        }
        hashMap.put("id", getIntent().getStringExtra("id"));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_disposejb_add:
                setupDialog();
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
                MyAsyncs myAsync = new MyAsyncs();
                myAsync.execute();
                break;
            case R.id.img_yuyin_close:
                //关闭录音弹窗
                pop3.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    dzList = new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        File newFile = CompressHelper.getDefault(this).compressToFile(new File(selectList.get(i).getPath()));
                        dzList.add(newFile);
                    }
                    MyAsync myAsync = new MyAsync();
                    myAsync.execute();
                    break;
            }
        }
    }

    private class MyAsync extends AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diaglog = createLoadingDialog(DisposeJbActivity.this, "上传文件中请稍后...");
            diaglog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            RequestParams param = new RequestParams(AppConfig.JBUPLOADFILE);
            List<KeyValue> list = new ArrayList<>();
            param.setReadTimeout(30000);
            param.setConnectTimeout(30000);
            for (int i = 0; i < dzList.size(); i++) {//遍历图片，我传的图片为下标6开始的位置
                try {
                    list.add(new KeyValue("file"//图片数组，或者单个图片的上传参数名
                            , dzList.get(i)));
                } catch (Exception e) {
                }
            }
            list.add(new KeyValue("optionType", "0"));
            org.xutils.http.body.MultipartBody body = new org.xutils.http.body.MultipartBody(list, "UTF-8");
            param.setRequestBody(body);
            param.setMultipart(true);
            param.addHeader("token", App.getInstance().getToken());
            x.http().post(param, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    diaglog.dismiss();
                    imgBean = GsonUtil.parseJsonWithGson(result, ImagBean.class);
                    for (int i = 0; i < imgBean.getData().size(); i++) {
                        imgList.add(imgBean.getData().get(i));
                    }
                    num = 9 - imgList.size();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    showToast(ex.getCause() + "");
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

    private class GirdAdapter extends BaseAdapter {
        @Override
        public int getCount() {

            if (imgList != null) {
                if (lyType == NOLY) {
                    return imgList.size();
                } else {
                    return imgList.size() + 1;
                }
            } else {
                return 0;

            }

        }

        @Override
        public Object getItem(int position) {

            return imgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (lyType == HASLY && position == imgList.size()) {
                //音频
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_scyp, null);
                final TextView tv_fjyp_time = (TextView) convertView.findViewById(R.id.tv_fjyp_time);
                final ImageView img_fjyp_yy = (ImageView) convertView.findViewById(R.id.img_fjyp_yy);
                final MediaPlayer player = MediaPlayer.create(getApplicationContext(), Uri.parse(ypUri));
                long f = player.getDuration() / 1000 / 60;
                long m = (player.getDuration() - (f * 1000 * 60)) / 1000;
                if (f < 10) {
                    if (m < 10) {
                        tv_fjyp_time.setText("0" + f + "'" + "0" + m + "\"");
                    } else {
                        tv_fjyp_time.setText("0" + f + "'" + m + "\"");
                    }
                } else {
                    if (m < 10) {
                        tv_fjyp_time.setText(f + "'" + "0" + m + "\"");
                    } else {
                        tv_fjyp_time.setText(f + "'" + m + "\"");
                    }
                }
                final CountDownTimer countDownTimer = new CountDownTimer(player.getDuration(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) { // millisUntilFinished is the left time at *Running State*
                        long f = millisUntilFinished / 1000 / 60;
                        long m = (millisUntilFinished - (f * 1000 * 60)) / 1000;
                        if (f < 10) {
                            if (m < 10) {
                                tv_fjyp_time.setText("0" + f + "'" + "0" + m + "\"");
                            } else {
                                tv_fjyp_time.setText("0" + f + "'" + m + "\"");
                            }
                        } else {
                            if (m < 10) {
                                tv_fjyp_time.setText(f + "'" + "0" + m + "\"");
                            } else {
                                tv_fjyp_time.setText(f + "'" + m + "\"");
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
                                tv_fjyp_time.setText("0" + a + "'" + "0" + b + "\"");
                            } else {
                                tv_fjyp_time.setText("0" + a + "'" + b + "\"");
                            }
                        } else {
                            if (b < 10) {
                                tv_fjyp_time.setText(a + "'" + "0" + b + "\"");
                            } else {
                                tv_fjyp_time.setText(a + "'" + b + "\"");
                            }
                        }
                        if (animationDrawable != null) {
                            if (animationDrawable.isRunning()) {
                                animationDrawable.stop();
                            }
                        }

                        img_fjyp_yy.setImageResource(R.mipmap.yp_bf);
                        type = 0;
                    }
                };
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 0) {
                            player.start();
                            countDownTimer.start();
                            img_fjyp_yy.setImageResource(R.drawable.animation1);
                            animationDrawable = (AnimationDrawable) img_fjyp_yy.getDrawable();
                            animationDrawable.start();
                            type = 1;
                        } else if (type == 1) {
                            img_fjyp_yy.setImageResource(R.mipmap.yp_bf);
                            player.pause();
                            countDownTimer.pause();
                            type = 2;
                            animationDrawable.stop();
                        } else {
                            player.start();
                            countDownTimer.resume();
                            img_fjyp_yy.setImageResource(R.drawable.animation1);
                            animationDrawable = (AnimationDrawable) img_fjyp_yy.getDrawable();
                            animationDrawable.start();
                            type = 1;
                        }
                    }
                });
                ((ImageView) convertView.findViewById(R.id.img_scyp_del)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lyType = NOLY;
                        player.stop();
                        countDownTimer.onFinish();
                        if (animationDrawable != null) {
                            animationDrawable.stop();
                        }
                        type = 0;
                        adapter.notifyDataSetChanged();
                        ypUri = "";
                    }
                });

            } else {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_scimg, null);
                final ImageView imagView = (ImageView) convertView.findViewById(R.id.img_fjimg_img);
                Glide.with(DisposeJbActivity.this).load(imgList.get(position)).into(imagView);
                ((ImageView) convertView.findViewById(R.id.img_scimg_del)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgList.remove(position);
                        num++;
                        Log.e("num", num + "");
                        adapter.notifyDataSetChanged();
                    }
                });
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ImagActivity.class);
                        intent.putStringArrayListExtra("imgList", (ArrayList<String>) imgList);
                        intent.putExtra("pos", position + "");
                        startActivity(intent);
                    }
                });
            }
            return convertView;
        }
    }

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

    private class MyAsyncs extends AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diaglog = createLoadingDialog(DisposeJbActivity.this, "上传文件中请稍后...");
            diaglog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            RequestParams param = new RequestParams(AppConfig.JBUPLOADFILE);
            param.addBodyParameter("file", new File(fileName));
            param.addBodyParameter("optionType", "1");
            param.addHeader("token", App.getInstance().getToken());
            x.http().post(param, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("result", result);
                    updataBean = GsonUtil.parseJsonWithGson(result, JbUpBean.class);
                    ypUri = updataBean.getData().get(0);
                    diaglog.dismiss();
                    lyType = HASLY;
                    adapter.notifyDataSetChanged();
                    pop3.dismiss();
                    showToast("上传成功");
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