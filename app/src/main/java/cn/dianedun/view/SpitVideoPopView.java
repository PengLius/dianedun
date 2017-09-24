package cn.dianedun.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.util.ConnectionDetector;

import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import java.util.List;

import cn.dianedun.R;

import cn.dianedun.tools.CommonUtil;
import cn.dianedun.tools.PopupWindowUtil;
import cn.dianedun.tools.ScreenUtils;

import static cn.dianedun.tools.App.getOpenSDK;


/**
 * <p>
 */
public class SpitVideoPopView extends PopupWindow {

    private View mContentView;
    RecyclerView mRecycleView;
    TextView mTv9box;
    TextView mTv4box;
    TextView mTvErr;
    RelativeLayout mRlLoadingLayout;
    RelativeLayout mRlErrLayout;
    LinearLayout mLlVideo;
    ImageView mImgArrow;

    private Context mContext;
    private List<EZDeviceInfo> mDeviceListInfo;


    public SpitVideoPopView(View contentView, int width, int height, boolean focusable){
        super(contentView,width,height,focusable);
        initBaseView(contentView);
        setBackgroundDrawable(new ColorDrawable());
        mContentView = contentView;
        mContext = mContentView.getContext();
    }

    private void initBaseView(View contentView) {
        mRecycleView = (RecyclerView)contentView.findViewById(R.id.vps_recycleview);
        mTv9box = (TextView)contentView.findViewById(R.id.vps_tv_9box);
        mTv4box = (TextView)contentView.findViewById(R.id.vps_tv_4box);
        mTvErr = (TextView)contentView.findViewById(R.id.vps_tv_tip);
        mRlLoadingLayout = (RelativeLayout)contentView.findViewById(R.id.vps_rl_loading);
        mRlErrLayout = (RelativeLayout)contentView.findViewById(R.id.vps_rl_err);
        mImgArrow = (ImageView)contentView.findViewById(R.id.vps_img_arrow);
        mLlVideo = (LinearLayout)contentView.findViewById(R.id.vps_ll_video);
    }

    private void getData() {
        new GetCamersInfoListTask().execute();
    }
    private class GetCamersInfoListTask extends AsyncTask<Void, Void, List<EZDeviceInfo>> {
        private int mErrorCode = 0;

        public GetCamersInfoListTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<EZDeviceInfo> doInBackground(Void... params) {
            if (!SpitVideoPopView.this.isShowing()) {
                return null;
            }

            if (!ConnectionDetector.isNetworkAvailable(mContext)) {
                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
                return null;
            }

            try {
                List<EZDeviceInfo> result = null;
                result = getOpenSDK().getDeviceList(0, 20);
                return result;

            } catch (BaseException e) {
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<EZDeviceInfo> result) {
            super.onPostExecute(result);
            mRlLoadingLayout.setVisibility(View.GONE);
            if (!SpitVideoPopView.this.isShowing()) {
                return;
            }

            if (result != null && result.size() > 0){
                onGetDeviceInfoSucess(result);
            }else{
                mTvErr.setText("暂无设备信息");
                mRlErrLayout.setVisibility(View.VISIBLE);
            }

            if (mErrorCode != 0) {
                onError(mErrorCode);
            }
        }

        protected void onError(int errorCode) {
            switch (errorCode) {
                default:
                    Toast.makeText(mContext,"获取摄像头设备列表失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void onGetDeviceInfoSucess(List<EZDeviceInfo> ezDeviceInfoList) {
        mDeviceListInfo = ezDeviceInfoList;
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        mDeviceListInfo.add(mDeviceListInfo.get(0));
        initView();
    }

    private void initView() {
        mLlVideo.setVisibility(View.VISIBLE);
        mRecycleView.setLayoutManager(new GridLayoutManager(mContext,4));
        mRecycleView.setAdapter(new CommonAdapter<EZDeviceInfo>(mContentView.getContext(),R.layout.item_spitvideo_entity,mDeviceListInfo) {
            @Override
            protected void convert(ViewHolder holder, EZDeviceInfo ezDeviceInfo, final int position) {
                int pos  = position + 1;
                holder.setText(R.id.ise_tv_videoname, pos + "号机");
                holder.setOnClickListener(R.id.ise_ll_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnVideoSelect!=null)
                            mOnVideoSelect.onVideoSelect(position);
                    }
                });
            }
        });
        mTv4box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVideoSelect!=null)
                    mOnVideoSelect.onVideoSelect(-1);
            }
        });
        mTv9box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVideoSelect!=null)
                    mOnVideoSelect.onVideoSelect(-2);
            }
        });

    }

    private OnVideoSelect mOnVideoSelect;

    public void setOnVideoSelect(OnVideoSelect onVideoSelect) {
        this.mOnVideoSelect = onVideoSelect;
    }

    public interface OnVideoSelect {
        //-2 9分屏/-1 4分屏/大于0选中
        void onVideoSelect(int tag);
    }

    private void autoAdjustArrowPos() {

        mContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = mContentView.getMeasuredHeight();
        final int anchorLoc[] = new int[2];
        final int anchorHeight = mAnchorView.getHeight();
        mAnchorView.getLocationOnScreen(anchorLoc);
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(mContext);
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        int d20p = CommonUtil.dip2px(mContentView.getContext(),20);
        if (isNeedShowUp){
            //在底部
            LinearLayout.LayoutParams arrowParams = (LinearLayout.LayoutParams) mImgArrow.getLayoutParams();
            arrowParams.topMargin = windowHeight - d20p;
        }else{
            //在顶部
            LinearLayout.LayoutParams arrowParams = (LinearLayout.LayoutParams) mImgArrow.getLayoutParams();
            arrowParams.topMargin = d20p;
        }


    }

    private View mAnchorView;
    public void showPopupWindow(View anchorView) {
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(anchorView, mContentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos[0] -= xOff;
        showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
        mAnchorView = anchorView;
        autoAdjustArrowPos();
        getData();
    }

}
