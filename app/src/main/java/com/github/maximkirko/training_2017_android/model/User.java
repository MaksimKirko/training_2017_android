package com.github.maximkirko.training_2017_android.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;

/**
 * Created by MadMax on 10.01.2017.
 */

public class User extends VKApiUserFull implements Parcelable {

    private static final String DEFAULT_NAME = "Unknown user";

    protected User(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {
        super();
        first_name = DEFAULT_NAME;
    }

    public User(int id, String first_name, String last_name, String photo_100, boolean online) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_100 = photo_100;
        this.online = online;
    }
}
