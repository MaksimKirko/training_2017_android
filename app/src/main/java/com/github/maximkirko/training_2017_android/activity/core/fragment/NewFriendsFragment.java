package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 12.02.2017.
 */

public class NewFriendsFragment extends FriendsFragment {

    private static NewFriendsFragment newFriendsFragment;

    public static NewFriendsFragment getNewFriendsFragment() {
        return newFriendsFragment;
    }

    public static NewFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        newFriendsFragment = new NewFriendsFragment();
        newFriendsFragment.setTaskFinisedCallback(taskFinishedCallback);
        return newFriendsFragment;
    }

    public static void setCursor(Cursor cursor) {
        newFriendsFragment.swapCursor(cursor);
    }

    public NewFriendsFragment() {
    }
}
