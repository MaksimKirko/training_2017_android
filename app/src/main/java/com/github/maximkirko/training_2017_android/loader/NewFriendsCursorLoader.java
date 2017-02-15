package com.github.maximkirko.training_2017_android.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;

/**
 * Created by MadMax on 14.02.2017.
 */

public class NewFriendsCursorLoader extends CursorLoader {

    public static final int LOADER_ID = 4;

    public NewFriendsCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return getContext().getContentResolver().query(FriendsContentProvider.FRIENDS_CONTENT_URI, null, null, null, DBHelper.FRIENDS_TABLE_FIELD_LAST_SEEN + " DESC LIMIT " + FriendsListActivity.NEW_COUNT_PREFERENCE);
    }
}