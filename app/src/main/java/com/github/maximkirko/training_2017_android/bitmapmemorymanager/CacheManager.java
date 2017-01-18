package com.github.maximkirko.training_2017_android.bitmapmemorymanager;

import android.graphics.Bitmap;

/**
 * Created by MadMax on 18.01.2017.
 */

public interface CacheManager {
    Bitmap getBitmapFromCache(String key);

    void addBitmapToCache(String key, Bitmap bitmap);
}
