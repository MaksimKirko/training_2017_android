package com.github.maximkirko.training_2017_android.bitmapmemorymanager;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by MadMax on 18.01.2017.
 */

public class BitmapMemoryCacheManager implements CacheManager {

    private LruCache<String, Bitmap> memoryCache;
    private int cacheSize;

    public BitmapMemoryCacheManager(int cacheSize) {
        this.cacheSize = cacheSize;
        memoryCache = new LruCache<>(cacheSize);
    }

    public Bitmap getBitmapFromCache(String key, int imageHeight, int imageWidth) {
        if (memoryCache == null) {
            memoryCache = new LruCache<>(cacheSize);
        }
        return memoryCache.get(key + "_" + imageHeight + "_" + imageWidth);
    }

    public void addBitmapToCache(String key, int imageHeight, int imageWidth, Bitmap bitmap) {
        if (getBitmapFromCache(key, bitmap.getHeight(), bitmap.getWidth()) == null) {
            memoryCache.put(key + "_" + imageHeight + "_" + imageWidth, bitmap);
        }
    }

    public void clearCache() {
        memoryCache = new LruCache<>(cacheSize);
    }
}
