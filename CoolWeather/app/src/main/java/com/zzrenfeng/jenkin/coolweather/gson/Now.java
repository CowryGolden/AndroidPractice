package com.zzrenfeng.jenkin.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气预报现在天气信息类<br/>
 * 和天气API接口返回json报文中 now 域主要数据对应的POJO类<br/>
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
 * 其中 now 域主要的数据内容为：
 * <pre>
 * "now": {
 *     "cloud": "100",
 *     "cond_code": "101",
 *     "cond_txt": "多云",
 *     "fl": "20",
 *     "hum": "78",
 *     "pcpn": "0.0",
 *     "pres": "1013",
 *     "tmp": "19",
 *     "vis": "2",
 *     "wind_deg": "261",
 *     "wind_dir": "西风",
 *     "wind_sc": "1",
 *     "wind_spd": "2",
 *     "cond": {
 * 	       "code": "101",
 * 	       "txt": "多云"
 *     }
 * },
 * </pre>
 */
public class Now {
    /**
     * 现在温度数值
     */
    @SerializedName("tmp")
    public String temperature;

    /**
     * 当前天气信息
     */
    @SerializedName("cond")
    public More more;

    /**
     * 当前天气信息类
     */
    public class More {
        /**
         * 当前天气信息内容
         */
        @SerializedName("txt")
        public String info;
    }
}
