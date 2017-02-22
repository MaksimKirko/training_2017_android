package com.github.maximkirko.training_2017_android.activity.core;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentActivity;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FavoriteFriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FriendsFragment;
import com.github.maximkirko.training_2017_android.activity.core.fragment.SearchResultsFragement;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;
import com.github.maximkirko.training_2017_android.contentprovider.SearchSuggestionProvider;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.loader.SearchFriendsCursorLoader;

/**
 * Created by MadMax on 22.02.2017.
 */

public class SearchableActivity extends FragmentActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>, TaskFinishedCallback {

    private static final String QUERY_EXTRAS = "QUERY_EXTRAS";

    private FriendsFragment searchResultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchable_activity);
        initFragment();
        handleIntent(getIntent());
    }

    private void initFragment() {
        searchResultsFragment = SearchResultsFragement.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.searchable_activity_fragment_container, searchResultsFragment, SearchResultsFragement.TAG)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_VIEW)) {
                Intent countryIntent = new Intent(this, UserDetailsActivity.class);
                countryIntent.setData(intent.getData());
                startActivity(countryIntent);
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
        if (getSupportFragmentManager().findFragmentByTag(FavoriteFriendsFragment.TAG).isHidden()) {
            ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setTableName(DBHelper.FRIEND_TABLE_NAME);
        } else {
            ((SearchFriendsCursorLoader) searchFriendsCursorLoader).setTableName(DBHelper.FAVORITE_FRIEND_TABLE_NAME);
        }
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
}
