package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;

/**
 * Created by MadMax on 14.02.2017.
 */

public class FavoriteFriendsFragment extends FriendsFragment {

    public static FavoriteFriendsFragment newInstance() {
        FavoriteFriendsFragment favoriteFriendsFragment = new FavoriteFriendsFragment();
        favoriteFriendsFragment.cursor = FriendsListActivity.getFavoriteFriendsCursor();
        return favoriteFriendsFragment;
    }

    public FavoriteFriendsFragment() {
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