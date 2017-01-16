package com.github.maximkirko.training_2017_android.application;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by MadMax on 09.01.2017.
 */

public class VKSimpleChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this.getApplicationContext());
    }
}
