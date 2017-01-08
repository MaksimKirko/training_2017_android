package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.model.Song;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

import java.lang.ref.WeakReference;

public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView titleView;
    private TextView descriptionView;
    private ImageView imageView;

    private WeakReference<SongClickListener> songClickListener;

    @Override
    public void onClick(View v) {
        SongClickListener songClickListener = this.songClickListener.get();
        if (songClickListener != null) {
            songClickListener.onItemClick(getSongByClick());
        }
    }

    private Song getSongByClick() {
        Song song = new Song();
        song.setTitle(titleView.getText().toString());
        song.setDescription(descriptionView.getText().toString());
        return song;
    }

    public SongViewHolder(@NonNull View itemView, @NonNull SongClickListener songClickListener) {
        super(itemView);

        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        this.songClickListener = new WeakReference<>(songClickListener);
        itemView.setOnClickListener(this);

        this.titleView = (TextView) itemView.findViewById(R.id.textview_musiclist_itemtitle);
        this.descriptionView = (TextView) itemView.findViewById(R.id.textview_musiclist_item_description);
        this.imageView = (ImageView) itemView.findViewById(R.id.imageview_musiclist_item_icon);
    }

    public void onBindData(@Nullable String title, @Nullable String description, @Nullable int imageId) {
        this.titleView.setText(title);
        this.descriptionView.setText(description);
        this.imageView.setImageResource(imageId);
    }
}