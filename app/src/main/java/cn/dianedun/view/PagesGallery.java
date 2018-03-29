/**  
 * MyGallery.java
 * @version 1.0
 * @author Haven
 * @createTime 2011-12-9 下午03:42:53
 * android.widget.Gallery的子函数。此类很重要。建议仔细看
 */
package cn.dianedun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Gallery;

public class PagesGallery extends Gallery {
    private static final String TAG = "PagesGallery";
    
    private boolean setSelection = false;

    private boolean mSwitch = true;

    public PagesGallery(Context context) {
        super(context);

    }

    public PagesGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PagesGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwitch(boolean bswitch) {
        this.mSwitch = bswitch;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mSwitch) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mSwitch) {
            return false;
        }

        if(setSelection) {
            setSelection = false;
            return false;
        }
        if (distanceX > 50 && this.getSelectedItemPosition() == this.getCount() - 1 && this.getCount() > 1)// 向左滑动
        {
            this.setSelection(0);
            setSelection = true;
        } else if (distanceX < -50 && this.getSelectedItemPosition() == 0 && this.getCount() > 1)// 向右滑动
        {
            this.setSelection(this.getCount() - 1);
            setSelection = true;
        } else {
            super.onScroll(e1, e2, distanceX * 2, distanceY);
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
