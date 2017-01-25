package com.github.maximkirko.training_2017_android.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.FriendsAdapter;
import com.github.maximkirko.training_2017_android.adapter.FrindsDBAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.loader.FriendsAsyncLoader;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.service.DownloadService;
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

    public static DBHelper dbHelper;
    private FriendsContentProvider friendsContentProvider;

    private List<User> friends;
    private VKParameters vkParameters;
    private Intent serviceIntent;

    private static final int LOADER_FRIENDS_ID = 1;
    public static final String USER_EXTRA = "USER";

    private static final String LOG_TAG_DOWNLOAD_SERVICE_RESULT = "DOWNLOAD_SERVICE RESULT";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(DownloadService.RESULT_EXTRA, Activity.RESULT_CANCELED);
            if (result == Activity.RESULT_OK) {
                friends = intent.getParcelableArrayListExtra(DownloadService.FRIENDS_EXTRA);
                Log.i(LOG_TAG_DOWNLOAD_SERVICE_RESULT, "OK");
                initDBHelper();
                initContentProvider();
                initAdapter();
                initItemDecoration();
                initItemAnimator();
                initRecyclerView();
            } else {
                friends = null;
                Log.i(LOG_TAG_DOWNLOAD_SERVICE_RESULT, "OK");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        vkParameters = VKService.initVKParameters();
        initServiceIntent();
        // getLoaderManager().initLoader(LOADER_FRIENDS_ID, null, this);
        // startFriendsLoader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(
                DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void initServiceIntent() {
        serviceIntent = new Intent(this, DownloadService.class);
        serviceIntent.putExtra(DownloadService.VK_PARAMS_EXTRA, vkParameters);
        startService(serviceIntent);
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
        initDBHelper();
        initContentProvider();
        initAdapter();
        initItemDecoration();
        initItemAnimator();
        initRecyclerView();
    }

    private void initDBHelper() {
        dbHelper = new DBHelper(getApplicationContext(), friends);
    }

    private void initContentProvider() {
        friendsContentProvider = new FriendsContentProvider();
    }

    private void initAdapter() {
        if (friendsContentProvider != null) {
            Cursor cursor = getContentResolver().query(FriendsContentProvider.FRIENDS_CONTENT_URI, null, null, null, null);
            recyclerViewAdapter = new FrindsDBAdapter(cursor, friends, this);
        } else {
            recyclerViewAdapter = new FriendsAdapter(friends, this);
        }
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

    @Override
    public void onItemClick(int position) {
        startUserDetailsActivity(friends.get(position));
    }

    private void startUserDetailsActivity(User user) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra(USER_EXTRA, user);
        startActivity(intent);
    }
}