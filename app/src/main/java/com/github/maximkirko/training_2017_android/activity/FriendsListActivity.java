package com.github.maximkirko.training_2017_android.activity;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import com.github.maximkirko.training_2017_android.adapter.FriendsCursorAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DeviceLoadingBroadcastReceiver;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.loader.FriendsCursorLoader;
import com.github.maximkirko.training_2017_android.broadcastreceiver.BroadcastReceiverCallback;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.service.DownloadService;

public class FriendsListActivity extends AppCompatActivity
        implements UserClickListener, BroadcastReceiverCallback, LoaderManager.LoaderCallbacks<Cursor> {
    // region Views
    private ProgressBar progressBar;
    // endregion

    //    region Music RecyclerView settings
    private RecyclerView friendsRecyclerView;
    private FriendsCursorAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerView.ItemAnimator itemAnimator;
    //    endregion

    // region Friends loading properties
    private Cursor cursor;
    private PendingIntent pendingIntent;
    private Intent serviceIntent;
    private AlarmManager alarmManager;
    private DeviceLoadingBroadcastReceiver deviceLoadingBroadcastReceiver;
    private DownloadServiceBroadcastReceiver downloadServiceBroadcastReceiver;

    private static final int LOADER_FRIENDS_ID = 1;
    private static final int ALARM_MANAGER_REPEATING_TIME = 1000 * 30 * 1; // 30 seconds
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        // initialize and show ProgressBar
        initProgressBar();
        enableProgressBar();

        // initialize download service
        initDeviceLoadingBroadcastReceiver();
        initDownloadServiceBroadcastReceiver();
        initServiceIntent(true);
        startServiceIntent();
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.friends_activity_progressbar);
    }

    private void enableProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void disableProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initDeviceLoadingBroadcastReceiver() {
        deviceLoadingBroadcastReceiver = new DeviceLoadingBroadcastReceiver(this);
    }

    private void initDownloadServiceBroadcastReceiver() {
        downloadServiceBroadcastReceiver = new DownloadServiceBroadcastReceiver(this);
    }

    private void initServiceIntent(boolean isFirstLoading) {
        serviceIntent = new Intent(this, DownloadService.class);
        serviceIntent.putExtra(DownloadService.IS_FIRST_LOADING_EXTRAS, isFirstLoading);
    }

    private void startServiceIntent() {
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(downloadServiceBroadcastReceiver, new IntentFilter(DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadServiceBroadcastReceiver);
    }

    @Override
    public void onItemClick(int id) {
        startUserDetailsActivity(id);
    }

    private void startUserDetailsActivity(int id) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.USER_EXTRA, id);
        startActivity(intent);
    }

    @Override
    public void onReceived(Class<? extends BroadcastReceiver> broadcastReceiver) {
        if (broadcastReceiver == DownloadServiceBroadcastReceiver.class) {
            startFriendsLoader();
        } else if (broadcastReceiver == DeviceLoadingBroadcastReceiver.class) {
            initAlarmManager();
        }
    }

    private void initAlarmManager() {
        initServiceIntent(false);
        initPendingIntent();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), ALARM_MANAGER_REPEATING_TIME, pendingIntent);
    }

    private void initPendingIntent() {
        pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
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
        disableProgressBar();
        if (this.cursor == null) {
            this.cursor = cursor;
            initAdapter();
            initRecyclerView();
            return;
        }
        this.cursor = cursor;
        recyclerViewAdapter.setCursor(cursor);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        recyclerViewAdapter = new FriendsCursorAdapter(cursor, this);
        initItemDecoration();
        initItemAnimator();
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