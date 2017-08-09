package cn.dianedun.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.dianedun.R;

/**
 * Created by Administrator on 2017/8/8.
 */

public class GaoYaFragment extends Fragment {

    View view;
    String result;

    public GaoYaFragment(String result) {
        this.result = result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gaoya, null);
        Log.e("result", result);
        return view;
    }

}
