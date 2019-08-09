package com.zzrenfeng.jenkin.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 县（区）实体类
 */
public class County extends DataSupport {
    /**
     * 县（区）ID
     */
    private int id;
    /**
     * 县（区）名称
     */
    private String countyName;
    /**
     * 县（区）天气ID
     */
    private String weatherId;
    /**
     * 所有城市ID
     */
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
