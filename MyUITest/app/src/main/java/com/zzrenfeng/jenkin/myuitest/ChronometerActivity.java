package com.zzrenfeng.jenkin.myuitest;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

/**
 * 计时器Activity
 */
public class ChronometerActivity extends AppCompatActivity implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    private Chronometer chronometer;
    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private Button btnFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);

        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                chronometer.start();  //开始计时
                break;
            case R.id.btnStop:
                chronometer.stop();  //停止计时
                break;
            case R.id.btnReset:
                chronometer.setBase(SystemClock.elapsedRealtime());  //复位
                break;
            case R.id.btnFormat:
                chronometer.setFormat("Time: %s");  //更改时间显示格式
                break;
            default:
                break;
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        String time = chronometer.getText().toString();
        if(time.equals("00:00")) {
            Toast.makeText(ChronometerActivity.this, "时间到了！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化视图控件
     */
    private void initView() {
        chronometer = (Chronometer) findViewById(R.id.my_chronometer);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnFormat = (Button) findViewById(R.id.btnFormat);

        chronometer.setOnChronometerTickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnFormat.setOnClickListener(this);
    }

}
