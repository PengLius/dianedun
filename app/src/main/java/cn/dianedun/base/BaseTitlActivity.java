package cn.dianedun.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.xsnow.ui.*;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;
import cn.dianedun.tools.AppManager;
import cn.dianedun.tools.CommonUtil;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/3.
 */

public abstract class BaseTitlActivity extends Activity {
    private ViewGroup mRootView;
    private View mContentView, mDeadStatusView;
    private TextView vt_tv_title;
    private Toast mToast;
    private RelativeLayout rl_titleback;
    protected Context mContext;
    protected Class<?> mClassThis;

    private ImageView vt_btn_left;
    private ImageView vt_img_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mClassThis = this.getClass();
        com.vise.xsnow.manager.AppManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_toolbar_root, null);
        mContentView = LayoutInflater.from(this).inflate(layoutResID, null);
        vt_img_right = (ImageView) mRootView.findViewById(R.id.vt_img_right);
        vt_btn_left = (ImageView) mRootView.findViewById(R.id.vt_btn_left);
        ((ViewGroup) mRootView.findViewById(R.id.ftr_fl_container)).addView(mContentView);
        super.setContentView(mRootView);
        ButterKnife.bind(this, mRootView);
        initView();
    }

    protected void initView() {
        mDeadStatusView = findViewById(R.id.ftr_dead_statusbg);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mDeadStatusView.setVisibility(GONE);
        }
        vt_tv_title = (TextView) findViewById(R.id.vt_tv_title);
        mDeadStatusView.getLayoutParams().height = CommonUtil.getStatusBarHeight(this);
    }


    protected void setTitleBack(int resource) {
        rl_titleback = (RelativeLayout) findViewById(R.id.rl_titleback);
        rl_titleback.setBackgroundResource(resource);

    }

    protected void setImgLeftVisibility(int visibility) {
        vt_btn_left.setVisibility(visibility);
        vt_btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppManager.getInstance().finishActivity(mClassThis);
                finish();
            }
        });
    }

    protected void setImgRightVisibility(int visibility) {
        vt_img_right.setVisibility(visibility);
    }

    protected void setImgRight(int resource) {
        vt_img_right.setImageResource(resource);
    }

    protected void setImgRightOnClick(View.OnClickListener listener) {
        vt_img_right.setOnClickListener(listener);
    }

    protected void setImgLeft(int resource) {
        vt_btn_left.setBackgroundResource(resource);
    }


    protected void setTvTitleText(String text) {
        if (isNull(vt_tv_title)) return;
        vt_tv_title.setText(text);
    }

    protected boolean isNull(Object object) {
        return object == null;
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

    @Override
    protected void onPause() {
        super.onPause();
        closeKeyboard();
    }


    /**
     * 关闭软键盘
     */
    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
