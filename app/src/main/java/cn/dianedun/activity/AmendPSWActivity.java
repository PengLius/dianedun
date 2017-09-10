package cn.dianedun.activity;

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
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.base.BaseTitlActivity;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.MyAsyncTast;

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

    @Bind(R.id.rl_amendpsw_qx)
    RelativeLayout rl_amendpsw_qx;

    @Bind(R.id.rl_amendpsw_ok)
    RelativeLayout rl_amendpsw_ok;

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
        rl_amendpsw_qx.setOnClickListener(this);

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
                HttpUtils httpUtils = new HttpUtils(10000);
                RequestParams params1 = new RequestParams("UTF-8");
                params1.addBodyParameter("username", "");
                httpUtils.send(HttpRequest.HttpMethod.POST, AppConfig.NOTECODE, params1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(responseInfo.result);
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
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                        tv_amendpsw_yzm.setClickable(true);
                    }
                });
                break;
            case R.id.rl_amendpsw_ok:
                if (ed_amendpsw_yzm.getText().length() < 4) {
                    showToast("请输入4位短信验证码");
                } else {
                    if (ed_amend_psw.getText().length() > 6 && ed_amend_psw.getText() != null) {

                        MyAsyncTast myAsyncTast = new MyAsyncTast(AmendPSWActivity.this, new HashMap<String, String>(), "", App.getInstance().getToken(), new MyAsyncTast.Callback() {
                            @Override
                            public void send(String result) {
                                

                            }
                        });
                        myAsyncTast.execute();
                    }
                }

                break;
            case R.id.rl_amendpsw_qx:
                finish();
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
