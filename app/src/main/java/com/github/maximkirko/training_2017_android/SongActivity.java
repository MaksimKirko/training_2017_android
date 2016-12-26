package com.github.maximkirko.training_2017_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MadMax on 25.12.2016.
 */

public class SongActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvDescription;
    private ImageView ivImage;

    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_activity);

        song = this.getIntent().getParcelableExtra(MainActivity.SONG_EXTRA);

        initViews();
        setViewsValues();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.song_activity_title);
        tvDescription = (TextView) findViewById(R.id.song_activity_description);
        ivImage = (ImageView) findViewById(R.id.song_activity_image);
    }

    private void setViewsValues() {
        tvTitle.setText(song.getTitle());
        tvDescription.setText(song.getDescription());
        ivImage.setImageResource(song.getImageId());
    }
}
