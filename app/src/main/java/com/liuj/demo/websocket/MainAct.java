package com.liuj.demo.websocket;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainAct extends AppCompatActivity {

    private WebSocketService.WSBinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mTvCon = findViewById(R.id.tv_con);
        mTvCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        Button mTvSend = findViewById(R.id.tv_send);
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinder.sendMsg("android : hello ~");
            }
        });
    }

    private void start() {
        Intent intent = new Intent(this, WebSocketService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = (WebSocketService.WSBinder) service;
                mBinder.setLsn(new WebSocketService.WSBinder.ICallback() {
                    @Override
                    public void onReceiveMsg(String str) {
                        Log.i("liujie", " receive str ");
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBinder = null;
            }
        }, BIND_AUTO_CREATE);
    }


}
