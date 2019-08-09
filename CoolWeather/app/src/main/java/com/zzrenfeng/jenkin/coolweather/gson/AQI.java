package com.zzrenfeng.jenkin.coolweather.gson;

/**
 * 天气预报AQI（空气质量指数）类<br/>
 * 和天气API接口返回json报文中 aqi 域主要数据对应的POJO类<br/>
 * API返回的整个json报文数据结构：
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
 * </pre>
 * 其中 aqi 域主要的数据内容为：
 * <pre>
 * "aqi": {
 *     "city": {
 * 	       "aqi": "83",
 * 	       "pm25": "61",
 * 	       "qlty": "良"
 *     }
 * },
 * </pre>
 */
public class AQI {
    /**
     * 城市AQI
     */
    public AQICity city;

    /**
     * 城市AQI类
     */
    public class AQICity {
        /**
         * AQI数值
         */
        public String aqi;
        /**
         * PM2.5数值
         */
        public String pm25;
    }

}
