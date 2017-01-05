package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.model.Song;

import java.lang.ref.WeakReference;

public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView title;
    private TextView description;
    private ImageView imageView;

    private WeakReference<SongClickListener> songClickListener;

    @Override
    public void onClick(View v) {
        songClickListener.get().onItemClick(getSongByClick());
    }

    private Song getSongByClick() {
        Song song = new Song();
        song.setTitle(title.getText().toString());
        song.setDescription(description.getText().toString());
//        song.setImageId(imageView.getId());
        return song;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setSongClickListener(SongClickListener songClickListener) {
        this.songClickListener = new WeakReference<SongClickListener>(songClickListener);
    }

    public SongViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.description = (TextView) itemView.findViewById(R.id.item_desription);
        this.imageView = (ImageView) itemView.findViewById(R.id.item_image);

    }

    public SongViewHolder(View itemView, String title, String description, int imageId) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.description = (TextView) itemView.findViewById(R.id.item_desription);
        this.imageView = (ImageView) itemView.findViewById(R.id.item_image);

        this.title.setText(title);
        this.description.setText(description);
        this.imageView.setImageResource(imageId);
    }
}