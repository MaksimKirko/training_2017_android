package com.github.maximkirko.training_2017_android.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.BuildConfig;
import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.read.FriendsJSONReader;
import com.github.maximkirko.training_2017_android.read.Reader;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by MadMax on 11.01.2017.
 */

public class FriendsAsyncLoader extends AsyncTaskLoader<List<User>> {
    private VKParameters params;
    private Reader<User> reader;

    public FriendsAsyncLoader(@NonNull Context context, @NonNull VKParameters params) {
        super(context);
        this.params = params;
    }

    @Nullable
    @Override
    public List<User> loadInBackground() {

        String urlString = getUrlString();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String jsonResponse = responseToString(bufferedReader);
            return getFriendsListFromJson(jsonResponse);
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    private String getUrlString() {
        //  method name
        String urlString = getContext().getString(R.string.base_vk_api_url) + "friends.get?";

        //  params
        for (VKParameters.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey() != "access_token" && entry.getValue() != null) {
                urlString += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }

        //  access token
        urlString += "access_token=" + params.get(VKApiConst.ACCESS_TOKEN);
        //  VK API version
        urlString += "&v=5.8";

        urlString = urlString.replaceAll(" ", "%20");
        return urlString;
    }

    private String responseToString(@NonNull BufferedReader bufferedReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = bufferedReader.readLine()) != null) {
            sb.append(output);
        }
        return sb.toString();
    }

    @Nullable
    private List<User> getFriendsListFromJson(String jsonResponse) {
        try {
            reader = new FriendsJSONReader(jsonResponse);
            return reader.readToList();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }
}
