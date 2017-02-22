package com.github.maximkirko.training_2017_android.db;

import android.app.SearchManager;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.contentprovider.FavoriteFriendsProvider;
import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.contentprovider.UserContentProvider;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // region db settings
    private final static int DB_VER = 1;
    private static final String DB_NAME = "vk-simple_chat.db";
    // endregion

    // region scripts
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    // endregion

    // region tables
    public static String FRIEND_TABLE_NAME = "friend";
    public static String USER_TABLE_NAME = "user";
    public static String FAVORITE_FRIEND_TABLE_NAME = "friend_favorite";
    // endregion

    // region query
    private static final String CREATE_USER_TABLE = CREATE_TABLE + USER_TABLE_NAME + "(id integer PRIMARY KEY, first_name text, last_name text, photo_100 text, online boolean);";
    private static final String CREATE_FRIEND_TABLE = CREATE_TABLE + FRIEND_TABLE_NAME + "(id integer PRIMARY KEY, first_name text, last_name text, photo_100 text, online boolean, " +
            "last_seen timestamp, rating integer, is_favorite boolean);";
    private static final String CREATE_FAVORITE_FRIENDS_TABLE = CREATE_TABLE + FAVORITE_FRIEND_TABLE_NAME + "(id integer PRIMARY KEY, become timestamp);";
    // created timestamp,
    // endregion

    // region fields
    public static final String USER_TABLE_FIELD_ID = "id";
    public static final String USER_TABLE_FIELD_FIRST_NAME = "first_name";
    public static final String USER_TABLE_FIELD_LAST_NAME = "last_name";
    public static final String USER_TABLE_FIELD_PHOTO_100 = "photo_100";
    public static final String USER_TABLE_FIELD_ONLINE = "online";
    public static final String FRIENDS_TABLE_FIELD_LAST_SEEN = "last_seen";
    public static final String FRIENDS_TABLE_FIELD_RATING = "rating";
    public static final String FRIENDS_TABLE_FIELD_IS_FAVORITE = "is_favorite";
    public static final String FAVORITE_FRIENDS_TABLE_FIELD_BECOME = "become";
    // endregion

    private HashMap<String, String> mAliasMap;

    public DBHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VER);
        mAliasMap = new HashMap<>();
        mAliasMap.put("_ID", USER_TABLE_FIELD_ID + " as " + "_id");
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, USER_TABLE_FIELD_FIRST_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_2, USER_TABLE_FIELD_LAST_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_2);
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_ICON_1, USER_TABLE_FIELD_PHOTO_100 + " as " + SearchManager.SUGGEST_COLUMN_ICON_1);
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, USER_TABLE_FIELD_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_FRIEND_TABLE);
        db.execSQL(CREATE_FAVORITE_FRIENDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        USER_TABLE_NAME += "_" + newVersion;
        FRIEND_TABLE_NAME += "_" + newVersion;
        FAVORITE_FRIEND_TABLE_NAME += "_" + newVersion;
        onCreate(db);
    }

    public void insertUserData(@NonNull SQLiteDatabase db, @NonNull Context context, @NonNull User user) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(userToContentProviderOperation(user, UserContentProvider.USER_CONTENT_URI));
        try {
            dropTable(db, USER_TABLE_NAME);
            db.execSQL(CREATE_USER_TABLE);
            context.getContentResolver().applyBatch(UserContentProvider.USER_CONTENT_AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    private ContentProviderOperation userToContentProviderOperation(@NonNull User user, @NonNull Uri contentUri) {
        return ContentProviderOperation.newInsert(contentUri)
                .withValue(USER_TABLE_FIELD_ID, user.getId())
                .withValue(USER_TABLE_FIELD_FIRST_NAME, user.getFirst_name())
                .withValue(USER_TABLE_FIELD_LAST_NAME, user.getLast_name())
                .withValue(USER_TABLE_FIELD_PHOTO_100, user.getPhoto_100())
                .withValue(USER_TABLE_FIELD_ONLINE, user.isOnline())
                .build();
    }

    @Nullable
    public User getFriendById(@NonNull Context context, int id) {
        Uri uri = ContentUris.withAppendedId(FriendsContentProvider.FRIENDS_CONTENT_URI, id);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToNext()) {
            return UserMapper.convert(cursor);
        }
        return null;
    }

    @Nullable
    public Cursor getCursorFriendById(@NonNull Context context, int id) {
        Uri uri = ContentUris.withAppendedId(FriendsContentProvider.FRIENDS_CONTENT_URI, id);
        return context.getContentResolver().query(uri, null, null, null, null);
    }

    public void updateFriendRating(@NonNull SQLiteDatabase db, int id, int rating) {
        db.execSQL("UPDATE " + FRIEND_TABLE_NAME + " SET " + FRIENDS_TABLE_FIELD_RATING + "=" + rating + " where id=" + id + ";");
    }

    public void insertFriendsBatch(@NonNull SQLiteDatabase db, @NonNull Context context, @NonNull List<User> friends) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        for (User user : friends) {
            ops.add(friendToContentProviderOperation(user, FriendsContentProvider.FRIENDS_CONTENT_URI));
        }
        try {
            dropTable(db, FRIEND_TABLE_NAME);
            db.execSQL(CREATE_FRIEND_TABLE);
            context.getContentResolver().applyBatch(FriendsContentProvider.FRIENDS_CONTENT_AUTHORITY, ops);
            setFriendsIsFavorite(db, context);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    private void setFriendsIsFavorite(@NonNull SQLiteDatabase db, Context context) {
        Cursor cursor = context.getContentResolver().query(FavoriteFriendsProvider.FAVORITE_FRIENDS_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.USER_TABLE_FIELD_ID));
            db.execSQL("UPDATE " + FRIEND_TABLE_NAME + " SET " + FRIENDS_TABLE_FIELD_IS_FAVORITE + "=1 where id=" + id + ";");
        }
    }

    private ContentProviderOperation friendToContentProviderOperation(@NonNull User user, @NonNull Uri contentUri) {
        return ContentProviderOperation.newInsert(contentUri)
                .withValue(USER_TABLE_FIELD_ID, user.getId())
                .withValue(USER_TABLE_FIELD_FIRST_NAME, user.getFirst_name())
                .withValue(USER_TABLE_FIELD_LAST_NAME, user.getLast_name())
                .withValue(USER_TABLE_FIELD_PHOTO_100, user.getPhoto_100())
                .withValue(USER_TABLE_FIELD_ONLINE, user.isOnline())
                .withValue(FRIENDS_TABLE_FIELD_LAST_SEEN, user.getLast_seen().getTime())
                .withValue(FRIENDS_TABLE_FIELD_RATING, user.getRating())
                .withValue(FRIENDS_TABLE_FIELD_IS_FAVORITE, user.is_favorite())
                .build();
    }

    public void insertFavoriteFriend(@NonNull SQLiteDatabase db, @NonNull Context context, @NonNull User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_FIELD_ID, user.getId());
        contentValues.put(FAVORITE_FRIENDS_TABLE_FIELD_BECOME, System.currentTimeMillis());
        context.getContentResolver().insert(FavoriteFriendsProvider.FAVORITE_FRIENDS_CONTENT_URI, contentValues);
        updateFriends(db, user.getId(), true);
    }

    public void updateFriends(SQLiteDatabase db, int id, boolean is_favorite) {
        db.execSQL("UPDATE " + FRIEND_TABLE_NAME + " SET " + FRIENDS_TABLE_FIELD_IS_FAVORITE + " = \"" + (is_favorite ? "1" : "0") + "\" where id=" + id + ";");
    }

    public Cursor getFavoriteFriends(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_FRIENDS_TABLE);
        String query = "SELECT * FROM " + FAVORITE_FRIEND_TABLE_NAME + " f1 INNER JOIN "
                + FRIEND_TABLE_NAME + " f2 ON f1.id=f2.id ORDER BY "
                + DBHelper.FAVORITE_FRIENDS_TABLE_FIELD_BECOME + " DESC";
        return db.rawQuery(query, null);
    }

    public Cursor searchFriends(@NonNull SQLiteDatabase db, @NonNull String tableName, @NonNull String searchString) {
        String query = "SELECT * FROM " + FRIEND_TABLE_NAME + " WHERE ";
        if (tableName.equals(FAVORITE_FRIEND_TABLE_NAME)) {
            query += FRIENDS_TABLE_FIELD_IS_FAVORITE + "=1 AND ";
        }
        query += USER_TABLE_FIELD_FIRST_NAME + " LIKE \"%" + searchString + "%\" OR "
                + USER_TABLE_FIELD_LAST_NAME + " LIKE \"%" + searchString + "%\";";
        return db.rawQuery(query, null);
    }

    public Cursor getSuggestionsFriends(String[] selectionArgs) {
        String selection = USER_TABLE_FIELD_FIRST_NAME + " like ? ";

        if (selectionArgs != null) {
            selectionArgs[0] = "%" + selectionArgs[0] + "%";
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setProjectionMap(mAliasMap);

        queryBuilder.setTables(FRIEND_TABLE_NAME);

        Cursor c = queryBuilder.query(
                getReadableDatabase(),
                new String[]{"_ID",
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_TEXT_2,
                        SearchManager.SUGGEST_COLUMN_ICON_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID},
                selection,
                selectionArgs,
                null,
                null,
                USER_TABLE_FIELD_FIRST_NAME + " asc ", "10"
        );
        return c;

    }

    public void dropTable(@NonNull SQLiteDatabase db, String tableName) {
        db.execSQL(DROP_TABLE + tableName);
    }
}