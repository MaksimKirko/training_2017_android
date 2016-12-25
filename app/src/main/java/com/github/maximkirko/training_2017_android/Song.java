package com.github.maximkirko.training_2017_android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadMax on 25.12.2016.
 */

public class Song {

    private String title;
    private String description;
    private int imageId;

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

}
