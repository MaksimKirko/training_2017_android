package com.github.maximkirko.training_2017_android.contentobserver;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 15.02.2017.
 */

public class FavoriteFriendsContentObserver extends ContentObserver {

    private WeakReference<ContentObserverCallback> contentObserverCallbackWeakReference;

    public FavoriteFriendsContentObserver(Handler handler, ContentObserverCallback contentObserverCallback) {
        super(handler);
        contentObserverCallbackWeakReference = new WeakReference<>(contentObserverCallback);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if(contentObserverCallbackWeakReference != null) {
            contentObserverCallbackWeakReference.get().onChange();
        }
    }
}
