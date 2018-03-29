package cn.dianedun.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezvizuikit.open.EZUIKit;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.util.ConnectionDetector;

import com.videogo.util.LogUtil;
import com.vise.xsnow.net.api.ViseApi;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import cn.dianedun.R;
import cn.dianedun.activity.LoginActivity;
import cn.dianedun.activity.VideoShowActivity;
import cn.dianedun.bean.AccesstokenBean;
import cn.dianedun.bean.CarmerListBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.CommonUtil;
import cn.dianedun.tools.PopupWindowUtil;
import cn.dianedun.tools.ScreenUtils;

import static cn.dianedun.tools.App.AppKey;
import static cn.dianedun.tools.App.getOpenSDK;


/**
 * <p>
 */
public class _SpitVideoPopView extends PopupWindow {

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
    private String mPlacesId;

    public _SpitVideoPopView(View contentView, int width, int height, boolean focusable,String placesId){
        super(contentView,width,height,focusable);
        initBaseView(contentView);
        setBackgroundDrawable(new ColorDrawable());
        mContentView = contentView;
        mContext = mContentView.getContext();
        mPlacesId = placesId;
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
        ViseApi api = new ViseApi.Builder(mContext).build();
        HashMap hashMap = new HashMap();
        hashMap.put("departId",mPlacesId);
        api.apiPost(AppConfig.GETACCESSTOKEN, App.getInstance().getToken(),hashMap,false,new ApiCallback<AccesstokenBean.DataBean>(){
            @Override
            public void onNext(AccesstokenBean.DataBean dataBean) {
                //设置授权accesstoken
//                EZUIKit.setAccessToken(dataBean.getAccessToken());
//                App.getOpenSDK().setAccessToken(dataBean.getAccessToken());
//                new GetCamersInfoListTask().execute();
                getCarmerList(dataBean.getAccessToken());
            }

            @Override
            public void onError(ApiException e) {
                if(e.getCode() == 2001)
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                Toast.makeText(mContext,"暂无设备信息",Toast.LENGTH_SHORT).show();
                mTvErr.setText("暂无设备信息");
                mRlLoadingLayout.setVisibility(View.GONE);
                mRlErrLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }
        });
    }

    private void getCarmerList(String token){
//        ViseApi api = new ViseApi.Builder(mContext).build();
//        HashMap hashMap = new HashMap();
//        hashMap.put("accessToken",token);
//        hashMap.put("pageStart",0);
//        hashMap.put("pageSize",20);
//        api.apiPost(AppConfig.GET_CARMERLIST,App.getInstance().getToken(),hashMap,false,new ApiCallback<CarmerListBean.DataBean>(){
//            @Override
//            public void onError(ApiException e) {
//                Toast.makeText(mContext,"暂无设备信息",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onNext(CarmerListBean.DataBean dataBean) {
//                mRlLoadingLayout.setVisibility(View.GONE);
//                if (!SpitVideoPopView.this.isShowing()) {
//                    return;
//                }
//
//                if (dataBean.getResult() != null && dataBean.getResult().size() > 0){
//                    onGetDeviceInfoSucess(dataBean.getResult());
//                }else{
//                    mTvErr.setText("暂无设备信息");
//                    mRlErrLayout.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//        });
        EZUIKit.initWithAppKey(App.getInstance(),AppKey);
        //设置授权accesstoken
        EZUIKit.setAccessToken(token);
        App.getOpenSDK().setAccessToken(token);
        new GetCamersInfoListTask().execute();
    }

//    private class GetCamersInfoListTask extends AsyncTask<Void, Void, List<EZDeviceInfo>> {
//        private int mErrorCode = 0;
//
//        public GetCamersInfoListTask() {
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<EZDeviceInfo> doInBackground(Void... params) {
//            if (!SpitVideoPopView.this.isShowing()) {
//                return null;
//            }
//
//            if (!ConnectionDetector.isNetworkAvailable(mContext)) {
//                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
//                return null;
//            }
//
//            try {
//                List<EZDeviceInfo> result = null;
//                result = getOpenSDK().getDeviceList(0, 20);
//                return result;
//
//            } catch (BaseException e) {
//                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
//                mErrorCode = errorInfo.errorCode;
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(List<EZDeviceInfo> result) {
//            super.onPostExecute(result);
//            mRlLoadingLayout.setVisibility(View.GONE);
//            if (!SpitVideoPopView.this.isShowing()) {
//                return;
//            }
//
//            if (result != null && result.size() > 0){
//                onGetDeviceInfoSucess(result);
//            }else{
//                mTvErr.setText("暂无设备信息");
//                mRlErrLayout.setVisibility(View.VISIBLE);
//            }
//
//            if (mErrorCode != 0) {
//                onError(mErrorCode);
//            }
//        }
//
//        protected void onError(int errorCode) {
//            switch (errorCode) {
//                default:
//                    Toast.makeText(mContext,"获取摄像头设备列表失败",Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }

    /**
     * 获取摄像头列表
     */
    List<EZDeviceInfo> mEzDeviceInfo = null;
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
            if (!isShowing()) {
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
                LogUtil.debugLog("spitvideopopview", errorInfo.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<EZDeviceInfo> result) {
            super.onPostExecute(result);
            if (!isShowing()) {
                return ;
            }

            mEzDeviceInfo = result;

            if (mEzDeviceInfo != null && mEzDeviceInfo.size() > 0){
                mRlLoadingLayout.setVisibility(View.GONE);
                if (!_SpitVideoPopView.this.isShowing()) {
                    return;
                }

                if (result.size() > 0){
                    onGetDeviceInfoSucess(result);
                }else{
                    mTvErr.setText("暂无设备信息");
                    mRlErrLayout.setVisibility(View.VISIBLE);
                }
            }else{
                Toast.makeText(mContext,"暂无设备信息",Toast.LENGTH_SHORT).show();
            }

            if (mErrorCode != 0) {
                onError(mErrorCode);
            }
        }

        protected void onError(int errorCode) {
            switch (errorCode) {
                case ErrorCode.ERROR_WEB_SESSION_ERROR:
                case ErrorCode.ERROR_WEB_SESSION_EXPIRE:
                    Toast.makeText(mContext,"已过期，请重新登录",Toast.LENGTH_SHORT).show();
//                    showToast("已过期，请重新登录");
//                    startActivity(new Intent(VideoShowActivity.this,LoginActivity.class));
                    break;
                default:
                    Toast.makeText(mContext,"获取摄像头设备列表失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    private void onGetDeviceInfoSucess(List<EZDeviceInfo> result) {
        mDeviceListInfo = result;
        initView();
    }

    private void initView() {
        mLlVideo.setVisibility(View.VISIBLE);
        mRecycleView.setLayoutManager(new GridLayoutManager(mContext,4));
        mRecycleView.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(mContext,1)));
        mRecycleView.setAdapter(new CommonAdapter<EZDeviceInfo>(mContentView.getContext(),R.layout.item_spitvideo_entity,mDeviceListInfo) {
            @Override
            protected void convert(ViewHolder holder, EZDeviceInfo ezDeviceInfo, final int position) {
                final int pos  = position + 1;
                holder.setText(R.id.ise_tv_videoname, pos + "号机");
                if (ezDeviceInfo.getStatus() == 1){
                    holder.setImageResource(R.id.ise_img_tag,R.mipmap.ic_nor_jiwei);
                }else{
                    holder.setImageResource(R.id.ise_img_tag,R.color.black);
                }
                holder.setOnClickListener(R.id.ise_ll_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnVideoSelect!=null)
                            mOnVideoSelect.onVideoSelect(position,position);
                        dismiss();
                    }
                });
            }
        });
        mTv4box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVideoSelect!=null)
                    mOnVideoSelect.onVideoSelect(-1,0);
                dismiss();
            }
        });
        mTv9box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVideoSelect!=null)
                    mOnVideoSelect.onVideoSelect(-2,0);
                dismiss();
            }
        });

    }

    private OnVideoSelect mOnVideoSelect;

    public void setOnVideoSelect(OnVideoSelect onVideoSelect) {
        this.mOnVideoSelect = onVideoSelect;
    }

    public interface OnVideoSelect {
        //-2 9分屏/-1 4分屏/大于0选中
        void onVideoSelect(int tag,int pos);
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
