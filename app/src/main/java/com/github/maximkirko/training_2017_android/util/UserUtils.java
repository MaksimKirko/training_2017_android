package com.github.maximkirko.training_2017_android.util;

import android.app.SearchManager;
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

    public static boolean isUserComplete(User user) {
        if (user.getId() == 0 || user.getFirst_name() == null || user.getLast_name() == null || user.getPhoto_100() == null) {
            return false;
        }
        return true;
    }

    public static User parseUserCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        user.setFirst_name(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));
        user.setLast_name(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)));
        user.setPhoto_100(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1)));
        return user;
    }
}
