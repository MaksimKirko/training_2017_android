package com.github.maximkirko.training_2017_android.contentobserver;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

public class FriendsContentObserver extends ContentObserver {

    private RecyclerView.Adapter adapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public FriendsContentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        adapter.notifyDataSetChanged();
    }

}