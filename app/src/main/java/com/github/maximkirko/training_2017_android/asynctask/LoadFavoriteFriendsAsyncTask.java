package com.github.maximkirko.training_2017_android.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 13.02.2017.
 */

public class LoadFavoriteFriendsAsyncTask extends AsyncTask<User, Void, Cursor> {

    private Context context;

    public LoadFavoriteFriendsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Cursor doInBackground(User... params) {
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        return dbHelper.getFavoriteFriends(dbHelper.getWritableDatabase());
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);

    }
}
