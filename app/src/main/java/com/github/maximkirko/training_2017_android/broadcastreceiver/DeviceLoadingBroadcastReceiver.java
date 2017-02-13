package com.github.maximkirko.training_2017_android.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 03.02.2017.
 */

public class DeviceLoadingBroadcastReceiver extends BroadcastReceiver {

    public static final String SERVICE_CLASS = "DEVICE";

    private static WeakReference<BroadcastReceiverCallback> callback;

    public DeviceLoadingBroadcastReceiver() {
        Log.i("DEFAULT CONSTRUCTOR", "TRUE");
    }

    public DeviceLoadingBroadcastReceiver(@NonNull BroadcastReceiverCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (callback != null) {
            callback.get().onReceived(SERVICE_CLASS);
        }
    }
}
