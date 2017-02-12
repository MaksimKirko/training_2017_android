package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

/**
 * Created by MadMax on 12.02.2017.
 */

public class AllFriendsFragment extends FriendsFragment {

    public static AllFriendsFragment newInstance(Cursor cursor) {
        AllFriendsFragment allFriendsFragment = new AllFriendsFragment();
        allFriendsFragment.cursor = cursor;
        return allFriendsFragment;
    }

    public AllFriendsFragment() {
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
