package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

/**
 * Created by MadMax on 28.12.2016.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView friendsCountView;
    private TextView friendsCountOnlineView;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        friendsCountView = (TextView) itemView.findViewById(R.id.textview_frriendslist_header_friendscount);
        friendsCountOnlineView = (TextView) itemView.findViewById(R.id.textview_frriendslist_header_friendscount_online);
    }

    public void onBindData(@NonNull int friendsCount, @NonNull int friendsCountOnline) {
        friendsCountView.setText(friendsCount + "");
        friendsCountOnlineView.setText(friendsCountOnline + "");
    }
}
