package com.github.maximkirko.training_2017_android.adapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.viewholder.HeaderViewHolder;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserViewHolder;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.UsersUtils;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by MadMax on 25.12.2016.
 */

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> friends;
    private UserClickListener songClickListener;

    public static final int HEADER_POSITION = 0;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    @Retention(SOURCE)
    @IntDef({TYPE_HEADER, TYPE_ITEM})
    public @interface ItemType {
    }

    @Override
    @ItemType
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public FriendsAdapter(List<User> friends, UserClickListener songClickListener) {
        this.friends = friends;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, @ItemType int viewType) {
        View itemView;
        if (viewType == TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        }
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_item, viewGroup, false);
            return new UserViewHolder(itemView, songClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) viewHolder).onBindData(friends.size(), UsersUtils.getOnlineCount(friends));
        } else if (viewHolder instanceof UserViewHolder) {
            User user = friends.get(position - 1);
            ((UserViewHolder) viewHolder).onBindData(user, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return friends.size() + 1;
    }
}