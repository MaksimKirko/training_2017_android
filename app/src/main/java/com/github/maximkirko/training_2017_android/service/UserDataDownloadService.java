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

/**
 * Created by MadMax on 09.02.2017.
 */

public class UserDataDownloadService extends IntentService {

    private Reader<User> reader;

    private int result = Activity.RESULT_CANCELED;
    public static final String DOWNLOAD_SERVICE_URI = "UserDataDownloadService";
    public static final String RESULT_EXTRAS = "RESULT";
    public static final String NOTIFICATION = "com.github.maximkirko.training_2017_android.service.receiver.UserDownloadServiceBroadcastReceiver";
    public static final String LOG_TAG_DOWNLOAD_SERVICE_RESULT = "USER_DATA_DOWNLOAD";

    public UserDataDownloadService() {
        super(DOWNLOAD_SERVICE_URI);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String userRequestUrl = VKService.getUserRequestUrl(getBaseContext());
        User user = getUserDataFromNetwork(userRequestUrl);
        saveUserDataToDB(user);
        publishResults();
    }

    private User getUserDataFromNetwork(@NonNull String urlString) {
        String jsonResponse = null;
        try {
            jsonResponse = responseToString(NetworkUtils.getConnectionInputStream(urlString));
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return getUserDataFromJson(jsonResponse);
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
    private User getUserDataFromJson(@NonNull String jsonResponse) {
        try {
            reader = new UserJSONReader(jsonResponse);
            return reader.read();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    private void saveUserDataToDB(@NonNull User user) {
        VKSimpleChatApplication.getDbHelper().insertUserData(VKSimpleChatApplication.getDbHelper().getWritableDatabase(), getApplicationContext(), user);
    }

    private void publishResults() {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT_EXTRAS, result);
        sendBroadcast(intent);
    }
}
