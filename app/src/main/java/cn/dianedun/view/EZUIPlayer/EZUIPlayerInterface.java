package cn.dianedun.view.EZUIPlayer;

import android.view.View;
import java.util.Calendar;
import java.util.List;

interface EZUIPlayerInterface {
    void setCallBack(EZUIPlayer.EZUIPlayerCallBack var1);

    void setUrl(String var1);

    int getStatus();

    void startPlay();

    void seekPlayback(Calendar var1);

    Calendar getOSDTime();

    void stopPlay();

    void pausePlay();

    void resumePlay();

    void releasePlayer();

    List getPlayList();

    void setSurfaceSize(int var1, int var2);

    void setLoadingView(View var1);
}
