package com.zzrenfeng.jenkin.listviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        ImageAdapter adapter = new ImageAdapter(this, 0, Images.imageUrls);
        listView.setAdapter(adapter);
    }
}
