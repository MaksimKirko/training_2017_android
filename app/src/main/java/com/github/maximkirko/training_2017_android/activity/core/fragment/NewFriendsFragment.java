package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;

import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 12.02.2017.
 */

public class NewFriendsFragment extends FriendsFragment {

    public static final String TAG = "NEW_FRIENDS_FRAGMENT";

    public static NewFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        NewFriendsFragment newFriendsFragment = new NewFriendsFragment();
        newFriendsFragment.setTaskFinisedCallback(taskFinishedCallback);
        return newFriendsFragment;
    }

    public NewFriendsFragment() {
    }
}
