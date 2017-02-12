package com.github.maximkirko.training_2017_android.reader;

import android.support.annotation.NonNull;
import android.util.JsonReader;

import com.github.maximkirko.training_2017_android.model.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MadMax on 10.01.2017.
 */

public class UserJSONReader implements Reader<User> {

    private String jsonFriendsList;

    public UserJSONReader(@NonNull String jsonFriendsList) {
        this.jsonFriendsList = jsonFriendsList;
    }

    public User read() throws IOException {
        InputStream in;
        in = new ByteArrayInputStream(jsonFriendsList.getBytes());
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginObject();
            if (reader.nextName().equals("response")) {
                reader.beginArray();
            }
            return readUser(reader);
        } finally {
            reader.close();
        }
    }

    public List<User> readToList() throws IOException {
        InputStream in;
        in = new ByteArrayInputStream(jsonFriendsList.getBytes());
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readUsersArray(reader);
        } finally {
            reader.close();
        }
    }

    private List<User> readUsersArray(JsonReader reader) throws IOException {
        List<User> users = new ArrayList<>();

        reader.beginObject();
        if (reader.nextName().equals("response")) {
            reader.beginObject();
        }
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("items")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    users.add(readUser(reader));
                }
                reader.endArray();
                return users;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return users;
    }

    private User readUser(JsonReader reader) throws IOException {
        int id = 0;
        String first_name = null;
        String last_name = null;
        String photo_100 = null;
        int online = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("first_name")) {
                first_name = reader.nextString();
            } else if (name.equals("last_name")) {
                last_name = reader.nextString();
            } else if (name.equals("photo_100")) {
                photo_100 = reader.nextString();
            } else if (name.equals("online")) {
                online = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new User(id, first_name, last_name, photo_100, online != 0, false, 0);
    }
}
