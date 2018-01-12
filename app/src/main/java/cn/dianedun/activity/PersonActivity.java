package cn.dianedun.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nanchen.compresshelper.CompressHelper;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.bean.ImagBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.GsonUtil;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/5.
 */

public class PersonActivity extends BaseTitlActivity {

    @Bind(R.id.img_person_head)
    CircleTextImageView img_person_head;


    @Bind(R.id.ed_person_phone)
    TextView ed_person_phone;

    @Bind(R.id.tv_person_nc)
    TextView tv_person_nc;

    @Bind(R.id.tv_person_zsxm)
    TextView tv_person_zsxm;

    @Bind(R.id.rl_person_ok)
    RelativeLayout rl_person_ok;


    @Bind(R.id.img_person_clear)
    ImageView img_person_clear;

    private Dialog diaglog;
    private List<LocalMedia> selectList;
    private String phone, imgUri, realname, username, imgUrs;
    private ImagBean imgBean;
    private File imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        setTvTitleText("个人资料");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        imgUrs = "";
        img_person_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDialog();
            }
        });
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        img_person_head.setImageBitmap(bmp);

        imgUri = getIntent().getStringExtra("imagUrl");
        phone = getIntent().getStringExtra("phone");
        realname = getIntent().getStringExtra("realname");
        username = getIntent().getStringExtra("username");
        tv_person_nc.setText(username);
        tv_person_zsxm.setText(realname);
        ed_person_phone.setText(phone);
        img_person_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_person_phone.setText("");
            }
        });
        ed_person_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    img_person_clear.setVisibility(View.VISIBLE);

                } else {
                    // 此处为失去焦点时的处理内容
                    img_person_clear.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (imgUri == null || imgUri.equals("")) {
            Glide.with(PersonActivity.this).load(R.mipmap.login_logo).into(img_person_head);
        } else {
            Glide.with(PersonActivity.this).load(imgUri).into(img_person_head);
        }

        rl_person_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_person_phone.getText().toString().length() < 11) {
                    showToast("请添加11位手机号码");
                } else {
                    HashMap hashMap = new HashMap();
                    if (imgUrs != null || imgUrs.equals("")) {
                        hashMap.put("headImg", imgUrs);
                    }
                    if (!(ed_person_phone.getText().toString().equals(phone))) {
                        hashMap.put("phone", ed_person_phone.getText().toString());
                    }
                    if (ed_person_phone.getText().toString().equals(phone) && (imgUrs == null || imgUrs.equals(""))) {
                        finish();
                    } else {
                        MyAsyncTast myAsyncTast = new MyAsyncTast(PersonActivity.this, hashMap, AppConfig.MONDIFYUSERHEADIMG, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                            @Override
                            public void onError(String result) {
                                showToast(result);
                            }

                            @Override
                            public void send(String result) {
                                showToast("修改成功");
                                finish();
                            }
                        });
                        myAsyncTast.execute();
                    }
                }
            }
        });
    }

    private void setupDialog() {
        final String[] items = {"拍照", "相册"};
        final AlertDialog.Builder listDialog = new AlertDialog.Builder(PersonActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    //拍照
                    // 判断存储卡是否可以用，可用进行存储
                    if (AndPermission.hasPermission(getApplicationContext(), android.Manifest.permission.CAMERA)) {
                        PictureSelector.create(PersonActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .enableCrop(true)
                                .showCropGrid(false)
                                .showCropFrame(false)
                                .circleDimmedLayer(true)
                                .forResult(PictureConfig.CHOOSE_REQUEST);

                    } else {
                        // 申请权限。
                        AndPermission.with(PersonActivity.this)
                                .requestCode(100)
                                .permission(android.Manifest.permission.CAMERA)
                                .send();
                    }
                } else if (i == 1) {
                    //相册
                    PictureSelector.create(PersonActivity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .isCamera(false)
                            .imageSpanCount(3)
                            .selectionMode(PictureConfig.SINGLE)
                            .enableCrop(true)
                            .showCropGrid(false)
                            .showCropFrame(false)
                            .circleDimmedLayer(true)
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                }
            }
        });
        listDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    imgUrl = CompressHelper.getDefault(this).compressToFile(new File(selectList.get(0).getPath()));
                    MyAsync myAsync = new MyAsync();
                    myAsync.execute();
                    break;
            }
        }
    }

    class MyAsync extends AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diaglog = createLoadingDialog(PersonActivity.this, "上传图片中请稍后...");
            diaglog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            RequestParams param = new RequestParams(AppConfig.UPLOADFILE);
            param.addBodyParameter("file", imgUrl);
            param.addBodyParameter("optionType", "0");
            param.setConnectTimeout(10000);
            param.addHeader("token", App.getInstance().getToken());
            x.http().post(param,
                    new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            // TODO Auto-generated method stub
                            diaglog.dismiss();
                            showToast("上传成功");
                            imgBean = GsonUtil.parseJsonWithGson(result, ImagBean.class);
                            Glide.with(PersonActivity.this).load(imgBean.getData().get(0)).into(img_person_head);
                            imgUrs = imgBean.getData().get(0);
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("个人资料");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人资料");
        MobclickAgent.onPause(this);
    }
}
