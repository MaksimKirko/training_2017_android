package com.github.maximkirko.training_2017_android.activity.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.maximkirko.training_2017_android.R;

public class DefaultPreferenceFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}