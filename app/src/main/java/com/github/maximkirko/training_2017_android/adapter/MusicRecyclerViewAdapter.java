package com.github.maximkirko.training_2017_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.viewholder.SongViewHolder;
import com.github.maximkirko.training_2017_android.model.Song;

import java.util.List;

/**
 * Created by MadMax on 25.12.2016.
 */

public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<SongViewHolder> {

    private List<Song> music;
    private View.OnClickListener onClickListener;
    private View itemView;

    public MusicRecyclerViewAdapter(List<Song> music, View.OnClickListener onClickListener) {
        this.music = music;
        this.onClickListener = onClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.music_recycler_view_item, viewGroup, false);
        itemView.setOnClickListener(onClickListener);
        return SongViewHolder.newBuilder(itemView).build();
    }

    @Override
    public void onBindViewHolder(SongViewHolder songViewHolder, int position) {
        Song song = music.get(position);

        TextView title = (TextView) itemView.findViewById(R.id.item_title);
        TextView description = (TextView) itemView.findViewById(R.id.item_desription);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.item_image);

        title.setText(song.getTitle());
        description.setText(song.getDescription());
        imageView.setImageResource(song.getImageId());

        songViewHolder = SongViewHolder.newBuilder(itemView)
                .setTitle(title)
                .setDescription(description)
                .setImageView(imageView)
                .build();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return music.size();
    }
}