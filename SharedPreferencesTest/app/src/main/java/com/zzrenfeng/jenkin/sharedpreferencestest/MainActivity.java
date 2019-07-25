package com.zzrenfeng.jenkin.sharedpreferencestest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button saveData = (Button) findViewById(R.id.save_data);
        Button restoreData = (Button) findViewById(R.id.restore_data);
        final EditText nameText = (EditText) findViewById(R.id.name);
        final EditText ageText = (EditText) findViewById(R.id.age);
        final EditText marriedText = (EditText) findViewById(R.id.married);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                String name = TextUtils.isEmpty(nameText.getText().toString()) ? "Jenkins" : nameText.getText().toString();
                int age = TextUtils.isEmpty(ageText.getText().toString()) ? 28 : Integer.parseInt(ageText.getText().toString());
                boolean married = TextUtils.isEmpty(marriedText.getText().toString()) ? false : Boolean.parseBoolean(marriedText.getText().toString());

                editor.putString("name", name);
                editor.putInt("age", age);
                editor.putBoolean("married", married);
                editor.apply();
                Toast.makeText(MainActivity.this, "Saved data successfully.", Toast.LENGTH_SHORT).show();
            }
        });
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                String name = sp.getString("name", "");
                int age = sp.getInt("age", 0);
                boolean married = sp.getBoolean("married", false);
                nameText.setText(name);
                ageText.setText(String.valueOf(age));
                marriedText.setText(String.valueOf(married));
                Toast.makeText(MainActivity.this, "Restored data successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
