package com.zzrenfeng.jenkin.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;
//    private NetworkChangeReceiver networkChangeReceiver;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);    //获取实例

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("com.zzrenfeng.jenkin.broadcasttest.MY_BROADCAST");    //自定义广播
                Intent intent = new Intent("com.zzrenfeng.jenkin.broadcasttest.LOCAL_BROADCAST");    //本地广播
//                sendBroadcast(intent);    //发送标准广播
//                sendOrderedBroadcast(intent, null);    //发送有序广播
                localBroadcastManager.sendBroadcast(intent);  //发送本地广播
            }
        });

        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver, intentFilter);

        intentFilter.addAction("com.zzrenfeng.jenkin.broadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);  //注册本地广播监听器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(networkChangeReceiver);
//        Toast.makeText(this, "The networkChangeReceiver is destroied.", Toast.LENGTH_SHORT).show();
        localBroadcastManager.unregisterReceiver(localReceiver);
        Toast.makeText(this, "The localReceiver is destroied.", Toast.LENGTH_SHORT).show();
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();    //必须在AndroidManifest.xml中声明用户权限，不然会报错
            if(null != networkInfo && networkInfo.isAvailable()) {
                Toast.makeText(context, "The network is available.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "The network is unavailable.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Received Local Broadcast.", Toast.LENGTH_SHORT).show();
        }
    }

}
