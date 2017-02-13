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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FriendsFragment;
import com.github.maximkirko.training_2017_android.activity.navigator.IntentManager;
import com.github.maximkirko.training_2017_android.adapter.FriendslistFragmentPagerAdapter;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.broadcastreceiver.BroadcastReceiverCallback;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DeviceLoadingBroadcastReceiver;
import com.github.maximkirko.training_2017_android.broadcastreceiver.FriendsDownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.broadcastreceiver.UserDataDownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.loader.FriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.UserDataCursorLoader;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.operation.VKRequestOperation;
import com.github.maximkirko.training_2017_android.service.FriendsDownloadService;
import com.github.maximkirko.training_2017_android.service.UserDataDownloadService;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

public class FriendsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BroadcastReceiverCallback, LoaderManager.LoaderCallbacks<Cursor> {

    // region Views
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View navHeaderView;
    private TextView headerTitleView;
    private TextView headerDescriptionView;
    private ImageView headerImageView;
    private ViewPager viewPager;
    private PagerTitleStrip pagerTitleStrip;
    private ProgressBar progressBar;
    // endregion

    private PagerAdapter pagerAdapter;

    // region Fragments
    private FriendsFragment allFriendsFragment;
    private FriendsFragment newFriendsFragment;
    private FriendsFragment topFriendsFragment;
    // endregion

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
        initUserDataServiceIntent();
        VKRequestOperation userDataRequest = VKRequestOperation.newRequest()
                .setContext(this)
                .setBroadcastReceiver(new UserDataDownloadServiceBroadcastReceiver(this))
                .setServiceIntent(userDataServiceIntent)
                .execute();

        // initialize friends download service
        initFriendsServiceIntent(true);
        VKRequestOperation friendsRequest = VKRequestOperation.newRequest()
                .setContext(this)
                .setBroadcastReceiver(new FriendsDownloadServiceBroadcastReceiver(this))
                .setServiceIntent(friendsServiceIntent)
                .execute();
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
                    int id = AppSharedPreferences.getInt(VKService.USER_ID_PREFERENCE, 0);
                    startActivity(IntentManager.getIntentForUserDetailsActivity(getBaseContext(), id));
                }
            }
        });
        headerTitleView = (TextView) navHeaderView.findViewById(R.id.textview_navigation_drawer_header_title);
        headerDescriptionView = (TextView) navHeaderView.findViewById(R.id.textview_navigation_drawer_header_description);
        headerImageView = (ImageView) navHeaderView.findViewById(R.id.imageview_navigation_drawer_header);
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar_friends_activity);
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

    private void initUserDataServiceIntent() {
        userDataServiceIntent = new Intent(this, UserDataDownloadService.class);
    }

    private void initFriendsServiceIntent(boolean isFirstLoading) {
        friendsServiceIntent = new Intent(this, FriendsDownloadService.class);
        friendsServiceIntent.putExtra(FriendsDownloadService.IS_FIRST_LOADING_EXTRAS, isFirstLoading);
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
                initViewPager();
                return;
            }
            this.friendsCursor = cursor;
            attachCursorToFragments(cursor);
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

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_friendslist_activity);
        pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.strip_friendslist_viewpager_title);
        initViewPagerAdapter();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void initViewPagerAdapter() {
        pagerAdapter = new FriendslistFragmentPagerAdapter(this, getSupportFragmentManager(), friendsCursor);
        viewPager.setAdapter(pagerAdapter);
    }

    private void attachCursorToFragments(Cursor cursor) {
        if (allFriendsFragment != null) {
            allFriendsFragment.attachCursor(cursor);
        }
        if (newFriendsFragment != null) {
            newFriendsFragment.attachCursor(cursor);
        }
        if (topFriendsFragment != null) {
            topFriendsFragment.attachCursor(cursor);
        }
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
        startActivity(IntentManager.getIntentForLoginActivity(this));
        finish();
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
}