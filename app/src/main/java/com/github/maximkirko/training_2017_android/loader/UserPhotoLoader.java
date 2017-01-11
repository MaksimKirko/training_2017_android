package com.github.maximkirko.training_2017_android.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MadMax on 11.01.2017.
 */

public class UserPhotoLoader extends AsyncTaskLoader<Bitmap> {

    private String url;

    public UserPhotoLoader(@NonNull Context context, @NonNull String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Bitmap loadInBackground() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }
}
