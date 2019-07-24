package com.zzrenfeng.jenkin.uibestpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Message> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs();  //初始化消息数据
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Message message = new Message(content, Message.TYPE_SEND);
                    msgList.add(message);
                    adapter.notifyItemInserted(msgList.size() - 1);  //当有新消息时，刷新RecyclerView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);  //将RecyclerView定位到最后一行
                    inputText.setText("");  //清空输入框内容

                }
            }
        });
    }

    /**
     * 初始化消息数据
     */
    private void initMsgs() {
        Message message1 = new Message("Hello guy.", Message.TYPE_RECEIVED);
        msgList.add(message1);
        Message message2 = new Message("Hello. Who is that?", Message.TYPE_SEND);
        msgList.add(message2);
        Message message3 = new Message("This is Tom. Nice talking to you.", Message.TYPE_RECEIVED);
        msgList.add(message3);
    }

}
