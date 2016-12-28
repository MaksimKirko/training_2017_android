package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by MadMax on 28.12.2016.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView title;

    public TextView getTitle() {
        return title;
    }

    private HeaderViewHolder(View itemView) {
        super(itemView);
    }

    public static Builder newBuilder(View itemView) {
        return new HeaderViewHolder(itemView).new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public HeaderViewHolder.Builder setTitle(TextView title) {
            HeaderViewHolder.this.title = title;
            return this;
        }

        public HeaderViewHolder build() {
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(itemView);
            headerViewHolder.title = HeaderViewHolder.this.title;
            return headerViewHolder;
        }
    }
}
