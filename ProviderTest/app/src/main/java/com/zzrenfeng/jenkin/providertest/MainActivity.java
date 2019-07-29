package com.zzrenfeng.jenkin.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addBookData = (Button) findViewById(R.id.add_book_data);
        Button queryBookData = (Button) findViewById(R.id.query_book_data);
        Button updateBookData = (Button) findViewById(R.id.update_book_data);
        Button deleteBookData = (Button) findViewById(R.id.delete_book_data);

        addBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加数据
                Uri uri = Uri.parse("content://com.zzrenfeng.jenkin.databasetest.provider/book");
                ContentValues values = new ContentValues();
                values.put("name", "A Clash of Kings");
                values.put("author", "George Martin");
                values.put("pages", 1040);
                values.put("price", 22.85);
                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);   //0为path，1为id

                Toast.makeText(MainActivity.this, "By ContentProvider Add data to the Book table of DatabaseTest.", Toast.LENGTH_SHORT).show();
            }
        });

        queryBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查询数据
                Uri uri = Uri.parse("content://com.zzrenfeng.jenkin.databasetest.provider/book");
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if(null != cursor) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d(TAG, "========>>>> Book name is: " + name);
                        Log.d(TAG, "========>>>> Book author is: " + author);
                        Log.d(TAG, "========>>>> Book pages is: " + pages);
                        Log.d(TAG, "========>>>> Book price is: " + price);
                    }
                    cursor.close();
                }
                Toast.makeText(MainActivity.this, "By ContentProvider Query data from the Book table of DatabaseTest.", Toast.LENGTH_SHORT).show();
            }
        });

        updateBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新数据
                Uri uri = Uri.parse("content://com.zzrenfeng.jenkin.databasetest.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name", "A Storm of Swords");
                values.put("pages", 1216);
                values.put("price", 24.05);
                getContentResolver().update(uri, values, null, null);

                Toast.makeText(MainActivity.this, "By ContentProvider Update data to the Book table of DatabaseTest.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除数据
                Uri uri = Uri.parse("content://com.zzrenfeng.jenkin.databasetest.provider/book/" + newId);
                getContentResolver().delete(uri, null, null);

                Toast.makeText(MainActivity.this, "By ContentProvider Delete data from the Book table of DatabaseTest.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
