package com.zzrenfeng.jenkin.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 异步下载任务类
 * 利用AsyncTask的异步处理机制，实现后台下载、前台显示下载进度等功能
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;

    private boolean isCanceled = false;
    private boolean isPaused = false;

    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    /**
     * 这个方法在后台任务开始执行前调用，
     * 用于一些界面上的初始化操作，比如：显示一个进度条对话框等
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog.show();  //显示进度条对话框
    }

    /**
     * 这个方法中所有代码都会在子线程中运行，我们应该再这里去处理所有的耗时任务。
     * 任务一旦完成就可以通过return语句来将任务的执行结果返回，如果AsyncTask的
     * 第三个泛型参数指定为Void，皆可以不返回任务的执行结果。
     * 注意：由于多有代码都是在子线程中运行的，因此在这个方法中是不能进行UI操作的，
     * 如果需要更新UI元素，比如说反馈当前任务的执行进度，可以调用：publishProgress(Progress...)
     * 方法来完成。
     *
     * @param params
     * @return
     */
    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadedLength = 0;  //记录已下载的文件长度
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if(file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if(contentLength == 0) {
                return TYPE_FAILED;
            } else if(contentLength == downloadedLength) {
                //已下载字节和文件的总字节相等，说明已经下载完成
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if(null != response) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);  //跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if(isCanceled) {
                        return TYPE_CANCELED;
                    } else if(isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        //计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return TYPE_FAILED;
        } finally {
            try {
                if(null != is) {
                    is.close();
                }
                if(null != savedFile) {
                    savedFile.close();
                }
                if(isCanceled && null != file) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return TYPE_FAILED;
    }

    /**
     * 当后台任务（doInBackground）中调用了 publishProgress(Progress...)方法后，此方法会很快被
     * 调用，该方法中携带的参数就是在后台任务中传递过来的（publishProgress方法传递的）。
     * 由于此方法时在主线程中的，因此可以对UI进行操作。如：利用参数中的数值就可以对界面
     * 元素进行相应的更新操作。
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /**
     * 当后台任务（doInBackground）执行完毕并通过return进行返回时，此方法就会很快被调用。
     * doInBackground()方法返回的数据会作为参数传递到此方法中，可以利用返回数据进行一些
     * UI操作（该方法时在主线程中的），比如：提醒任务执行的结果，以及关闭掉进度条对话框等。
     *
     * @param status
     */
    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused(lastProgress);
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    /**
     * 暂停下载
     */
    public void pauseDownload() {
        isPaused = true;
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        isCanceled = true;
    }

    /**
     * 获取下载内容长度
     *
     * @param downloadUrl
     * @return
     * @throws IOException
     */
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if(null != response && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }

}
