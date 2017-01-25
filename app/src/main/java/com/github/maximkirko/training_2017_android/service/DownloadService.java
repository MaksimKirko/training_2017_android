package com.github.maximkirko.training_2017_android.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

public class DownloadService extends IntentService {

    private VKParameters params;
    private Reader<User> reader;

    private int result = Activity.RESULT_CANCELED;
    private static final String NAME = "DownloadService";
    public static final String RESULT_EXTRA = "RESULT";
    public static final String FRIENDS_EXTRA = "FRIENDS";
    public static final String VK_PARAMS_EXTRA = "VK PARAMS";
    public static final String NOTIFICATION = "com.github.maximkirko.training_2017_android.service.receiver";

    public DownloadService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        params = (VKParameters) intent.getSerializableExtra(VK_PARAMS_EXTRA);
        String urlString = getUrlString();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String jsonResponse = responseToString(bufferedReader);
            result = Activity.RESULT_OK;
            publishResults(getFriendsListFromJson(jsonResponse));
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
    }

    private String getUrlString() {
        //  method name
        String urlString = getApplicationContext().getString(R.string.base_vk_api_url) + "friends.get?";

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

    private void publishResults(List<User> friends) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT_EXTRA, result);
        intent.putParcelableArrayListExtra(FRIENDS_EXTRA, (ArrayList<? extends Parcelable>) friends);
        sendBroadcast(intent);
    }
}