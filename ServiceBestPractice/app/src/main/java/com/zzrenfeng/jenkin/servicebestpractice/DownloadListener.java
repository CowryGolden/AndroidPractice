package com.zzrenfeng.jenkin.servicebestpractice;

/**
 * 下载监听器接口
 * 用于下载过程中各种状态的监听和回调
 */
public interface DownloadListener {
    /**
     * 通知当前的下载进度事件
     * @param progress 下载进度
     */

    void onProgress(int progress);
    /**
     * 通知下载成功事件
     */
    void onSuccess();

    /**
     * 通知下载失败事件
     */
    void onFailed();

    /**
     * 通知下载停止，及已下载进度事件
     * @param progressed 已下载的进度
     */
    void onPaused(int progressed);

    /**
     * 通知下载取消事件
     */
    void onCanceled();

}
