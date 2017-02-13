package com.github.maximkirko.training_2017_android.db;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.contentprovider.UserContentProvider;
import com.github.maximkirko.training_2017_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // region db settings
    private final static int DB_VER = 1;
    private static final String DB_NAME = "vk-simple_chat.db";
    // endregion

    // region scripts
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    // endregion

    // region tables
    public static String FRIEND_TABLE_NAME = "friend";
    public static String USER_TABLE_NAME = "user";
    public static String USER_FAVORITE_TABLE_NAME = "user_favorite";
    // endregion

    // region query
    private static final String CREATE_USER_TABLE = CREATE_TABLE + USER_TABLE_NAME + "(id integer PRIMARY KEY, first_name text, last_name text, photo_100 text, online boolean);";
    private static final String CREATE_FRIEND_TABLE = CREATE_TABLE + FRIEND_TABLE_NAME + "(id integer PRIMARY KEY, first_name text, last_name text, photo_100 text, online boolean, " +
            "rating integer, is_favorite boolean);";
    private static final String CREATE_USER_FAVORITE_TABLE = CREATE_TABLE + USER_FAVORITE_TABLE_NAME + "(id integer PRIMARY KEY, become timestamp);";
    // created timestamp,
    // endregion

    // region fields
    public static final String USER_TABLE_FIELD_ID = "id";
    public static final String USER_TABLE_FIELD_FIRST_NAME = "first_name";
    public static final String USER_TABLE_FIELD_LAST_NAME = "last_name";
    public static final String USER_TABLE_FIELD_PHOTO_100 = "photo_100";
    public static final String USER_TABLE_FIELD_ONLINE = "online";
    public static final String USER_FAVORITE_TABLE_FIELD_BECOME = "become";
    public static final String FRIENDS_TABLE_FIELD_RATING = "rating";
    public static final String FRIENDS_TABLE_FIELD_IS_FAVORITE = "is_favorite";
    // endregion

    public DBHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        USER_TABLE_NAME += "_" + newVersion;
        FRIEND_TABLE_NAME += "_" + newVersion;
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

    public void insertFriendsBatch(@NonNull SQLiteDatabase db, @NonNull Context context, @NonNull List<User> friends) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        for (User user : friends) {
            ops.add(friendToContentProviderOperation(user, FriendsContentProvider.FRIENDS_CONTENT_URI));
        }
        try {
            dropTable(db, FRIEND_TABLE_NAME);
            db.execSQL(CREATE_FRIEND_TABLE);
            context.getContentResolver().applyBatch(FriendsContentProvider.FRIENDS_CONTENT_AUTHORITY, ops);
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


    private ContentProviderOperation friendToContentProviderOperation(@NonNull User user, @NonNull Uri contentUri) {
        return ContentProviderOperation.newInsert(contentUri)
                .withValue(USER_TABLE_FIELD_ID, user.getId())
                .withValue(USER_TABLE_FIELD_FIRST_NAME, user.getFirst_name())
                .withValue(USER_TABLE_FIELD_LAST_NAME, user.getLast_name())
                .withValue(USER_TABLE_FIELD_PHOTO_100, user.getPhoto_100())
                .withValue(USER_TABLE_FIELD_ONLINE, user.isOnline())
                .withValue(FRIENDS_TABLE_FIELD_IS_FAVORITE, user.is_favorite())
                .withValue(FRIENDS_TABLE_FIELD_RATING, user.getRating())
                .build();
    }

    public void dropTable(@NonNull SQLiteDatabase db, String tableName) {
        db.execSQL(DROP_TABLE + tableName);
    }
}