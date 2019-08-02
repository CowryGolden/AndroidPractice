package com.zzrenfeng.jenkin.androidthreadtest.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.zzrenfeng.jenkin.androidthreadtest.MainActivity;

/**
 * 利用AsyncTask的异步处理处理机制，实现后台下载前台显示下载进度的功能
 */
public class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

    Context context;

    ProgressDialog progressDialog = new ProgressDialog(context);

    public DownloadTask(Context context) {
        this.context = context;
    }

    /**
     * 这个方法在后台任务开始执行前调用，
     * 用于一些界面上的初始化操作，比如：显示一个进度条对话框等
     */
    @Override
    protected void onPreExecute() {
        progressDialog.show();  //显示进度条对话框
    }

    /**
     * 这个方法中所有代码都会在子线程中运行，我们应该再这里去处理所有的耗时任务。
     * 任务一旦完成就可以通过return语句来将任务的执行结果返回，如果AsyncTask的
     * 第三个泛型参数指定为Void，皆可以不返回任务的执行结果。
     * 注意：由于多有代码都是在子线程中运行的，因此在这个方法中是不能进行UI操作的，
     * 如果需要更新UI元素，比如说反馈当前任务的执行进度，可以调用：publishProgress(Progress...)
     * 方法来完成。
     * @param voids
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            while (true) {
                int downloadPercent = doDownload();  //这是一个虚构方法
                publishProgress(downloadPercent);
                if(downloadPercent >= 100) {
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 当后台任务（doInBackground）中调用了 publishProgress(Progress...)方法后，此方法会很快被
     * 调用，该方法中携带的参数就是在后台任务中传递过来的（publishProgress方法传递的）。
     * 由于此方法时在主线程中的，因此可以对UI进行操作。如：利用参数中的数值就可以对界面
     * 元素进行相应的更新操作。
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        //这个方法在主线程中执行，在这里更新UI元素，更新下载进度
        progressDialog.setMessage("Downloaded " + values[0] + "%");
    }

    /**
     * 当后台任务（doInBackground）执行完毕并通过return进行返回时，此方法就会很快被调用。
     * doInBackground()方法返回的数据会作为参数传递到此方法中，可以利用返回数据进行一些
     * UI操作（该方法时在主线程中的），比如：提醒任务执行的结果，以及关闭掉进度条对话框等。
     * @param result
     */
    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();  //关闭进度对话框
        if (result) {  //这个方法在主线程中执行，可以进行UI操作
            Toast.makeText(context, "Download succeeded!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Download failed!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 这里暂虚构一个执行下载操作的方法；
     * 该方法主要功能有：执行耗时、耗资源的下载操作，并计算当前的下载进度并返回
     * @return
     */
    private int doDownload() {
        int progress = 0;
        //下载操作，并计算当前的下载进度并返回
        /*
         * download()
         * progress = dowloadedProgress...
         */
        return progress;
    }
}
