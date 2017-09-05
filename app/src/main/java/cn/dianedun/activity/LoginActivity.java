package cn.dianedun.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.vise.xsnow.manager.AppManager;

import org.json.JSONException;
import org.json.JSONObject;

import cn.dianedun.R;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;

/**
 * Created by Administrator on 2017/8/3.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText ed_login_user, ed_login_psw, ed_login_yzm;
    private TextView tv_login_yzm, tv_login_forget;
    private ImageView img_login_clear, img_login_eyes, img_login_gou;
    private LinearLayout ll_login_zddl, ll_login_yz;
    private RelativeLayout rl_login_ok;
    private Boolean boolxs = true;
    private Boolean boolzddl = true;
    private String zddl = "y";
    private Toast mToast;
    private String idStr, pswStr;
    private ProgressDialog dialog = null;
    private myAsyncTast tast;
    private Boolean booljy = false;
    private boolean offs = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_login_yzm.setText("剩余" + msg.arg1 + "秒");
            if (msg.arg1 == 0) {
                tv_login_yzm.setText("点击获取");
                tv_login_yzm.setClickable(true);
            }
            if (!offs) {
                tv_login_yzm.setText("点击获取");
                tv_login_yzm.setClickable(true);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getInstance().addActivity(this);
        initView();
        getData();

    }

    private void initView() {
        ed_login_user = (EditText) findViewById(R.id.ed_login_user);
        ed_login_psw = (EditText) findViewById(R.id.ed_login_psw);
        ed_login_yzm = (EditText) findViewById(R.id.ed_login_yzm);
        tv_login_yzm = (TextView) findViewById(R.id.tv_login_yzm);
        tv_login_forget = (TextView) findViewById(R.id.tv_login_forget);
        img_login_clear = (ImageView) findViewById(R.id.img_login_clear);
        img_login_eyes = (ImageView) findViewById(R.id.img_login_eyes);
        ll_login_zddl = (LinearLayout) findViewById(R.id.ll_login_zddl);
        rl_login_ok = (RelativeLayout) findViewById(R.id.rl_login_ok);
        ll_login_yz = (LinearLayout) findViewById(R.id.ll_login_yz);
        img_login_gou = (ImageView) findViewById(R.id.img_login_gou);
        tv_login_yzm.setOnClickListener(this);
        tv_login_forget.setOnClickListener(this);
        img_login_clear.setOnClickListener(this);
        img_login_eyes.setOnClickListener(this);
        ll_login_zddl.setOnClickListener(this);
        rl_login_ok.setOnClickListener(this);
        ed_login_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    img_login_clear.setVisibility(View.VISIBLE);

                } else {
                    // 此处为失去焦点时的处理内容
                    img_login_clear.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_yzm:
                //获取验证码
                HttpUtils httpUtils = new HttpUtils(10000);
                RequestParams params1 = new RequestParams("UTF-8");
                params1.addBodyParameter("username", ed_login_user.getText().toString());
                httpUtils.send(HttpRequest.HttpMethod.POST, AppConfig.NOTECODE, params1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(responseInfo.result);
                            if (jsonObject.getString("code").equals("0")) {
                                Toast.makeText(getApplicationContext(), "短信验证码发送成功", Toast.LENGTH_SHORT).show();
                                offs = true;
                                tv_login_yzm.setClickable(false);
                                new Thread(sendable).start();
                            } else {
                                Toast.makeText(getApplicationContext(), "短信验证码发送失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                        ed_login_yzm.setClickable(true);
                    }
                });


                break;
            case R.id.img_login_clear:
                //清空账号
                ed_login_user.setText("");
            case R.id.img_login_eyes:
                //是否显示密码
                if (boolxs) {
                    img_login_eyes.setBackgroundResource(R.mipmap.login_eye);
                    ed_login_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ed_login_psw.setSelection(ed_login_psw.length());
                    boolxs = false;
                } else {
                    img_login_eyes.setBackgroundResource(R.mipmap.login_eye1);
                    ed_login_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ed_login_psw.setSelection(ed_login_psw.length());
                    boolxs = true;
                }
                break;
            case R.id.tv_login_forget:
                //忘记密码
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ForgetPSWActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_login_zddl:
                //自动登录
                if (boolzddl) {
                    img_login_gou.setBackgroundResource(R.mipmap.login_gou1);
                    boolzddl = false;
                    zddl = "n";
                } else {
                    img_login_gou.setBackgroundResource(R.mipmap.login_gou);
                    boolzddl = true;
                    zddl = "y";
                }
                break;
            case R.id.rl_login_ok:
                //登陆
//                Log.e("registrationId", JPushInterface.getRegistrationID(getApplicationContext()));
                if (ed_login_user.getText().length() == 0 || ed_login_user.getText().toString() == null) {
                    showToast("请输入用户名");
                } else if (ed_login_psw.getText().length() < 6 || ed_login_psw.getText().length() > 12) {
                    showToast("请输入6~12位密码");
                } else if (booljy) {
                    if (ed_login_yzm.getText().length() < 4) {
                        showToast("请输入4位短信验证码");
                    } else {
                        idStr = ed_login_user.getText().toString();
                        pswStr = ed_login_psw.getText().toString();
                        tast = new myAsyncTast();
                        tast.execute();
                    }
                } else {
                    idStr = ed_login_user.getText().toString();
                    pswStr = ed_login_psw.getText().toString();
                    tast = new myAsyncTast();
                    tast.execute();
                }
                break;


            default:
                break;
        }
    }

    public void getData() {

    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    class myAsyncTast extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this, "登录提示", "正在登录，请稍等。。。", false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            logIn();
            return null;
        }
    }

    private void logIn() {
        HttpUtils httpUtils = new HttpUtils(10000);
        RequestParams params1 = new RequestParams("UTF-8");
        params1.addBodyParameter("username", idStr);
        params1.addBodyParameter("password", pswStr);
        if (ed_login_yzm.getText() != null) {
            params1.addBodyParameter("code", ed_login_yzm.getText().toString());
        }
//        params1.addBodyParameter("registrationId", JPushInterface.getRegistrationID(getApplicationContext()));
        httpUtils.send(HttpRequest.HttpMethod.POST, AppConfig.LOGIN, params1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("result", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (jsonObject.getString("code").equals("0")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String token = jsonObject1.getString("token");
                        String isAdmin = jsonObject1.getString("isAdmin");
                        App.getInstance().setToken(token);
                        App.getInstance().setIsAdmin(isAdmin);
                        SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("token", token);
                        editor.putString("isAdmin", isAdmin);
                        editor.putString("zddl", zddl);
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (jsonObject.getString("code").equals("1002")) {
                        if (jsonObject.getString("msg").equals("password 只能包含字母和数字")) {
                            showToast("登录密码不能包含特殊字符！");
                        } else {
                            String msg = jsonObject.getString("msg");
                            showToast(msg);
                        }
                        ed_login_psw.setText("");
                    } else if (jsonObject.getString("code").equals("1008")) {
                        ll_login_yz.setVisibility(View.VISIBLE);
                        booljy = true;
                    } else {
                        String msg = jsonObject.getString("msg");
                        showToast(msg);
                        ed_login_psw.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                showToast("请检查网络");
                dialog.dismiss();
            }
        });
    }


    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            int a = 60;
            while (-1 < a && offs) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.arg1 = a;
                    handler.sendMessage(message);
                    a--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void onPause() {
        super.onPause();
        closeKeyboard();
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
