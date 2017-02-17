package com.github.maximkirko.training_2017_android.contentprovider;

import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

/**
 * Created by MadMax on 13.02.2017.
 */

public class FavoriteFriendsProvider extends AbstractContentProvider {

    public static final String FAVORITE_FRIENDS_CONTENT_AUTHORITY = "com.github.maximkirko.providers.favorites";
    protected static final String FAVORITE_FRIENDS_PATH = "favorite";

    public static final Uri FAVORITE_FRIENDS_CONTENT_URI = Uri.parse("content://" + FAVORITE_FRIENDS_CONTENT_AUTHORITY + "/" + FAVORITE_FRIENDS_PATH);
    protected static final String FAVORITE_FRIENDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + FAVORITE_FRIENDS_CONTENT_AUTHORITY + "." + FAVORITE_FRIENDS_PATH;
    protected static final String FAVORITE_FRIENDS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + FAVORITE_FRIENDS_CONTENT_AUTHORITY + "." + FAVORITE_FRIENDS_PATH;

    {
        CONTENT_AUTHORITY = FAVORITE_FRIENDS_CONTENT_AUTHORITY;
        PATH = FAVORITE_FRIENDS_PATH;
        CONTENT_URI = FAVORITE_FRIENDS_CONTENT_URI;
        CONTENT_TYPE = FAVORITE_FRIENDS_CONTENT_TYPE;
        CONTENT_ITEM_TYPE = FAVORITE_FRIENDS_CONTENT_ITEM_TYPE;
        TABLE_NAME = DBHelper.FAVORITE_FRIENDS_TABLE_NAME;

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH, URI);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH + "/#", URI_ID);
    }

    public FavoriteFriendsProvider() {

    }
}
