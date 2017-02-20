package com.github.maximkirko.training_2017_android.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

/**
 * Created by MadMax on 18.02.2017.
 */

public class SearchFriendsCursorLoader extends CursorLoader {

    public static final int LOADER_ID = 6;

    private String tableName;
    private String query;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SearchFriendsCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        return dbHelper.searchFriends(dbHelper.getReadableDatabase(), tableName, query);
    }
}
