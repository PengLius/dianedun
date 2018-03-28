package cn.dianedun.tools;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/25.
 */

public class ActivityUtils {

    private static ActivityUtils activityUtils;
    private Map<String, Activity> activityMap = new HashMap<String, Activity>();

    public static ActivityUtils getInstance() {
        if (activityUtils == null) {
            activityUtils = new ActivityUtils();

        }
        return activityUtils;
    }

    /**
     * 保存指定key值的activity（activity启动时调用）
     *
     * @param key
     * @param activity
     */
    public void addActivity(String key, Activity activity) {
        if (activityMap.get(key) == null) {
            activityMap.put(key, activity);
        }
    }

    /**
     * 移除指定key值的activity （activity关闭时调用）
     *
     * @param key
     */
    public void delActivity(String key) {
        activityMap.remove(key);
    }

    public void closeActivity(String key) {
        Activity activity = activityMap.get(key);
        if (activity != null) {
            activity.finish();
        }
    }
}
