package com.github.maximkirko.training_2017_android.contentprovider;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.util.Log;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.github.maximkirko.training_2016_android.contentprovider.SearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    private static final int SUGGESTIONS_FRIEND = 1;
    private static final int SEARCH_FRIEND = 2;
    private static final int GET_FRIEND = 3;

    UriMatcher mUriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SUGGESTIONS_FRIEND);
        uriMatcher.addURI(AUTHORITY, "friend", SEARCH_FRIEND);
        uriMatcher.addURI(AUTHORITY, "friend/#", GET_FRIEND);

        return uriMatcher;
    }

    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public boolean onCreate() {
        super.onCreate();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor customSuggestions = null;
        DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
        switch (mUriMatcher.match(uri)) {
            case SUGGESTIONS_FRIEND:
                customSuggestions = dbHelper.getSuggestionsFriends(selectionArgs);
                break;
            case SEARCH_FRIEND:
                customSuggestions = dbHelper.searchFriends(dbHelper.getReadableDatabase(), DBHelper.FRIEND_TABLE_NAME, selectionArgs[0]);
                return customSuggestions;
            case GET_FRIEND:
                String id = uri.getLastPathSegment();
                try {
                    customSuggestions = dbHelper.getCursorFriendById(getContext(), Integer.parseInt(id));
                } catch (IllegalStateException e) {
                    Log.e(e.getClass().getSimpleName(), e.getMessage());
                }
        }

        Cursor recentSuggestions = null;
        try {
            recentSuggestions = super.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (IllegalStateException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
        customSuggestions = new MergeCursor(new Cursor[]{customSuggestions, recentSuggestions});
        FriendsListActivity.suggestionsCursorAdapter.swapCursor(customSuggestions);
        return customSuggestions;
    }
}