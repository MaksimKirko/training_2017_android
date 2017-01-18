package com.github.maximkirko.training_2017_android.bitmapmemorymanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MadMax on 18.01.2017.
 */

public class BitmapDiskCacheManager implements CacheManager {

    private int cacheSize;

    public BitmapDiskCacheManager(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Nullable
    public Bitmap getBitmapFromCache(String key) {
        try {
            File directory = FileUtils.getImageDirectory(key);
            File path = new File(directory, Uri.parse(key).getLastPathSegment());
            return BitmapFactory.decodeStream(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    public void addBitmapToCache(String url, Bitmap bitmap) {
        File directory = FileUtils.getImageDirectory(url);
        while (bitmap.getByteCount() + FileUtils.folderSize(directory) > cacheSize) {
            FileUtils.removeOldest(directory);
        }

        File path = new File(directory, Uri.parse(url).getLastPathSegment());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e(e.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
