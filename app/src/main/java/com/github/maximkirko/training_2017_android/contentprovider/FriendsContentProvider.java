package com.github.maximkirko.training_2017_android.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.github.maximkirko.training_2017_android.activity.FriendsListActivity;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;

import java.util.List;

public class FriendsContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.github.maximkirko.providers.friends";
    private static final String PATH = "friends";

    public static final Uri FRIENDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    private static final String FRIENDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH;
    private static final String FRIENDS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PATH;

    private static final int URI_FRIENDS = 1;
    private static final int URI_FRIENDS_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, URI_FRIENDS);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_FRIENDS_ID);
    }

    private SQLiteDatabase db;

    public FriendsContentProvider() {
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_FRIENDS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DBHelper.DB_FIELD_FIRST_NAME + " ASC";
                }
                break;
            case URI_FRIENDS_ID:
                String id = uri.getLastPathSegment();
                Log.d("DB", "URI_FRIENDS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.DB_FIELD_USER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper.DB_FIELD_USER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = FriendsListActivity.dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                FRIENDS_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_FRIENDS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = FriendsListActivity.dbHelper.getWritableDatabase();
        long rowID = db.insert(DBHelper.TABLE_NAME, null, values);
        Uri resultUri = ContentUris.withAppendedId(FRIENDS_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_FRIENDS:
                break;
            case URI_FRIENDS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.DB_FIELD_USER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper.DB_FIELD_USER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = FriendsListActivity.dbHelper.getWritableDatabase();
        int cnt = db.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_FRIENDS:
                break;
            case URI_FRIENDS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.DB_FIELD_USER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper.DB_FIELD_USER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = FriendsListActivity.dbHelper.getWritableDatabase();
        int cnt = db.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_FRIENDS:
                return FRIENDS_CONTENT_TYPE;
            case URI_FRIENDS_ID:
                return FRIENDS_CONTENT_ITEM_TYPE;
        }
        return null;
    }
}