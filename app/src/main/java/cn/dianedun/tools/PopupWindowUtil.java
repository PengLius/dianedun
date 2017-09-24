package cn.dianedun.tools;

import android.view.View;
public class PopupWindowUtil {
    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        int d20p = CommonUtil.dip2px(contentView.getContext(),20);
        if (isNeedShowUp) {
            windowPos[0] = anchorLoc[0] + anchorView.getWidth() + d20p;
            windowPos[1] = anchorLoc[1] + anchorView.getHeight() - windowHeight;
        } else {
            windowPos[0] = anchorLoc[0] + anchorView.getWidth() + d20p;
            windowPos[1] = anchorLoc[1] - anchorView.getHeight() - d20p + anchorHeight;
        }
        return windowPos;
    }
}
