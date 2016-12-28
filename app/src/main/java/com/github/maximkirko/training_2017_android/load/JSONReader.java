package com.github.maximkirko.training_2017_android.load;

import android.content.Context;
import android.util.JsonReader;

import com.github.maximkirko.training_2017_android.model.Song;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadMax on 26.12.2016.
 */

public class JSONReader implements IReader<Song> {

    private int resourceId;

    public JSONReader(int recourceId) {
        this.resourceId = recourceId;
    }

    public List<Song> readToList(Context context) throws IOException {
        InputStream in = context.getResources().openRawResource(resourceId);
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readSongsArray(reader);
        } finally {
            reader.close();
        }
    }

    private List<Song> readSongsArray(JsonReader reader) throws IOException {
        List<Song> songs = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            songs.add(readSong(reader));
        }
        reader.endArray();
        return songs;
    }

    private Song readSong(JsonReader reader) throws IOException {
        String title = null;
        String description = null;
        String imageUrl = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                title = reader.nextString();
            } else if (name.equals("description")) {
                description = reader.nextString();
            } else if (name.equals("imageUrl")) {
                imageUrl = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Song(title, description);
    }

}
