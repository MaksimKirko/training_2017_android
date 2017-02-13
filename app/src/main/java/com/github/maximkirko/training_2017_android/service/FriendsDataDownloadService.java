package com.github.maximkirko.training_2017_android.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.reader.UserJSONReader;

import java.io.IOException;
import java.util.List;

public class FriendsDataDownloadService extends VKRequestAbstractService<List<User>> {

    public static final String DOWNLOAD_SERVICE_URI = "FriendsDataDownloadService";
    public static final String SERVICE_CLASS = "FRIENDS";

    public FriendsDataDownloadService() {
        super(DOWNLOAD_SERVICE_URI, SERVICE_CLASS);
        requestUrl = VKService.getFriendsRequestUrl();
    }

    @Nullable
    @Override
    protected List<User> getDataFromJson(@NonNull String jsonResponse) {
        try {
            reader = new UserJSONReader(jsonResponse);
            return reader.readToList();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void saveData(@NonNull List<User> data) {
        VKSimpleChatApplication.getDbHelper().insertFriendsBatch(VKSimpleChatApplication.getDbHelper().getWritableDatabase(), getApplicationContext(), data);
    }
}