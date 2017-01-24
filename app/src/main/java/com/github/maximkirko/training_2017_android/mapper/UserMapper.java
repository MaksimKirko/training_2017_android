package com.github.maximkirko.training_2017_android.mapper;

import android.database.Cursor;
import android.util.Log;

import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 23.01.2017.
 */

public class UserMapper {

    public static User convert(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.DB_FIELD_USER_ID)));
        user.setFirst_name(cursor.getString(cursor.getColumnIndex(DBHelper.DB_FIELD_FIRST_NAME)));
        user.setLast_name(cursor.getString(cursor.getColumnIndex(DBHelper.DB_FIELD_LAST_NAME)));
        user.setPhoto_100(cursor.getString(cursor.getColumnIndex(DBHelper.DB_FIELD_PHOTO_100)));
        user.setOnline(cursor.getInt(cursor.getColumnIndex(DBHelper.DB_FIELD_ONLINE)) > 0);
        return user;
    }
}
