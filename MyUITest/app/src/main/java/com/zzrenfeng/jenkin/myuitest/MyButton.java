package com.zzrenfeng.jenkin.myuitest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 实现Material Design水波效果的Button
 */
public class MyButton extends AppCompatImageButton {

    private static final int INVALIDATE_DURATION = 15;  //每次刷新的时间间隔
    private static int DIFFUSE_GAP = 10;  //扩散半径增量
    private static int TAP_TIMEOUT;  //判断点击和长按时间

    private int viewWidth;  //控件宽度
    private int viewHeight;  //控件高度
    private int pointX;  //控件左上角x坐标
    private int pointY;  //控件左上角y坐标
    private int maxRadio;  //扩散的最大半径
    private int shaderRadio;  //扩散的半径

    private Paint bottomPaint;  //画笔背景
    private Paint colorPaint;  //画笔水波纹颜色
    private boolean isPushButton;  //记录是否按钮被按下

    private int eventX;  //触摸位置x坐标
    private int eventY;  //触摸位置y坐标
    private long downTime = 0;  //按下时间

    public MyButton(Context context) {
        super(context);
        initPaint();
        TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    }

    /**
     * 一定不能缺少这个构造方法
     * 不然会报错：android.view.InflateException: Binary XML file line #9: ...
     * 实际调用该构造方法，将数据初始化内容置于该构造方法中
     * @param context
     * @param attrs
     */
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        colorPaint = new Paint();
        bottomPaint = new Paint();
        colorPaint.setColor(getResources().getColor(R.color.reveal_color));
        bottomPaint.setColor(getResources().getColor(R.color.bottom_color));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(downTime == 0) {
                    downTime = SystemClock.elapsedRealtime();
                }
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                //计算最大半径
                calcMaxRadio();
                isPushButton = true;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if((SystemClock.elapsedRealtime() - downTime) < TAP_TIMEOUT) {
                    DIFFUSE_GAP = 30;
                    postInvalidate();
                } else {
                    resetData();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(!isPushButton) return;  //如果按钮没有被按下则返回
        //绘制按下后的整个背景
        canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight, bottomPaint);
        canvas.save();
        //绘制扩散圆形背景
        canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight);
        canvas.drawCircle(eventX, eventY, shaderRadio, colorPaint);
        canvas.restore();
        //直到半径等于最大半径
        if(shaderRadio < maxRadio) {
            postInvalidateDelayed(INVALIDATE_DURATION, pointX, pointY, pointX + viewWidth, pointY + viewHeight);
            shaderRadio += DIFFUSE_GAP;
        } else {
            resetData();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewWidth = w;
        this.viewHeight = h;
    }

    /**
     * 计算最大半径
     */
    private void calcMaxRadio() {
        if(viewWidth > viewHeight) {
            if(eventX < viewWidth / 2) {
                maxRadio = viewWidth - eventX;
            } else {
                maxRadio = viewWidth / 2 + eventX;
            }
        } else {
            if(eventY < viewHeight / 2) {
                maxRadio = viewHeight - eventY;
            } else {
                maxRadio = viewHeight / 2 + eventY;
            }
        }
    }

    /**
     * 重置数据
     */
    private void resetData() {
        downTime = 0;
        DIFFUSE_GAP = 10;
        isPushButton = false;
        shaderRadio = 0;
        postInvalidate();
    }

}
