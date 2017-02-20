package com.github.maximkirko.training_2017_android.activity.core.fragment;

import android.database.Cursor;

import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;

/**
 * Created by MadMax on 18.02.2017.
 */

public class SearchResultsFragement extends FriendsFragment {

    public static final String TAG = "SEARCH_RESULTS_FRAGMENT";

    public static SearchResultsFragement newInstance(TaskFinishedCallback taskFinishedCallback) {
        SearchResultsFragement searchResultsFragement = new SearchResultsFragement();
        searchResultsFragement.setTaskFinisedCallback(taskFinishedCallback);
        return searchResultsFragement;
    }

    public void setCursor(Cursor cursor) {
        swapCursor(cursor);
    }

    public SearchResultsFragement() {
    }
}
