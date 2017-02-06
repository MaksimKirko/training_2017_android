package com.github.maximkirko.training_2017_android.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MadMax on 04.02.2017.
 */

public class AuthorizationTask extends AsyncTask<String, Void, Void> {

    // input https://oauth.vk.com/authorize?client_id=5814025&redirect_uri=http://vk.com&display=page&scope=friends&response_type=token&v=5.62
    // output http://api.vk.com/blank.html#access_token=3d6c7afdfe96c571f0809ea79950d0b8038cbafd480b09e2a181d8d24d5e8f43650d50ca5e757199d470c&expires_in=86400&user_id=181965790

    private WeakReference<AsyncTaskCallback<String>> asyncTaskCallback;

    public AuthorizationTask(AsyncTaskCallback<String> asyncTaskCallback) {
        this.asyncTaskCallback = new WeakReference<>(asyncTaskCallback);
    }

    @Override
    protected Void doInBackground(String... params) {
        String urlString = params[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();
            String redirect_uri = connection.getURL().toString();
            if (asyncTaskCallback != null) {
                asyncTaskCallback.get().onAsyncTaskFinished(redirect_uri);
            }
            is.close();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }
}
