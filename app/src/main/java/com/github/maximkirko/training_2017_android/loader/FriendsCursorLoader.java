package com.github.maximkirko.training_2017_android.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;

/**
 * Created by MadMax on 22.01.2017.
 */

public class FriendsCursorLoader extends CursorLoader {

    private FriendsContentProvider friendsContentProvider;

    public FriendsCursorLoader(Context context) {
        super(context);
        this.friendsContentProvider = new FriendsContentProvider();
    }

    @Override
    public Cursor loadInBackground() {
        return friendsContentProvider.query(FriendsContentProvider.FRIENDS_CONTENT_URI, null, null, null, null);
    }
}
