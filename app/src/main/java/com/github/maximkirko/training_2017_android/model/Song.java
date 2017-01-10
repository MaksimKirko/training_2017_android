package com.github.maximkirko.training_2017_android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.maximkirko.training_2017_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadMax on 25.12.2016.
 */

public class Song implements Parcelable {

    private String title;
    private String description;
    private int imageId;

    private static final String DEFAULT_TITLE = "Unknown title";
    private static final String DEFAULT_DESCRIPTION = "Unknown description";
    private static final int DEFAULT_IMAGE_ID = R.drawable.musiclist_default_image;

    public Song() {

    }

    public Song(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected Song(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageId = in.readInt();
    }

    @NonNull
    public String getTitle() {
        return (title == null) ? DEFAULT_TITLE : title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return (description == null) ? DEFAULT_DESCRIPTION : description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @NonNull
    public int getImageId() {
        return (imageId == 0) ? DEFAULT_IMAGE_ID : imageId;
    }

    public void setImageId(@Nullable int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageId=" + imageId +
                '}';
    }

    public static List<Song> getSongsList() {

        List<Song> songs = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            Song song = new Song();
            song.setTitle("Title " + i);
            song.setDescription("Description " + i);
            song.setImageId(R.drawable.musiclist_default_image);
            songs.add(song);
        }

        return songs;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(imageId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

}
