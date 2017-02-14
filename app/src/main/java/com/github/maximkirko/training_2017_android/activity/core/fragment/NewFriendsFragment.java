package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 12.02.2017.
 */

public class NewFriendsFragment extends FriendsFragment {

    public static NewFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        NewFriendsFragment newFriendsFragment = new NewFriendsFragment();
        newFriendsFragment.init(taskFinishedCallback);
        newFriendsFragment.cursor = FriendsListActivity.getNewFriendsCursor();
        return newFriendsFragment;
    }

    public NewFriendsFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initAdapter();
        initRecyclerView();
        if (cursor != null) {
            recyclerViewAdapter.setCursor(cursor);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
