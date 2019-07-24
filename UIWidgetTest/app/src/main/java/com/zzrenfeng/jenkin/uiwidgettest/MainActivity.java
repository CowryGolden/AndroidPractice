package com.zzrenfeng.jenkin.uiwidgettest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private EditText editText;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.edit_text);
        imageView = (ImageView) findViewById(R.id.image_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        /*button.setOnClickListener(new View.OnClickListener() {  //通过匿名类方式来注册监听器
            @Override
            public void onClick(View v) {
                //在此添加逻辑
            }
        });*/
        button.setOnClickListener(this);
    }

    /**
     * 通过实现View.OnClickListener接口方式来注册监听器，重写按钮点击方法
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                //在此添加逻辑
                //编辑框及图片框
                String inputText = editText.getText().toString();
                if(imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.img_1).getConstantState())) {
                    imageView.setImageResource(R.drawable.img_2);
                    inputText = inputText + ": img_2=" + R.drawable.img_2;
                } else {
                    imageView.setImageResource(R.drawable.img_1);
                    inputText = inputText + ": img_1=" + R.drawable.img_1;
                }
                Toast.makeText(MainActivity.this, inputText, Toast.LENGTH_SHORT).show();

//                if(progressBar.getVisibility() == View.GONE) {
//                    progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    progressBar.setVisibility(View.GONE);
//                }
                //进度条
                int progress = progressBar.getProgress();
                if(progress == 100) {
                    progress = 0;
                } else {
                    progress += 10;
                }
                progressBar.setProgress(progress);

                //弹出对话框
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("This is a Dialog.");
                dialog.setMessage("Something important. eg: " + inputText);
                dialog.setCancelable(false);    //表示不能通过“Back”键来取消
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "You have clicked the button OK.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "You have clicked the button Cancel.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

                //进度加载对话框
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("This is a ProgressDialog.");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);  //表示可以通过“Back”键来取消，或点击区域外任意地方取消；若设置为false，则在数据加载完成后通过dimiss()方法来关闭对话框，否则ProgressDialog将会一直存在。
                progressDialog.show();

                break;
            default:
                break;
        }
    }

    /**
     * 通过java的反射机制获取已加载原生ImageView（android.widget.ImageView）图片的资源ID
     * 参考链接：https://blog.csdn.net/drgzong/article/details/51823741
     * 备注：测试未通过！！！
     * @param imageView
     * @return
     */
    public int getImageViewResourceId(ImageView imageView) {
        int resId = 0;
        Field[] fields = imageView.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals("mResource")) {    //通过查看ImageView类的setImageResource()方法中line:475（mResource = resId;）可知
                field.setAccessible(true);
                try {
                    resId = field.getInt(imageView);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return resId;
    }

    /**
     * 通过java的反射机制获取已加载v7中的AppCompatImageView（扩展自ImageView，android.support.v7.widget.AppCompatImageView）图片的资源ID
     * 参考链接：https://blog.csdn.net/neabea2016/article/details/84621918
     * 备注：测试未通过！！！
     * @param imageView
     * @return
     */
    public int getV7ImageViewResourceId(ImageView imageView) {
        int resId = 0;
        Field[] fields = imageView.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.getName().equals("mBackgroundTintHelper")){
                field.setAccessible(true);
                try {
                    Object obj = field.get(imageView);
                    Field[] fields2 = obj.getClass().getDeclaredFields();
                    for(Field f2 : fields2){
                        if(f2.getName().equals("mBackgroundResId")){
                            f2.setAccessible(true);
                            resId = f2.getInt(obj);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return resId;
    }

}
