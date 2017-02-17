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

public class TopFriendsFragment extends FriendsFragment {

    private static TopFriendsFragment topFriendsFragment;

    public static TopFriendsFragment getTopFriendsFragment() {
        return topFriendsFragment;
    }

    public static TopFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        topFriendsFragment = new TopFriendsFragment();
        topFriendsFragment.setTaskFinisedCallback(taskFinishedCallback);
        return topFriendsFragment;
    }

    public static void setCursor(Cursor cursor) {
        topFriendsFragment.swapCursor(cursor);
    }

    public TopFriendsFragment() {
    }
}
