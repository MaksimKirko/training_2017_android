package com.github.maximkirko.training_2017_android.application;

import android.app.Application;
import android.util.Log;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.bitmapmemorymanager.BitmapMemoryManagerConfigurator;
import com.github.maximkirko.training_2017_android.bitmapmemorymanager.CacheManager;
import com.vk.sdk.VKSdk;

/**
 * Created by MadMax on 09.01.2017.
 */

public class VKSimpleChatApplication extends Application {

    public static BitmapMemoryManagerConfigurator bitmapMemoryManagerConfigurator;

    private static final int MEMORY_CACHE_SIZE = 4194304; //4mb
    private static final int DISK_CACHE_SIZE = 4194304;

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this.getApplicationContext());
        initBitmapMemoryManager();
    }

    private void initBitmapMemoryManager() {
        bitmapMemoryManagerConfigurator = BitmapMemoryManagerConfigurator.newBuilder()
                .setMemCacheSize(MEMORY_CACHE_SIZE)
                .setDiskCacheSize(DISK_CACHE_SIZE)
                .build();
    }
}
