package com.plenry.sparkline.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Xiaoyu on 5/17/16.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public static LruCache<String, Bitmap> mMemoryCache;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    protected Bitmap doInBackground(String... urls) {
        final String imageKey = urls[0];

        Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            return bitmap;
        } else {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageKey).getContent());
                addBitmapToMemoryCache(imageKey, bitmap);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        Bitmap cornered_image = ImageHelper.getRoundedCornerBitmap(result, 100);
        if (result != null) {
            bmImage.setImageBitmap(cornered_image);
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}