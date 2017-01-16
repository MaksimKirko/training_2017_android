package com.github.maximkirko.training_2017_android.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.FriendsRecyclerViewAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.loader.FriendsAsyncLoader;
import com.github.maximkirko.training_2017_android.memorymanage.BitmapMemoryManager;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.read.FriendsJSONReader;
import com.github.maximkirko.training_2017_android.read.Reader;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;

import java.io.IOException;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity
        implements UserClickListener, LoaderManager.LoaderCallbacks<String> {

    //    region Music RecyclerView settings
    private RecyclerView friendsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerView.ItemAnimator itemAnimator;
//    endregion

    private List<User> friends;
    private Reader<User> friendsJsonReader;
    private VKParameters vkParameters;
    private BitmapMemoryManager bitmapMemoryManager;

    private static final int LOADER_FRIENDS_ID = 1;

    public static final String USER_EXTRA = "USER";
    public static final String BMM_EXTRA = "BMM";

    private static final int MEMORY_CACHE_SIZE = 4194304;
    private static final int DISK_CACHE_SIZE = 4194304;

    @Override
    public void onItemClick(int position) {
        startUserDetailsActivity(friends.get(position));
    }

    private void startUserDetailsActivity(User user) {
        Intent intent = new Intent(FriendsListActivity.this, UserDetailsActivity.class);
        intent.putExtra(USER_EXTRA, user);
        intent.putExtra(BMM_EXTRA, bitmapMemoryManager);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        vkParameters = VKService.initVKParameters();
        initBitmapMemoryManager();
        getLoaderManager().initLoader(LOADER_FRIENDS_ID, null, this);
        startFriendsLoader();
    }

    private void initBitmapMemoryManager() {
        bitmapMemoryManager = BitmapMemoryManager.newBuilder()
                .setMemCacheSize(MEMORY_CACHE_SIZE)
                .setDiskCacheSize(DISK_CACHE_SIZE)
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setImageHeight(getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .setImageWidth(getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .build();
    }

    public void startFriendsLoader() {
        Loader<String> loader = getLoaderManager().getLoader(LOADER_FRIENDS_ID);
        loader.forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle bundle) {
        if (id == LOADER_FRIENDS_ID) {
            return new FriendsAsyncLoader(getApplicationContext(), vkParameters);
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        initFriendsList(s);
        initAdapter();
        initItemDecoration();
        initItemAnimator();
        initRecyclerView();
    }

    private void initFriendsList(String jsonFriendsList) {
        if (jsonFriendsList == null) {
            return;
        }
        friendsJsonReader = new FriendsJSONReader(jsonFriendsList);
        try {
            friends = friendsJsonReader.readToList();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
    }

    private void initAdapter() {
        recyclerViewAdapter = new FriendsRecyclerViewAdapter(friends, this, bitmapMemoryManager);
    }

    private void initItemDecoration() {
        int offset = this.getResources().getDimensionPixelSize(R.dimen.margin_friendslist_item_card);
        itemDecoration = new DefaultItemDecoration(offset);//new GridLayoutItemDecorator(offset);
    }

    private void initItemAnimator() {
        itemAnimator = new LandingAnimator();
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);//new GridLayoutManager(this, 2);
        friendsRecyclerView = (RecyclerView) findViewById(R.id.friends_recycler_view);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.addItemDecoration(itemDecoration);
        friendsRecyclerView.setItemAnimator(itemAnimator);
        friendsRecyclerView.setAdapter(recyclerViewAdapter);
    }
}
