package com.zzrenfeng.jenkin.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 天气预报综合信息类<br/>
 * 和天气API接口返回json报文的整个json报文数据结构：
 * <pre>
 * {
 *     "HeWeather": [{
 * 	        "status": "ok",
 *          "basic": {},
 * 	        "aqi": {},
 * 	        "now": {},
 * 	        "suggestion": {},
 *          "daily_forecast": [{}, {}],
 *          "update": {},
 *          "msg": ""
 *     }]
 * }
 */
public class Weather {
    /**
     * 请求返回状态
     */
    public String status;
    /**
     * 天气预报基本信息
     */
    public Basic basic;
    /**
     * 天气预报AQI（空气质量指数）信息
     */
    public AQI aqi;
    /**
     * 天气预报现在天气信息
     */
    public Now now;
    /**
     * 天气预报建议信息
     */
    public Suggestion suggestion;
    /**
     * 天气预报未来几天预报信息列表
     */
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
