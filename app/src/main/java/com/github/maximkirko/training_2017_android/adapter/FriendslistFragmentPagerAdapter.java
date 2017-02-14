package com.github.maximkirko.training_2017_android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.AllFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.NewFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.TopFriendsFragment;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

import java.lang.ref.WeakReference;

public class FriendslistFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Cursor cursor;
    private WeakReference<TaskFinishedCallback> taskFinishedCallbackWeakReference;

    public FriendslistFragmentPagerAdapter(@NonNull Context context, @NonNull FragmentManager fm, @NonNull Cursor cursor, TaskFinishedCallback taskFinishedCallback) {
        super(fm);
        this.context = context;
        this.cursor = cursor;
        taskFinishedCallbackWeakReference = new WeakReference<>(taskFinishedCallback);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AllFriendsFragment.newInstance(taskFinishedCallbackWeakReference.get());
            case 1:
                return NewFriendsFragment.newInstance(taskFinishedCallbackWeakReference.get());
            case 2:
                return TopFriendsFragment.newInstance(taskFinishedCallbackWeakReference.get());
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
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