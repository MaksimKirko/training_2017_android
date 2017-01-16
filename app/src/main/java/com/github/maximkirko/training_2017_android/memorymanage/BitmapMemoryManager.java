package com.github.maximkirko.training_2017_android.memorymanage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.task.DownloadImageTask;
import com.github.maximkirko.training_2017_android.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MadMax on 15.01.2017.
 */

public class BitmapMemoryManager implements Parcelable {

    private LruCache<String, Bitmap> mMemoryCache;
    private int memCacheSize;
    private int diskCacheSize;
    private int placeHolder;
    private int imageHeight;
    private int imageWidth;

    private BitmapMemoryManager() {

    }

    protected BitmapMemoryManager(Parcel in) {
        memCacheSize = in.readInt();
        diskCacheSize = in.readInt();
    }

    public static final Creator<BitmapMemoryManager> CREATOR = new Creator<BitmapMemoryManager>() {
        @Override
        public BitmapMemoryManager createFromParcel(Parcel in) {
            return new BitmapMemoryManager(in);
        }

        @Override
        public BitmapMemoryManager[] newArray(int size) {
            return new BitmapMemoryManager[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(memCacheSize);
        parcel.writeInt(diskCacheSize);
    }

    public static Builder newBuilder() {
        return new BitmapMemoryManager().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setMemCacheSize(int memCacheSize) {
            BitmapMemoryManager.this.memCacheSize = memCacheSize;
            return this;
        }

        public Builder setDiskCacheSize(int diskCacheSize) {
            BitmapMemoryManager.this.diskCacheSize = diskCacheSize;
            return this;
        }

        public Builder setPlaceHolder(int placeHolder) {
            BitmapMemoryManager.this.placeHolder = placeHolder;
            return this;
        }

        public Builder setImageHeight(int imageHeight) {
            BitmapMemoryManager.this.imageHeight = imageHeight;
            return this;
        }

        public Builder setImageWidth(int imageWidth) {
            BitmapMemoryManager.this.imageWidth = imageWidth;
            return this;
        }

        public BitmapMemoryManager build() {
            BitmapMemoryManager bitmapMemoryManager = new BitmapMemoryManager();
            bitmapMemoryManager.memCacheSize = BitmapMemoryManager.this.memCacheSize;
            bitmapMemoryManager.diskCacheSize = BitmapMemoryManager.this.diskCacheSize;
            bitmapMemoryManager.placeHolder = BitmapMemoryManager.this.placeHolder;
            bitmapMemoryManager.imageHeight = BitmapMemoryManager.this.imageHeight;
            bitmapMemoryManager.imageWidth = BitmapMemoryManager.this.imageWidth;

            bitmapMemoryManager.mMemoryCache = new LruCache<>(memCacheSize);
            return BitmapMemoryManager.this;
        }

    }

    public void setBitmap(String url, ImageView userPhotoView) {
        Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap == null) {
            userPhotoView.setImageResource(placeHolder);
            bitmap = getBitmapFromDiskCache(url);
            if (bitmap == null) {
                DownloadImageTask task = new DownloadImageTask(this, userPhotoView, imageHeight, imageWidth);
                task.execute(new String[]{url});
                Log.i("IMAGE LOADING", "FROM NETWORK");
                return;
            }
            Log.i("IMAGE LOADING", "FROM DISK");
            addBitmapToMemoryCache(url, bitmap);
            userPhotoView.setImageBitmap(bitmap);
            return;
        }
        Log.i("IMAGE LOADING", "FROM CACHE");
        userPhotoView.setImageBitmap(bitmap);
    }

    //    region memory cache methods
    private Bitmap getBitmapFromMemCache(String url) {
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<>(memCacheSize);
        }
        return mMemoryCache.get(url);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        Log.i("MEM CACHE SIZE", mMemoryCache.size() + "");
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    //    endregion memory cache methods

    //    region disk cache methods
    @Nullable
    private Bitmap getBitmapFromDiskCache(String url) {
        try {
            File path = FileUtils.getImageDirectory(url);
            return BitmapFactory.decodeStream(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    public void addBitmapToDiskCache(String url, Bitmap bitmap) {
        File directory = FileUtils.getImageDirectory(url);
        while (bitmap.getByteCount() + FileUtils.folderSize(directory) > diskCacheSize) {
            FileUtils.removeOldest(directory);
            Log.i("DISK CACHE SIZE", FileUtils.folderSize(directory) + "");
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
    //    endregion
}
