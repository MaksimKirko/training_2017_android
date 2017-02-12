package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

/**
 * Created by MadMax on 12.02.2017.
 */

public class NewFriendsFragment extends FriendsFragment {

    public static NewFriendsFragment newInstance(Cursor cursor) {
        NewFriendsFragment newFriendsFragment = new NewFriendsFragment();
        newFriendsFragment.cursor = cursor;
        return newFriendsFragment;
    }

    public NewFriendsFragment() {
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        initAdapter();
        initRecyclerView();
        if(cursor != null) {
            recyclerViewAdapter.setCursor(cursor);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void attachCursor(Cursor cursor) {
        recyclerViewAdapter.setCursor(cursor);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
