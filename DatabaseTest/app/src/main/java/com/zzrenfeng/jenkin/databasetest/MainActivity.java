package com.zzrenfeng.jenkin.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建数据库并初始化基础表
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);  //version > 1 表示数据库升级了，此时才会执行onUpgrade方法重新创建创建数据库和表
        Button createDatabase = (Button) findViewById(R.id.create_database);
        Button addBookData = (Button) findViewById(R.id.add_book_data);
        Button updateBookData = (Button) findViewById(R.id.update_book_data);
        Button deleteBookData = (Button) findViewById(R.id.delete_book_data);
        Button queryBookData = (Button) findViewById(R.id.query_book_data);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        addBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //组装第1条Book数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");;
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values);    //插入第1条数据；insert()三个参数：table-要插入数据的数据库表名；nullColumnHack-位指定添加数据的情况下，给某些可为空的列自动复制NULL；ContentValues对象-每列要添加的数据；
                values.clear();  //清除对象内容
                //组装第2条Book数据
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");;
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values);    //插入第2条数据；
                values.clear();

                Toast.makeText(MainActivity.this, "Added Book data successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        updateBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                db.update("Book", values, "name = ?", new String[] {"The Da Vinci Code"});

                Toast.makeText(MainActivity.this, "Updated Book data successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        deleteBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[] {"500"});

                Toast.makeText(MainActivity.this, "Deleted Book data successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        queryBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //查询Book表中所有数据
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    do {
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "========>>>>Book name is: " + name);
                        Log.d(TAG, "========>>>>Book author is: " + author);
                        Log.d(TAG, "========>>>>Book pages is: " + pages);
                        Log.d(TAG, "========>>>>Book price is: " + price);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(MainActivity.this, "Retrieved Book data successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
