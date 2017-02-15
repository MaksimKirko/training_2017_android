package com.github.maximkirko.training_2017_android.contentprovider;

import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

public class FriendsContentProvider extends AbstractContentProvider {

    public static final String FRIENDS_CONTENT_AUTHORITY = "com.github.maximkirko.providers.friends";
    protected static final String FRIENDS_PATH = "friends";

    public static final Uri FRIENDS_CONTENT_URI = Uri.parse("content://" + FRIENDS_CONTENT_AUTHORITY + "/" + FRIENDS_PATH);
    protected static final String FRIENDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + FRIENDS_CONTENT_AUTHORITY + "." + FRIENDS_PATH;
    protected static final String FRIENDS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + FRIENDS_CONTENT_AUTHORITY + "." + FRIENDS_PATH;

    {
        CONTENT_AUTHORITY = FRIENDS_CONTENT_AUTHORITY;
        PATH = FRIENDS_PATH;
        CONTENT_URI = FRIENDS_CONTENT_URI;
        CONTENT_TYPE = FRIENDS_CONTENT_TYPE;
        CONTENT_ITEM_TYPE = FRIENDS_CONTENT_ITEM_TYPE;
        TABLE_NAME = DBHelper.FRIEND_TABLE_NAME;

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH, URI);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH + "/#", URI_ID);
    }

    public FriendsContentProvider() {
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DBHelper.USER_TABLE_FIELD_FIRST_NAME + " ASC";
                }
                break;
            case URI_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.USER_TABLE_FIELD_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper.USER_TABLE_FIELD_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = VKSimpleChatApplication.getDbHelper().getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    CONTENT_URI);
        }
        return cursor;
    }
}