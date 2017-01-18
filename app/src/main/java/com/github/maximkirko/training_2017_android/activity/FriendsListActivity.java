package com.github.maximkirko.training_2017_android.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.FriendsRecyclerViewAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.loader.FriendsAsyncLoader;
import com.github.maximkirko.training_2017_android.bitmapmemorymanager.BitmapMemoryManagerConfigurator;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.vk.sdk.api.VKParameters;

import java.util.List;

public class FriendsListActivity extends AppCompatActivity
        implements UserClickListener, LoaderManager.LoaderCallbacks<List<User>> {

    //    region Music RecyclerView settings
    private RecyclerView friendsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerView.ItemAnimator itemAnimator;
    //    endregion

    private List<User> friends;
    private VKParameters vkParameters;
    private BitmapMemoryManagerConfigurator bitmapMemoryManagerConfigurator;

    private static final int LOADER_FRIENDS_ID = 1;

    public static final String USER_EXTRA = "USER";
    public static final String BMM_EXTRA = "BMM";

    @Override
    public void onItemClick(int position) {
        startUserDetailsActivity(friends.get(position));
    }

    private void startUserDetailsActivity(User user) {
        Intent intent = new Intent(FriendsListActivity.this, UserDetailsActivity.class);
        intent.putExtra(USER_EXTRA, user);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        vkParameters = VKService.initVKParameters();
        getLoaderManager().initLoader(LOADER_FRIENDS_ID, null, this);
        startFriendsLoader();
    }

    public void startFriendsLoader() {
        Loader<String> loader = getLoaderManager().getLoader(LOADER_FRIENDS_ID);
        loader.forceLoad();
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle bundle) {
        if (id == LOADER_FRIENDS_ID) {
            return new FriendsAsyncLoader(getApplicationContext(), vkParameters);
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> friends) {
        this.friends = friends;
        initAdapter();
        initItemDecoration();
        initItemAnimator();
        initRecyclerView();
    }

    private void initAdapter() {
        recyclerViewAdapter = new FriendsRecyclerViewAdapter(friends, this);
    }

    private void initItemDecoration() {
        int offset = this.getResources().getDimensionPixelSize(R.dimen.margin_friendslist_item_card);
        itemDecoration = new DefaultItemDecoration(offset);
    }

    private void initItemAnimator() {
        itemAnimator = new LandingAnimator();
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        friendsRecyclerView = (RecyclerView) findViewById(R.id.friends_recycler_view);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.addItemDecoration(itemDecoration);
        friendsRecyclerView.setItemAnimator(itemAnimator);
        friendsRecyclerView.setAdapter(recyclerViewAdapter);
    }
}