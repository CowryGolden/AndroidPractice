package com.zzrenfeng.jenkin.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.send_request) {
//            sendRequestWithHttpURLConnection();  //在子线程中使用HttpURLConnection发送HTTP request请求
            sendRequestWithOkHttp();  //在子线程中使用OkHttp发送HTTP request请求
        }
    }

    /**
     * 开启子线程，在子线程中使用HttpURLConnection发送HTTP request请求
     */
    private void sendRequestWithHttpURLConnection() {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    //从服务器获取数据
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    //向服务器提交数据
                    /*connection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("username=admin&password=123456");*/

                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line = null;
                    while (null != (line = reader.readLine())) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(null != reader) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(null != connection) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 开启子线程，在子线程中使用OkHttp发送HTTP request请求
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.baidu.com")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 将HTTP返回数据展示在TextView中
     * @param response
     */
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {  //由于Android不允许在子线程中进行UI操作，因此这里使用runOnUiThread()这个方法将线程切换到主线程，再更新UI元素
            @Override
            public void run() {
                //在这里进行UI操作，将结果显示到界面控件中
                responseText.setText(response);
            }
        });
    }

}
