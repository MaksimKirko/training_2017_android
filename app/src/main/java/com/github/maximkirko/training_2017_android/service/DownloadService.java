package com.github.maximkirko.training_2017_android.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.read.FriendsJSONReader;
import com.github.maximkirko.training_2017_android.read.Reader;
import com.vk.sdk.api.VKParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadService extends IntentService {

    private Reader<User> reader;

    private int result = Activity.RESULT_CANCELED;
    public static final String DOWNLOAD_SERVICE_URI = "DownloadService";
    public static final String IS_FIRST_LOADING_EXTRAS = "IS_FIRST_LOADING";
    public static final String RESULT_EXTRAS = "RESULT";
    public static final String NOTIFICATION = "com.github.maximkirko.training_2017_android.service.receiver";
    public static final String LOG_TAG_DOWNLOAD_SERVICE_RESULT = "DOWNLOAD_SERVICE RESULT";

    public DownloadService() {
        super(DOWNLOAD_SERVICE_URI);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlString;
        if (intent.getBooleanExtra(IS_FIRST_LOADING_EXTRAS, false)) {
            urlString = getUrl();
        } else {
            urlString = getUrl();
            //urlString = getUrl().replace("count=3", "count=6");
        }
        List<User> friends = getFriendsFromNetwork(urlString);
        saveFriendsToDB(friends);
        publishResults();
    }

    @NonNull
    private String getUrl() {
        VKParameters vkParameters = VKService.initVKParametersForFriendsRequest();
        return VKService.getUrlString(vkParameters);
    }

    @Nullable
    private List<User> getFriendsFromNetwork(@NonNull String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String jsonResponse = responseToString(bufferedReader);
            result = Activity.RESULT_OK;
            return getFriendsListFromJson(jsonResponse);
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
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
    private List<User> getFriendsListFromJson(@NonNull String jsonResponse) {
        try {
            reader = new FriendsJSONReader(jsonResponse);
            return reader.readToList();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    private void saveFriendsToDB(@NonNull List<User> friends) {
        VKSimpleChatApplication.getDbHelper().setFriends(friends);
        VKSimpleChatApplication.getDbHelper().insertFriendsBatch(VKSimpleChatApplication.getDbHelper().getWritableDatabase(), getApplicationContext());
    }

    private void publishResults() {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT_EXTRAS, result);
        sendBroadcast(intent);
    }
}