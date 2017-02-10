package com.github.maximkirko.training_2017_android.activity.core;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.login.LoginActivity;
import com.github.maximkirko.training_2017_android.adapter.FriendsCursorAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.broadcastreceiver.BroadcastReceiverCallback;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DeviceLoadingBroadcastReceiver;
import com.github.maximkirko.training_2017_android.broadcastreceiver.FriendsDownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.broadcastreceiver.UserDataDownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.DefaultItemDecoration;
import com.github.maximkirko.training_2017_android.loader.FriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.UserDataCursorLoader;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.service.FriendsDownloadService;
import com.github.maximkirko.training_2017_android.service.UserDataDownloadService;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

public class FriendsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserClickListener, BroadcastReceiverCallback, LoaderManager.LoaderCallbacks<Cursor> {

    // region Views
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View navHeaderView;
    private TextView headerTitleView;
    private TextView headerDescriptionView;
    private ImageView headerImageView;
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
    private User user;
    private Cursor friendsCursor;
    private PendingIntent pendingIntent;
    private Intent userDataServiceIntent;
    private Intent friendsServiceIntent;
    private AlarmManager alarmManager;
    private DeviceLoadingBroadcastReceiver deviceLoadingBroadcastReceiver;
    private UserDataDownloadServiceBroadcastReceiver userDataDownloadServiceBroadcastReceiver;
    private FriendsDownloadServiceBroadcastReceiver friendsDownloadServiceBroadcastReceiver;

    private static final int ALARM_MANAGER_REPEATING_TIME = 1000 * 30 * 1; // 30 seconds
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        // init toolbar and drawer layout
        initToolbar();
        initDrawerLayout();

        // initialize and show ProgressBar
        initProgressBar();
        enableProgressBar();

        initDeviceLoadingBroadcastReceiver();

        // initialize user data download service
        initUserDataDownloadServiceBroadcastReceiver();
        initUserDataServiceIntent();
        startServiceIntent(userDataServiceIntent);

        // initialize friends download service
        initFriendsDownloadServiceBroadcastReceiver();
        initFriendsServiceIntent(true);
        startServiceIntent(friendsServiceIntent);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void initDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        initActionBarDrawerToggle();
        initNavigationView();
    }

    private void initActionBarDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationViewHeader();
    }

    private void initNavigationViewHeader() {
        navHeaderView = navigationView.getHeaderView(0);
        navHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    startUserDetailsActivity(AppSharedPreferences.getInt(VKService.USER_ID_PREFERENCE, 0));
                }
            }
        });
        headerTitleView = (TextView) navHeaderView.findViewById(R.id.textview_navigation_drawer_header_title);
        headerDescriptionView = (TextView) navHeaderView.findViewById(R.id.textview_navigation_drawer_header_description);
        headerImageView = (ImageView) navHeaderView.findViewById(R.id.imageview_navigation_drawer_header);
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

    private void initUserDataDownloadServiceBroadcastReceiver() {
        userDataDownloadServiceBroadcastReceiver = new UserDataDownloadServiceBroadcastReceiver(this);
    }

    private void initFriendsDownloadServiceBroadcastReceiver() {
        friendsDownloadServiceBroadcastReceiver = new FriendsDownloadServiceBroadcastReceiver(this);
    }

    private void initUserDataServiceIntent() {
        userDataServiceIntent = new Intent(this, UserDataDownloadService.class);
    }

    private void initFriendsServiceIntent(boolean isFirstLoading) {
        friendsServiceIntent = new Intent(this, FriendsDownloadService.class);
        friendsServiceIntent.putExtra(FriendsDownloadService.IS_FIRST_LOADING_EXTRAS, isFirstLoading);
    }

    private void startServiceIntent(Intent serviceIntent) {
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(userDataDownloadServiceBroadcastReceiver, new IntentFilter(UserDataDownloadService.NOTIFICATION));
        registerReceiver(friendsDownloadServiceBroadcastReceiver, new IntentFilter(FriendsDownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(userDataDownloadServiceBroadcastReceiver);
        unregisterReceiver(friendsDownloadServiceBroadcastReceiver);
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
        if (broadcastReceiver == UserDataDownloadServiceBroadcastReceiver.class) {
            startUserDataLoader();
        } else if (broadcastReceiver == FriendsDownloadServiceBroadcastReceiver.class) {
            startFriendsLoader();
        } else if (broadcastReceiver == DeviceLoadingBroadcastReceiver.class) {
            initAlarmManager();
        }
    }

    private void startUserDataLoader() {
        getLoaderManager().initLoader(UserDataCursorLoader.USER_DATA_LOADER_ID, null, this);
        Loader<Cursor> loader = getLoaderManager().getLoader(UserDataCursorLoader.USER_DATA_LOADER_ID);
        loader.forceLoad();
    }

    private void initAlarmManager() {
        initFriendsServiceIntent(false);
        initPendingIntent();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                ALARM_MANAGER_REPEATING_TIME, ALARM_MANAGER_REPEATING_TIME, pendingIntent);
    }

    private void initPendingIntent() {
        pendingIntent = PendingIntent.getService(this, 0, friendsServiceIntent, 0);
    }

    private void startFriendsLoader() {
        getLoaderManager().initLoader(FriendsCursorLoader.FRIENDS_LOADER_ID, null, this);
        Loader<Cursor> loader = getLoaderManager().getLoader(FriendsCursorLoader.FRIENDS_LOADER_ID);
        loader.forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == UserDataCursorLoader.USER_DATA_LOADER_ID) {
            return new UserDataCursorLoader(getApplicationContext());
        } else if (id == FriendsCursorLoader.FRIENDS_LOADER_ID) {
            return new FriendsCursorLoader(getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        System.out.println(loader.getClass().getSimpleName());
        if (loader instanceof UserDataCursorLoader) {
            if (cursor.moveToNext()) {
                user = UserMapper.convert(cursor);
                setUserData();
            }
        } else if (loader instanceof FriendsCursorLoader) {
            disableProgressBar();
            if (this.friendsCursor == null) {
                this.friendsCursor = cursor;
                initAdapter();
                initRecyclerView();
                return;
            }
            this.friendsCursor = cursor;
            recyclerViewAdapter.setCursor(cursor);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void setUserData() {
        if (user != null) {
            headerTitleView.setText(user.getFirst_name() + " " + user.getLast_name());
            headerDescriptionView.setText(user.isOnline() ? getResources().getString(R.string.all_online_status_true) : "");
            ImageLoadingAsyncTask.newLoader()
                    .setTargetView(headerImageView)
                    .setPlaceHolder(R.drawable.all_default_user_image)
                    .setImageHeight(headerImageView.getHeight())
                    .setImageWidth(headerImageView.getWidth())
                    .load(user.getPhoto_100());
        }
    }

    private void initAdapter() {
        recyclerViewAdapter = new FriendsCursorAdapter(friendsCursor, this);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_draw_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_logout) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        clearSharedPreferences();
        clearCache();
        clearDB();
        startLoginActivity();
    }

    private void clearSharedPreferences() {
        AppSharedPreferences.removePreference(VKService.ACCESS_PERMISSION_PREFERENCE);
        AppSharedPreferences.removePreference(VKService.ACCESS_TOKEN_PREFERENCE);
        AppSharedPreferences.removePreference(VKService.USER_ID_PREFERENCE);
    }

    private void clearCache() {
        VKSimpleChatApplication.getBitmapManagerConfigurator().getBitmapMemoryCacheManager().clearCache();
        VKSimpleChatApplication.getBitmapManagerConfigurator().getBitmapDiskCacheManager().clearCache();
    }

    private void clearDB() {
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        dbHelper.dropTable(dbHelper.getWritableDatabase(), DBHelper.FRIEND_TABLE_NAME);
        dbHelper.dropTable(dbHelper.getWritableDatabase(), DBHelper.USER_TABLE_NAME);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}