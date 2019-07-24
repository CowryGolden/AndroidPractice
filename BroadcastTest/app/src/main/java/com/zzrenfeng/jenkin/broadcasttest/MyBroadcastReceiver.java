package com.zzrenfeng.jenkin.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        Toast.makeText(context, "Received in MyBroadcastReceiver.", Toast.LENGTH_SHORT).show();
        abortBroadcast();   //中断广播，不让广播继续传递；测试有序广播
    }
}
