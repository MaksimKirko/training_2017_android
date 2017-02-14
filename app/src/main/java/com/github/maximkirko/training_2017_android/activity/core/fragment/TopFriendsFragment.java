package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 12.02.2017.
 */

public class TopFriendsFragment extends FriendsFragment {

    public static TopFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        TopFriendsFragment topFriendsFragment = new TopFriendsFragment();
        topFriendsFragment.init(taskFinishedCallback);
        topFriendsFragment.cursor = FriendsListActivity.getTopFriendsCursor();
        return topFriendsFragment;
    }

    public TopFriendsFragment() {
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
