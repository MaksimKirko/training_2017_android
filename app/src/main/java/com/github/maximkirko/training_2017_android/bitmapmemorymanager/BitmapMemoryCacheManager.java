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

    public Bitmap getBitmapFromCache(String key) {
        if (memoryCache == null) {
            memoryCache = new LruCache<>(cacheSize);
        }
        return memoryCache.get(key);
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }
}
