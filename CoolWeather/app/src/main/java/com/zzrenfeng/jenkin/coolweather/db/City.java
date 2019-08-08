package com.zzrenfeng.jenkin.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 城市实体类
 */
public class City extends DataSupport {
    /**
     * 城市ID
     */
    private int id;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 城市代码
     */
    private int cityCode;
    /**
     * 所属省份ID
     */
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
