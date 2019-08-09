package com.zzrenfeng.jenkin.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气预报基本信息类<br/>
 * 和天气API接口返回json报文中 basic 域主要数据对应的POJO类<br/>
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
 * 其中 basic 域主要的数据内容为：
 * <pre>
 * "basic": {
 *     "cid": "CN101180101",
 *     "location": "郑州",
 *     "parent_city": "郑州",
 *     "admin_area": "河南",
 *     "cnty": "中国",
 *     "lat": "28.19408989",
 *     "lon": "112.98227692",
 *     "tz": "+8.00",
 *     "city": "郑州",
 *     "id": "CN101180101",
 *     "update": {
 * 	       "loc": "2019-08-08 16:21",
 * 	       "utc": "2019-08-08 08:21"
 *     }
 * },
 * </pre>
 */
public class Basic {
    /**
     * 城市名称<br/>
     * json报文中的字段名称不适合直接作为Java POJO类中的字段名称，
     * 因此这里使用了 @SerializedName 注解来让json字段和Java字段之间建立映射关系；
     */
    @SerializedName("city")
    public String cityName;

    /**
     * 城市对应的天气ID
     */
    @SerializedName("id")
    public String weatherId;

    /**
     * 天气更新时间
     */
    public Update update;

    /**
     * 天气更新时间类
     */
    public class Update {
        /**
         * 天气更新的本地时间
         */
        @SerializedName("loc")
        public String updateTime;
    }
}
