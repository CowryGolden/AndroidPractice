package com.zzrenfeng.jenkin.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 县（区）实体类
 */
public class Country extends DataSupport {
    /**
     * 县（区）ID
     */
    private int id;
    /**
     * 县（区）名称
     */
    private String countryName;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
