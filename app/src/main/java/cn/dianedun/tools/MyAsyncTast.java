package cn.dianedun.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.dianedun.R;
import cn.dianedun.activity.LoginActivity;

/**
 * Created by Administrator on 2017/9/2.
 */

public class MyAsyncTast extends AsyncTask<Object, Object, String> {
    private Dialog diaglog;
    private Context context;
    private String url, token;
    private HashMap<String, String> hashMap;
    private String result;
    private Callback callback;
    private Intent intent;
    private Boolean loding = true;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (loding) {
            diaglog = createLoadingDialog(context, "正在加载");
            diaglog.show();
        }
    }

    public MyAsyncTast(Context contexts, HashMap<String, String> hashMaps, String urls, Callback callbacks) {
        hashMap = hashMaps;
        context = contexts;
        url = urls;
        callback = callbacks;
    }

    public MyAsyncTast(Context contexts, HashMap<String, String> hashMaps, String urls, String tokens, Callback callbacks) {
        hashMap = hashMaps;
        context = contexts;
        url = urls;
        token = tokens;
        callback = callbacks;
    }

    public MyAsyncTast(Context contexts, HashMap<String, String> hashMaps, String urls, String tokens, Boolean lodings, Callback callbacks) {
        hashMap = hashMaps;
        context = contexts;
        url = urls;
        token = tokens;
        callback = callbacks;
        loding = lodings;
    }

    @Override
    protected String doInBackground(Object... params) {
        RequestParams params1 = new RequestParams(url);
        if (hashMap.size() > 0) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                params1.addBodyParameter(entry.getKey(), entry.getValue());
                Log.e(entry.getKey(), entry.getValue());
            }
        }
        if (token != null && !token.equals("")) {
            params1.addHeader("token", token);
        }

        x.http().post(params1, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("result", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("0")) {
                        if (callback != null) {
                            callback.send(result);
                        }
                    } else if (jsonObject.getString("code").equals("2001")) {
                        Toast.makeText(context, "授权验证失败，请重新登录", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("user",
                                Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("zddl", "n");
                        editor.commit();
                        intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        AppManager.getInstance().finishAllActivity();
                    } else {
                        if (callback != null) {
                            callback.onError(jsonObject.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (loding) {
                    diaglog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loding) {
                    diaglog.dismiss();
                }
                if (ex instanceof HttpException) {
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    callback.onError(responseMsg);
                } else {
                    callback.onError("网络异常，请检查网络是否畅通");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
        return result;
    }

    private static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loadings, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        LinearInterpolator lir = new LinearInterpolator();
        hyperspaceJumpAnimation.setInterpolator(lir);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout);// 设置布局
        return loadingDialog;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public interface Callback {
        void send(String result);

        void onError(String result);
    }
}
