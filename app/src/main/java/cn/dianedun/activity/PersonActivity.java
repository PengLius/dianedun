package cn.dianedun.activity;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;
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

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.FileUtil;
import cn.dianedun.tools.FileUtils;

/**
 * Created by Administrator on 2017/8/5.
 */

public class PersonActivity extends BaseTitlActivity {

    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/CameraCache");
    private String fileName;
    private File mCurrentPhotoFile;// 照相机拍照得到的图片
    private Uri imageUri;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private Dialog diaglog;
    private static final String IMAGE_FILE_NAME = "headImage.jpg";
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    private File tempFile;
    private Bitmap bm;
    private static String path = "/sdcard/myHead/";// sd路径
    private File mCacheFile;
    private static String paths = "file:///sdcard/myHead/";
    private static String lsimg = paths + "temp.jpg";


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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 判断存储卡是否可以用，可用进行存储
                    if (AndPermission.hasPermission(getApplicationContext(), android.Manifest.permission.CAMERA)) {
                        Log.e("aaa", "有权限");
                        // 有权限，直接do anything.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (!PHOTO_DIR.exists()) {
                                PHOTO_DIR.mkdirs();// 创建照片的存储目录
                            }
                            fileName = System.currentTimeMillis() + ".jpg";
                            mCurrentPhotoFile = new File(PHOTO_DIR, fileName);
                            Intent intentC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            imageUri = FileProvider.getUriForFile(PersonActivity.this, "cn.dianedun.fileprovider", mCurrentPhotoFile);
                            intentC.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intentC.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intentC, CAMERA_REQUEST_CODE);
                        } else {
                            if (hasSdcard()) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                                        Environment.getExternalStorageDirectory(),
                                        IMAGE_FILE_NAME)));
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                            }
                        }
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
                    Intent intentFromGallery = new Intent();
                    intentFromGallery.setType("image/*"); // 设置文件类型
                    intentFromGallery.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                }
            }
        });
        listDialog.show();
    }


    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (currentapiVersion < 24) {
                        if (hasSdcard()) {
                            tempFile = new File(
                                    Environment.getExternalStorageDirectory(),
                                    IMAGE_FILE_NAME);
                            startPhotoZoom(Uri.fromFile(tempFile));
                        } else {
                            showToast("未找到存储卡，无法存储照片！");
                        }
                    } else {
                        if (hasSdcard()) {
                            if (mCurrentPhotoFile == null || !mCurrentPhotoFile.exists()) {
                                mCurrentPhotoFile = new File(PHOTO_DIR, fileName);
                            }
                            startPhotoZoom(Uri.fromFile(mCurrentPhotoFile));
                        } else {
                            showToast("未找到存储卡，无法存储照片！");
                        }
                    }
                    break;
                case RESULT_REQUEST_CODE:
                    bm = dealCrop(getApplicationContext());
                    setPicToView(bm);
                    break;
            }
        }
    }

    public void startPhotoZoom(Uri uri) {

        String fileName = System.currentTimeMillis() + ".jpg";
        if (!PHOTO_DIR.exists()) {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
        }
        mCacheFile = new File(PHOTO_DIR, fileName);
        String url = FileUtil.getPath(PersonActivity.this, uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri imageUri = FileProvider.getUriForFile(PersonActivity.this, "cn.dianedun.fileprovider", new File(url));//通过FileProvider创建一个content类型的Uri
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(lsimg));
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    public static Bitmap dealCrop(Context context) {
        // 裁剪返回
        Uri uri = Uri.parse(lsimg);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void setPicToView(Bitmap mBitmap) {
        // TODO Auto-generated method stub
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        MyAsync myAsync = new MyAsync();
//        myAsync.execute();
    }

//    class MyAsync extends AsyncTask<Object, Object, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            diaglog = createLoadingDialog(PersonActivity.this, "上传图片中请稍后...");
//            diaglog.show();
//        }
//
//        @Override
//        protected String doInBackground(Object... params) {
//            HttpUtils httpUtils = new HttpUtils();
//            RequestParams param = new RequestParams();
//            param.addBodyParameter("headImg", new File(fileName));
//            httpUtils.send(HttpRequest.HttpMethod.POST, AppConfig.HEADIMG, param,
//                    new RequestCallBack<String>() {
//                        @Override
//                        public void onFailure(HttpException error, String msg) {
//                            // TODO Auto-generated method stub
//                            showToast("上传失败");
//
//                        }
//
//                        @Override
//                        public void onSuccess(ResponseInfo<String> responseInfo) {
//                            // TODO Auto-generated method stub
//                            try {
//                                JSONObject jsonObject = new JSONObject(responseInfo.result);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//            return null;
//        }
//        public Dialog createLoadingDialog(Context context, String msg) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View v = inflater.inflate(R.layout.dialog_loadings, null);// 得到加载view
//            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//            // main.xml中的ImageView
//            ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
//            TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//            // 加载动画
//            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                    context, R.anim.load_animation);
//            // 使用ImageView显示动画
//            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//            tipTextView.setText(msg);// 设置加载信息
//            Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
//            loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//            loadingDialog.setContentView(layout);// 设置布局
//            return loadingDialog;
//        }
//    }
}
