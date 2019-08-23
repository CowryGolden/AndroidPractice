package com.zzrenfeng.jenkin.floatwindow360;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startFloatWindow = (Button) findViewById(R.id.start_float_window);

        startFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (checkPermission()) {
                    openSuspensionWindow();
                } else {
                    requestPermission();
                }
            }
        });
    }

    /**
     * 开启悬浮窗
     */
    private void openSuspensionWindow() {
        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
        startService(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (checkPermission()) {  //用户授权成功
                    openSuspensionWindow();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 开启权限管理界面，进行授权
     */
    private void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"));
        startActivityForResult(intent, 1);
    }

    /**
     * 当前目标版本大于23时，检查权限
     * @return
     */
    private boolean checkPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(this);
        } else {
            return true;
        }
    }

}
