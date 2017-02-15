package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 12.02.2017.
 */

public class AllFriendsFragment extends FriendsFragment {

    public static RecyclerView.Adapter adapter;

    public static AllFriendsFragment newInstance(TaskFinishedCallback taskFinishedCallback) {
        AllFriendsFragment allFriendsFragment = new AllFriendsFragment();
        allFriendsFragment.init(taskFinishedCallback);
        allFriendsFragment.cursor = FriendsListActivity.getAllFriendsCursor();
        adapter = allFriendsFragment.recyclerViewAdapter;
        return allFriendsFragment;
    }

    public AllFriendsFragment() {
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
