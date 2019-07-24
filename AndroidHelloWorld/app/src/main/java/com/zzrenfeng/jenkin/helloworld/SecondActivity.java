package com.zzrenfeng.jenkin.helloworld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//public class SecondActivity extends AppCompatActivity {
public class SecondActivity extends BaseActivity {

    public static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "========>>>>[2] onCreate: " + this.toString());
        Log.d(TAG, "========>>>>[2] Task id is: " + getTaskId());
        setContentView(R.layout.activity_second);

//        Intent intent = getIntent();    //获取到用于启动SecondActivity的Intent
//        String data = intent.getStringExtra("extra_data");    //获取intent传递的数据
//          //Log.d("SecondActivity", data);
//        Toast.makeText(SecondActivity.this, data, Toast.LENGTH_SHORT).show();

        Button button2 = (Button) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("data_return", "Hello FirstActivity! This is SecondActivity.");
//                setResult(RESULT_OK, intent);
//                finish();  //由于使用了startActivityForResult()方法来启动SecondActivity，在SecondActivity销毁后会调用上一个活动的onActivityResult()方法

//                //此代码旨在研究启动模式：singleTop模式。
//                Intent intent = new Intent(SecondActivity.this, FirstActivity.class);

                //此代码旨在研究启动模式：singleInstance模式。
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);

                startActivity(intent);
            }
        });

    }

    /**
     * 启动活动SecondActivity，及其需要传递的参数
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, SecondActivity.class);
        Log.d(TAG, "========>>>>[2] actionStart: param1-data1=" + data1 + ", param2-data2=" + data2);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    //使得用户点击Back键和按钮Button 2的效果一样
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity! This is SecondActivity.");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "========>>>>[2] onDestroy: " + this.toString());
    }
}
