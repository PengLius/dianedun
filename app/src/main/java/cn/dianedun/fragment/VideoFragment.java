package cn.dianedun.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.videogo.constant.IntentConsts;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.api.ViseApi;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.LoginActivity;
import cn.dianedun.activity.VideoShowActivity;
import cn.dianedun.base.BaseFragment;
import cn.dianedun.base.BaseTitlFragment;
import cn.dianedun.bean.AccesstokenBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.tools.EZUtils;
import rx.Observable;

import static android.view.View.GONE;
import static cn.dianedun.tools.App.getOpenSDK;

/**
 * Created by Administrator on 2017/8/3.
 */

public class VideoFragment extends BaseTitlFragment {

    protected static final String TAG = "VideoFragment";

    @Bind(R.id.fv_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.fv_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.fv_rl_loadfaild)
    RelativeLayout mRlLoadFaild;

    @Bind(R.id.fv_rl_nodata)
    RelativeLayout mRlNodata;

    public static VideoFragment getInstance() {
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setTvTitleText("视频");
        setTitleBack(R.mipmap.home_backg_null);
        initRefreshLayout();
    }

    private AccesstokenBean.DataBean mTokenBean;

    @Override
    protected void initData() {
        getData(mRefreshOrLoadMore);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (mInitLoad) {
            mRefreshLayout.autoRefresh();
        }
    }

    private Boolean mRefreshOrLoadMore = null;
    private void getData(final Boolean headOrLoadMore){
        ViseApi api = new ViseApi.Builder(_mActivity).build();
        HashMap hashMap = new HashMap();
        hashMap.put("departId","admin");
        api.apiPost(AppConfig.GETACCESSTOKEN,hashMap,false,new ApiCallback<AccesstokenBean.DataBean>(){
            @Override
            public void onStart() {

            }

            @Override
            public void onNext(AccesstokenBean.DataBean accesstokenBean){
                mRlLoadFaild.setVisibility(GONE);
                mTokenBean = accesstokenBean;
                App.getOpenSDK().setAccessToken(mTokenBean.getAccessToken());
                new GetCamersInfoListTask(headOrLoadMore).execute();
            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                mRlLoadFaild.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    boolean mInitLoad = false;

    private void initRefreshLayout(){
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefreshOrLoadMore = true;
                getData(mRefreshOrLoadMore);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mRefreshOrLoadMore = false;
                getData(mRefreshOrLoadMore);
            }
        });
    }

    /**
     * 获取事件消息任务
     */
    private class GetCamersInfoListTask extends AsyncTask<Void, Void, List<EZDeviceInfo>> {
        private Boolean mHeaderOrFooter;
        private int mErrorCode = 0;

        public GetCamersInfoListTask(Boolean headerOrFooter) {
            mHeaderOrFooter = headerOrFooter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<EZDeviceInfo> doInBackground(Void... params) {
            if (_mActivity.isFinishing()) {
                return null;
            }

            if (!ConnectionDetector.isNetworkAvailable(_mActivity)) {
                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
                return null;
            }

            try {
                List<EZDeviceInfo> result = null;
                if (mHeaderOrFooter == null || mHeaderOrFooter) {
                    result = getOpenSDK().getDeviceList(0, 20);
                } else {
                    result = getOpenSDK().getDeviceList((mCameraAdapter.getItemCount() / 20)+(mCameraAdapter.getItemCount() % 20>0?1:0), 20);
                }
                return result;

            } catch (BaseException e) {
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                LogUtil.debugLog(TAG, errorInfo.toString());

                return null;
            }
        }

        @Override
        protected void onPostExecute(List<EZDeviceInfo> result) {
            super.onPostExecute(result);
            if (_mActivity.isFinishing()) {
                return;
            }

            if (result != null) {
                if (mHeaderOrFooter == null) {
                    //首次刷新
                    if (result.size() == 0){
                        //没有数据
                        mRlNodata.setVisibility(View.VISIBLE);
                    }else{
                        mRlNodata.setVisibility(View.GONE);
                        addCameraList(result);
                    }
                }else{
                    if (mHeaderOrFooter){
                        //下拉
                        mRefreshLayout.finishRefresh();
                        if (result.size() == 0){
                            mRlNodata.setVisibility(View.VISIBLE);
                        }else{
                            mRlNodata.setVisibility(View.GONE);
                            mCameraAdapter.setDatas(result);
                            mCameraAdapter.notifyDataSetChanged();
                        }

                    }else{
                        //上拉
                        mRefreshLayout.finishLoadmore();
                        if (result.size() == 0){
                            mRefreshLayout.setLoadmoreFinished(true);
                        }else{
                            mCameraAdapter.addDatas(result);
                            mCameraAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            if (mErrorCode != 0) {
                onError(mErrorCode);
            }else{
                mRlLoadFaild.setVisibility(GONE);
            }
        }

        protected void onError(int errorCode) {
            switch (errorCode) {
                case ErrorCode.ERROR_WEB_SESSION_ERROR:
                case ErrorCode.ERROR_WEB_SESSION_EXPIRE:
                    showToast("已过期，请重新登录");
                    startActivity(new Intent(_mActivity,LoginActivity.class));
                    break;
                default:
                    if (mCameraAdapter !=null && mCameraAdapter.getItemCount() == 0) {
                        mRlLoadFaild.setVisibility(View.VISIBLE);
                    } else {
                        showToast("获取设备列表失败");
                    }
                    if (mHeaderOrFooter){
                        mRefreshLayout.finishRefresh();
                    }else{
                        mRefreshLayout.finishLoadmore();
                    }
                    break;
            }
        }
    }

    private CommonAdapter mCameraAdapter;
    private void addCameraList(List<EZDeviceInfo> result) {
        if (mCameraAdapter == null){
            mInitLoad = true;
            mCameraAdapter = new CommonAdapter<EZDeviceInfo>(_mActivity,R.layout.item_cameralist,result) {
                @Override
                protected void convert(ViewHolder holder, EZDeviceInfo bean, final int position) {
                    holder.setText(R.id.ic_tv_camerastatus,bean.getStatus() == 1 ? "在线" : "离线");
                    holder.setText(R.id.ic_tv_places,bean.getDeviceName());
                    holder.setText(R.id.ic_tv_recenttime,bean.getDeviceCover());
                    holder.setOnClickListener(R.id.ia_ll_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final EZDeviceInfo deviceInfo = getDatas().get(position);
                            if (deviceInfo.getCameraNum() <= 0 || deviceInfo.getCameraInfoList() == null || deviceInfo.getCameraInfoList().size() <= 0) {
                                LogUtil.d(TAG, "cameralist is null or cameralist size is 0");
                                return;
                            }
                            if (deviceInfo.getCameraNum() == 1 && deviceInfo.getCameraInfoList() != null && deviceInfo.getCameraInfoList().size() == 1) {
                                LogUtil.d(TAG, "cameralist have one camera");
                                final EZCameraInfo cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo, 0);
                                if (cameraInfo == null) {
                                    return;
                                }

                                Intent intent = new Intent(_mActivity, VideoShowActivity.class);
                                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
                                intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo);
                                startActivity(intent);
                                return;
                            }

                        }
                    });
                }
            };
            mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
            mRecycleView.setAdapter(mCameraAdapter);
        }else{
            mCameraAdapter.addDatas(result);
            mCameraAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRlLoadFaild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRlLoadFaild.setVisibility(GONE);
                mRefreshLayout.autoRefresh();
//                getData(mRefreshOrLoadMore);
            }
        });
    }

}
