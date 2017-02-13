package com.github.maximkirko.training_2017_android.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.reader.Reader;
import com.github.maximkirko.training_2017_android.util.NetworkUtils;
import com.github.maximkirko.training_2017_android.util.StringUtils;

import java.io.IOException;

/**
 * Created by Max on 13.02.2017.
 */

public abstract class VKRequestAbstractService<T> extends IntentService {

    protected Reader<User> reader;

    protected int result = Activity.RESULT_CANCELED;
    public static final String RESULT_EXTRAS = "RESULT";
    public static final String NOTIFICATION = "com.github.maximkirko.training_2017_android.service.receiver";
    public static final String IS_FIRST_LOADING_EXTRAS = "IS_FIRST_LOADING";
    public static final String SERVICE_CLASS_EXTRA = "SERVICE_CLASS_EXTRA";
    private String serviceClass;
    protected String requestUrl;

    public VKRequestAbstractService(String name, String serviceClass) {
        super(name);
        this.serviceClass = serviceClass;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        T data = getDataFromNetwork(requestUrl);
        saveData(data);
        publishResults();
    }

    protected T getDataFromNetwork(@NonNull String urlString) {
        String jsonResponse = null;
        try {
            jsonResponse = StringUtils.bufferedReaderToString(NetworkUtils.getConnectionInputStream(urlString));
            result = Activity.RESULT_OK;
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return getDataFromJson(jsonResponse);
    }

    @Nullable
    protected abstract T getDataFromJson(@NonNull String jsonResponse);

    protected abstract void saveData(@NonNull T data);

    protected void publishResults() {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT_EXTRAS, result);
        intent.putExtra(SERVICE_CLASS_EXTRA, serviceClass);
        sendBroadcast(intent);
    }
}

