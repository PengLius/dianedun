package cn.dianedun.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.dianedun.R;

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        diaglog = createLoadingDialog(context, "正在加载");
        diaglog.show();
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

    @Override
    protected String doInBackground(Object... params) {
        HttpUtils httpUtils = new HttpUtils(5000);
        RequestParams params1 = new RequestParams("UTF-8");
        if (hashMap.size() > 0) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                params1.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        if (token != null && !token.equals("")) {
            params1.addHeader("token", token);
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                result = responseInfo.result;
                Log.e("result", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("0")) {
                        if (callback != null) {
                            callback.send(result);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                diaglog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                diaglog.dismiss();
                Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
                Log.e("msg", msg);
                Log.e("error", error + "");
            }
        });
        return result;
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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public interface Callback {
        void send(String result);
    }
}
