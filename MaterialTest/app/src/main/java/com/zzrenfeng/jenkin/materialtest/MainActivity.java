package com.zzrenfeng.jenkin.materialtest;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);  //让导航按钮显示；在Toolbar最左侧的按钮叫作HomeAsUp按钮，它默认的图标是一个返回的箭头，意为返回上一个活动；这里我们对它默认的样式和作用都进行了修改；
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);  //设置导航按钮图标
        }

        navView.setCheckedItem(R.id.nav_call);  //将Call菜单设置为默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();  //点击菜单项时将滑动菜单关闭
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You clicked the FloatingActionButton.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 加载toolbar.xml中菜单项
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * 处理按钮的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  //HomeAsUp按钮的id永远是android.R.id.home。
                mDrawerLayout.openDrawer(GravityCompat.START);  //这里openDrawer()方法中传入的Gravity参数一定要和activity_main.xml中定义的android:layout_gravity属性值一致；
                break;
            case R.id.backup:
                Toast.makeText(this, "You clicked the menu of Backup.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked the menu of Delete.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked the menu of Settings.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }


}
