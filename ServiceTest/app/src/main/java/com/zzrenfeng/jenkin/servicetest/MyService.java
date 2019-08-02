package com.zzrenfeng.jenkin.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 自定义服务
 * 注：在服务的任何地方调用stopSelf()方法即可停止这个服务
 */
public class MyService extends Service {

    private static final String TAG = "MyService";

    private DownloadBinder mBinder = new DownloadBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 服务创建的时候调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "========>>>> onCreate: executed.");
    }

    /**
     * 每次服务启动时调用
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "========>>>> onStartCommand: executed.");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 在服务销毁时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "========>>>> onDestroy: executed.");
    }

    /**
     * 下载绑定器
     */
    class DownloadBinder extends Binder {
        public void startDownload() {
            Log.d(TAG, "========>>>> startDownload: executed.");
        }

        public int getProgress() {
            Log.d(TAG, "========>>>> getProgress: executed.");
            return 0;
        }
    }

}
