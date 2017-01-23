package com.github.maximkirko.training_2017_android.mapper;

import android.content.ContentValues;

import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 23.01.2017.
 */

public class UserMapper {

    public static ContentValues convert(User user) {
        ContentValues values = new ContentValues();

        values.put("id", user.getId());
        values.put("first_name", user.getFirst_name());
        values.put("last_name", user.getLast_name());
        values.put("photo_100", user.getPhoto_100());
        values.put("online", user.isOnline());

        return values;
    }
}
