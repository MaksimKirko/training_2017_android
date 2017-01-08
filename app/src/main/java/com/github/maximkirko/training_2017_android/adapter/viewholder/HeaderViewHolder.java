package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

/**
 * Created by MadMax on 28.12.2016.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        this.titleView = (TextView) itemView.findViewById(R.id.textview_musiclist_headertitle);
    }

    public void onBindData(@Nullable String title) {
        this.titleView.setText(title);
    }
}
