package com.github.maximkirko.training_2017_android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.AllFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.NewFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.TopFriendsFragment;

public class FriendslistFragmentPagerAdapter extends FragmentPagerAdapter {

    public static int PAGE_COUNT = 3;

    private Context context;
    private FragmentManager fragmentManager;

    public FriendslistFragmentPagerAdapter(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragmentManager.findFragmentByTag(AllFriendsFragment.TAG);
            case 1:
                return fragmentManager.findFragmentByTag(NewFriendsFragment.TAG);
            case 2:
                return fragmentManager.findFragmentByTag(TopFriendsFragment.TAG);
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