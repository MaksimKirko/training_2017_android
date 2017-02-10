package com.github.maximkirko.training_2017_android.broadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.maximkirko.training_2017_android.service.FriendsDownloadService;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 30.01.2017.
 */

public class FriendsDownloadServiceBroadcastReceiver extends BroadcastReceiver {

    private WeakReference<BroadcastReceiverCallback> callback;

    public FriendsDownloadServiceBroadcastReceiver() {
    }

    public FriendsDownloadServiceBroadcastReceiver(@NonNull BroadcastReceiverCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int result = intent.getIntExtra(FriendsDownloadService.RESULT_EXTRAS, Activity.RESULT_CANCELED);
        if (result == Activity.RESULT_OK) {
            Log.i(FriendsDownloadService.LOG_TAG_DOWNLOAD_SERVICE_RESULT, "OK");
        } else {
            Log.i(FriendsDownloadService.LOG_TAG_DOWNLOAD_SERVICE_RESULT, "CANCEL");
        }
        if (callback != null) {
            callback.get().onReceived(this.getClass());
        }
    }
}
