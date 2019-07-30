package com.zzrenfeng.jenkin.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice:
                Intent intent = new Intent(this, NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("This is content title.")
                        .setContentText("This is content text.")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pi)
                        .setAutoCancel(true)  //方法一：点击查看通知信息后自动取消通知栏图标（方法二参见NotificationActivity的line13-14行）
                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Lyra.ogg")))  //设置提示声音
                        .setVibrate(new long[] {0, 1000, 1000, 1000})    //设置有提示时震动（震动1s，静止1s，以此类推）；需要在AndroidManifest.xml中申请震动权限
                        .setLights(Color.GREEN, 1000, 1000)    //设置LED灯闪烁，亮1s暗1s，一闪一闪
//                        .setDefaults(NotificationCompat.DEFAULT_ALL)    //若不想进行如上3行的繁杂设置，可以使用通知的默认效果，根据当前手机环境来决定播放什么铃声以及如何震动等
                        .build();
                manager.notify(1, notification);
                break;
            default:
                break;
        }
    }
}
