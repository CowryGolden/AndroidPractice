package com.zzrenfeng.jenkin.listviewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * 图片数据适配器
 *
 * 优化：解决ListView异步加载乱序的问题；解决方案三：使用NetworkImageView
 * NetworkImageView这个控件的话就非常简单了，就不用像前两种方案一样做过多的逻辑判断，
 * 它自身就已经考虑到了这个问题，我们直接使用它就可以了，不用做任何额外的处理也不会出现图片乱序的情况。
 */
public class ImageAdapter extends ArrayAdapter<String> {

    private ImageLoader mImageLoader;

    /**
     * 构造方法
     * @param context
     * @param resource
     * @param objects
     */
    public ImageAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        RequestQueue queue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(queue, new BitmapCache());
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
        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.image);
        imageView.setDefaultImageResId(R.drawable.empty_photo);
        imageView.setErrorImageResId(R.drawable.empty_photo);
        imageView.setImageUrl(url, mImageLoader);

        return view;
    }

    /**
     * 内部类：图片缓存内部类，使用LruCache来缓存图片
     * （最近最少算法（Least Recently Used，LRU），为内存缓存区域算法）
     */
    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mLruCache;

        public BitmapCache() {
            //获取应用程序最大可用内存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            mLruCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mLruCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mLruCache.put(url, bitmap);
        }
    }

}














