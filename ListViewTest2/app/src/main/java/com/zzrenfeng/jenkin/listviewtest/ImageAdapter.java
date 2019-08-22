package com.zzrenfeng.jenkin.listviewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片数据适配器
 */
public class ImageAdapter extends ArrayAdapter<String> {

    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;

    /**
     * 构造方法
     * @param context
     * @param resource
     * @param objects
     */
    public ImageAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        //获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull BitmapDrawable drawable) {
                return drawable.getBitmap().getByteCount();
            }
        };
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String url = getItem(position);
        View view;
        if(null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.image_item, null);
        } else {
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        BitmapDrawable drawable = getBitmapFromMemoryCache(url);
        if(null != drawable) {
            imageView.setImageDrawable(drawable);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.execute(url);
        }

        return view;
    }

    /**
     * 将一张图片缓存到LruCache中
     * @param key
     *          LruCache的键，这里传入图片的URL地址
     * @param drawable
     *          LruCache的值，这里传入从网络上下载的BitmapDrawable对象
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if(null == getBitmapFromMemoryCache(key)) {
            mMemoryCache.put(key, drawable);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null
     * @param key
     *          LruCache的键，这里传入图片的URL地址
     * @return 对应传入键的BitmapDrawable对象，或者null
     */
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 内部类：异步下载图片的任务
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {
        private ImageView mImageView;

        public BitmapWorkerTask(ImageView imageView) {
            this.mImageView = imageView;
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            String imageUrl = params[0];
            //在后台开始下载图片
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            if(null != mImageView && null != drawable) {
                mImageView.setImageDrawable(drawable);
            }
        }

        /**
         * 建立HTTP请求，并获取Bitmap对象
         * @param imageUrl
         *          图片的URL地址
         * @return 解析后的Bitmap对象
         */
        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(imageUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(null != conn) {
                    conn.disconnect();
                }
            }
            return bitmap;
        }
    }

}















