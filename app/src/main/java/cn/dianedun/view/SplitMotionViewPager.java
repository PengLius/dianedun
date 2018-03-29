package cn.dianedun.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/3/16.
 */

public class SplitMotionViewPager extends ViewPager {
    public SplitMotionViewPager(Context context) {
        super(context);
    }

    public SplitMotionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1){
            return false;
        }
        try {
            return super.onInterceptTouchEvent(ev);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1){
            return true;
        }
        try {
            return super.onTouchEvent(ev);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
