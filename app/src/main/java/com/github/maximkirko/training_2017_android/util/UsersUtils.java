package com.github.maximkirko.training_2017_android.util;

import com.github.maximkirko.training_2017_android.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadMax on 14.01.2017.
 */

public class UsersUtils {

    public static int getOnlineCount(List<User> users) {
        int i = 0;
        for (User user : users) {
            if (user.isOnline()) {
                i++;
            }
        }
        return i;
    }

    public static List<User> getOnline(List<User> users) {
        List<User> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline()) {
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }
}
