package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SongViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView description;
    private ImageView imageView;

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public ImageView getImageView() {
        return imageView;
    }

    private SongViewHolder(View itemView) {
        super(itemView);
    }

    public static Builder newBuilder(View itemView) {
        return new SongViewHolder(itemView).new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setTitle(TextView title) {
            SongViewHolder.this.title = title;
            return this;
        }

        public Builder setDescription(TextView description) {
            SongViewHolder.this.description = description;
            return this;
        }

        public Builder setImageView(ImageView imageView) {
            SongViewHolder.this.imageView = imageView;
            return this;
        }

        public SongViewHolder build() {
            SongViewHolder songViewHolder = new SongViewHolder(itemView);
            songViewHolder.title = SongViewHolder.this.title;
            songViewHolder.description = SongViewHolder.this.description;
            songViewHolder.imageView = SongViewHolder.this.imageView;
            return songViewHolder;
        }
    }
}