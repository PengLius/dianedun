package cn.dianedun.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.xsnow.manager.AppManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.MyAsyncTast;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ForgetPSWActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.ed_forget_user)
    EditText ed_forget_user;

    @Bind(R.id.ed_forget_yzm)
    EditText ed_forget_yzm;

    @Bind(R.id.ed_forget_psw)
    EditText ed_forget_psw;

    @Bind(R.id.img_forget_clear)
    ImageView img_forget_clear;

    @Bind(R.id.img_forget_eyes)
    ImageView img_forget_eyes;

    @Bind(R.id.tv_forget_yzm)
    TextView tv_forget_yzm;


    @Bind(R.id.rl_forget_ok)
    RelativeLayout rl_forget_ok;

    private Boolean boolxs = true;
    private boolean offs = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_forget_yzm.setText("剩余" + msg.arg1 + "秒");
            if (msg.arg1 == 0) {
                tv_forget_yzm.setText("点击获取");
                tv_forget_yzm.setClickable(true);
            }
            if (!offs) {
                tv_forget_yzm.setText("点击获取");
                tv_forget_yzm.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsw);
        setTvTitleText("找回密码");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);
        img_forget_clear.setOnClickListener(this);
        img_forget_eyes.setOnClickListener(this);
        tv_forget_yzm.setOnClickListener(this);
        rl_forget_ok.setOnClickListener(this);

        ed_forget_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    img_forget_clear.setVisibility(View.VISIBLE);

                } else {
                    // 此处为失去焦点时的处理内容
                    img_forget_clear.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_forget_clear:
                //清空用户名
                ed_forget_user.setText("");
                break;
            case R.id.img_forget_eyes:
                //是否显示密码
                if (boolxs) {
                    img_forget_eyes.setBackgroundResource(R.mipmap.login_eye);
                    ed_forget_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ed_forget_psw.setSelection(ed_forget_psw.length());
                    boolxs = false;
                } else {
                    img_forget_eyes.setBackgroundResource(R.mipmap.login_eye1);
                    ed_forget_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ed_forget_psw.setSelection(ed_forget_psw.length());
                    boolxs = true;
                }
                break;
            case R.id.tv_forget_yzm:
                if (ed_forget_user.getText().length() != 0) {
                    RequestParams params1 = new RequestParams(AppConfig.NOTECODE);
                    params1.addBodyParameter("username", ed_forget_user.getText().toString());
                    x.http().post(params1, new Callback.CommonCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(result);
                                if (jsonObject.getString("code").equals("0")) {
                                    Toast.makeText(getApplicationContext(), "短信验证码发送成功", Toast.LENGTH_SHORT).show();
                                    offs = true;
                                    tv_forget_yzm.setClickable(false);
                                    new Thread(sendable).start();
                                } else {
                                    Toast.makeText(getApplicationContext(), "短信验证码发送失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                            tv_forget_yzm.setClickable(true);
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }

                    });
                }
                break;
            case R.id.rl_forget_ok:
                if (ed_forget_psw.getText().length() == 0 || ed_forget_psw.getText().toString() == null) {
                    showToast("请输入用户名");
                } else if (ed_forget_yzm.getText().length() < 4) {
                    showToast("请输入4位短信验证码");
                } else if (ed_forget_psw.getText().length() < 6 || ed_forget_psw.getText().length() > 12) {
                    showToast("请输入6~12位密码");
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("username", ed_forget_user.getText().toString());
                    hashMap.put("password", ed_forget_psw.getText().toString());
                    hashMap.put("code", ed_forget_yzm.getText().toString());
                    MyAsyncTast myAsyncTast = new MyAsyncTast(ForgetPSWActivity.this, hashMap, AppConfig.RESETPWD, new MyAsyncTast.Callback() {

                        @Override
                        public void onError(String result) {

                        }

                        @Override
                        public void send(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
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
                                    editor.putString("zddl", "y");
                                    editor.commit();
                                    AppManager.getInstance().finishActivity(LoginActivity.class);
                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    showToast(jsonObject.getString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    myAsyncTast.execute();
                }
                break;
            default:
                break;
        }
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
}
