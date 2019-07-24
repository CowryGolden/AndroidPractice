package com.zzrenfeng.jenkin.helloworld;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.*;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * This is a log tag
     */
    private static final String TAG = "MainActivity";
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    //指定当前Activity的布局文件为：res/layout/main.xml
        Log.d("data", "onCreate: execute");  //debug级别日志

        TextView tv = (TextView) findViewById(R.id.textView);
        TextView t1 = (TextView) findViewById(R.id.textOne);
        TextView t2 = (TextView) findViewById(R.id.textTwo);

        //测试文本框显示纯文本内容
//        tv.setText("哎呀，才发现Android操作控件也不难呀！");
//        String str = tv.getText().toString();

        //测试文本框显示HTML内容
//        String  s1 = "<font color='blue'><b>百度一下，你就知道~：</b></font><br>";
//        s1 += "<a href = 'http://www.baidu.com'>百度</a>";
//        tv.setText(Html.fromHtml(s1));
//        tv.setMovementMethod(LinkMovementMethod.getInstance());

        //测试文本框显示：SpannableString&SpannableStringBuilder定制文本
//        SpannableString span = new SpannableString("红色打电话斜体删除线绿色下划线图片：。");
//        //1.设置背景色，setSpan时需要指定的flag，Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
//        span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //2.用超链接标记文本
//        span.setSpan(new URLSpan("tel:0123456789"), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //3.用样式标记文本（斜体）
//        span.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //4.用删除线标记文本
//        span.setSpan(new StrikethroughSpan(), 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //5.用下划线标记文本
//        span.setSpan(new UnderlineSpan(), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //6.用颜色标记文本
//        span.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //7.获取Drawable资源
//        Drawable d = getResources().getDrawable(R.drawable.icon);
//        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//        //8.创建ImageSpan，然后用ImageSpan来替换文本
//        ImageSpan imgSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
//        span.setSpan(imgSpan, 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        t1.setText(span);

        //测试部分可点击的TextView
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("好友" + i + "，");
        }
        String likeUsers = sb.substring(0, sb.lastIndexOf("，")).toString();
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(addClickPart(likeUsers), TextView.BufferType.SPANNABLE);

    }

    /*
     * 定义一个点击每个部分文字的处理方法
     */
    private SpannableStringBuilder addClickPart(String str) {
        //点赞图标
        ImageSpan imgSpan = new ImageSpan(MainActivity.this, R.drawable.ic_thumb);
        SpannableString spanStr = new SpannableString("p.");
        spanStr.setSpan(imgSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        //创建一个SpannableStringBuilder对象，连接多个字符串
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str);
        String[] likeUsers = str.split("，");
        if (likeUsers.length > 0) {
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start =  str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //删除下划线，设置字体颜色为蓝色
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                }, start, start + name.length(), 0);
            }
        }
        return ssb.append("等" + likeUsers.length + "人觉得很赞");
    }

}
