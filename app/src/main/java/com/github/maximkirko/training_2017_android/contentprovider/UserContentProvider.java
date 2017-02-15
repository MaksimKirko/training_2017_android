package com.github.maximkirko.training_2017_android.contentprovider;

import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

/**
 * Created by MadMax on 08.02.2017.
 */

public class UserContentProvider extends AbstractContentProvider {

    public static final String USER_CONTENT_AUTHORITY = "com.github.maximkirko.providers.user";
    protected static final String USER_PATH = "user";

    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + USER_CONTENT_AUTHORITY + "/" + USER_PATH);
    protected static final String USER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + USER_CONTENT_AUTHORITY + "." + USER_PATH;
    protected static final String USER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + USER_CONTENT_AUTHORITY + "." + USER_PATH;

    {
        CONTENT_AUTHORITY = USER_CONTENT_AUTHORITY;
        PATH = USER_PATH;
        CONTENT_URI = USER_CONTENT_URI;
        CONTENT_TYPE = USER_CONTENT_TYPE;
        CONTENT_ITEM_TYPE = USER_CONTENT_ITEM_TYPE;
        TABLE_NAME = DBHelper.USER_TABLE_NAME;

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH, URI);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH + "/#", URI_ID);
    }

    public UserContentProvider() {
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
