package com.zzrenfeng.jenkin.uibestpractice;

/**
 * 消息实体类
 */
public class Message {
    /**
     * 表示这是一条收到的消息
     */
    public static final int TYPE_RECEIVED = 0;
    /**
     * 表示这是一条发出的消息
     */
    public static final int TYPE_SEND = 1;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型
     */
    private int type;

    public Message(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
