package com.github.maximkirko.training_2017_android.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.FriendsRecyclerViewAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.SpacesItemDecoration;
import com.github.maximkirko.training_2017_android.load.FriendsJSONReader;
import com.github.maximkirko.training_2017_android.load.Reader;
import com.github.maximkirko.training_2017_android.loader.FriendsAsyncLoader;
import com.github.maximkirko.training_2017_android.model.Song;
import com.github.maximkirko.training_2017_android.model.User;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;

import java.io.IOException;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity
        implements UserClickListener, LoaderManager.LoaderCallbacks<String> {

    //    region Views
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabRemove;
//    endregion

    //    region Music RecyclerView settings
    private RecyclerView friendsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerView.ItemAnimator itemAnimator;
//    endregion

    private List<User> friends;
    private Reader<User> friendsJsonReader;
    private SharedPreferences sharedPreferences;
    private VKParameters vkParameters;

    private static final String ACCESS_PERMISSION_PREFERENCE = "ACCESS_PERMISSION";
    private static final int LOADER_FRIENDS_ID = 1;

    public static final String USER_EXTRA = "USER";

    @Override
    public void onItemClick(Song song) {
        startSongActivity(song);
    }

    private void startSongActivity(Song song) {
        Intent intent = new Intent(FriendsListActivity.this, UserDetailsActivity.class);
        intent.putExtra(USER_EXTRA, song);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        initSharedPreferences();

        if (!sharedPreferences.getBoolean(ACCESS_PERMISSION_PREFERENCE, false)) {
            VKSdk.login(this, VKScope.FRIENDS);
            saveAccessPermission();
        }
        initFabs();
        initVKParameters();
        getLoaderManager().initLoader(LOADER_FRIENDS_ID, null, this);
        startFriendsLoader();
    }

    private void initSharedPreferences() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
    }

    public void saveAccessPermission() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ACCESS_PERMISSION_PREFERENCE, true);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {

            @Override
            public void onResult(VKAccessToken res) {
                Log.i("AUTORIZATION", "TRUE");
            }

            @Override
            public void onError(VKError error) {
                Log.i("AUTORIZATION", "ERROR");
            }

        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initFabs() {
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_friendslist_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friends.add(new User());
                recyclerViewAdapter.notifyItemInserted(friends.size());
            }
        });

        fabRemove = (FloatingActionButton) findViewById(R.id.fab_friendslist_remove);
        fabRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = friends.size();
                if (index > 1) {
                    friends.remove(index - 1);
                    recyclerViewAdapter.notifyItemRemoved(index + 1);
                }
            }
        });
    }

    private void initVKParameters() {
        vkParameters = new VKParameters();
        vkParameters.put(VKApiConst.ACCESS_TOKEN, VKSdk.getAccessToken());
        vkParameters.put(VKApiConst.FIELDS, "nickname, online, photo_50");
    }

    public void startFriendsLoader() {
        Loader<String> loader;
        loader = getLoaderManager().getLoader(LOADER_FRIENDS_ID);
        loader.forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Loader<String> loader = null;
        if (id == LOADER_FRIENDS_ID) {
            loader = new FriendsAsyncLoader(this, vkParameters);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        initJSONReader(s);
        initAdapter();
        initItemDecoration();
        initItemAnimator();
        initRecyclerView();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private void initJSONReader(String jsonFriendsList) {
        friendsJsonReader = new FriendsJSONReader(jsonFriendsList);
    }

    private void initAdapter() {
        try {
            friends = friendsJsonReader.readToList(this);
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        recyclerViewAdapter = new FriendsRecyclerViewAdapter(friends, this, this);
    }

    private void initItemDecoration() {
        int offset = this.getResources().getDimensionPixelSize(R.dimen.margin_friendslist_item_card);
        itemDecoration = new SpacesItemDecoration(offset);//new MusicListItemDecorator(offset);
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
