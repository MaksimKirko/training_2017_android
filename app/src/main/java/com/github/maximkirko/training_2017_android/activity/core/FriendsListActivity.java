package com.github.maximkirko.training_2017_android.activity.core;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.AllFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FavoriteFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.NewFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.SearchResultsFragement;
import com.github.maximkirko.training_2017_android.activity.core.fragment.TopFriendsFragment;
import com.github.maximkirko.training_2017_android.adapter.FriendslistFragmentPagerAdapter;
import com.github.maximkirko.training_2017_android.adapter.SuggestionsCursorAdapter;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.asynctask.FriendRatingUpdateAsyncTask;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;
import com.github.maximkirko.training_2017_android.broadcastreceiver.BroadcastReceiverCallback;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DeviceLoadingBroadcastReceiver;
import com.github.maximkirko.training_2017_android.broadcastreceiver.DownloadServiceBroadcastReceiver;
import com.github.maximkirko.training_2017_android.contentobserver.ContentObserverCallback;
import com.github.maximkirko.training_2017_android.contentobserver.FavoriteFriendsContentObserver;
import com.github.maximkirko.training_2017_android.contentprovider.SearchSuggestionProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.loader.FavoriteFriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.FriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.NewFriendsCursorLoader;
import com.github.maximkirko.training_2017_android.loader.SearchFriendsCursorLoader;
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
import com.github.maximkirko.training_2017_android.util.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BroadcastReceiverCallback, LoaderManager.LoaderCallbacks<Cursor>, TaskFinishedCallback, ContentObserverCallback {

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

    // region Features
    private User user;
    private PendingIntent pendingIntent;
    private Intent userDataServiceIntent;
    private Intent friendsServiceIntent;
    private AlarmManager alarmManager;
    private DeviceLoadingBroadcastReceiver deviceLoadingBroadcastReceiver;
    private DownloadServiceBroadcastReceiver downloadServiceBroadcastReceiver;
    private FavoriteFriendsContentObserver favoriteFriendsContentObserver;
    private PagerAdapter pagerAdapter;
    private List<FriendsFragment> pages;
    private boolean isFavoriteFragmentActive = false;
    // endregion

    // region Constants
    private static final int ALARM_MANAGER_REPEATING_TIME = 1000 * 30 * 1; // 30 seconds
    private static final String QUERY_EXTRAS = "QUERY_EXTRAS";
    // endregion

    // region Static fields
    public static String NEW_COUNT_PREFERENCE;
    public static String TOP_COUNT_PREFERENCE;
    public static SuggestionsCursorAdapter suggestionsCursorAdapter;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_activity);

        // setTaskFinisedCallback toolbar and drawer layout
        initToolbar();
        initDrawerLayout();

        // initialize and show ProgressBar
        initViews();

        //initSearchRecentSuggestions();
        initSettingsPreferences();
        initSuggestioncursorAdapter();

        initDeviceLoadingBroadcastReceiver();
        initDownloadServiceBroadcastReceiver();
        initContentObserver();

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
        progressBar.setVisibility(View.VISIBLE);
        errorView = (TextView) findViewById(R.id.textview_friendslist_error);
        errorView.setVisibility(View.INVISIBLE);
        initFragments();
        initViewPager();
    }

    private void initFragments() {
        AllFriendsFragment allFriendsFragment = AllFriendsFragment.newInstance(this);
        NewFriendsFragment newFriendsFragment = NewFriendsFragment.newInstance(this);
        TopFriendsFragment topFriendsFragment = TopFriendsFragment.newInstance(this);
        pages = new ArrayList<>();
        pages.add(allFriendsFragment);
        pages.add(newFriendsFragment);
        pages.add(topFriendsFragment);

        FavoriteFriendsFragment favoriteFriendsFragment = FavoriteFriendsFragment.newInstance(this);
        SearchResultsFragement searchResultsFragement = SearchResultsFragement.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.friendslist_fragment_container, favoriteFriendsFragment, FavoriteFriendsFragment.TAG)
                .add(R.id.friendslist_fragment_container, searchResultsFragement, SearchResultsFragement.TAG)
                .hide(favoriteFriendsFragment)
                .hide(searchResultsFragement)
                .commit();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_friendslist_activity);
        pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.strip_friendslist_viewpager_title);
        viewPager.setOffscreenPageLimit(FriendslistFragmentPagerAdapter.PAGE_COUNT);
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
        pagerAdapter = new FriendslistFragmentPagerAdapter(this, getSupportFragmentManager(), pages);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initSettingsPreferences() {
        NEW_COUNT_PREFERENCE = AppSharedPreferences.getString("new_count", "10");
        TOP_COUNT_PREFERENCE = AppSharedPreferences.getString("top_count", "10");
    }

    private void initSuggestioncursorAdapter() {
        suggestionsCursorAdapter = new SuggestionsCursorAdapter(this, null);
    }

    private void initDeviceLoadingBroadcastReceiver() {
        deviceLoadingBroadcastReceiver = new DeviceLoadingBroadcastReceiver(this);
    }

    private void initDownloadServiceBroadcastReceiver() {
        downloadServiceBroadcastReceiver = new DownloadServiceBroadcastReceiver(this);
    }

    private void initContentObserver() {
        favoriteFriendsContentObserver = new FavoriteFriendsContentObserver(new Handler(), this);
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
        getContentResolver().registerContentObserver(FavoriteFriendsContentObserver.FAVORITE_FRIENDS_URI, false, favoriteFriendsContentObserver);
        registerReceiver(downloadServiceBroadcastReceiver, new IntentFilter(VKRequestAbstractService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(favoriteFriendsContentObserver);
        try {
            unregisterReceiver(downloadServiceBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onChange() {
        startFriendsLoaders();
    }

    @Override
    public void onReceived(String serviceClass) {
        if (serviceClass.equals(UserDataDownloadService.SERVICE_CLASS)) {
            startLoader(UserDataCursorLoader.LOADER_ID);
        } else if (serviceClass.equals(FriendsDataDownloadService.SERVICE_CLASS)) {
            startFriendsLoaders();
            startLoader(FavoriteFriendsCursorLoader.LOADER_ID);
        } else if (serviceClass.equals(DeviceLoadingBroadcastReceiver.SERVICE_CLASS)) {
            initAlarmManager();
        }
    }

    private void startFriendsLoaders() {
        startLoader(FriendsCursorLoader.LOADER_ID);
        startLoader(NewFriendsCursorLoader.LOADER_ID);
        startLoader(TopFriendsCursorLoader.LOADER_ID);
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
    public void onTaskFinished(Class<? extends AsyncTask> asyncTaskClass) {
        if (asyncTaskClass == FriendRatingUpdateAsyncTask.class) {
            startFriendsLoaders();
        }
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
        } else if (id == SearchFriendsCursorLoader.LOADER_ID) {
            return new SearchFriendsCursorLoader(getApplicationContext());
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
            ((FavoriteFriendsFragment) getSupportFragmentManager().findFragmentByTag(FavoriteFriendsFragment.TAG)).swapCursor(cursor);
        } else if (loader instanceof SearchFriendsCursorLoader) {
            ((SearchResultsFragement) getSupportFragmentManager().findFragmentByTag(SearchResultsFragement.TAG)).swapCursor(cursor);
            showFragment(SearchResultsFragement.TAG);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            if (cursor.getCount() < 1) {
                errorView.setVisibility(View.VISIBLE);
                return;
            }
            if (loader instanceof FriendsCursorLoader) {
                pages.get(0).swapCursor(cursor);
            } else if (loader instanceof NewFriendsCursorLoader) {
                pages.get(1).swapCursor(cursor);
            } else if (loader instanceof TopFriendsCursorLoader) {
                pages.get(2).swapCursor(cursor);
            }
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


    private void showFragment(String tag) {
        viewPager.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction()
                .show(getSupportFragmentManager().findFragmentByTag(tag))
                .commit();
    }

    private void hideFragment(String tag) {
        getSupportFragmentManager().beginTransaction()
                .hide(getSupportFragmentManager().findFragmentByTag(tag))
                .commit();
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
        initSearchView(menu);
        return true;
    }

    private void initSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.searchable_hint));
        searchView.setSuggestionsAdapter(suggestionsCursorAdapter);

        EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchPlate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFavoriteFragmentActive) {
                    ((FavoriteFriendsFragment) getSupportFragmentManager().findFragmentByTag(FavoriteFriendsFragment.TAG)).getAdapter().getFilter().filter(s.toString());
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Uri uri = Uri.parse("content://com.github.maximkirko.training_2016_android.contentprovider.SearchSuggestionProvider/search_suggest_query?limit=50");
                try {
                    getBaseContext().getContentResolver().query(uri, null, null, new String[]{searchView.getQuery().toString()}, null);
                } catch (IllegalStateException e) {
                    Log.e(e.getClass().getSimpleName(), e.getMessage());
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                viewPager.setVisibility(View.INVISIBLE);
                if (isFavoriteFragmentActive) {
                    hideFragment(FavoriteFriendsFragment.TAG);
                }
                addQueryToRecent(query);
                performSearch(query);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideFragment(SearchResultsFragement.TAG);
                if (isFavoriteFragmentActive) {
                    showFragment(FavoriteFriendsFragment.TAG);
                } else {
                    viewPager.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                User user = UserUtils.parseUserCursor(cursor);
                if (UserUtils.isUserComplete(user)) {
                    startActivity(IntentManager.getIntentForUserDetailsActivity(getBaseContext(), user.getId()));
                    return false;
                }
                String suggest = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                searchView.setQuery(suggest, true);
                return false;
            }
        });
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//        if (intent.getAction() != null) {
//            if (intent.getAction().equals(Intent.ACTION_VIEW)) {
//                Intent userIntent = new Intent(this, UserDetailsActivity.class);
//                userIntent.setData(intent.getData());
//                startActivity(userIntent);
//                finish();
//            } else if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
//                String query = intent.getStringExtra(SearchManager.QUERY);
//                addQueryToRecent(query);
//                performSearch(query);
//            }
//        }
//    }

    private void addQueryToRecent(String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY,
                SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void performSearch(String query) {
        Bundle data = new Bundle();
        data.putString(QUERY_EXTRAS, query);

        getLoaderManager().initLoader(SearchFriendsCursorLoader.LOADER_ID, null, this);
        Loader<Cursor> searchFriendsCursorLoader =
                getLoaderManager().getLoader(SearchFriendsCursorLoader.LOADER_ID);
        if (getSupportFragmentManager().findFragmentByTag(FavoriteFriendsFragment.TAG).isHidden()) {
            ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setTableName(DBHelper.FRIEND_TABLE_NAME);
        } else {
            ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setTableName(DBHelper.FAVORITE_FRIEND_TABLE_NAME);
        }
        ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setQuery(query);
        searchFriendsCursorLoader.forceLoad();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_settings:
                startActivity(IntentManager.getIntentForSettingsActivity(this));
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        hideFragment(SearchResultsFragement.TAG);
        if (id == R.id.nav_friends) {
            isFavoriteFragmentActive = false;
            hideFragment(FavoriteFriendsFragment.TAG);
            viewPager.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_favorite_friends) {
            isFavoriteFragmentActive = true;
            showFragment(FavoriteFriendsFragment.TAG);
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
        clearSearchHistory();
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

    private void clearSearchHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY,
                SearchSuggestionProvider.MODE);
        suggestions.clearHistory();
    }
}