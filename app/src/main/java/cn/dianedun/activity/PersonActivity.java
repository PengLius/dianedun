package cn.dianedun.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yanzhenjie.permission.AndPermission;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;

/**
 * Created by Administrator on 2017/8/5.
 */

public class PersonActivity extends BaseTitlActivity {

    @Bind(R.id.img_person_head)
    CircleTextImageView img_person_head;

    @Bind(R.id.ed_person_nc)
    EditText ed_person_nc;

    @Bind(R.id.ed_person_lxr)
    EditText ed_person_lxr;

    @Bind(R.id.img_person_clear)
    ImageView img_person_clear;

    @Bind(R.id.img_person_clear1)
    ImageView img_person_clear1;


    private Dialog diaglog;
    private List<LocalMedia> selectList;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        setTvTitleText("个人资料");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        img_person_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDialog();
            }
        });
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        img_person_head.setImageBitmap(bmp);

        ed_person_nc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        ed_person_lxr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    img_person_clear1.setVisibility(View.VISIBLE);
                } else {
                    // 此处为失去焦点时的处理内容
                    img_person_clear1.setVisibility(View.INVISIBLE);
                }
            }
        });
        img_person_clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_person_lxr.setText("");
            }
        });
        img_person_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_person_nc.setText("");
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
                        Log.e("aaa", "没权限");
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
                    showToast(selectList.get(0).getCutPath());
                    imgUrl = selectList.get(0).getCutPath();
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
            RequestParams param = new RequestParams("");
            param.addBodyParameter("headImg", new File(imgUrl));
            x.http().post(param,
                    new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            // TODO Auto-generated method stub
                            try {
                                JSONObject jsonObject = new JSONObject(result);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            showToast("上传失败");
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
