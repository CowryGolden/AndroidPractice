package com.zzrenfeng.jenkin.listviewtest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片数据适配器
 *
 * 优化：解决ListView异步加载乱序的问题；解决方案二：使用弱引用关联
 *
 * 场景分析：BitmapWorkerTask.getAttachedImageView()方法中做了双向验证的逻辑判断从而解决图片加载乱序的问题；
 * 那么为什么做了这个逻辑判断之后，图片乱序的问题就可以得到解决呢？
 * 其实最主要的奥秘就是在getAttachedImageView()方法当中，它会使用当前BitmapWorkerTask所关联的ImageView
 * 来反向获取这个ImageView所关联的BitmapWorkerTask，然后用这两个BitmapWorkerTask做对比，
 * 如果发现是同一个BitmapWorkerTask才会返回ImageView，否则就返回null。
 * 那么什么情况下这两个BitmapWorkerTask才会不同呢？
 * 比如说某个图片被移出了屏幕，它的ImageView被另外一个新进入屏幕的图片重用了，
 * 那么就会给这个ImageView关联一个新的BitmapWorkerTask，这种情况下，
 * 上一个BitmapWorkerTask和新的BitmapWorkerTask肯定就不相等了，这时getAttachedImageView()方法会返回null，
 * 而我们又判断ImageView等于null的话是不会设置图片的，因此就不会出现图片乱序的情况了。
 *
 */
public class ImageAdapter extends ArrayAdapter<String> {

    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;

    private ListView mListView;
    private Bitmap mLoadingBitmap;

    /**
     * 构造方法
     * @param context
     * @param resource
     * @param objects
     */
    public ImageAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
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
        if (null == mListView) {
            mListView = (ListView) parent;
        }
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
        } else if(cancelPotentialWork(url, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            AsyncDrawable asyncDrawable = new AsyncDrawable(getContext().getResources(), mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);  //调用ImageView的setImageDrawable()方法把AsyncDrawable设置了进去，那么ImageView就可以通过getDrawable()方法获取到和它关联的AsyncDrawable，然后再借助AsyncDrawable就可以获取到BitmapWorkerTask了。这样ImageView指向BitmapWorkerTask的弱引用关联也成功建立。
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
     * 增加让这个BitmapWorkerTask持有ImageView的弱引用。
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {
        private String imageUrl;

        private WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            this.imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            //在后台开始下载图片
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = getAttachedImageView();
            if(null != imageView && null != drawable) {
                imageView.setImageDrawable(drawable);
            }
        }

        /**
         * 获取当前BitmapWorkerTask所关联的ImageView对象；
         *
         * 内部逻辑：先获取当前BitmapWorkerTask所关联的ImageView，
         * 然后调用getBitmapWorkerTask()方法来获取该ImageView所对应的BitmapWorkerTask，
         * 最后判断，如果获取到的BitmapWorkerTask等于this（也就是当前的BitmapWorkerTask）
         * 那么就将ImageView返回，否则就返回null。
         *
         * @return
         */
        private ImageView getAttachedImageView() {
            ImageView imageView = this.imageViewReference.get();
            BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if(this == bitmapWorkerTask) {
                return imageView;
            }
            return null;
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

    /**
     * 内部类：自定义的一个Drawable，让这个Drawable持有BitmapWorkerTask的弱引用。
     *
     * 内部逻辑：但是ImageView指向BitmapWorkerTask的弱引用关联就没这么容易了，
     * 因为我们很难将BitmapWorkerTask的一个弱引用直接设置到ImageView当中。这该怎么办呢？
     * 这里使用了一个比较巧的方法，就是借助自定义Drawable的方式来实现。可以看到，
     * 我们自定义了一个AsyncDrawable类并让它继承自BitmapDrawable，然后重写了AsyncDrawable的构造函数，
     * 在构造函数中要求把BitmapWorkerTask传入，然后在这里给它包装了一层弱引用。
     *
     * 场景分析：那么现在AsyncDrawable指向BitmapWorkerTask的关联已经有了，
     * 但是ImageView指向BitmapWorkerTask的关联还不存在，怎么办呢？
     * 很简单，让ImageView和AsyncDrawable再关联一下就可以了。
     * 可以看到，在getView()方法当中，我们调用了ImageView的setImageDrawable()方法
     * 把AsyncDrawable设置了进去，那么ImageView就可以通过getDrawable()方法获取到和它关联的AsyncDrawable，
     * 然后再借助AsyncDrawable就可以获取到BitmapWorkerTask了。这样ImageView指向BitmapWorkerTask的弱引用关联也成功建立。
     *
     */
    class AsyncDrawable extends BitmapDrawable {

        private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        /**
         * 获取AsyncDrawable关联的BitmapWorkerTask对象。
         * @return
         */
        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }

    }

    /**
     * 获取传入的ImageView它所对应的BitmapWorkerTask。
     * 内部的逻辑就是先获取ImageView对应的AsyncDrawable，再获取AsyncDrawable对应的BitmapWorkerTask。
     */
    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (null != imageView) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * 取消掉后台的潜在任务，当认为当前ImageView存在着一个另外图片请求任务时，
     * 则把它取消掉并返回true，否则返回false。该方法大大提高了ListView加载图片的工作效率。
     *
     * 内部逻辑：首先它也是调用了getBitmapWorkerTask()方法来获取传入的ImageView所对应的BitmapWorkerTask，
     * 接下来拿BitmapWorkerTask中的imageUrl和传入的url做比较，
     * 如果两个url不等的话就调用BitmapWorkerTask的cancel()方法，然后返回true，如果两个url相等的话就返回false。
     *
     * 场景分析：两个url做比对时，如果发现是相同的，说明请求的是同一张图片，
     * 那么直接返回false，这样就不会再去启动BitmapWorkerTask来请求图片，
     * 而如果两个url不相同，说明这个ImageView被另外一张图片重新利用了，
     * 这个时候就调用了BitmapWorkerTask的cancel()方法把之前的请求取消掉，
     * 然后重新启动BitmapWorkerTask来去请求新图片。有了这个操作保护之后，
     * 就可以把一些已经移出屏幕的无效的图片请求过滤掉，从而整体提升ListView加载图片的工作效率。
     *
     */
    public boolean cancelPotentialWork(String url, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (null != bitmapWorkerTask) {
            String imageUrl = bitmapWorkerTask.imageUrl;
            if (null == imageUrl || !imageUrl.equals(url)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

}















