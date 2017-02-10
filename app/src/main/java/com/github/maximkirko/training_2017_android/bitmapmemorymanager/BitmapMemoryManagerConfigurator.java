package com.github.maximkirko.training_2017_android.bitmapmemorymanager;

import android.content.Context;

/**
 * Created by MadMax on 15.01.2017.
 */

public class BitmapMemoryManagerConfigurator {

    private BitmapMemoryCacheManager bitmapMemoryCacheManager;
    private BitmapDiskCacheManager bitmapDiskCacheManager;
    private int memCacheSize;
    private int diskCacheSize;
    private Context context;

    public BitmapMemoryCacheManager getBitmapMemoryCacheManager() {
        return bitmapMemoryCacheManager;
    }

    public BitmapDiskCacheManager getBitmapDiskCacheManager() {
        return bitmapDiskCacheManager;
    }

    private BitmapMemoryManagerConfigurator() {
    }

    public static Builder newBuilder() {
        return new BitmapMemoryManagerConfigurator().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder setMemCacheSize(int memCacheSize) {
            BitmapMemoryManagerConfigurator.this.memCacheSize = memCacheSize;
            return this;
        }

        public Builder setDiskCacheSize(int diskCacheSize) {
            BitmapMemoryManagerConfigurator.this.diskCacheSize = diskCacheSize;
            return this;
        }

        public Builder setContext(Context context) {
            BitmapMemoryManagerConfigurator.this.context = context;
            return this;
        }

        public BitmapMemoryManagerConfigurator build() {
            BitmapMemoryManagerConfigurator bitmapMemoryManagerConfigurator = new BitmapMemoryManagerConfigurator();
            bitmapMemoryManagerConfigurator.bitmapMemoryCacheManager = new BitmapMemoryCacheManager(memCacheSize);
            bitmapMemoryManagerConfigurator.bitmapDiskCacheManager = new BitmapDiskCacheManager(diskCacheSize, context);
            return bitmapMemoryManagerConfigurator;
        }
    }
}
