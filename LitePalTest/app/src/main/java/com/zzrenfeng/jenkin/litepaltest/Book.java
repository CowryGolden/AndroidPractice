package com.zzrenfeng.jenkin.litepaltest;

import org.litepal.crud.DataSupport;

/**
 * 图书实体类
 * 实体类继承litepal的DataSupport类时才能进行CRUD操作，而对表进行管理操作时不需要继承此类
 */
public class Book extends DataSupport {
    /**
     * 主键ID
     */
    private int id;
    /**
     * 书名
     */
    private  String name;
    /**
     * 作者
     */
    private String author;
    /**
     * 页数
     */
    private int pages;
    /**
     * 价格
     */
    private double price;
    /**
     * 出版社
     */
    private String press;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }
}
