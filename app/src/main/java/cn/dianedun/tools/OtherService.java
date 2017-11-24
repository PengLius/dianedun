package cn.dianedun.tools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONException;
import org.json.JSONObject;

import cn.dianedun.activity.DialogActivity;


/**
 * Created by Administrator on 2017/11/2.
 */

public class OtherService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        startWebSocket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private AsyncHttpClient.WebSocketConnectCallback callback = new AsyncHttpClient.WebSocketConnectCallback() {
        @Override
        public void onCompleted(Exception ex, final WebSocket webSocket) {
            if (ex != null) {
                ex.printStackTrace();
                startWebSocket();
                return;
            }
            webSocket.setClosedCallback(new CompletedCallback() {
                @Override
                public void onCompleted(Exception ex) {
                    startWebSocket();
                }
            });

            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", App.getInstance().getToken() + "");
                webSocket.send(jsonObject + "");// 发送消息的方法
            } catch (JSONException e) {
                e.printStackTrace();
            }

            webSocket.setStringCallback(new WebSocket.StringCallback() {
                public void onStringAvailable(String s) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(s);
                        if (jsonObject1.getString("code").equals("0")) {
                            if (jsonObject1.getString("data").equals("JB")) {
                                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                                startActivity(intent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    public void startWebSocket() {
        AsyncHttpClient.getDefaultInstance().websocket(
//                "ws://47.92.155.108:8081/webSocketServer",
//                "8081",
                "ws://dyd-app.dianedun.cn/webSocketServer",
                "443",
                callback);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
