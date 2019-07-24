package com.zzrenfeng.jenkin.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 自己创建的第一个Activity
 */
//public class FirstActivity extends AppCompatActivity {
public class FirstActivity extends BaseActivity {

    public static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "========>>>>[1] onCreate: " + this.toString());
        Log.d(TAG, "========>>>>[1] Task id is: " + getTaskId());
        setContentView(R.layout.first_layout);
        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FirstActivity.this, "You clicked Button 1, The Activity will be destroyed.", Toast.LENGTH_SHORT).show();
//                finish();    //销毁当前活动

//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);  //显式Intent

//                Intent intent = new Intent("com.zzrenfeng.jenkin.helloworld.ACTION_START");  //隐式Intent，配合AndroidManifest.xml文件中SecondActivity的配置
//                intent.addCategory("com.zzrenfeng.jenkin.helloworld.MY_CATEGORY");

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("https://www.baidu.com"));

//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:10086"));

//                //使用intent在活动间传递数据
//                String data = "Hello SecondActivity!";
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                intent.putExtra("extra_data", data);

//                //返回数据给上一个活动
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivityForResult(intent, 1);   //使用该方法启动一个Activity，可以在启动的Activity销毁时返回一个结果给上一个Activity（即此Activity）

                //此代码旨在研究启动模式：standard模式。每次启动都创建一个新活动，置于栈顶位置；此种模式不必在意栈中是否存在。
//                Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
//                //此代码旨在研究启动模式：singleTop模式。
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//
//                startActivity(intent);

                //使用带参数的方法启动活动SecondActivity
                SecondActivity.actionStart(FirstActivity.this, "data1", "data2");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data_return");
//                    Log.d("FirstActivity", returnedData);
                    Toast.makeText(FirstActivity.this, returnedData, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;    //true：表示允许创建的菜单显示出来；false：表示创建的菜单将无法显示；
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(this, "You clicked Add menu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "You clicked Remove menu", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "========>>>>[1] onRestart: " + this.toString());
    }
}
