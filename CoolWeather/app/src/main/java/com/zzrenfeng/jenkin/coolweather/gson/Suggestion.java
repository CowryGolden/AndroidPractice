package com.zzrenfeng.jenkin.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气预报建议信息类<br/>
 * 和天气API接口返回json报文中 suggestion 域主要数据对应的POJO类<br/>
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
 * 其中 suggestion 域主要的数据内容为：
 * <pre>
 * "suggestion": {
 *     "comf": {
 * 	       "type": "comf",
 * 	       "brf": "舒适",
 * 	       "txt": "白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"
 *     },
 *     "sport": {
 * 	       "type": "sport",
 * 	       "brf": "适宜",
 * 	       "txt": "天气较好，赶快投身大自然参与户外运动，尽情感受运动的快乐吧。"
 *     },
 *     "cw": {
 * 	       "type": "cw",
 * 	       "brf": "较适宜",
 * 	       "txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
 *     }
 * },
 * </pre>
 */
public class Suggestion {
    /**
     * 舒适度信息
     */
    @SerializedName("comf")
    public Comfort comfort;
    /**
     * 洗车信息
     */
    @SerializedName("cw")
    public CarWash carWash;
    /**
     * 运动信息
     */
    @SerializedName("sport")
    public Sport sport;

    /**
     * 舒适度信息类
     */
    public class Comfort {
        /**
         * 舒适度信息内容
         */
        @SerializedName("txt")
        public String info;
    }
    /**
     * 洗车信息类
     */
    public class CarWash {
        /**
         * 洗车信息内容
         */
        @SerializedName("txt")
        public String info;
    }
    /**
     * 运动信息类
     */
    public class Sport {
        /**
         * 运动信息内容
         */
        @SerializedName("txt")
        public String info;
    }
}
