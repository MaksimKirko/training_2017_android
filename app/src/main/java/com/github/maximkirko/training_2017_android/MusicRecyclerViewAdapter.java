package com.github.maximkirko.training_2017_android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MadMax on 25.12.2016.
 */

public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<MusicRecyclerViewAdapter.SongViewHolder> {

    private List<Song> music;
    private RecyclerView recyclerView;
    private View.OnClickListener onClickListener;

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public ImageView imageView;

        public SongViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            description = (TextView) itemView.findViewById(R.id.item_desription);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }

    public MusicRecyclerViewAdapter(List<Song> music, View.OnClickListener onClickListener) {
        this.music = music;
        this.onClickListener = onClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.music_recycler_view_item, viewGroup, false);
        v.setOnClickListener(onClickListener);
        SongViewHolder svh = new SongViewHolder(v);

        return svh;
    }

    @Override
    public void onBindViewHolder(SongViewHolder songViewHolder, int position) {

        Song song = music.get(position);
        Song.validate(song);

        songViewHolder.title.setText(song.getTitle());
        songViewHolder.description.setText(song.getDescription());
        songViewHolder.imageView.setImageResource(song.getImageId());
    }

    //TODO google it!
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return music.size();
    }
}