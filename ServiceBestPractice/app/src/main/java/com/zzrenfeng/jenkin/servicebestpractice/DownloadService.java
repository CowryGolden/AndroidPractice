package com.zzrenfeng.jenkin.servicebestpractice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

/**
 * 下载服务类
 */
public class DownloadService extends Service {

    private DownloadTask downloadTask;

    private String downloadUrl;

    public DownloadService() {
    }

    private DownloadListener listener = new DownloadListener() {
        /**
         * 开始下载创建一个下载开始的通知
         * @param progress
         */
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        /**
         * 下载成功时将前台服务通知关闭，并创建一个下载成功的通知
         */
        @Override
        public void onSuccess() {
            downloadTask = null;
            //下载成功时将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        /**
         * 下载失败时将前台服务通知关闭，并创建一个下载失败的通知
         */
        @Override
        public void onFailed() {
            downloadTask = null;
            //下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        /**
         * 停止下载时创建一个已下载进度的通知
         * @param progressed 已下载的进度
         */
        @Override
        public void onPaused(int progressed) {
            downloadTask = null;
            getNotificationManager().notify(1, getNotification("Download Paused", progressed));
            Toast.makeText(DownloadService.this, "Download Paused", Toast.LENGTH_SHORT).show();
        }

        /**
         * 取消下载时将前台服务通知关闭
         */
        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 下载绑定器内部类
     */
    class DownloadBinder extends Binder {
        /**
         * 开始下载业务
         * @param url
         */
        public void startDownload(String url) {
            if(null == downloadTask) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 停止下载业务
         */
        public void pauseDownload() {
            if(null != downloadTask) {
                downloadTask.pauseDownload();
            }
        }

        /**
         * 取消下载业务
         */
        public void cancelDownload() {
            if(null != downloadTask) {
                downloadTask.cancelDownload();
            }
            if(null != downloadUrl) {
                //取消下载时需要将已存在的下载文件删除，并关闭通知
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if(file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this, "Download Canceled!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获取通知管理器
     * @return
     */
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 获取通知实例
     * @param title
     * @param progress
     * @return
     */
    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress >= 0) {
            //当progress大于或等于0时才显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

}
