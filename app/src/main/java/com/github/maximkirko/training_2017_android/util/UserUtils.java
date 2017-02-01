package com.github.maximkirko.training_2017_android.util;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadMax on 14.01.2017.
 */

public class UserUtils {

    @Nullable
    public static User getById(@NonNull List<User> users, @NonNull int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public static int getUserPositionById(@NonNull List<User> users, @NonNull int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return users.indexOf(user);
            }
        }
        return -1;
    }

    public static int getOnlineCount(@NonNull List<User> users) {
        int i = 0;
        for (User user : users) {
            if (user.isOnline()) {
                i++;
            }
        }
        return i;
    }

    public static int getOnlineCount(@NonNull Cursor cursor) {
        int i = 0;
        while (cursor.moveToNext()) {
            User user = UserMapper.convert(cursor);
            if (user.isOnline()) {
                i++;
            }
        }
        return i;
    }

    @Nullable
    public static List<User> getOnline(@NonNull List<User> users) {
        List<User> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline()) {
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }

    @NonNull
    public static List<User> getUserListFromCursor(@NonNull Cursor cursor) {
        List<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            users.add(UserMapper.convert(cursor));
        }
        return users;
    }
}
