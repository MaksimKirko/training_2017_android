package com.github.maximkirko.training_2017_android.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.service.DownloadService;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by MadMax on 30.01.2017.
 */

public class DownloadServiceBroadcastReceiver extends BroadcastReceiver {

    private List<User> friends;

    private WeakReference<BroadcastReceiverCallback> callback;

    public DownloadServiceBroadcastReceiver() {
    }

    public DownloadServiceBroadcastReceiver(@Nullable List<User> friends, @NonNull BroadcastReceiverCallback callback) {
        this.friends = friends;
        this.callback = new WeakReference<BroadcastReceiverCallback>(callback);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int result = intent.getIntExtra(DownloadService.RESULT_EXTRA, Activity.RESULT_CANCELED);
        if (result == Activity.RESULT_OK) {
            friends = intent.getParcelableArrayListExtra(DownloadService.FRIENDS_EXTRA);
            Log.i(DownloadService.LOG_TAG_DOWNLOAD_SERVICE_RESULT, "OK");
        } else {
            friends = null;
            Log.i(DownloadService.LOG_TAG_DOWNLOAD_SERVICE_RESULT, "OK");
        }
        callback.get().onReceived();
    }
}
