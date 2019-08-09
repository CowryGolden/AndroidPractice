package com.zzrenfeng.jenkin.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气预报未来几天预报信息类<br/>
 * 和天气API接口返回json报文中 daily_forecast 域主要数据对应的POJO类<br/>
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
 * 其中 daily_forecast 域主要的数据内容为：
 * <pre>
 * "daily_forecast": [{
 *     "date": "2019-08-09",
 *     "cond": {
 * 	       "txt_d": "多云"
 *     },
 *     "tmp": {
 * 	       "max": "24",
 * 	       "min": "12"
 *     }
 * }, {
 *     "date": "2019-08-10",
 *     "cond": {
 * 	       "txt_d": "多云"
 *     },
 *     "tmp": {
 * 	       "max": "25",
 * 	       "min": "11"
 *     }
 * },
 * ...
 * }],
 * </pre>
 */
public class Forecast {
    /**
     * 预报天气的日期
     */
    public String date;

    /**
     * 预报天气的温度信息
     */
    @SerializedName("tmp")
    public Temperature temperature;

    /**
     * 预报天气的信息
     */
    @SerializedName("cond")
    public More more;

    /**
     * 预报天气的温度信息类
     */
    public class Temperature {
        /**
         * 预报的最高温度
         */
        public String max;
        /**
         * 预报的最低温度
         */
        public String min;
    }

    /**
     * 预报天气的信息类
     */
    public class More {
        /**
         * 预报天气的信息内容
         */
        @SerializedName("txt_d")
        public String info;
    }

}
