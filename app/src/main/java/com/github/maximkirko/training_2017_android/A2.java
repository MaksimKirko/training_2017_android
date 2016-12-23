package com.github.maximkirko.training_2017_android;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Properties;

public class A2 extends AppCompatActivity {

//    intent extras
    private String E1_text;
    private String E2_text;
    private String S_text;
    private int S_pos;
    private boolean C_value;
    private int imageId;

    //    views
    private Toolbar toolbar;
    private Fragment F1;
    private Fragment F2;

    public static final String F1_TAG = "F1";
    public static final String F2_TAG = "F2";

    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);

        Intent intent = getIntent();
        getIntentExtras(intent);

        toolbar = (Toolbar) findViewById(R.id.toolbar);;
        if (toolbar != null) {
            setToolbarSettings(toolbar);
            setViewsValues();
        }

        F1 = Fragment1.newInstance(E2_text);
        F2 = Fragment2.newInstance(S_text, imageId);

        if (savedInstanceState == null) {
            setFragmentsOrder(fragmentTransaction, F1, F2);
        }

    }

    private void setToolbarSettings(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getIntentExtras(Intent intent) {
        E1_text = intent.getStringExtra(A1.E1_EXTRA);
        E2_text = intent.getStringExtra(A1.E2_EXTRA);
        S_text = intent.getStringExtra(A1.S_EXTRA);
        S_pos = intent.getIntExtra(A1.S_POS_EXTRA, 0);
        C_value = intent.getBooleanExtra(A1.C_EXTRA, true);
    }

    private void setViewsValues() {
        getSupportActionBar().setTitle(E1_text);
        imageId = S_pos;
    }

    private void setFragmentsOrder(FragmentTransaction fragmentTransaction, Fragment f1, Fragment f2) {
        if (C_value) {
            buildFragment(fragmentTransaction, f2, F2_TAG);
            buildFragment(fragmentTransaction, f1, F1_TAG);
        } else {
            buildFragment(fragmentTransaction, f1, F1_TAG);
            buildFragment(fragmentTransaction, f2, F2_TAG);
        }
    }

    private void buildFragment(FragmentTransaction fragmentTransaction, Fragment fragment, String tag) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment, tag);
        fragmentTransaction.commit();
    }

}
