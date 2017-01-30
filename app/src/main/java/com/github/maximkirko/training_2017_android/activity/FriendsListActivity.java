package com.github.maximkirko.training_2017_android.activity;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.FriendsDBAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.loader.FriendsCursorLoader;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.receiver.BroadcastReceiverCallback;
import com.github.maximkirko.training_2017_android.receiver.DownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.service.DownloadService;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.vk.sdk.api.VKParameters;

import java.util.List;

public class FriendsListActivity extends AppCompatActivity
        implements UserClickListener, BroadcastReceiverCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private ProgressBar progressBar;

    //    region Music RecyclerView settings
    private RecyclerView friendsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerView.ItemAnimator itemAnimator;
    //    endregion

    private List<User> friends;
    private VKParameters vkParameters;
    private String urlFriendsRequest;
    private PendingIntent pendingIntent;
    private Intent serviceIntent;
    private AlarmManager alarmManager;
    private DownloadServiceBroadcastReceiver broadcastReceiver;

    public static final String USER_EXTRA = "USER";
    private static final int LOADER_FRIENDS_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        initProgressBar();
        vkParameters = VKService.initVKParameters();
        urlFriendsRequest = VKService.getUrlString(vkParameters);
        initBroadcastReceiver();
        initServiceIntent();
        initPendingIntent();
        initAlarmManager();
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.friends_activity_progressbar);
    }

    private void initBroadcastReceiver() {
        broadcastReceiver = new DownloadServiceBroadcastReceiver(friends, this);
    }

    private void initServiceIntent() {
        progressBar.setVisibility(View.VISIBLE);
        serviceIntent = new Intent(this, DownloadService.class);
        serviceIntent.putExtra(DownloadService.URL_EXTRA, urlFriendsRequest);
    }

    private void initPendingIntent() {
        pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
    }

    private void initAlarmManager() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), 1000 * 60 * 10, pendingIntent);
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

    @Override
    public void onItemClick(int id) {
        startUserDetailsActivity(id);
    }

    private void startUserDetailsActivity(int id) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra(USER_EXTRA, id);
        startActivity(intent);
    }

    @Override
    public void onReceived() {
        progressBar.setVisibility(View.INVISIBLE);
        startFriendsLoader();
    }

    public void startFriendsLoader() {
        getLoaderManager().initLoader(LOADER_FRIENDS_ID, null, this);
        Loader<Cursor> loader = getLoaderManager().getLoader(LOADER_FRIENDS_ID);
        loader.forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == LOADER_FRIENDS_ID) {
            return new FriendsCursorLoader(getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        initAdapter(cursor);
        initItemDecoration();
        initItemAnimator();
        initRecyclerView();
    }

    private void initAdapter(Cursor cursor) {
        recyclerViewAdapter = new FriendsDBAdapter(cursor, this);
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
        friendsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_friends_activity);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.addItemDecoration(itemDecoration);
        friendsRecyclerView.setItemAnimator(itemAnimator);
        friendsRecyclerView.setAdapter(recyclerViewAdapter);
    }
}