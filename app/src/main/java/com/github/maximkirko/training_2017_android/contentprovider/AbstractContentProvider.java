package com.github.maximkirko.training_2017_android.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

/**
 * Created by MadMax on 13.02.2017.
 */

public abstract class AbstractContentProvider extends ContentProvider {

    public String CONTENT_AUTHORITY;
    protected String PATH;

    public Uri CONTENT_URI;
    protected String CONTENT_TYPE;
    protected String CONTENT_ITEM_TYPE;

    protected String TABLE_NAME;

    protected final int URI = 1;
    protected final int URI_ID = 2;
    protected UriMatcher uriMatcher;

    protected SQLiteDatabase db;

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI:
//                if (TextUtils.isEmpty(sortOrder)) {
//                    sortOrder = DBHelper.USER_TABLE_FIELD_FIRST_NAME + " ASC";
//                }
                break;
            case URI_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = TABLE_NAME + " = " + id;
                } else {
                    selection = selection + " AND " + TABLE_NAME + " = " + id;
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

    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = VKSimpleChatApplication.getDbHelper().getWritableDatabase();
        long rowID = db.insert(TABLE_NAME, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(resultUri, null);
        }
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI:
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
        int cnt = db.delete(TABLE_NAME, selection, selectionArgs);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI:
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
        int cnt = db.update(TABLE_NAME, values, selection, selectionArgs);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return cnt;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI:
                return CONTENT_TYPE;
            case URI_ID:
                return CONTENT_ITEM_TYPE;
        }
        return null;
    }
}
