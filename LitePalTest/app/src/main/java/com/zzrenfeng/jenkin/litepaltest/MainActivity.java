package com.zzrenfeng.jenkin.litepaltest;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createDatabase = (Button) findViewById(R.id.create_database);
        Button addBookData = (Button) findViewById(R.id.add_book_data);
        Button updateBookData = (Button) findViewById(R.id.update_book_data);
        Button deleteBookData = (Button) findViewById(R.id.delete_book_data);
        Button queryBookData = (Button) findViewById(R.id.query_book_data);

        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.getDatabase();
                Toast.makeText(MainActivity.this, "Created Database successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        addBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(16.96);
                book.setPress("Unknown");
                book.save();    //第1条数据插入

                Toast.makeText(MainActivity.this, "Added Data successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        updateBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book1 = new Book();
                book1.setName("The Lost Symbol");
                book1.setAuthor("Dan Brown");
                book1.setPages(510);
                book1.setPrice(19.95);
                book1.setPress("Unknown");
                book1.save();    //第2条数据插入
//                book1.setPrice(10.99);
//                book1.save();    //更改数据（此种方式会产生两条数据）
                Book book = new Book();
                book.setPrice(14.95);
                book.setPress("Anchor");
                book.updateAll("name = ? and author = ?", "The Lost Symbol", "Dan Brown");

                /* 以下代码为将字段更新为默认值 */
//                book.setToDefault("pages");  //int类型默认为0
//                book.updateAll();  //不带参数，对所有数据生效

                Toast.makeText(MainActivity.this, "Updated Data successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Book.class, "price < ?", "15");
                Toast.makeText(MainActivity.this, "Deleted Data successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        queryBookData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询全部数据
                List<Book> books = DataSupport.findAll(Book.class);

                //按条件查询数据
                //说明：查询Book表中第11~20条满足页数大于400这个条件的name、author、pages这3列的数据，并将查询结果按页数升序排列
                /*List<Book> books = DataSupport.select("name", "author", "pages")
                                                .where("pages > ?", "400")
                                                .order("pages asc")
                                                .limit(10)
                                                .offset(10)
                                                .find(Book.class);*/
                //原生SQL查询
                //注意findBySQL()方法返回类型为Cursor，按之前方式老方法将数据一一取出
                /*Cursor cursor = DataSupport.findBySQL("select * from Book where pages > ? and price < ?", "400", "20");*/

                for (Book book : books) {
                    Log.d(TAG, "========>>>>Book name is: " + book.getName());
                    Log.d(TAG, "========>>>>Book author is: " + book.getAuthor());
                    Log.d(TAG, "========>>>>Book pages is: " + book.getPages());
                    Log.d(TAG, "========>>>>Book price is: " + book.getPrice());
                    Log.d(TAG, "========>>>>Book press is: " + book.getPress());
                }
                Toast.makeText(MainActivity.this, "Retrieved Data successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
