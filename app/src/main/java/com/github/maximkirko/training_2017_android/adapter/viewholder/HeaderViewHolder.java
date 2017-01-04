package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;

/**
 * Created by MadMax on 28.12.2016.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView title;

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public HeaderViewHolder(View itemView) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.header_title);
    }

    public HeaderViewHolder(View itemView, String title) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.header_title);
        this.title.setText(title);
    }
}
