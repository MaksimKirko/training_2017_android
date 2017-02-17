package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 14.02.2017.
 */

public class FavoriteFriendsFragment extends FriendsFragment {

    private static FavoriteFriendsFragment favoriteFriendsFragment;

    public static FavoriteFriendsFragment getFavoriteFriendsFragment() {
        return favoriteFriendsFragment;
    }

    public static FavoriteFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        favoriteFriendsFragment = new FavoriteFriendsFragment();
        favoriteFriendsFragment.setTaskFinisedCallback(taskFinishedCallback);
        return favoriteFriendsFragment;
    }

    public static void setCursor(Cursor cursor) {
        favoriteFriendsFragment.swapCursor(cursor);
    }

    public FavoriteFriendsFragment() {
    }
}