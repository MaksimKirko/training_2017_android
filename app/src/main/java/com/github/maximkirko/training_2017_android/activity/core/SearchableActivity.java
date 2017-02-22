package com.github.maximkirko.training_2017_android.activity.core;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.SearchResultsFragement;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;
import com.github.maximkirko.training_2017_android.contentprovider.SearchSuggestionProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.loader.SearchFriendsCursorLoader;

/**
 * Created by MadMax on 22.02.2017.
 */

public class SearchableActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>, TaskFinishedCallback {

    private static final String QUERY_EXTRAS = "QUERY_EXTRAS";

    private FriendsFragment searchResultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchable_activity);

        initFragment();
    }

    private void initFragment() {
        searchResultsFragment = SearchResultsFragement.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.searchable_activity_fragment_container, searchResultsFragment, SearchResultsFragement.TAG)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_VIEW)) {
                Intent userIntent = new Intent(this, UserDetailsActivity.class);
                userIntent.setData(intent.getData());
                startActivity(userIntent);
                finish();
            } else if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                addQueryToRecent(query);
                performSearch(query);
            }
        }
    }

    private void addQueryToRecent(String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY,
                SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void performSearch(String query) {
        Bundle data = new Bundle();
        data.putString(QUERY_EXTRAS, query);

        getLoaderManager().initLoader(SearchFriendsCursorLoader.LOADER_ID, data, this);
        android.content.Loader<Cursor> searchFriendsCursorLoader =
                getLoaderManager().getLoader(SearchFriendsCursorLoader.LOADER_ID);
        ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setTableName(DBHelper.FRIEND_TABLE_NAME);
//        if (getSupportFragmentManager().findFragmentByTag(FavoriteFriendsFragment.TAG).isHidden()) {
//            ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setTableName(DBHelper.FRIEND_TABLE_NAME);
//        } else {
//            ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setTableName(DBHelper.FAVORITE_FRIEND_TABLE_NAME);
//        }
        ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setQuery(query);
        searchFriendsCursorLoader.forceLoad();
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == SearchFriendsCursorLoader.LOADER_ID) {
            return new SearchFriendsCursorLoader(getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        searchResultsFragment.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
    }

    @Override
    public void onTaskFinished(Class<? extends AsyncTask> asyncTaskClass) {
        // TODO refresh loaders
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_draw_friends, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //onSearchRequested();
                return true;
            default:
                return false;
        }
    }
}
