package com.zzrenfeng.jenkin.notificationtest;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);    //方法二：点击通知后取消通知栏图标，此方法为显示调用；其中id=1表示为这条通知设置的id，此id在MainActivity的line41行设置；（方法一参见MainActivity的line39行）
    }
}
