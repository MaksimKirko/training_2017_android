package com.github.maximkirko.training_2017_android.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

/**
 * Created by MadMax on 13.02.2017.
 */

public class FavoriteFriendsCursorLoader extends CursorLoader {

    public static final int LOADER_ID = 3;

    public FavoriteFriendsCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        return dbHelper.getFavoriteFriends(dbHelper.getWritableDatabase());
    }
}
