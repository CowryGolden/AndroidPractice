package com.zzrenfeng.jenkin.myuitest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

public class FrameLayoutActivity extends AppCompatActivity {
    /**
     * 本应用发出的信息标志
     */
    public static final int MSG_FLAG = 0x123;
    //初始化帧布局变量
    FrameLayout frameLayout = null;
    //自定义一个用于定时更新UI界面的handler对象
    Handler handler = new Handler() {
        int i = 0;

        @Override
        public void handleMessage(Message msg) {
            //判断信息是否为本应用发出的
            if(msg.what == MSG_FLAG) {
                i++;
                move(i % 8);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_framelayout);

        //随手指移动的萌妹子练习
/*
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.my_framelayout);
        final  MeiziView meiziView = new MeiziView(MainActivity.this);
        //添加触摸监听事件
        meiziView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                meiziView.bitmapX = event.getX() - 150;
                meiziView.bitmapY = event.getY() - 150;
                //调用重绘方法
                meiziView.invalidate();
                return true;
            }
        });
        frameLayout.addView(meiziView);
*/

        //跑动的萌妹子练习
        frameLayout = (FrameLayout) findViewById(R.id.my_framelayout);
        //定义一个定时器对象，定时发送信息给handler
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //发送一条空消息来通知系统改变前景图片
                handler.sendEmptyMessage(MSG_FLAG);
            }
        }, 0, 170);  //每隔170ms发送定时信息

    }


    //定义妹子走路时切换图片的方法
    private void move(int i) {
        Drawable a = getResources().getDrawable(R.drawable.s_1);
        Drawable b = getResources().getDrawable(R.drawable.s_2);
        Drawable c = getResources().getDrawable(R.drawable.s_3);
        Drawable d = getResources().getDrawable(R.drawable.s_4);
        Drawable e = getResources().getDrawable(R.drawable.s_5);
        Drawable f = getResources().getDrawable(R.drawable.s_6);
        Drawable g = getResources().getDrawable(R.drawable.s_7);
        Drawable h = getResources().getDrawable(R.drawable.s_8);
        //通过setForeground来设置前景图片
        switch (i) {
            case 0:
                frameLayout.setForeground(a);
                break;
            case 1:
                frameLayout.setForeground(b);
                break;
            case 2:
                frameLayout.setForeground(c);
                break;
            case 3:
                frameLayout.setForeground(d);
                break;
            case 4:
                frameLayout.setForeground(e);
                break;
            case 5:
                frameLayout.setForeground(f);
                break;
            case 6:
                frameLayout.setForeground(g);
                break;
            case 7:
                frameLayout.setForeground(h);
                break;
            default:
                break;
        }
    }

}
