package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;

import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 12.02.2017.
 */

public class TopFriendsFragment extends FriendsFragment {

    public static final String TAG = "TOP_FRIENDS_FRAGMENT";

    public static TopFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        TopFriendsFragment topFriendsFragment = new TopFriendsFragment();
        topFriendsFragment.setTaskFinisedCallback(taskFinishedCallback);
        return topFriendsFragment;
    }

    public void setCursor(Cursor cursor) {
        swapCursor(cursor);
    }

    public TopFriendsFragment() {
    }
}
