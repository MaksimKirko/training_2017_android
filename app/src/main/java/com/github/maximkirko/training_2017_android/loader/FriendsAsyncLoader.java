package com.github.maximkirko.training_2017_android.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MadMax on 11.01.2017.
 */

public class FriendsAsyncLoader extends AsyncTaskLoader<String> {

    private static final String BASE_URL = "https://api.vk.com/method/";
    private VKParameters params;

    public FriendsAsyncLoader(@NonNull Context context, @NonNull VKParameters params) {
        super(context);
        this.params = params;
    }


    @Nullable
    @Override
    public String loadInBackground() {

        String urlString = getUrlString();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    private String getUrlString() {
//        method name
        String urlString = BASE_URL + "friends.get?";

//        params
        for (VKParameters.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey() != "access_token" && entry.getValue() != null) {
                urlString += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }

//        access token
        urlString += "access_token=" + params.get(VKApiConst.ACCESS_TOKEN);
//        VK API version
        urlString += "&v=5.8";

        urlString = urlString.replaceAll(" ", "%20");
        return urlString;
    }
}
