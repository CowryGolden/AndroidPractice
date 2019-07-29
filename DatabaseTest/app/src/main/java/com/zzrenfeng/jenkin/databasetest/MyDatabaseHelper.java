package com.zzrenfeng.jenkin.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 数据库工具类
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 创建Book表的SQL语句,数据库BookStore.db的创建在MainActivity中
     */
    public static final String CREATE_TABLE_BOOK = "create table Book ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "price real,"
            + "pages integer, "
            + "name text)";
    /**
     * 创建Category表的SQL语句
     */
    public static final String CREATE_TABLE_CATEGORY = "create table Category ("
            + "id integer primary key autoincrement, "
            + "category_name text, "
            + "category_code integer)";

    private Context mContext;

    /**
     * 数据库初始化构造方法
     * @param context  上下文实例
     * @param name  数据库名称，如：BookStore.db
     * @param factory  数据查询时返回一个自定义的Cursor工厂实例，一般传入null
     * @param version  当前数据库的版本号
     */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOK);    //数据库初始化创建完成后，同时创建Book表
        db.execSQL(CREATE_TABLE_CATEGORY);
        //20190729-zjc noted;chapter7：自定义内容提供器，实现跨程序数据共享；由于跨程序访问时不能直接使用Toast，故注释；
//        Toast.makeText(mContext, "Database created and initialized successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
