package cn.dianedun.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ezvizuikit.open.EZUIError;
import com.videogo.openapi.bean.EZDeviceInfo;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import cn.dianedun.R;
import cn.dianedun.view.EZUIPlayer.EZUIPlayer;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by Administrator on 2017/9/22.
 */

public class SpitVideoFragment extends SupportFragment {

    private List<EZDeviceInfo> mEZDeviceInfoList;

    public static SpitVideoFragment getInstance() {
        SpitVideoFragment fragment = new SpitVideoFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Bind(R.id.fs_videocontainer)
    LinearLayout mLlVideoContainer;

    @Bind(R.id.fs_ll_rootview)
    LinearLayout mLLRootView;

    private int mType = -1; //0-4box 1-9box
    private static final int BOX_4 = 0;
    private static final int BOX_9 = 1;


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        startPlay();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlay();
        mSpitPlayerArr.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        mType = getArguments().getInt("type");
        if (mType == BOX_4)
            return inflater.inflate(R.layout.fragment_spitvideo_4box,container,false);
        else
            return inflater.inflate(R.layout.fragment_spitvideo_9box,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView((view));
    }

    private boolean mAutoPlay = false;
    protected void initView(View contentView) {

        mEZDeviceInfoList = getArguments().getParcelableArrayList("device");
        mAutoPlay = getArguments().getBoolean("auto");

        if (mType == BOX_4){
            for (int i=0;i < mEZDeviceInfoList.size(); i++){
                switch (i){
                    case 0:
                        buildChildPlayer(0,R.id.fs_rl_ezuiplayer_1,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 1:
                        buildChildPlayer(1,R.id.fs_rl_ezuiplayer_2,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 2:
                        buildChildPlayer(2,R.id.fs_rl_ezuiplayer_3,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 3:
                        buildChildPlayer(3,R.id.fs_rl_ezuiplayer_4,contentView,mEZDeviceInfoList.get(i));
                        break;
                }
            }
        }else{
            for (int i=0;i < mEZDeviceInfoList.size(); i++){
                switch (i){
                    case 0:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_1,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 1:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_2,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 2:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_3,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 3:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_4,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 4:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_5,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 5:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_6,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 6:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_7,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 7:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_8,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 8:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_9,contentView,mEZDeviceInfoList.get(i));
                        break;
                }
            }
        }
    }

    public void startPlay(){
        if (!mIsPlay){
            mIsPlay = true;
//            View view = mLLRootView.findViewById(R.id.fs_videocontainer);
//            if (view == null)
//                mLLRootView.addView(mLlVideoContainer);
            for (int i=0; i<mSpitPlayerArr.size(); i++){
                mSpitPlayerArr.get(i).startPlay();
            }
        }
    }

    private boolean mIsPlay = false;
    public void stopPlay(){
        if (mIsPlay){
            mIsPlay = false;
//            mLLRootView.removeView(mLlVideoContainer);
            for (int i=0; i<mSpitPlayerArr.size(); i++){
                if (mSpitPlayerArr.get(i)!=null)
                    mSpitPlayerArr.get(i).stopPlay();
            }
        }
    }

    public void releasePlay(){
        for (int i=0; i<mSpitPlayerArr.size(); i++){
            EZUIPlayer ezuiPlayer = mSpitPlayerArr.get(i);
            if (ezuiPlayer!=null) {
                ezuiPlayer.releasePlayer();
                mSpitPlayerArr.put(i,null);
            }
        }
    }

    private SparseArray<EZUIPlayer> mSpitPlayerArr = new SparseArray<>();
    private void buildChildPlayer(int key,int parentId,View contentView,EZDeviceInfo deviceInfo){
        RelativeLayout rlVideoContainer = (RelativeLayout)contentView.findViewById(parentId);
        final EZUIPlayer ezuiPlayer = new EZUIPlayer(_mActivity);
//        ezuiPlayer.setSurfaceSize(mSpitLayoutWidth/mSpitTagCount, mSpitLayoutHeight/mSpitTagCount);
        ezuiPlayer.setOpenSound(false);
        ezuiPlayer.setAutoPlay(mAutoPlay);
        ezuiPlayer.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {
            @Override
            public void onPlaySuccess() {
                Log.e("ezuiplayer","onPlaySuccess");
//                            ezUIPlayer.setZOrderOnTop(true);
            }

            @Override
            public void onPlayFail(EZUIError ezuiError) {
                if (ezuiError.getErrorString().equals(EZUIError.UE_ERROR_INNER_VERIFYCODE_ERROR)){

                }else if(ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_NOT_FOUND_RECORD_FILES)){
                    // TODO: 2017/5/12
                    //未发现录像文件
                }
            }

            @Override
            public void onVideoSizeChange(int width, int height) {
            }

            @Override
            public void onPrepared() {
//                ezuiPlayer.startPlay();
            }

            @Override
            public void onPlayTime(Calendar calendar) {

            }

            @Override
            public void onPlayFinish() {
                Log.e("ezuiplayer","onPlayFinish");
            }
        });
        String ezopenUrl = "ezopen://open.ys7.com/" + deviceInfo.getDeviceSerial() + "/1.live";
        rlVideoContainer.addView(ezuiPlayer);

        ezuiPlayer.setUrl(ezopenUrl);
        mSpitPlayerArr.put(key,ezuiPlayer);

    }
}

