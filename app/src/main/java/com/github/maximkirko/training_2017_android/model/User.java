package com.github.maximkirko.training_2017_android.model;

import com.vk.sdk.api.model.VKApiUser;

/**
 * Created by MadMax on 10.01.2017.
 */

public class User extends VKApiUser {

    private static final String DEFAULT_NAME = "Unknown user";

    public User() {
        super();
        first_name = DEFAULT_NAME;
    }

    public User(int id, String first_name, String last_name, String photo_50, boolean online) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_50 = photo_50;
        this.online = online;
    }

}
