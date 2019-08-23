package com.zzrenfeng.jenkin.myuitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MeiziView extends View {

    //定义相关变量依此为妹子图片的X，Y左边
    public float bitmapX;
    public float bitmapY;

    public MeiziView(Context context) {
        super(context);
        bitmapX = 0;
        bitmapY = 200;
    }

    /**
     * 重写View类的onDraw()方法
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建并且实例化Paint对象
        Paint paint = new Paint();
        //根据图片生成位图对象
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.s_jump);
        //绘制萌妹子
        canvas.drawBitmap(bitmap, bitmapX, bitmapY, paint);
        //判断图片是否回收，没有则强制回收
        if(!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
