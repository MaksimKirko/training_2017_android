package com.github.maximkirko.training_2017_android;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadMax on 25.12.2016.
 */

public class Song implements Parcelable {

    private static final String DEFAULT_TITLE = "Unknown title";
    private static final String DEFAULT_DESCRIPTION = "Unknown description";
    private static final int DEFAULT_IMAGE_ID = R.drawable.rv_item_image_default;


    private String title;
    private String description;
    private int imageId;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
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

    public static void validate(Song song) {
        if(song.getTitle() == null) {
            song.setTitle(DEFAULT_TITLE);
        }
        if(song.getDescription() == null) {
            song.setDescription(DEFAULT_DESCRIPTION);
        }
        if(song.getImageId() == 0) {
            song.setImageId(DEFAULT_IMAGE_ID);
        }
    }

    public static List<Song> getSongsList() {

        List<Song> songs = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            Song song = new Song();
            song.setTitle("Title " + i);
            song.setDescription("Description " + i);
            song.setImageId(R.drawable.rv_item_image_default);
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
