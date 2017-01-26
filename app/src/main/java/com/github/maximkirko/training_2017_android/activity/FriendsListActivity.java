package com.github.maximkirko.training_2017_android.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.FriendsAdapter;
import com.github.maximkirko.training_2017_android.adapter.FriendsDBAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.service.DownloadService;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.vk.sdk.api.VKParameters;

import java.util.List;

public class FriendsListActivity extends AppCompatActivity
        implements UserClickListener {

    private ProgressBar progressBar;

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
    private String urlFriendsRequest;
    private PendingIntent pendingIntent;
    private Intent serviceIntent;
    private AlarmManager alarmManager;

    public static final String USER_EXTRA = "USER";
    private static final String LOG_TAG_DOWNLOAD_SERVICE_RESULT = "DOWNLOAD_SERVICE RESULT";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(DownloadService.RESULT_EXTRA, Activity.RESULT_CANCELED);
            if (result == Activity.RESULT_OK) {
                progressBar.setVisibility(View.INVISIBLE);
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

        initProgressBar();
        vkParameters = VKService.initVKParameters();
        urlFriendsRequest = VKService.getUrlString(vkParameters);
        initServiceIntent();
        initPendingIntent();
        initAlarmManager();
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

    private void initPendingIntent() {
        pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
    }

    private void initAlarmManager() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 3000, 10 * 60000, pendingIntent);
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.friends_activity_progressbar);
    }

    private void initServiceIntent() {
        progressBar.setVisibility(View.VISIBLE);
        serviceIntent = new Intent(this, DownloadService.class);
        serviceIntent.putExtra(DownloadService.URL_EXTRA, urlFriendsRequest);
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
            recyclerViewAdapter = new FriendsDBAdapter(cursor, friends, this);
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
        friendsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_friends_activity);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.addItemDecoration(itemDecoration);
        friendsRecyclerView.setItemAnimator(itemAnimator);
        friendsRecyclerView.setAdapter(recyclerViewAdapter);
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
}