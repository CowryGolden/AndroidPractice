package com.zzrenfeng.jenkin.runtimepermissiontest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
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
        Button makeCall = (Button) findViewById(R.id.make_call);
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //以下代码低于Android6.0版本可以正常运行，Android6.0以上版本由于ACTION_CALL为危险权限，程序报错：java.lang.SecurityException: Permission Denial；需要运行时授权处理
                /*try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:10086"));
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }*/

                //ACTION_CALL等危险权限在Android6.0以上版本需要运行时授权处理
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {  //判断用户是否已经授权；不具权限时，在程序运行时进行授权处理
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
                } else {  //已经授权的直接调用拨打电话的逻辑
                    call();
                }
            }
        });
    }

    /**
     * 拨打电话
     */
    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用完ActivityCompat.requestPermissions()方法向用户申请权限后，系统会弹出一个权限申请对话框，用户可以选择同意或拒绝，
     * 不论哪种结果，最终都会调用onRequestPermissionsResult()方法进行后续处理；其中授权结果会封装在grantResults参数中。
     * 因此我们只需要判断一下最后授权结果，若用户同意就调用call()方法拨打电话，若用户拒绝放弃拨打电话操作，并给予用户失败提示。
     * @param requestCode  请求码
     * @param permissions  （要申请的）权限集合
     * @param grantResults  用户授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:  //根据line38，传入的requestCode判断
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:

        }
    }
}
