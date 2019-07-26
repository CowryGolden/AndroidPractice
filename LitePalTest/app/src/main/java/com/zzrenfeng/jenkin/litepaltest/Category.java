package com.zzrenfeng.jenkin.litepaltest;

import org.litepal.crud.DataSupport;

/**
 * 图书类别实体类
 * 实体类继承litepal的DataSupport类时才能进行CRUD操作，而对表进行管理操作时不需要继承此类
 */
public class Category extends DataSupport {
    /**
     * 主键ID
     */
    private int id;
    /**
     * 类别名称
     */
    private String categoryName;
    /**
     * 类别代码
     */
    private int categoryCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }
}
