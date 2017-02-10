package com.github.maximkirko.training_2017_android.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.db.DBHelper;

/**
 * Created by MadMax on 08.02.2017.
 */

public class UserContentProvider extends ContentProvider {

    public static final String USER_CONTENT_AUTHORITY = "com.github.maximkirko.providers.user";
    private static final String PATH = "user";

    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + USER_CONTENT_AUTHORITY + "/" + PATH);
    private static final String USER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + USER_CONTENT_AUTHORITY + "." + PATH;
    private static final String USER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + USER_CONTENT_AUTHORITY + "." + PATH;

    private static final int URI_USER = 1;
    private static final int URI_USER_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(USER_CONTENT_AUTHORITY, PATH, URI_USER);
        uriMatcher.addURI(USER_CONTENT_AUTHORITY, PATH + "/#", URI_USER_ID);
    }

    private SQLiteDatabase db;

    public UserContentProvider() {
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_USER:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DBHelper.USER_TABLE_FIELD_FIRST_NAME + " ASC";
                }
                break;
            case URI_USER_ID:
                String id = uri.getLastPathSegment();
                Log.d("DB", "URI_USER_ID, " + id);
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
        Cursor cursor = db.query(DBHelper.USER_TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    USER_CONTENT_URI);
        }
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_USER)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = VKSimpleChatApplication.getDbHelper().getWritableDatabase();
        long rowID = db.insert(DBHelper.USER_TABLE_NAME, null, values);
        Uri resultUri = ContentUris.withAppendedId(USER_CONTENT_URI, rowID);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(resultUri, null);
        }
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_USER:
                break;
            case URI_USER_ID:
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
        int cnt = db.delete(DBHelper.USER_TABLE_NAME, selection, selectionArgs);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_USER:
                break;
            case URI_USER_ID:
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
        int cnt = db.update(DBHelper.USER_TABLE_NAME, values, selection, selectionArgs);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return cnt;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_USER:
                return USER_CONTENT_TYPE;
            case URI_USER_ID:
                return USER_CONTENT_ITEM_TYPE;
        }
        return null;
    }
}
