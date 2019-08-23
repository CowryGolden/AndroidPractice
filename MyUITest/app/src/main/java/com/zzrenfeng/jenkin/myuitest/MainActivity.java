package com.zzrenfeng.jenkin.myuitest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.*;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    //星级评分条
    private RatingBar mRatingBar;
    //日期选择器
    private DatePicker mDatePicker;
    //时间选择器
    private TimePicker mTimePicker;
    //日历控件
    private CalendarView mCalender;
    //翻转视图控件(静态导入)
    private ViewFlipper myViewFlipper1;
    //翻转视图控件(动态导入)
    private ViewFlipper myViewFlipper2;
    //需要动态导入的图片资源
    private int[] resIds = {
            R.mipmap.ic_help_view_1,
            R.mipmap.ic_help_view_2,
            R.mipmap.ic_help_view_3,
            R.mipmap.ic_help_view_4
    };
    //首饰移动最小距离
    private static final int MIN_MOVE = 200;
    //自定义手势监听器
    private MyGestureListener mgListener;
    //手势探测器
    private GestureDetector gestureDetector;
    //手指按下的初始位置的X坐标
    private float startX;
    //手势是否有改变
    private boolean isChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //星级评分条事件处理
        dealRatingBar();

        //日期选择器事件处理
        dealDatePicker();

        //时间选择器事件处理
        dealTimePicker();

        //日历控件事件处理
        dealCalenderView();

        //使用ViewFlipper实现图片轮播(静态导入)
        dealStaticViewFlipper();

        //支持手势滑动的ViewFlipper(动态导入)
        dealDynamicViewFlipper();

    }

    /**
     * 模拟器上因为是鼠标的关系，并不会频繁的抖动，使用MyGestureListener中的处理方式没有任何问题；
     * 而真机上，因为手指一直是颤抖的 所以ACTION_MOVE会一直触发，然后View一直切换；在onTouchEvent中直接处理，会让操作更流畅
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                isChange = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isChange) {
                    if(event.getX() < startX) {  //向左滑动，看下一页
                        myViewFlipper2.setInAnimation(mContext, R.anim.right_in);
                        myViewFlipper2.setOutAnimation(mContext, R.anim.right_out);
                        myViewFlipper2.showNext();
                    } else if (event.getX() > startX) {  //向右滑动，看上一页
                        myViewFlipper2.setInAnimation(mContext, R.anim.left_in);
                        myViewFlipper2.setOutAnimation(mContext, R.anim.left_out);
                        myViewFlipper2.showPrevious();
                    }
                    isChange = false;
                }
                break;
            case MotionEvent.ACTION_UP:
//                isChange = false;
                break;
            default:
                break;

        }
        return super.onTouchEvent(event);
//        return gestureDetector.onTouchEvent(event);
    }

    /**
     * 星级评分条事件处理
     */
    private void dealRatingBar() {
        mRatingBar = (RatingBar) findViewById(R.id.my_ratingbar);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(MainActivity.this, "Rating: " + String.valueOf(rating), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 日期选择器事件处理
     */
    private void dealDatePicker() {
        mDatePicker = (DatePicker) findViewById(R.id.my_date_picker);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(MainActivity.this, "您选择的日期是：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 时间选择器事件处理
     */
    private void dealTimePicker() {
        mTimePicker = (TimePicker) findViewById(R.id.my_time_picker);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(MainActivity.this, "您选择的时间是：" + hourOfDay + "时" + minute + "分", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 日历控件事件处理
     */
    private void dealCalenderView() {
        mCalender = (CalendarView) findViewById(R.id.my_calendar_view);
        mCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(MainActivity.this, "您选择的日期是：" + year + "年" + (month + 1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 视图翻转控件事件处理
     * 使用ViewFlipper实现图片轮播(静态导入)
     */
    private void dealStaticViewFlipper() {
        myViewFlipper1 = (ViewFlipper) findViewById(R.id.my_static_view_flipper);
        myViewFlipper1.startFlipping();
    }

    /**
     * 视图翻转控件事件处理
     * 支持手势滑动的ViewFlipper(动态导入)
     */
    private void dealDynamicViewFlipper() {
        mContext = MainActivity.this;
        myViewFlipper2 = (ViewFlipper) findViewById(R.id.my_dynamic_view_flipper);
//        mgListener = new MyGestureListener();
//        gestureDetector = new GestureDetector(this, mgListener);
        //动态添加子View
        for (int i = 0; i < resIds.length; i++) {
            myViewFlipper2.addView(getImageView(resIds[i]));
        }
    }

    /**
     * 根据图片资源ID获取图片控件对象
     * @param resId
     * @return
     */
    private ImageView getImageView(int resId) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(resId);
        return imageView;
    }

    /**
     * 自定义手势监听器
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //模拟器上因为是鼠标的关系，并不会频繁的抖动，如下代码没有任何问题；而真机上，因为手指一直是颤抖的 所以ACTION_MOVE会一直触发，然后View一直切换；在onTouchEvent中直接处理
            if((e1.getX() - e2.getX()) > MIN_MOVE) {
                myViewFlipper2.setInAnimation(mContext, R.anim.right_in);
                myViewFlipper2.setOutAnimation(mContext, R.anim.right_out);
                myViewFlipper2.showNext();
            } else if ((e2.getX() - e1.getX()) > MIN_MOVE) {
                myViewFlipper2.setInAnimation(mContext, R.anim.left_in);
                myViewFlipper2.setOutAnimation(mContext, R.anim.left_out);
                myViewFlipper2.showPrevious();
            }
            return true;
        }
    }

}
