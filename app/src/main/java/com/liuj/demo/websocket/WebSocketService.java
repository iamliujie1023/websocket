package com.liuj.demo.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketService extends Service {

    private String address = "ws://172.17.48.143:8080/app/remote/connect/client_android_liuj";

    private WebSocketClient mWebSocketClient;

    private WSBinder mWSBinder = new WSBinder(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mWSBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("liujie", "WebSocketService onCreate()");

        if (null != mWebSocketClient) {
            mWebSocketClient.close();
        }
        URI uri;
        try {
            uri = new URI(address);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("WebSocketService address is not verfiy");
        }
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception ex) {
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mWebSocketClient.isFlushAndClose()) {
            mWebSocketClient.reconnect();
        } else {
            mWebSocketClient.connect();
        }

        Log.i("liujie", "WebSocketService onStartCommand()");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("liujie", "WebSocketService onDestroy()");
        super.onDestroy();
    }

    public void sendMsg(String msg) {
        if (null == mWebSocketClient) {
            return;
        }
        mWebSocketClient.send(msg);
    }

    public static class WSBinder extends Binder {

        WebSocketService mWebSocketClient;
        public WSBinder(WebSocketService webSocketClient) {
            mWebSocketClient = webSocketClient;
        }

        public void sendMsg(String msg) {
            mWebSocketClient.sendMsg(msg);
        }


        ICallback mCallback;
        public void setLsn(ICallback lsn) {
            this.mCallback = lsn;
        }

        public void dispathReceiveMsg(String str) {
            if (null == mCallback) {
                return;
            }
            mCallback.onReceiveMsg(str);
        }

        public interface ICallback {
            void onReceiveMsg(String str);
        }
    }

}