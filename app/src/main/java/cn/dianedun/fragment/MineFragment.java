package cn.dianedun.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.util.HashMap;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.AmendPSWActivity;
import cn.dianedun.activity.MessageActivity;
import cn.dianedun.activity.PersonActivity;
import cn.dianedun.activity.WeActivity;
import cn.dianedun.base.BaseTitlFragment;
import cn.dianedun.bean.MineBean;
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


    private Intent intent;
    private MyAsyncTast myAsyncTast;
    private HashMap<String, String> hashMap;
    private MineBean bean;

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
        ll_mine_xgdlmm.setOnClickListener(this);
        ll_mine_message.setOnClickListener(this);
        ll_mine_phone.setOnClickListener(this);
        ll_mine_we.setOnClickListener(this);
        ll_mine_jcgx.setOnClickListener(this);
        ll_mine_person.setOnClickListener(this);
        getData();
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
                startActivity(intent);
                break;
            case R.id.ll_mine_message:
                intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_mine_phone:
                intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + 123456789);
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.ll_mine_we:
                intent = new Intent(getActivity(), WeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_mine_jcgx:

                break;
            case R.id.ll_mine_person:
                intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public void getData() {
        hashMap = new HashMap<>();
        myAsyncTast = new MyAsyncTast(getActivity(), hashMap, AppConfig.GETUSERINFO, App.getInstance().getToken(), new MyAsyncTast.Callback() {
            @Override
            public void send(String result) {
                bean = GsonUtil.parseJsonWithGson(result, MineBean.class);
                Glide.with(getActivity()).load(bean.getData().getHeadimg()).into(img_mine_head);
                tv_mine_phone.setText(bean.getData().getMobilephone() + "");
                tv_mine_name.setText(bean.getData().getUsername() + "");
                tv_mine_adrestime.setText("");
            }
        });
        myAsyncTast.execute();
    }
}
