package com.github.maximkirko.training_2017_android.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 13.02.2017.
 */

public class FavoriteFriendsSaveAsyncTask extends AsyncTask<User, Void, Void> {

    private Context context;

    public FavoriteFriendsSaveAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(User... params) {
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        dbHelper.insertFavoriteFriend(dbHelper.getWritableDatabase(), context, params[0]);
        return null;
    }
}
