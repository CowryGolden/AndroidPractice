package com.zzrenfeng.jenkin.networktest.util;

/**
 * Http请求回到接口
 */
public interface HttpCallbackListener {
    /**
     * 当Http请求成功响应时调用
     * @param response
     */
    void onFinish(String response);

    /**
     * 当Http请求网络出现错误时调用
     * @param e
     */
    void onError(Exception e);
}
