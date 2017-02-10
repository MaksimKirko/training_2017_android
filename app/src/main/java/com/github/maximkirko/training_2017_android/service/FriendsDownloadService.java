package com.github.maximkirko.training_2017_android.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.reader.Reader;
import com.github.maximkirko.training_2017_android.reader.UserJSONReader;
import com.github.maximkirko.training_2017_android.util.NetworkUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class FriendsDownloadService extends IntentService {

    private Reader<User> reader;

    private int result = Activity.RESULT_CANCELED;
    public static final String DOWNLOAD_SERVICE_URI = "FriendsDownloadService";
    public static final String IS_FIRST_LOADING_EXTRAS = "IS_FIRST_LOADING";
    public static final String RESULT_EXTRAS = "RESULT";
    public static final String NOTIFICATION = "com.github.maximkirko.training_2017_android.service.receiver.FriendsDownloadServiceBroadcastReceiver";
    public static final String LOG_TAG_DOWNLOAD_SERVICE_RESULT = "FRIENDS_DOWNLOAD";

    public FriendsDownloadService() {
        super(DOWNLOAD_SERVICE_URI);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String friendsRequestUrl = VKService.getFriendsRequestUrl(getBaseContext());
//        if (intent.getBooleanExtra(IS_FIRST_LOADING_EXTRAS, false)) {
//            urlString = getUrl();
//        } else {
//            urlString = getUrl().replace("count=3", "count=6");
//        }
        List<User> friends = getFriendsFromNetwork(friendsRequestUrl);
        saveFriendsToDB(friends);
        publishResults();
    }

    @Nullable
    private List<User> getFriendsFromNetwork(@NonNull String urlString) {
        String jsonResponse = null;
        try {
            jsonResponse = responseToString(NetworkUtils.getConnectionInputStream(urlString));
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        result = Activity.RESULT_OK;
        return getFriendsListFromJson(jsonResponse);
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
            reader = new UserJSONReader(jsonResponse);
            return reader.readToList();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    private void saveFriendsToDB(@NonNull List<User> friends) {
        VKSimpleChatApplication.getDbHelper().insertFriendsBatch(VKSimpleChatApplication.getDbHelper().getWritableDatabase(), getApplicationContext(), friends);
    }

    private void publishResults() {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT_EXTRAS, result);
        sendBroadcast(intent);
    }
}