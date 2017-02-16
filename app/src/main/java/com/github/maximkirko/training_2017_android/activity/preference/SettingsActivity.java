package com.github.maximkirko.training_2017_android.activity.preference;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.navigator.IntentManager;

/**
 * Created by MadMax on 14.02.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        initToolbar();
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_fragment_container, new DefaultPreferenceFragment()).commit();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_settings_activity);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(IntentManager.getIntentForFriendsListActivity(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
