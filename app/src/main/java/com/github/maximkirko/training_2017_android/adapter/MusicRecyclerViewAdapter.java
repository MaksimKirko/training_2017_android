package com.github.maximkirko.training_2017_android.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.viewholder.HeaderViewHolder;
import com.github.maximkirko.training_2017_android.adapter.viewholder.SongClickListener;
import com.github.maximkirko.training_2017_android.adapter.viewholder.SongViewHolder;
import com.github.maximkirko.training_2017_android.model.Song;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by MadMax on 25.12.2016.
 */

public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Song> music;
    private SongClickListener songClickListener;
    private Context context;

    private static final int HEADER_POSITION = 0;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    @Retention(SOURCE)
    @IntDef({TYPE_HEADER, TYPE_ITEM})
    public @interface ItemType {
    }

    @Override
    @ItemType
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public MusicRecyclerViewAdapter(List<Song> music, SongClickListener songClickListener, Context context) {
        this.music = music;
        this.songClickListener = songClickListener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;

        if (viewType == TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.music_recycler_view_header, viewGroup, false);
            return new HeaderViewHolder(itemView, context);
        }
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.music_recycler_view_item, viewGroup, false);


            SongViewHolder songViewHolder = new SongViewHolder(itemView, context);
            songViewHolder.setSongClickListener(songClickListener);
            return songViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == HEADER_POSITION) {
            viewHolder = new HeaderViewHolder(viewHolder.itemView, context);
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