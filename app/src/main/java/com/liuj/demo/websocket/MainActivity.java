package com.liuj.demo.websocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private String address = "ws://172.17.48.143:8080/app/remote/connect/client_android_liuj";
    private URI uri;
    private static final String TAG = "JavaWebSocket";

    WebSocketClient mWebSocketClient;

    Button mTvCon, mTvSend, mTvClosed;

    private boolean mIsFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvCon = findViewById(R.id.tv_con);
        mTvCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, " isFlushAndClose = " + mWebSocketClient.isFlushAndClose());
                Log.i(TAG, " isOpen = " + mWebSocketClient.isOpen());
                Log.i(TAG, " isClosing = " + mWebSocketClient.isClosing());
                Log.i(TAG, " isClosed = " + mWebSocketClient.isClosed());

                if(mWebSocketClient.isOpen() || mWebSocketClient.isClosing()) {
                    return;
                }
                if(mIsFirst) {
                    mIsFirst = false;
                    mWebSocketClient.connect();
                } else {
                    mWebSocketClient.reconnect();
                }
            }
        });

        mTvClosed = findViewById(R.id.tv_close);
        mTvClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWebSocketClient.isClosing() || mWebSocketClient.isClosed()){
                    return;
                }
                mWebSocketClient.close();

                Log.i(TAG, " isFlushAndClose = " + mWebSocketClient.isFlushAndClose());
                Log.i(TAG, " isOpen = " + mWebSocketClient.isOpen());
                Log.i(TAG, " isClosing = " + mWebSocketClient.isClosing());
                Log.i(TAG, " isClosed = " + mWebSocketClient.isClosed());
            }
        });

        mTvSend = findViewById(R.id.tv_send);
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mWebSocketClient && !mWebSocketClient.isOpen()) {
                    return;
                }
                send("{\n" +
                        "    \"action\":\"client_info\",\n" +
                        "    \"data\":{\n" +
                        "        \"bd\":\"android\",\n" +
                        "        \"abd\":\"01d2ac9158\",\n" +
                        "        \"package\":\"show\",\n" +
                        "        \"os\":\"8.0.0\",\n" +
                        "        \"screen\":\"1080x1920\",\n" +
                        "        \"dn\":\"STF-AL10\",\n" +
                        "        \"version\":\"7.18.00\",\n" +
                        "        \"platform\":\"Android\",\n" +
                        "        \"network\":\"WiFi\",\n" +
                        "        \"app_name\":\"beibei\",\n" +
                        "        \"imei\":\"867666033130006\",\n" +
                        "        \"model\":\"STF-AL10\",\n" +
                        "        \"udid\":\"1085bdaf00f366c0\"\n" +
                        "    }\n" +
                        "}");
            }
        });

        initSockect();
    }

    public void initSockect() {
        try {
            uri = new URI(address);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (null == mWebSocketClient) {
            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.i(TAG, "onOpen: ");
                }

                @Override
                public void onMessage(String s) {
                    Log.i(TAG, "onMessage: " + s);

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.i(TAG, "onClose: s=" + s + ",b= " + b);
                }

                @Override
                public void onError(Exception e) {
                    Log.i(TAG, "onError: " + e.toString());
                }

            };
        }
    }

    private void send(String s) {
        mWebSocketClient.send(s);
    }

}
