package cn.dianedun.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.thinkcool.circletextimageview.CircleTextImageView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.AmendPSWActivity;
import cn.dianedun.activity.LoginActivity;
import cn.dianedun.activity.MessageActivity;
import cn.dianedun.activity.PersonActivity;
import cn.dianedun.activity.WeActivity;
import cn.dianedun.base.BaseTitlFragment;
import cn.dianedun.bean.MineBean;
import cn.dianedun.bean.VersionBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MineFragment extends BaseTitlFragment implements View.OnClickListener {

    @Bind(R.id.ll_mine_xgdlmm)
    LinearLayout ll_mine_xgdlmm;

    @Bind(R.id.ll_mine_message)
    LinearLayout ll_mine_message;

    @Bind(R.id.ll_mine_phone)
    LinearLayout ll_mine_phone;

    @Bind(R.id.ll_mine_we)
    LinearLayout ll_mine_we;

    @Bind(R.id.ll_mine_jcgx)
    LinearLayout ll_mine_jcgx;

    @Bind(R.id.ll_mine_person)
    LinearLayout ll_mine_person;

    @Bind(R.id.img_mine_head)
    CircleTextImageView img_mine_head;

    @Bind(R.id.tv_mine_phone)
    TextView tv_mine_phone;

    @Bind(R.id.tv_mine_name)
    TextView tv_mine_name;

    @Bind(R.id.tv_mine_adrestime)
    TextView tv_mine_adrestime;

    @Bind(R.id.img_mine_msg)
    ImageView img_mine_msg;

    @Bind(R.id.rl_mine_out)
    RelativeLayout rl_mine_out;

    @Bind(R.id.srl_mine)
    SmartRefreshLayout srl_mine;

    @Bind(R.id.headView)
    TextView headView;

    @Bind(R.id.tv_mine_bbh)
    TextView tv_mine_bbh;

    private Intent intent;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private MineBean bean;
    private String imagUrl, phone, username, time, realname, downUrl;
    private VersionBean versionBean;
    private PopupWindow popupWindow;
    private TextView pop_tv_qx, pop_tv_qr, textView2;
    private View vies;
    private Dialog diaglog;

    public static MineFragment getInstance() {
        MineFragment fragment = new MineFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setTvTitleText("我的");
        setTitleBack(R.mipmap.home_backg_null);
        vies = LayoutInflater.from(getActivity()).inflate(R.layout.pop_my_tc, null);
        pop_tv_qx = (TextView) vies.findViewById(R.id.pop_tv_qx);
        pop_tv_qr = (TextView) vies.findViewById(R.id.pop_tv_qr);
        textView2 = (TextView) vies.findViewById(R.id.textView2);

        ll_mine_xgdlmm.setOnClickListener(this);
        ll_mine_message.setOnClickListener(this);
        ll_mine_phone.setOnClickListener(this);
        ll_mine_we.setOnClickListener(this);
        ll_mine_jcgx.setOnClickListener(this);
        ll_mine_person.setOnClickListener(this);
        rl_mine_out.setOnClickListener(this);
        pop_tv_qx.setOnClickListener(this);
        pop_tv_qr.setOnClickListener(this);
        String bbh = getVersionName(getActivity());
        tv_mine_bbh.setText("当前版本：" + bbh);
        initRefreshLayout();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mine_xgdlmm:
                intent = new Intent(getActivity(), AmendPSWActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("imagUrl", imagUrl);
                intent.putExtra("phone", phone);
                intent.putExtra("time", time);
                startActivity(intent);
                break;
            case R.id.ll_mine_message:
                intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_mine_phone:
                intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "400-096-2006");
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.ll_mine_we:
                intent = new Intent(getActivity(), WeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_mine_jcgx:
                hashMap = new HashMap<>();
                myAsyncTast = new MyAsyncTast(getActivity(), hashMap, AppConfig.ANDROIDVERSION, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                    @Override
                    public void send(String result) {
                        versionBean = GsonUtil.parseJsonWithGson(result, VersionBean.class);
                        int hcode = Integer.parseInt(versionBean.getData().getVer_no());
                        downUrl = versionBean.getData().getDownload();
                        int code = getVersionCode(getActivity());
                        if (hcode == code) {
                            showToast("当前是最新版本");
                        } else if (hcode > code) {
                            showPopupWindow(headView);
                            pop_tv_qr.setText("是");
                            pop_tv_qx.setText("否");
                            textView2.setText("当前有新版本" + versionBean.getData().getVer_name() + "\n" + "是否更新？");
                        } else if (hcode < code) {
                            showToast("当前是最新版本");
                        }
                    }

                    @Override
                    public void onError(String result) {

                    }
                });
                myAsyncTast.execute();


                break;
            case R.id.ll_mine_person:
                intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("imagUrl", imagUrl);
                intent.putExtra("phone", phone);
                intent.putExtra("realname", realname);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            case R.id.rl_mine_out:
                //退出登录
                hashMap = new HashMap<>();
                hashMap.put("username", username);
                myAsyncTast = new MyAsyncTast(getActivity(), hashMap, AppConfig.LOGINOUT, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
                    @Override
                    public void onError(String result) {

                    }

                    @Override
                    public void send(String result) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",
                                Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("zddl", "n");
                        editor.commit();
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        showToast("退出成功");
                    }
                });
                myAsyncTast.execute();
                break;

            case R.id.pop_tv_qx:
                popupWindow.dismiss();
                break;
            case R.id.pop_tv_qr:
                downloadApk();
                popupWindow.dismiss();
                break;
            default:
                break;
        }
    }

    public void getData() {
        hashMap = new HashMap<>();
        myAsyncTast = new MyAsyncTast(getActivity(), hashMap, AppConfig.GETUSERINFO, App.getInstance().getToken(), false, new MyAsyncTast.Callback() {
            @Override
            public void onError(String result) {
                srl_mine.finishRefresh();
                showToast(result);
            }

            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, MineBean.class);
                imagUrl = bean.getData().getHeadimg();
                phone = bean.getData().getMobilephone() + "";
                username = bean.getData().getUsername();
                realname = bean.getData().getRealname();
                if (bean.getData().getLogtime() == null) {
                    tv_mine_adrestime.setText("欢迎使用电e盾");
                } else {
                    if (bean.getData().getLogtime().equals("null")) {
                        tv_mine_adrestime.setText("欢迎使用电e盾");
                    } else {
                        time = bean.getData().getLogtime();
                        tv_mine_adrestime.setText("您上次于 " + time + "登录");
                    }
                }
                if (imagUrl == null || imagUrl.equals("")) {
                    Glide.with(getActivity()).load(R.mipmap.login_logo).into(img_mine_head);
                } else {
                    Glide.with(getActivity()).load(imagUrl).into(img_mine_head);
                }
                tv_mine_phone.setText("手机号：" + phone + "");
                tv_mine_name.setText("用户名：" + username + "");
                if (bean.getData().getUnreadCount() > 0) {
                    img_mine_msg.setImageResource(R.mipmap.my_msg);
                } else {
                    img_mine_msg.setImageResource(R.mipmap.my_msg1);
                }
                srl_mine.finishRefresh();
            }
        });
        myAsyncTast.execute();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        srl_mine.autoRefresh();
    }

    private void initRefreshLayout() {
        srl_mine.setLoadmoreFinished(true);
        srl_mine.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
            }
        });
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    private void showPopupWindow(View views) {
        popupWindow = new PopupWindow(vies, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        popupWindow.setTouchable(true);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(views);
    }

    protected void downloadApk() {
        //apk下载链接地址,放置apk的所在路径

        //1,判断sd卡是否可用,是否挂在上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2,获取sd路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "minEgou.apk";
            //3,发送请求,获取apk,并且放置到指定路径
            RequestParams params = new RequestParams(downUrl);
            //4,发送请求,传递参数(下载地址,下载应用放置位置)
            params.setSaveFilePath(path);
            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Log.i("", "下载成功");
                    File file = result;
                    //提示用户安装
                    diaglog.dismiss();
                    installApk(file);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    diaglog.dismiss();
                    showToast("下载失败");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }


                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    diaglog = createLoadingDialog(getActivity(), "安装包下载中...");
                    diaglog.show();
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    Log.i("", "下载中........");
                    Log.i("", "total = " + total);
                    Log.i("", "current = " + current);
                }
            });
        }
    }


    /**
     * 安装对应的apk
     */
    protected void installApk(File file) {
        //系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //文件作为数据源
        //设置安装的类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivityForResult(intent, 0);
    }

    public static Dialog createLoadingDialog(Context context, String msg) {

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
