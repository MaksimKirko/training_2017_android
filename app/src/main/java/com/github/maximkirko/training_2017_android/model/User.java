package com.github.maximkirko.training_2017_android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MadMax on 10.01.2017.
 */

public class User implements Parcelable {

    private int id;
    private String first_name;
    private String last_name;
    private String photo_100;
    private boolean online;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhoto_100() {
        return photo_100;
    }

    public void setPhoto_100(String photo_100) {
        this.photo_100 = photo_100;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    protected User(Parcel in) {
        id = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        photo_100 = in.readString();
        online = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(photo_100);
        dest.writeByte((byte) (online ? 1 : 0));
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
    }

    public User(int id, String first_name, String last_name, String photo_100, boolean online) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_100 = photo_100;
        this.online = online;
    }
}
