package com.github.maximkirko.training_2017_android.asynctask;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.contentprovider.FavoriteFriendsProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 14.02.2017.
 */

public class FavoriteRemoveAsyncTask extends AsyncTask<Integer, Void, Void> {

    private Context context;
    private WeakReference<TaskFinishedCallback> taskFinishedCallbackWeakReference;

    public FavoriteRemoveAsyncTask(Context context, TaskFinishedCallback taskFinishedCallback) {
        this.context = context;
        taskFinishedCallbackWeakReference = new WeakReference<>(taskFinishedCallback);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        Uri uri = ContentUris.withAppendedId(FavoriteFriendsProvider.FAVORITE_FRIENDS_CONTENT_URI, params[0]);
        context.getContentResolver().delete(uri, null, null);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (taskFinishedCallbackWeakReference != null) {
            taskFinishedCallbackWeakReference.get().onTaskFinished();
        }
    }
}
