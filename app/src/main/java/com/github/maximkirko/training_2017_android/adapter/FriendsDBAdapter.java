package com.github.maximkirko.training_2017_android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.model.User;

import java.util.List;

/**
 * Created by MadMax on 23.01.2017.
 */

public class FriendsDBAdapter extends CursorAdapter {

    private List<User> friends;
    private UserClickListener songClickListener;

    public FriendsDBAdapter(Context context, Cursor c, boolean autoRequery, List<User> friends, UserClickListener songClickListener) {
        super(context, c, autoRequery);
        this.friends = friends;
        this.songClickListener = songClickListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
