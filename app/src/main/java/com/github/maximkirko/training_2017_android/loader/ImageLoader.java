package com.github.maximkirko.training_2017_android.loader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.bitmapmemorymanager.BitmapMemoryManagerConfigurator;
import com.github.maximkirko.training_2017_android.util.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MadMax on 18.01.2017.
 */

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    // region fields for builder
    private String url;
    private WeakReference<ImageView> targetView;
    private int placeHolder;
    private int imageHeight;
    private int imageWidth;
    // endregion

    // link on configurator
    private BitmapMemoryManagerConfigurator bitmapMemoryManagerConfigurator = VKSimpleChatApplication.bitmapMemoryManagerConfigurator;

    private ImageLoader() {
    }

    public static ImageLoader.Builder newBuilder() {
        return new ImageLoader().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder setTargetView(ImageView targetView) {
            ImageLoader.this.targetView = new WeakReference<>(targetView);
            return this;
        }

        public Builder setPlaceHolder(int placeHolder) {
            ImageLoader.this.placeHolder = placeHolder;
            return this;
        }

        public Builder setImageHeight(int imageHeight) {
            ImageLoader.this.imageHeight = imageHeight;
            return this;
        }

        public Builder setImageWidth(int imageWidth) {
            ImageLoader.this.imageWidth = imageWidth;
            return this;
        }

        public ImageLoader build() {
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.targetView = ImageLoader.this.targetView;
            imageLoader.placeHolder = ImageLoader.this.placeHolder;
            imageLoader.imageHeight = ImageLoader.this.imageHeight;
            imageLoader.imageWidth = ImageLoader.this.imageWidth;
            return ImageLoader.this;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        targetView.get().setImageResource(placeHolder);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        url = strings[0];
//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Bitmap bitmap = bitmapMemoryManagerConfigurator.getBitmapMemoryCacheManager().getBitmapFromCache(url);
        if (bitmap == null) {
            bitmap = bitmapMemoryManagerConfigurator.getBitmapDiskCacheManager().getBitmapFromCache(url);
            if (bitmap == null) {
                Log.i("IMAGE LOADING", "FROM NETWORK");
                return getBitmapFromNetwork(url);
            }
            Log.i("IMAGE LOADING", "FROM DISK");
            bitmapMemoryManagerConfigurator.getBitmapMemoryCacheManager().addBitmapToCache(url, bitmap);
            return bitmap;
        }
        Log.i("IMAGE LOADING", "FROM CACHE");
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        targetView.get().setImageBitmap(bitmap);
        bitmapMemoryManagerConfigurator.getBitmapDiskCacheManager().addBitmapToCache(Uri.parse(url).getLastPathSegment(), bitmap);
        bitmapMemoryManagerConfigurator.getBitmapMemoryCacheManager().addBitmapToCache(url, bitmap);
    }

    private Bitmap getBitmapFromNetwork(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapUtils.decodeSampledBitmapFromUrl(input, imageHeight, imageWidth);
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }
}
