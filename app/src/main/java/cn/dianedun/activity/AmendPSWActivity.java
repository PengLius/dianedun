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


import com.bumptech.glide.Glide;
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
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/8/5.
 */

public class AmendPSWActivity extends BaseTitlActivity implements View.OnClickListener {

    @Bind(R.id.ed_amendpsw_yzm)
    EditText ed_amendpsw_yzm;

    @Bind(R.id.tv_amendpsw_yzm)
    TextView tv_amendpsw_yzm;

    @Bind(R.id.ed_amend_psw)
    EditText ed_amend_psw;

    @Bind(R.id.img_amendpsw_eyes)
    ImageView img_amendpsw_eyes;

    @Bind(R.id.rl_amendpsw_ok)
    RelativeLayout rl_amendpsw_ok;

    @Bind(R.id.img_amendpsw_head)
    ImageView img_amendpsw_head;

    @Bind(R.id.tv_amendpsw_phone)
    TextView tv_amendpsw_phone;

    @Bind(R.id.tv_amendpsw_name)
    TextView tv_amendpsw_name;

    @Bind(R.id.tv_amendpsw_time)
    TextView tv_amendpsw_time;

    private Boolean boolxs = true;
    private boolean offs = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_amendpsw_yzm.setText("剩余" + msg.arg1 + "秒");
            if (msg.arg1 == 0) {
                tv_amendpsw_yzm.setText("点击获取");
                tv_amendpsw_yzm.setClickable(true);
            }
            if (!offs) {
                tv_amendpsw_yzm.setText("点击获取");
                tv_amendpsw_yzm.setClickable(true);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amendpsw);
        setTvTitleText("修改密码");
        setTitleBack(R.mipmap.home_backg_rightnull);
        setImgLeftVisibility(View.VISIBLE);
        setImgLeft(R.mipmap.bt_back);

        img_amendpsw_eyes.setOnClickListener(this);
        tv_amendpsw_yzm.setOnClickListener(this);
        rl_amendpsw_ok.setOnClickListener(this);
        String imagUrl = getIntent().getStringExtra("imagUrl");
        if (imagUrl == null || imagUrl.equals("")) {
            Glide.with(AmendPSWActivity.this).load(R.mipmap.login_logo).into(img_amendpsw_head);
        } else {
            Glide.with(AmendPSWActivity.this).load(imagUrl).into(img_amendpsw_head);
        }

        tv_amendpsw_phone.setText(getIntent().getStringExtra("phone"));
        tv_amendpsw_name.setText(getIntent().getStringExtra("username"));
        String time = getIntent().getStringExtra("time");
        if (time != null) {
            if (time.equals("null")) {
                tv_amendpsw_time.setText("欢迎使用电e盾");
            } else {
                tv_amendpsw_time.setText("您上次于 " + getIntent().getStringExtra("time") + " 登录");
            }
        } else {
            tv_amendpsw_time.setText("欢迎使用电e盾");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_amendpsw_eyes:
                //是否显示密码
                if (boolxs) {
                    img_amendpsw_eyes.setBackgroundResource(R.mipmap.login_eye);
                    ed_amend_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ed_amend_psw.setSelection(ed_amend_psw.length());
                    boolxs = false;
                } else {
                    img_amendpsw_eyes.setBackgroundResource(R.mipmap.login_eye1);
                    ed_amend_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ed_amend_psw.setSelection(ed_amend_psw.length());
                    boolxs = true;
                }
                break;

            case R.id.tv_amendpsw_yzm:
                RequestParams params1 = new RequestParams(AppConfig.NOTECODE);
                params1.addBodyParameter("username", getIntent().getStringExtra("username"));
                x.http().post(params1, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")) {
                                Toast.makeText(getApplicationContext(), "短信验证码发送成功", Toast.LENGTH_SHORT).show();
                                offs = true;
                                tv_amendpsw_yzm.setClickable(false);
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
                        Toast.makeText(getApplicationContext(), "网络异常，请检查网络是否畅通", Toast.LENGTH_SHORT).show();
                        tv_amendpsw_yzm.setClickable(true);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
                break;
            case R.id.rl_amendpsw_ok:
                if (ed_amendpsw_yzm.getText().length() < 4) {
                    showToast("请输入4位短信验证码");
                } else if (ed_amend_psw.getText().length() < 6) {
                    showToast("请输入6~12位登录密码");
                } else {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("username", getIntent().getStringExtra("username"));
                    hashMap.put("code", ed_amendpsw_yzm.getText().toString());
                    hashMap.put("password", ed_amend_psw.getText().toString());
                    hashMap.put("registrationid", JPushInterface.getRegistrationID(getApplicationContext()));
                    MyAsyncTast myAsyncTast = new MyAsyncTast(AmendPSWActivity.this, hashMap, AppConfig.RESETPWD, App.getInstance().getToken(), new MyAsyncTast.Callback() {
                        @Override
                        public void onError(String result) {
                            showToast(result);
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
                                    editor.commit();
                                    showToast("修改成功");
                                    AppManager.getInstance().finishActivity(MainActivity.class);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
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
