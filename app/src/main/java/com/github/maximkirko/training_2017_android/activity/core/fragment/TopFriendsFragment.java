package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

/**
 * Created by MadMax on 12.02.2017.
 */

public class TopFriendsFragment extends FriendsFragment {

    public static TopFriendsFragment newInstance(Cursor cursor) {
        TopFriendsFragment topFriendsFragment = new TopFriendsFragment();
        topFriendsFragment.cursor = cursor;
        return topFriendsFragment;
    }

    public TopFriendsFragment() {
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
