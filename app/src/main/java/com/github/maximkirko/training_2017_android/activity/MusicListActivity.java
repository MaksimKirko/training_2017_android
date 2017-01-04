package com.github.maximkirko.training_2017_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.adapter.MusicRecyclerViewAdapter;
import com.github.maximkirko.training_2017_android.itemanimator.LandingAnimator;
import com.github.maximkirko.training_2017_android.itemdecorator.SpacesItemDecoration;
import com.github.maximkirko.training_2017_android.load.IReader;
import com.github.maximkirko.training_2017_android.load.JSONReader;
import com.github.maximkirko.training_2017_android.model.Song;

import java.io.IOException;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {

    private RecyclerView musicRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private RecyclerView.ItemAnimator itemAnimator;

    private List<Song> music;

    private IReader<Song> jsonReader;

    public static final String SONG_EXTRA = "SONG";
    public static final int MUSIC_RESOURCE_ID = R.raw.music;
    public static final int CARDS_SPACE_PIXEL = 10;

    public View.OnClickListener onMusicRecyclerViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Song song = getSongByClick(view);
            startSongActivity(song);
        }
    };

    private void startSongActivity(Song song) {
        Intent intent = new Intent(MusicListActivity.this, SongActivity.class);
        intent.putExtra(SONG_EXTRA, song);
        startActivity(intent);
    }

    private Song getSongByClick(View view) {
        int itemPosition = musicRecyclerView.getChildLayoutPosition(view) - 1;
        return music.get(itemPosition);
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
        recyclerViewAdapter = new MusicRecyclerViewAdapter(music, onMusicRecyclerViewClickListener);
    }

    private void initItemDecoration() {
        itemDecoration = new SpacesItemDecoration(CARDS_SPACE_PIXEL); //new DividerItemDecoration(this, R.drawable.divider);
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

}
