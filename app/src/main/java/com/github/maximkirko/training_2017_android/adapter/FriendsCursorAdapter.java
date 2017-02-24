package com.github.maximkirko.training_2017_android.adapter;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.itemtype.FriendsListItemTypeAware;
import com.github.maximkirko.training_2017_android.adapter.viewholder.CheckBoxOnChangeListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.HeaderViewHolder;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserClickListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.UserViewHolder;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.asynctask.TaskFinishedCallback;
import com.github.maximkirko.training_2017_android.db.DBHelper;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.UserUtils;

public class FriendsCursorAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> implements Filterable {

    private FriendsFilter friendsFilter;
    private UserClickListener songClickListener;
    private CheckBoxOnChangeListener checkBoxOnChangeListener;
    private String query;

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public FriendsCursorAdapter(Cursor c, UserClickListener songClickListener, CheckBoxOnChangeListener checkBoxOnChangeListener) {
        super(c);
        this.songClickListener = songClickListener;
        this.checkBoxOnChangeListener = checkBoxOnChangeListener;
    }

    @FriendsListItemTypeAware.FriendsListItemType
    @Override
    public int getItemViewType(int position) {
        return FriendsListItemTypeAware.getItemViewType(position);
    }

    @Nullable
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, @FriendsListItemTypeAware.FriendsListItemType int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == FriendsListItemTypeAware.TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_header, viewGroup, false);
            viewHolder = new HeaderViewHolder(itemView);
        } else if (viewType == FriendsListItemTypeAware.TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friendslist_recycler_view_item, viewGroup, false);
            viewHolder = new UserViewHolder(itemView, songClickListener, checkBoxOnChangeListener);
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
            ((UserViewHolder) viewHolder).onBindData(user, query);
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
        return cursor == null ? 0 : cursor.getCount() + 1;
    }

    @Override
    public Filter getFilter() {
        if (friendsFilter == null) {
            friendsFilter = new FriendsFilter();
        }
        return friendsFilter;
    }

    class FriendsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            DBHelper dbHelper = VKSimpleChatApplication.getDbHelper();
            cursor = dbHelper.searchFriends(dbHelper.getReadableDatabase(), DBHelper.FAVORITE_FRIEND_TABLE_NAME, constraint.toString());
            results.values = cursor;
            results.count = cursor.getCount();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}