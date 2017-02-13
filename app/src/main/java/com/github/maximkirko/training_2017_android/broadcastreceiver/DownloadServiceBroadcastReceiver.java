package com.github.maximkirko.training_2017_android.broadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.maximkirko.training_2017_android.service.FriendsDataDownloadService;
import com.github.maximkirko.training_2017_android.service.VKRequestAbstractService;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 10.02.2017.
 */

public class DownloadServiceBroadcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG_DOWNLOAD_SERVICE_RESULT = "SERVICE_RESULT";
    private WeakReference<BroadcastReceiverCallback> callback;

    public DownloadServiceBroadcastReceiver() {
    }

    public DownloadServiceBroadcastReceiver(@NonNull BroadcastReceiverCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int result = intent.getIntExtra(FriendsDataDownloadService.RESULT_EXTRAS, Activity.RESULT_CANCELED);
        if (result == Activity.RESULT_OK) {
            Log.i(LOG_TAG_DOWNLOAD_SERVICE_RESULT, "OK");
        } else {
            Log.i(LOG_TAG_DOWNLOAD_SERVICE_RESULT, "CANCEL");
        }
        String serviceClass = intent.getStringExtra(VKRequestAbstractService.SERVICE_CLASS_EXTRA);
        if (callback != null) {
            callback.get().onReceived(serviceClass);
        }
    }
}
