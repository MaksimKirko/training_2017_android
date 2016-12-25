package com.github.maximkirko.training_2017_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView musicRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;

    private List<Song> music;

    public View.OnClickListener onMusicRecyclerViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = musicRecyclerView.getChildLayoutPosition(view);
            Song song = music.get(itemPosition);
            Intent intent = new Intent(MainActivity.this, SongActivity.class);
            //setIntentExtras(intent);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAdapter();
        initItemDecoration();
        initRecyclerView();
    }

    private void initAdapter() {
        music = Song.getSongsList();
        recyclerViewAdapter = new MusicRecyclerViewAdapter(music, onMusicRecyclerViewClickListener);
    }

    private void initItemDecoration() {
        itemDecoration = new DividerItemDecoration(this, R.drawable.divider);
    }

    private void initRecyclerView() {

        layoutManager = new LinearLayoutManager(this);

        musicRecyclerView = (RecyclerView) findViewById(R.id.music_recycler_view);
        musicRecyclerView.setLayoutManager(layoutManager);
        musicRecyclerView.setHasFixedSize(true);
        musicRecyclerView.addItemDecoration(itemDecoration);
        musicRecyclerView.setAdapter(recyclerViewAdapter);
    }

}
