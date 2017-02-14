package com.github.maximkirko.training_2017_android.activity.preference;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.maximkirko.training_2017_android.R;

/**
 * Created by MadMax on 14.02.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new DefaultPreferenceFragment()).commit();
    }
}
