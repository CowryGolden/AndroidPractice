package com.zzrenfeng.jenkin.networktest.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http工具类
 */
public class HttpUtil {

    /**
     * 使用HttpURLConnection发送Http请求
     * @param address
     * @param listener
     */
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while (null != (line = reader.readLine())) {
                        response.append(line);
                    }
                    if(null != listener) {
                        //回调onFinish方法，让主线程获取该子线程的执行结果（由于子线程无法返回数据，利用回调机制给主线程返回数据）
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if(null != listener) {
                        //回调onError方法，让主线程获取该子线程的错误信息
                        listener.onError(e);
                    }
                } finally {
                    if(null != connection) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 使用OkHttp发送Http请求
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        //测试使用HttpURLConnection发送Http请求
        HttpUtil.sendHttpRequest("https://www.baidu.com", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //在这里根据返回内容执行具体的逻辑
            }

            @Override
            public void onError(Exception e) {
                //在这里对异常情况进行处理
            }
        });

        //测试使用OkHttp发送Http请求
        HttpUtil.sendOkHttpRequest("https://www.baidu.com", new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData = response.body().string();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
            }
        });
    }

}
