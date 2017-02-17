package com.github.maximkirko.training_2017_android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.AllFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FavoriteFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.NewFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.TopFriendsFragment;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

import java.lang.ref.WeakReference;

public class FriendslistFragmentPagerAdapter extends FragmentPagerAdapter {

    public static int PAGE_COUNT = 3;

    private Context context;

    public FriendslistFragmentPagerAdapter(@NonNull Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FavoriteFriendsFragment.getFavoriteFriendsFragment().getView().setVisibility(View.INVISIBLE);
                return AllFriendsFragment.getAllFriendsFragment();
            case 1:
                return NewFriendsFragment.getNewFriendsFragment();
            case 2:
                return TopFriendsFragment.getTopFriendsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.friendslist_viewpager_title_1);
            case 1:
                return context.getString(R.string.friendslist_viewpager_title_2);
            case 2:
                return context.getString(R.string.friendslist_viewpager_title_3);
        }
        return null;
    }
}