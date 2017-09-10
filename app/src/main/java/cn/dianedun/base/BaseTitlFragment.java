package cn.dianedun.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;
import cn.dianedun.tools.CommonUtil;
import me.yokeyword.fragmentation.SupportFragment;

import static android.view.View.GONE;


/**
 * Created by Administrator on 2017/8/5.
 */

public abstract class BaseTitlFragment extends SupportFragment {
    protected Context mContext;
    protected Resources mResources;
    protected LayoutInflater mInflater;
    protected ViewGroup mRootView;
    private boolean isOnResumeRegisterBus = false;
    private boolean isOnStartRegisterBus = false;
    private TextView vt_tv_right;

    RelativeLayout rl_titleback;

    ImageView vt_btn_left;

    ImageView vt_img_right;

    TextView mTvTitle;

    View mDeadStatusView;

    public  Bundle savedInstanceState;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        this.mResources = mContext.getResources();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_toolbar_root, container, false);
        View mContentView = inflater.inflate(getLayoutID(), mRootView, false);
        this.savedInstanceState =savedInstanceState;
        rl_titleback = (RelativeLayout) mRootView.findViewById(R.id.rl_titleback);
        vt_btn_left = (ImageView) mRootView.findViewById(R.id.vt_btn_left);
        vt_img_right = (ImageView) mRootView.findViewById(R.id.vt_img_right);
        mTvTitle = (TextView) mRootView.findViewById(R.id.vt_tv_title);
        mDeadStatusView =mRootView.findViewById(R.id.ftr_dead_statusbg);

        ((ViewGroup) mRootView.findViewById(R.id.ftr_fl_container)).addView(mContentView);
        vt_tv_right = (TextView) mRootView.findViewById(R.id.vt_tv_right);
        ButterKnife.bind(this, mRootView);
        initView(mRootView);
        bindEvent();
        initData();
        return mRootView;
    }


    @CallSuper
    protected void bindEvent() {
    }


    protected void setTitleBack(int resource) {
        rl_titleback.setBackgroundResource(resource);
    }

    protected void setImgLeftVisibility(int visibility) {
        vt_btn_left.setVisibility(visibility);
    }

    protected void setImgLeft(int resource) {
        vt_btn_left.setImageResource(resource);
    }

    protected void setImgLeftOnClick(View.OnClickListener listener) {
        vt_btn_left.setOnClickListener(listener);
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


    protected void setTvTitleText(String text) {
        if (isNull(mTvTitle)) return;
        mTvTitle.setText(text);
    }

    /**
     * 布局的LayoutID
     *
     * @return
     */
    abstract protected int getLayoutID();

    abstract protected void initData();


    protected boolean isNull(Object object) {
        return object == null;
    }


    @CallSuper
    protected void initView(View contentView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mDeadStatusView.setVisibility(GONE);
        }
        ViewGroup.LayoutParams params = mDeadStatusView.getLayoutParams();
        params.height = CommonUtil.getStatusBarHeight(_mActivity);
        mDeadStatusView.setLayoutParams(params);
    }

    private Toast mToast;

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(_mActivity, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
