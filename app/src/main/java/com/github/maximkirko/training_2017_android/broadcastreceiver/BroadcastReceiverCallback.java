package com.github.maximkirko.training_2017_android.broadcastreceiver;

import android.content.Intent;

/**
 * Created by MadMax on 30.01.2017.
 */

public interface BroadcastReceiverCallback {
    void onReceived(Intent intent);
}
