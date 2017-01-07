package com.github.maximkirko.training_2017_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.MusicRecyclerViewAdapter;
import com.github.maximkirko.training_2017_android.adapter.viewholder.SongClickListener;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.MusicListItemDecorator;
import com.github.maximkirko.training_2017_android.load.JSONReader;
import com.github.maximkirko.training_2017_android.load.Reader;
import com.github.maximkirko.training_2017_android.model.Song;

import java.io.IOException;
import java.util.List;

public class MusicListActivity extends AppCompatActivity
        implements SongClickListener {

    private RecyclerView musicRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerView.ItemAnimator itemAnimator;

    private List<Song> music;

    private Reader<Song> jsonReader;

    public static final String SONG_EXTRA = "SONG";
    public static final int MUSIC_RESOURCE_ID = R.raw.music;

    private FloatingActionButton fabAdd;
    private FloatingActionButton fabRemove;

    @Override
    public void onItemClick(Song song) {
        startSongActivity(song);
    }

    private void startSongActivity(Song song) {
        Intent intent = new Intent(MusicListActivity.this, SongActivity.class);
        intent.putExtra(SONG_EXTRA, song);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list_activity);

        initJSONReader();
        initAdapter();
        initItemDecoration();
        initItemAnimator();
        initRecyclerView();
        initFabs();
    }

    private void initJSONReader() {
        jsonReader = new JSONReader(MUSIC_RESOURCE_ID);
    }

    private void initAdapter() {
        try {
            music = jsonReader.readToList(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerViewAdapter = new MusicRecyclerViewAdapter(music, this, this);
    }

    private void initItemDecoration() {
        int offset = this.getResources().getDimensionPixelSize(R.dimen.music_recycler_view_item_card_layout_margin);
        itemDecoration = new MusicListItemDecorator(offset);
    }

    private void initItemAnimator() {
        itemAnimator = new LandingAnimator();
    }

    private void initRecyclerView() {
        layoutManager = new GridLayoutManager(this, 2);

        musicRecyclerView = (RecyclerView) findViewById(R.id.music_recycler_view);
        musicRecyclerView.setLayoutManager(layoutManager);
        musicRecyclerView.addItemDecoration(itemDecoration);
        musicRecyclerView.setItemAnimator(itemAnimator);
        musicRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initFabs() {
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.add(new Song());
                recyclerViewAdapter.notifyItemInserted(music.size());
            }
        });

        fabRemove = (FloatingActionButton) findViewById(R.id.fabRemove);
        fabRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = music.size();
                if (index > 1) {
                    music.remove(index - 1);
                    recyclerViewAdapter.notifyItemRemoved(index + 1);
                }
            }
        });
    }
}
