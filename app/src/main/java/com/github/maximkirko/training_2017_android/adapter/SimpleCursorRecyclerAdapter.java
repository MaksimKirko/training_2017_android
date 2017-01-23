package com.github.maximkirko.training_2017_android.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.viewholder.HeaderViewHolder;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserViewHolder;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.UsersUtils;

public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {

    private UserClickListener songClickListener;

    public SimpleCursorRecyclerAdapter(Cursor c, UserClickListener songClickListener) {
        super(c);
        this.songClickListener = songClickListener;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).cancelTask();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, @FriendsAdapter.ItemType int viewType) {
        View itemView;

        if (viewType == FriendsAdapter.TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        }
        if (viewType == FriendsAdapter.TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_item, viewGroup, false);
            return new UserViewHolder(itemView, songClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        if (viewHolder instanceof HeaderViewHolder) {
            //((HeaderViewHolder) viewHolder).onBindData(friends.size(), UsersUtils.getOnlineCount(friends));
        } else if (viewHolder instanceof UserViewHolder) {
            User user = UserMapper.convert()
            ((UserViewHolder) viewHolder).onBindData(user, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return friends.size() + 1;
    }
}