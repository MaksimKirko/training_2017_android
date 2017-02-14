package com.github.maximkirko.training_2017_android.application;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.github.maximkirko.training_2017_android.bitmapmemorymanager.BitmapMemoryManagerConfigurator;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

/**
 * Created by MadMax on 09.01.2017.
 */

public class VKSimpleChatApplication extends Application {

    // region Cache size
    private static final int MEMORY_CACHE_SIZE = 4194304; //4mb
    private static final int DISK_CACHE_SIZE = 4194304;
    // endregion

    private static BitmapMemoryManagerConfigurator bitmapMemoryManagerConfigurator;
    private static DBHelper dbHelper;

    public static BitmapMemoryManagerConfigurator getBitmapManagerConfigurator() {
        return bitmapMemoryManagerConfigurator;
    }

    public static DBHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        initSharedPreferences();
        initBitmapMemoryManager();
        initDBHelper();
    }

    private void initSharedPreferences() {
        AppSharedPreferences.init(this);
    }

    private void initBitmapMemoryManager() {
        bitmapMemoryManagerConfigurator = BitmapMemoryManagerConfigurator.newBuilder()
                .setMemCacheSize(MEMORY_CACHE_SIZE)
                .setDiskCacheSize(DISK_CACHE_SIZE)
                .setContext(this)
                .build();
    }

    private void initDBHelper() {
        dbHelper = new DBHelper(getApplicationContext());
    }
}