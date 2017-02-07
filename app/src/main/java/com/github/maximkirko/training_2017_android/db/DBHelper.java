package com.github.maximkirko.training_2017_android.db;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private final static int DB_VER = 1;
    private static final String DB_NAME = "vk-simple_chat.db";
    public static String TABLE_NAME = "user";
    private final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id integer PRIMARY KEY, first_name text, last_name text, photo_100 text, online boolean);";
    private final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // region DB fields
    public static final String DB_FIELD_USER_ID = "id";
    public static final String DB_FIELD_FIRST_NAME = "first_name";
    public static final String DB_FIELD_LAST_NAME = "last_name";
    public static final String DB_FIELD_PHOTO_100 = "photo_100";
    public static final String DB_FIELD_ONLINE = "online";
    // endregion

    private List<User> friends;

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public DBHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME += "_" + newVersion;
        onCreate(db);
    }

    public void insertFriendsBatch(@NonNull SQLiteDatabase db, @NonNull Context context) {
        if (friends != null) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            for (User user : friends) {
                ops.add(
                        ContentProviderOperation.newInsert(FriendsContentProvider.FRIENDS_CONTENT_URI)
                                .withValue(DB_FIELD_USER_ID, user.getId())
                                .withValue(DB_FIELD_FIRST_NAME, user.getFirst_name())
                                .withValue(DB_FIELD_LAST_NAME, user.getLast_name())
                                .withValue(DB_FIELD_PHOTO_100, user.getPhoto_100())
                                .withValue(DB_FIELD_ONLINE, user.isOnline())
                                .build());
            }
            try {
                db.execSQL(DROP_TABLE);
                db.execSQL(CREATE_TABLE);
                context.getContentResolver().applyBatch(FriendsContentProvider.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(e.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    public void dropUserTable(@NonNull SQLiteDatabase db) {
        db.execSQL(DROP_TABLE);
    }
}