package com.github.maximkirko.training_2017_android.reader;

import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.Log;

import com.github.maximkirko.training_2017_android.model.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        User user = new User();
        try {
            reader.beginObject();
        } catch (IllegalStateException e) {
            Log.e(e.getClass().getSimpleName(), jsonFriendsList);
        }
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                user.setId(reader.nextInt());
            } else if (name.equals("first_name")) {
                user.setFirst_name(reader.nextString());
            } else if (name.equals("last_name")) {
                user.setLast_name(reader.nextString());
            } else if (name.equals("photo_100")) {
                user.setPhoto_100(reader.nextString());
            } else if (name.equals("online")) {
                user.setOnline(reader.nextInt() != 0);
            } else if (name.equals("last_seen")) {
                reader.beginObject();
                String last_seen_name = reader.nextName();
                if (last_seen_name.equals("time")) {
                    user.setLast_seen(new Timestamp(reader.nextLong() * 1000));
                }
            } else if (name.equals("platform")) {
                reader.nextInt();
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return user;
    }
}
