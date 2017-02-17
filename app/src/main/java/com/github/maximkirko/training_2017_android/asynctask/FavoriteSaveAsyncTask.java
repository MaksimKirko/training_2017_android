package com.github.maximkirko.training_2017_android.asynctask;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.contentobserver.FavoriteFriendsContentObserver;
import com.github.maximkirko.training_2017_android.contentprovider.FavoriteFriendsProvider;
import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 13.02.2017.
 */

public class FavoriteSaveAsyncTask extends AsyncTask<Integer, Void, Void> {

    private Context context;
    private WeakReference<TaskFinishedCallback> taskFinishedCallbackWeakReference;

    public FavoriteSaveAsyncTask(Context context, TaskFinishedCallback taskFinishedCallback) {
        this.context = context;
        taskFinishedCallbackWeakReference = new WeakReference<>(taskFinishedCallback);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        Uri uri = ContentUris.withAppendedId(FriendsContentProvider.FRIENDS_CONTENT_URI, params[0]);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        User user;
        if (cursor.moveToNext()) {
            user = UserMapper.convert(cursor);
            dbHelper.insertFavoriteFriend(dbHelper.getWritableDatabase(), context, user);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.getContentResolver().notifyChange(FavoriteFriendsContentObserver.FAVORITE_FRIENDS_URI, null);
        if (taskFinishedCallbackWeakReference != null) {
            taskFinishedCallbackWeakReference.get().onTaskFinished(this.getClass());
        }
    }
}
