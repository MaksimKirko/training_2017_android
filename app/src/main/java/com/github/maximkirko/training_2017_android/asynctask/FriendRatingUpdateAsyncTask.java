package com.github.maximkirko.training_2017_android.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.contentobserver.FavoriteFriendsContentObserver;
import com.github.maximkirko.training_2017_android.db.DBHelper;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 17.02.2017.
 */

public class FriendRatingUpdateAsyncTask extends AsyncTask<Integer, Void, Void> {

    private Context context;
    private WeakReference<TaskFinishedCallback> taskFinishedCallbackWeakReference;

    public FriendRatingUpdateAsyncTask(Context context, TaskFinishedCallback taskFinishedCallback) {
        this.context = context;
        taskFinishedCallbackWeakReference = new WeakReference<>(taskFinishedCallback);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        dbHelper.updateFriendRating(dbHelper.getWritableDatabase(), params[0], params[1]);
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
