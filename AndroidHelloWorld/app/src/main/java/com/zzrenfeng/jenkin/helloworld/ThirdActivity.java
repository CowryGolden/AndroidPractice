package com.zzrenfeng.jenkin.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//public class ThirdActivity extends AppCompatActivity {
public class ThirdActivity extends BaseActivity {

    public static final String TAG = "ThirdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "========>>>>[3] onCreate: " + this.toString());
        Log.d(TAG, "========>>>>[3] Task id is: " + getTaskId());
        setContentView(R.layout.activity_third);
        Button button3 = (Button) findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();    //退出程序
            }
        });
    }
}
