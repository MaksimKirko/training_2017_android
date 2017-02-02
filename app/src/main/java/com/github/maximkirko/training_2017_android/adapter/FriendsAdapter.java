package com.github.maximkirko.training_2017_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.itemtype.ItemTypeAware;
import com.github.maximkirko.training_2017_android.adapter.viewholder.HeaderViewHolder;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserViewHolder;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.UserUtils;

import java.util.List;

/**
 * Created by MadMax on 25.12.2016.
 */

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> friends;
    private UserClickListener songClickListener;

    @ItemTypeAware.ItemType
    @Override
    public int getItemViewType(int position) {
        return ItemTypeAware.getItemViewType(position);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, @ItemTypeAware.ItemType int viewType) {
        View itemView;
        if (viewType == ItemTypeAware.TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        }
        if (viewType == ItemTypeAware.TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_item, viewGroup, false);
            return new UserViewHolder(itemView, songClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) viewHolder).onBindData(friends.size(), UserUtils.getOnlineCount(friends));
        } else if (viewHolder instanceof UserViewHolder) {
            User user = friends.get(position - 1);
            ((UserViewHolder) viewHolder).onBindData(user);
        }
    }

    @Override
    public int getItemCount() {
        return friends.size() + 1;
    }
}