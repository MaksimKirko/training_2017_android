package com.github.maximkirko.training_2017_android.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    // link on configurator
    private BitmapMemoryManagerConfigurator bitmapMemoryManagerConfigurator = VKSimpleChatApplication.getBitmapManagerConfigurator();

    private ImageLoader() {
    }

    public static Loader newLoader() {
        return new ImageLoader().new Loader();
    }

    public class Loader {

        private Loader() {
        }

        public Loader setTargetView(ImageView targetView) {
            ImageLoader.this.targetView = new WeakReference<>(targetView);
            return this;
        }

        public Loader setPlaceHolder(int placeHolder) {
            ImageLoader.this.placeHolder = placeHolder;
            return this;
        }

        public Loader setImageHeight(int imageHeight) {
            ImageLoader.this.imageHeight = imageHeight;
            return this;
        }

        public Loader setImageWidth(int imageWidth) {
            ImageLoader.this.imageWidth = imageWidth;
            return this;
        }

        public ImageLoader load(String url) {
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.url = url;
            imageLoader.targetView = ImageLoader.this.targetView;
            imageLoader.placeHolder = ImageLoader.this.placeHolder;
            imageLoader.imageHeight = ImageLoader.this.imageHeight;
            imageLoader.imageWidth = ImageLoader.this.imageWidth;

            imageLoader.executeOnExecutor(executorService);
            return imageLoader;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        targetView.get().setImageResource(placeHolder);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
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
            BitmapFactory.Options options = BitmapUtils.calculateBitmapFactoryOptions(getHTTPConnectionInputStream(url), imageHeight, imageWidth);
            return BitmapUtils.decodeBitmapFromUrl(getHTTPConnectionInputStream(url), options);
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    private InputStream getHTTPConnectionInputStream(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        return connection.getInputStream();
    }
}
