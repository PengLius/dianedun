package cn.dianedun.tools;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/9/5.
 */

public class GsonUtil {

    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }
}
