package com.github.maximkirko.training_2017_android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by MadMax on 10.01.2017.
 */

public class User implements Parcelable {

    private int id;
    private String first_name;
    private String last_name;
    private String photo_100;
    private boolean online;
    private Timestamp last_seen;
    private boolean is_favorite;
    private int rating;

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

    public Timestamp getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(Timestamp last_seen) {
        this.last_seen = last_seen;
    }

    public boolean is_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User() {
    }

    public User(int id, String first_name, String last_name, String photo_100, boolean online, boolean is_favorite, int rating) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_100 = photo_100;
        this.online = online;
        this.is_favorite = is_favorite;
        this.rating = rating;
    }

    protected User(Parcel in) {
        id = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        photo_100 = in.readString();
        online = in.readByte() != 0;
        is_favorite = in.readByte() != 0;
        rating = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(photo_100);
        dest.writeByte((byte) (online ? 1 : 0));
        dest.writeByte((byte) (is_favorite ? 1 : 0));
        dest.writeInt(rating);
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", photo_100='" + photo_100 + '\'' +
                ", online=" + online +
                ", last_seen=" + last_seen +
                ", is_favorite=" + is_favorite +
                ", rating=" + rating +
                '}';
    }
}
