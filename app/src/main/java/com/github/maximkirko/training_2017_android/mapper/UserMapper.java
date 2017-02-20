package com.github.maximkirko.training_2017_android.mapper;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.model.User;

import java.sql.Timestamp;

/**
 * Created by MadMax on 23.01.2017.
 */

public class UserMapper {

    @Nullable
    public static User convert(@NonNull Cursor cursor) {
        if (cursor.getCount() < 1) {
            return null;
        }
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.USER_TABLE_FIELD_ID)));
        user.setFirst_name(cursor.getString(cursor.getColumnIndex(DBHelper.USER_TABLE_FIELD_FIRST_NAME)));
        user.setLast_name(cursor.getString(cursor.getColumnIndex(DBHelper.USER_TABLE_FIELD_LAST_NAME)));
        user.setPhoto_100(cursor.getString(cursor.getColumnIndex(DBHelper.USER_TABLE_FIELD_PHOTO_100)));
        user.setOnline(cursor.getInt(cursor.getColumnIndex(DBHelper.USER_TABLE_FIELD_ONLINE)) > 0);
        try {
            user.setIs_favorite(cursor.getInt(cursor.getColumnIndex(DBHelper.FRIENDS_TABLE_FIELD_IS_FAVORITE)) > 0);
            user.setLast_seen(new Timestamp(cursor.getLong(cursor.getColumnIndex(DBHelper.FRIENDS_TABLE_FIELD_LAST_SEEN))));
            user.setRating(cursor.getInt(cursor.getColumnIndex(DBHelper.FRIENDS_TABLE_FIELD_RATING)));
        } catch (IllegalStateException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
        }
        return user;
    }
}
