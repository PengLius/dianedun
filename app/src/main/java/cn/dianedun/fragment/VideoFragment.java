package cn.dianedun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.vise.xsnow.net.api.ViseApi;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.activity.VideoPlayActivity;
import cn.dianedun.activity.VideoShowActivity;
import cn.dianedun.base.BaseTitlFragment;
import cn.dianedun.bean.DepartPlacesListBean;
import cn.dianedun.tools.App;
import cn.dianedun.tools.AppConfig;
import cn.dianedun.view.SpitVideoPopView;

import static android.view.View.GONE;

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
        api.apiPost(AppConfig.GETDEPARTPLACES, App.getInstance().getToken(),new HashMap(),false,new ApiCallback<DepartPlacesListBean.DataBean>(){
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
                            if (mCameraAdapter!=null) {
                                mCameraAdapter.setDatas(dataBean.getResult());
                                mCameraAdapter.notifyDataSetChanged();
                            }else{
                                addCameraList(dataBean.getResult());
                            }
                        }

                    }else{
                        //上拉
                        mRefreshLayout.finishLoadmore();
                        if (dataBean.getResult().size() == 0){
                            mRefreshLayout.setLoadmoreFinished(true);
                        }else{
                            addCameraList(dataBean.getResult());
                        }
                    }
                }
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
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefreshOrLoadMore = true;
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
                    if (bean.getCameraCount().equals("0")){
                        holder.setText(R.id.ic_tv_camerastatus,"无");
                        holder.setOnClickListener(R.id.ia_ll_container, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               showToast("暂无设备信息");
                            }
                        });
                    }else{
                        holder.setText(R.id.ic_tv_camerastatus,bean.getCameraCount()+"台");
                        holder.setOnClickListener(R.id.ia_ll_container, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(_mActivity, VideoPlayActivity.class);
                                intent.putExtra("id", bean.getId());
                                intent.putExtra("place",bean.getDepartname());
                                intent.putExtra("pos", 0);
                                startActivity(intent);
                            }
                        });
                    }
                    holder.setOnClickListener(R.id.ic_img_spitVideo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpitVideoPopView spitVideoPopView = new SpitVideoPopView(LayoutInflater.from(_mActivity).inflate(R.layout.view_pop_spitvideo,null),
                                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true,bean.getId());
                            spitVideoPopView.setOnVideoSelect(new SpitVideoPopView.OnVideoSelect() {
                                @Override
                                public void onVideoSelect(int tag,int pos) {
                                    Intent intent = new Intent(_mActivity, VideoPlayActivity.class);
                                    intent.putExtra("id", bean.getId());
                                    intent.putExtra("place",bean.getDepartname());
                                    intent.putExtra("spit", tag);
                                    intent.putExtra("pos",pos);
                                    startActivity(intent);
                                }
                            });
                            spitVideoPopView.showPopupWindow(v);
                        }
                    });
                    holder.setText(R.id.ic_tv_recenttime,bean.getAddress());
                    holder.setText(R.id.ic_tv_places,bean.getDepartname());
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
            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("视频");
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("视频");
    }

}
