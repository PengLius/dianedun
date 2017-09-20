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
import cn.dianedun.bean.DepartPlacesListBean;
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

    private DepartPlacesListBean.DataBean mDataBean;

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
        api.apiPost(AppConfig.GETDEPARTPLACES,App.getInstance().getToken(),new HashMap(),false,new ApiCallback<DepartPlacesListBean.DataBean>(){
            @Override
            public void onStart() {

            }

            @Override
            public void onNext(DepartPlacesListBean.DataBean dataBean){
                mRlLoadFaild.setVisibility(GONE);
                mDataBean = dataBean;
//                App.getOpenSDK().setAccessToken(mTokenBean.getAccessToken());

                if (headOrLoadMore == null) {
                    //首次刷新
                    if (dataBean.getResult().size() == 0){
                        //没有数据
                        mRlNodata.setVisibility(View.VISIBLE);
                    }else{
                        mRlNodata.setVisibility(View.GONE);
                        addCameraList(dataBean.getResult());
                    }
                }else{
                    if (headOrLoadMore){
                        //下拉
                        mRefreshLayout.finishRefresh();
                        if (dataBean.getResult().size() == 0){
                            mRlNodata.setVisibility(View.VISIBLE);
                        }else{
                            mRlNodata.setVisibility(View.GONE);
                            mCameraAdapter.setDatas(dataBean.getResult());
                            mCameraAdapter.notifyDataSetChanged();
                        }

                    }else{
                        //上拉
                        mRefreshLayout.finishLoadmore();
                        if (dataBean.getResult().size() == 0){
                            mRefreshLayout.setLoadmoreFinished(true);
                        }else{
                            mCameraAdapter.addDatas(dataBean.getResult());
                            mCameraAdapter.notifyDataSetChanged();
                        }
                    }
                }

//                if (mErrorCode != 0) {
//                    onError(mErrorCode);
//                }else{
//                    mRlLoadFaild.setVisibility(GONE);
//                }
//                new GetCamersInfoListTask(headOrLoadMore).execute();
            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                if (headOrLoadMore!= null){
                    if (headOrLoadMore)
                        mRefreshLayout.finishRefresh();
                    else if (headOrLoadMore == false)
                        mRefreshLayout.finishLoadmore();
                }
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


    private CommonAdapter mCameraAdapter;
    private void addCameraList(List<DepartPlacesListBean.DataBean.ResultBean> result) {
        if (mCameraAdapter == null){
            mInitLoad = true;
            mCameraAdapter = new CommonAdapter<DepartPlacesListBean.DataBean.ResultBean>(_mActivity,R.layout.item_cameralist,result) {
                @Override
                protected void convert(ViewHolder holder, final DepartPlacesListBean.DataBean.ResultBean bean, final int position) {
//                    holder.setText(R.id.ic_tv_camerastatus,bean.getStatus() == 1 ? "在线" : "离线");
                    holder.setText(R.id.ic_tv_places,bean.getAddress() + bean.getDepartname());
//                    holder.setText(R.id.ic_tv_recenttime,bean.getDeviceCover());
                    holder.setOnClickListener(R.id.ia_ll_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            final EZDeviceInfo deviceInfo = getDatas().get(position);
//                            if (deviceInfo.getCameraNum() <= 0 || deviceInfo.getCameraInfoList() == null || deviceInfo.getCameraInfoList().size() <= 0) {
//                                LogUtil.d(TAG, "cameralist is null or cameralist size is 0");
//                                return;
//                            }
//                            if (deviceInfo.getCameraNum() == 1 && deviceInfo.getCameraInfoList() != null && deviceInfo.getCameraInfoList().size() == 1) {
//                                LogUtil.d(TAG, "cameralist have one camera");
//                                final EZCameraInfo cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo, 0);
//                                if (cameraInfo == null) {
//                                    return;
//                                }
//
//                                Intent intent = new Intent(_mActivity, VideoShowActivity.class);
//                                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                                intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo);
//                                startActivity(intent);
//                                return;
//                            }
                            Intent intent = new Intent(_mActivity, VideoShowActivity.class);
                            intent.putExtra("id", bean.getId());
                            startActivity(intent);
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
