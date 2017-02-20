package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 12.02.2017.
 */

public class AllFriendsFragment extends FriendsFragment {

    public static final String TAG = "ALL_FRIENDS_FRAGMENT";

    public static AllFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        AllFriendsFragment allFriendsFragment = new AllFriendsFragment();
        allFriendsFragment.setTaskFinisedCallback(taskFinishedCallback);
        return allFriendsFragment;
    }

    public void setCursor(Cursor cursor) {
        swapCursor(cursor);
    }

    public AllFriendsFragment() {
    }
}
