package com.github.maximkirko.training_2017_android.loader;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import com.github.maximkirko.training_2017_android.contentprovider.UserContentProvider;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

/**
 * Created by MadMax on 09.02.2017.
 */

public class UserDataCursorLoader extends CursorLoader {

    public static final int LOADER_ID = 1;

    private UserContentProvider userContentProvider;

    public UserDataCursorLoader(Context context) {
        super(context);
        this.userContentProvider = new UserContentProvider();
    }

    @Override
    public Cursor loadInBackground() {
        //Uri uri = ContentUris.withAppendedId(UserContentProvider.CONTENT_URI, AppSharedPreferences.getInt(VKService.USER_ID_PREFERENCE, 0));
        return userContentProvider.query(UserContentProvider.USER_CONTENT_URI, null, null, null, null);
    }
}
