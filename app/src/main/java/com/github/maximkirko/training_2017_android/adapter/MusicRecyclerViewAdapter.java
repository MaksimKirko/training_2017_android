package com.github.maximkirko.training_2017_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.viewholder.HeaderViewHolder;
import com.github.maximkirko.training_2017_android.adapter.viewholder.SongViewHolder;
import com.github.maximkirko.training_2017_android.model.Song;

import java.util.List;

/**
 * Created by MadMax on 25.12.2016.
 */

public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Song> music;
    private View.OnClickListener onClickListener;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private static final int HEADER_POSITION = 0;

    public MusicRecyclerViewAdapter(List<Song> music, View.OnClickListener onClickListener) {
        this.music = music;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView;

        if (viewType == TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.music_recycler_view_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        }
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.music_recycler_view_item, viewGroup, false);
            itemView.setOnClickListener(onClickListener);

            SongViewHolder songViewHolder = new SongViewHolder(itemView);
            songViewHolder.setOnClickListener(onClickListener);
            return songViewHolder;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == HEADER_POSITION) {
            viewHolder = new HeaderViewHolder(viewHolder.itemView);
        } else {
            Song song = music.get(position - 1);
            viewHolder = new SongViewHolder(viewHolder.itemView, song.getTitle(), song.getDescription(), song.getImageId());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return music.size() + 1;
    }
}