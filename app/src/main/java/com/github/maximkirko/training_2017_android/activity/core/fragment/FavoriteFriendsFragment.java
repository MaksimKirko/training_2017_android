package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;

import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 14.02.2017.
 */

public class FavoriteFriendsFragment extends FriendsFragment {

    public static final String TAG = "FAVORITE_FRIENDS_FRAGMENT";

    public static FavoriteFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        FavoriteFriendsFragment favoriteFriendsFragment = new FavoriteFriendsFragment();
        favoriteFriendsFragment.setTaskFinisedCallback(taskFinishedCallback);
        return favoriteFriendsFragment;
    }

    public FavoriteFriendsFragment() {
    }
}