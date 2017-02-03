package com.github.maximkirko.training_2017_android.adapter;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.itemtype.ItemTypeAware;
import com.github.maximkirko.training_2017_android.adapter.viewholder.HeaderViewHolder;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserViewHolder;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.UserUtils;

public class FriendsCursorAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {

    private UserClickListener songClickListener;

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public FriendsCursorAdapter(Cursor c, UserClickListener songClickListener) {
        super(c);
        this.songClickListener = songClickListener;
    }

    @ItemTypeAware.ItemType
    @Override
    public int getItemViewType(int position) {
        return ItemTypeAware.getItemViewType(position);
    }

    @Nullable
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, @ItemTypeAware.ItemType int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == ItemTypeAware.TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_header, viewGroup, false);
            viewHolder = new HeaderViewHolder(itemView);
        } else if (viewType == ItemTypeAware.TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_item, viewGroup, false);
            viewHolder = new UserViewHolder(itemView, songClickListener);
            if (cursor.moveToNext()) ;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        if (viewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) viewHolder).onBindData(cursor.getCount(), UserUtils.getOnlineCount(cursor));
        } else if (viewHolder instanceof UserViewHolder) {
            User user = UserMapper.convert(cursor);
            ((UserViewHolder) viewHolder).onBindData(user);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).cancelTask();
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount() + 1;
    }
}