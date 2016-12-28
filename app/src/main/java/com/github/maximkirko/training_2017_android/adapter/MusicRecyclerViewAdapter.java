package com.github.maximkirko.training_2017_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private View itemView;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private static final int HEADER_POSITION = 0;

    public MusicRecyclerViewAdapter(List<Song> music, View.OnClickListener onClickListener) {
        this.music = music;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.music_recycler_view_header, viewGroup, false);
            return HeaderViewHolder.newBuilder(itemView).build();
        }
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.music_recycler_view_item, viewGroup, false);
            itemView.setOnClickListener(onClickListener);
            return SongViewHolder.newBuilder(itemView).build();
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

            TextView title = (TextView) itemView.findViewById(R.id.header_title);

            viewHolder = HeaderViewHolder.newBuilder(itemView)
                    .setTitle(title)
                    .build();

        } else {
            Song song = music.get(position - 1);
//            Log.i("song in pos", position - 1 + " = " + song.toString());

            TextView title = (TextView) itemView.findViewById(R.id.item_title);
            TextView description = (TextView) itemView.findViewById(R.id.item_desription);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_image);

            title.setText(song.getTitle());
            description.setText(song.getDescription());
            imageView.setImageResource(song.getImageId());

            viewHolder = SongViewHolder.newBuilder(itemView)
                    .setTitle(title)
                    .setDescription(description)
                    .setImageView(imageView)
                    .build();
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