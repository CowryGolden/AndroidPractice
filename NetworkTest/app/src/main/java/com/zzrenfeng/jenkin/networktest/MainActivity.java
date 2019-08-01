package com.zzrenfeng.jenkin.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

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
                            //指定访问服务器地址是电脑本机的资源url（查看方法：打开cmd，输入：adb devices 查看模拟设备列表；然后输入：adb -s [emulator-5554] shell 进入指定设备shell界面；再输入：getprop 获取设备信息；其中：[net.gprs.local-ip]: [10.0.2.15] 为本模拟手机IP；[net.eth0.gw]: [10.0.2.2]为电脑本机的IP；）
                            .url("http://10.0.2.2:8080/get_data.xml")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);    //将返回数据展示在TextView中
//                    parseXMLWithPull(responseData);  //使用XMLPull解析器来解析XML
                    parseXMLWithSAX(responseData);  //使用SAX解析器来解析XML
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

    /**
     * 使用XMLPull解析器来解析XML
     * @param xmlData
     */
    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG: {
                        if("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    //完成解析某个节点，打印解析信息
                    case XmlPullParser.END_TAG: {
                        if("app".equals(nodeName)) {
                            Log.d(TAG, "========[XmlPullParser]>>>> id is: " + id);
                            Log.d(TAG, "========[XmlPullParser]>>>> name is: " + name);
                            Log.d(TAG, "========[XmlPullParser]>>>> version is: " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用SAX解析器来解析XML
     * @param xmlData
     */
    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            //将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);
            //开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
