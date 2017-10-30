package cn.dianedun.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;


import org.json.JSONException;
import org.json.JSONObject;

import cn.dianedun.activity.MessageActivity;
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
                Intent i = new Intent(context, MessageActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
