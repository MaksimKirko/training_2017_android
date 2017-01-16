package com.github.maximkirko.training_2017_android.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.github.maximkirko.training_2017_android.memorymanage.BitmapMemoryManager;
import com.github.maximkirko.training_2017_android.util.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private BitmapMemoryManager bitmapMemoryManager;
    private String urlString;
    private ImageView imageView;
    private int imageHeight;
    private int imageWidth;

    public DownloadImageTask(BitmapMemoryManager bitmapMemoryManager, ImageView imageView, int imageHeight, int imageWidth) {
        this.bitmapMemoryManager = bitmapMemoryManager;
        this.imageView = imageView;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            urlString = strings[0];
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromUrl(input, imageHeight, imageWidth);
//            if(bitmap == null) {
//                bitmap = BitmapFactory.decodeStream(input);
//            }
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        bitmapMemoryManager.addBitmapToDiskCache(Uri.parse(urlString).getLastPathSegment(), bitmap);
        bitmapMemoryManager.addBitmapToMemoryCache(urlString, bitmap);
    }
}