package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.FriendsCursorAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.CheckBoxOnChangeListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.asynctask.FavoriteRemoveAsyncTask;
import com.github.maximkirko.training_2017_android.asynctask.FavoriteSaveAsyncTask;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.navigator.IntentManager;

import java.lang.ref.WeakReference;

/**
 * Created by MadMax on 12.02.2017.
 */

public abstract class FriendsFragment extends Fragment implements UserClickListener, CheckBoxOnChangeListener {

    protected View v;
    protected WeakReference<TaskFinishedCallback> taskFinishedCallbackWeakReference;

    //    region Music RecyclerView settings
    protected RecyclerView friendsRecyclerView;
    protected FriendsCursorAdapter recyclerViewAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected RecyclerView.ItemDecoration itemDecoration;
    protected RecyclerView.ItemAnimator itemAnimator;
    //    endregion

    protected void setTaskFinisedCallback(TaskFinishedCallback taskFinishedCallback) {
        taskFinishedCallbackWeakReference = new WeakReference<>(taskFinishedCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(false);
        v = inflater.inflate(R.layout.friendslist_fragment, container, false);
        if (recyclerViewAdapter == null) {
            initAdapter();
            initRecyclerView();
        }
        return v;
    }

    protected void swapCursor(Cursor cursor) {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.setCursor(cursor);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int id) {
        startActivity(IntentManager.getIntentForUserDetailsActivity(getActivity().getBaseContext(), id));
    }

    protected void initAdapter() {
        recyclerViewAdapter = new FriendsCursorAdapter(null, this, this);
        initItemDecoration();
        initItemAnimator();
    }

    protected void initItemDecoration() {
        int offset = this.getResources().getDimensionPixelSize(R.dimen.margin_friendslist_item_card);
        itemDecoration = new DefaultItemDecoration(offset);
    }

    protected void initItemAnimator() {
        itemAnimator = new LandingAnimator();
    }

    protected void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        friendsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_friends_activity);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.addItemDecoration(itemDecoration);
        friendsRecyclerView.setItemAnimator(itemAnimator);
        friendsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onChecked(int id, boolean isChecked) {
        if (isChecked) {
            startFavoriteSaveTask(id);
        } else {
            startFavoriteRemoveTask(id);
        }
    }

    protected void startFavoriteSaveTask(int id) {
        if (taskFinishedCallbackWeakReference != null) {
            FavoriteSaveAsyncTask favoriteSaveAsyncTask = new FavoriteSaveAsyncTask(getContext(), taskFinishedCallbackWeakReference.get());
            favoriteSaveAsyncTask.execute(id);
        }
    }

    protected void startFavoriteRemoveTask(int id) {
        if (taskFinishedCallbackWeakReference != null) {
            FavoriteRemoveAsyncTask favoriteRemoveAsyncTask = new FavoriteRemoveAsyncTask(getContext(), taskFinishedCallbackWeakReference.get());
            favoriteRemoveAsyncTask.execute(id);
        }
    }
}
