package com.github.maximkirko.training_2017_android.activity.core;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FavoriteFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.preference.DefaultPreferenceFragment;
import com.github.maximkirko.training_2017_android.adapter.FriendslistFragmentPagerAdapter;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;
import com.github.maximkirko.training_2017_android.broadcastreceiver.BroadcastReceiverCallback;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DeviceLoadingBroadcastReceiver;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.loader.FavoriteFriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.FriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.NewFriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.TopFriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.UserDataCursorLoader;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.navigator.IntentManager;
import com.github.maximkirko.training_2017_android.operation.VKRequestOperation;
import com.github.maximkirko.training_2017_android.service.FriendsDataDownloadService;
import com.github.maximkirko.training_2017_android.service.UserDataDownloadService;
import com.github.maximkirko.training_2017_android.service.VKRequestAbstractService;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

public class FriendsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BroadcastReceiverCallback, LoaderManager.LoaderCallbacks<Cursor>, TaskFinishedCallback {

    // region Views
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View navHeaderView;
    private TextView headerTitleView;
    private TextView headerDescriptionView;
    private ImageView headerImageView;
    private TextView errorView;
    private ViewPager viewPager;
    private PagerTitleStrip pagerTitleStrip;
    private ProgressBar progressBar;
    // endregion

    private PagerAdapter pagerAdapter;

    private FragmentTransaction fragmentTransaction;
    private FavoriteFriendsFragment favoriteFriendsFragment;

    // region Cursors
    private static Cursor allFriendsCursor;
    private static Cursor newFriendsCursor;
    private static Cursor topFriendsCursor;
    private static Cursor favoriteFriendsCursor;

    public static Cursor getAllFriendsCursor() {
        return allFriendsCursor;
    }

    public static Cursor getNewFriendsCursor() {
        return newFriendsCursor;
    }

    public static Cursor getTopFriendsCursor() {
        return topFriendsCursor;
    }

    public static Cursor getFavoriteFriendsCursor() {
        return favoriteFriendsCursor;
    }
    // endregion

    // region Friends loading properties
    private User user;
    private PendingIntent pendingIntent;
    private Intent userDataServiceIntent;
    private Intent friendsServiceIntent;
    private AlarmManager alarmManager;
    private DeviceLoadingBroadcastReceiver deviceLoadingBroadcastReceiver;
    private DownloadServiceBroadcastReceiver downloadServiceBroadcastReceiver;

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
        initViews();
        enableProgressBar();

        initDeviceLoadingBroadcastReceiver();
        initDownloadServiceBroadcastReceiver();

        // initialize user data download service
        initUserDataServiceIntent();
        VKRequestOperation.newRequest()
                .setContext(this)
                .setBroadcastReceiver(downloadServiceBroadcastReceiver)
                .setServiceIntent(userDataServiceIntent)
                .execute();

        // initialize friends download service
        initFriendsServiceIntent(true);
        VKRequestOperation.newRequest()
                .setContext(this)
                .setBroadcastReceiver(downloadServiceBroadcastReceiver)
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

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar_friends_activity);
        errorView = (TextView) findViewById(R.id.textview_friendslist_error);
        disableErrorView();
    }

    private void enableProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void disableProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void enableErrorView() {
        errorView.setVisibility(View.VISIBLE);
    }

    private void disableErrorView() {
        errorView.setVisibility(View.INVISIBLE);
    }


    private void initDeviceLoadingBroadcastReceiver() {
        deviceLoadingBroadcastReceiver = new DeviceLoadingBroadcastReceiver(this);
    }

    private void initDownloadServiceBroadcastReceiver() {
        downloadServiceBroadcastReceiver = new DownloadServiceBroadcastReceiver(this);
    }

    private void initUserDataServiceIntent() {
        userDataServiceIntent = new Intent(this, UserDataDownloadService.class);
    }

    private void initFriendsServiceIntent(boolean isFirstLoading) {
        friendsServiceIntent = new Intent(this, FriendsDataDownloadService.class);
        friendsServiceIntent.putExtra(FriendsDataDownloadService.IS_FIRST_LOADING_EXTRAS, isFirstLoading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(downloadServiceBroadcastReceiver, new IntentFilter(VKRequestAbstractService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(downloadServiceBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onReceived(String serviceClass) {
        if (serviceClass.equals(UserDataDownloadService.SERVICE_CLASS)) {
            startLoader(UserDataCursorLoader.LOADER_ID);
        } else if (serviceClass.equals(FriendsDataDownloadService.SERVICE_CLASS)) {
            startLoader(FriendsCursorLoader.LOADER_ID);
            startLoader(NewFriendsCursorLoader.LOADER_ID);
            startLoader(TopFriendsCursorLoader.LOADER_ID);
            startLoader(FavoriteFriendsCursorLoader.LOADER_ID);
        } else if (serviceClass.equals(DeviceLoadingBroadcastReceiver.SERVICE_CLASS)) {
            initAlarmManager();
        }
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

    @Override
    public void onTaskFinished() {
        startLoader(FavoriteFriendsCursorLoader.LOADER_ID);
    }

    private void startLoader(int loaderId) {
        getLoaderManager().initLoader(loaderId, null, this);
        Loader<Cursor> loader = getLoaderManager().getLoader(loaderId);
        loader.forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == UserDataCursorLoader.LOADER_ID) {
            return new UserDataCursorLoader(getApplicationContext());
        } else if (id == FriendsCursorLoader.LOADER_ID) {
            return new FriendsCursorLoader(getApplicationContext());
        } else if (id == NewFriendsCursorLoader.LOADER_ID) {
            return new NewFriendsCursorLoader(getApplicationContext());
        } else if (id == TopFriendsCursorLoader.LOADER_ID) {
            return new TopFriendsCursorLoader(getApplicationContext());
        } else if (id == FavoriteFriendsCursorLoader.LOADER_ID) {
            return new FavoriteFriendsCursorLoader(getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader instanceof UserDataCursorLoader) {
            if (cursor.moveToNext()) {
                user = UserMapper.convert(cursor);
                setUserData();
            }
        } else if (loader instanceof FavoriteFriendsCursorLoader) {
            favoriteFriendsCursor = cursor;
        } else {
            disableProgressBar();
            if (cursor.getCount() < 1) {
                enableErrorView();
                return;
            }
            if (loader instanceof FriendsCursorLoader) {
                allFriendsCursor = cursor;
            } else if (loader instanceof NewFriendsCursorLoader) {
                newFriendsCursor = cursor;
            } else if (loader instanceof TopFriendsCursorLoader) {
                topFriendsCursor = cursor;
            }
            if (isAllCursorsLoaded()) {
                initViewPager();
            }
        }
    }

    private boolean isAllCursorsLoaded() {
        return allFriendsCursor != null && newFriendsCursor != null && topFriendsCursor != null;
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
        pagerAdapter = new FriendslistFragmentPagerAdapter(this, getSupportFragmentManager(), allFriendsCursor, this);
        viewPager.setAdapter(pagerAdapter);
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
            startActivity(IntentManager.getIntentForSettingsActivity(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_friends) {
            //removeFavoriteFriendsFragment();
        } else if (id == R.id.nav_favorite_friends) {
            startFavoriteFriendsActivity();
            //showFavoriteFriendsFragment();
        } else if (id == R.id.nav_logout) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startFavoriteFriendsActivity() {
        startActivity(IntentManager.getIntentForFavoriteFriendsListActivity(this));
    }

    private void removeFavoriteFriendsFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(favoriteFriendsFragment);
        fragmentTransaction.commit();
        viewPager.setVisibility(View.VISIBLE);
    }

    private void showFavoriteFriendsFragment() {
        viewPager.setVisibility(View.INVISIBLE);
        favoriteFriendsFragment = FavoriteFriendsFragment.newInstance();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.activity_friendslist, favoriteFriendsFragment);
        fragmentTransaction.commit();
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