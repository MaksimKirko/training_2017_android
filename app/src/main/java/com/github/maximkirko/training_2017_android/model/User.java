package com.github.maximkirko.training_2017_android.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.vk.sdk.api.model.VKApiUser;

/**
 * Created by MadMax on 10.01.2017.
 */

public class User extends VKApiUser implements Parcelable {

    private static final String DEFAULT_NAME = "Unknown user";

    private Bitmap userPhoto50;

    protected User(Parcel in) {
        super(in);
        userPhoto50 = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(userPhoto50, flags);
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

    public Bitmap getUserPhoto50() {
        return userPhoto50;
    }

    public void setUserPhoto50(Bitmap userPhoto50) {
        this.userPhoto50 = userPhoto50;
    }

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
