package cn.dianedun.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/1/14.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            try {
                String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject jsonObject = new JSONObject(jsonStr);
                String type = jsonObject.getString("type");
                if (type.equals("notice")) {
//                    Intent i = new Intent(context, InformationActivity.class);
//                    i.putExtra("name", "停电公告");
//                    i.putExtra("uri", AppConfig.DIANCUT);
//                    i.putExtra("type", "message");
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
                } else if (type.equals("message")) {
//                    Intent i = new Intent(context, InformationActivity.class);
//                    i.putExtra("name", "我的消息");
//                    i.putExtra("uri", AppConfig.MYMSG);
//                    i.putExtra("type", "message");
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
