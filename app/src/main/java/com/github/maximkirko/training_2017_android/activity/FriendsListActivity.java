package com.github.maximkirko.training_2017_android.activity;

import android.content.Context;
import android.content.Intent;
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
import com.github.maximkirko.training_2017_android.itemdecorator.MusicListItemDecorator;
import com.github.maximkirko.training_2017_android.itemdecorator.SpacesItemDecoration;
import com.github.maximkirko.training_2017_android.load.FriendsJSONReader;
import com.github.maximkirko.training_2017_android.load.Reader;
import com.github.maximkirko.training_2017_android.model.Song;
import com.github.maximkirko.training_2017_android.model.User;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity
        implements UserClickListener {

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
    Reader<User> friendsJsonReader;

    private SharedPreferences sharedPreferences;
    private static final String ACCESS_PERMISSION_PREFERENCE = "ACCESS_PERMISSION";

    public static final String USER_EXTRA = "USER";
    public static final int FRIENDS_RESOURCE_ID = R.raw.friendslist_vk_data;
    private static String jsonFriendsList;

    private static boolean flag = false;

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

        getFriendsList();

        initJSONReader();
        initAdapter();
        initItemDecoration();
        initItemAnimator();
        initRecyclerView();
        initFabs();
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

    private void getFriendsList() {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.ACCESS_TOKEN, VKSdk.getAccessToken());
        params.put(VKApiConst.FIELDS, "nickname, online, photo_50");
        final VKRequest request = VKApi.friends().get(params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                jsonFriendsList = response.json.toString();
                Log.i("RESPONSE", response.json.toString());
                flag = true;
            }

            @Override
            public void onError(VKError error) {
                Log.i("ERROR", "request");
                flag = true;
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.i("ATTEMPT FAILED", "request");
                flag = true;
            }
        });
    }

    private void initJSONReader() {
        friendsJsonReader = new FriendsJSONReader(FRIENDS_RESOURCE_ID);
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
}
