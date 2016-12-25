package com.github.maximkirko.training_2017_android;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.maximkirko.training_2017_android.builders.A1Data;

public class A2 extends AppCompatActivity {

    //    intent extras
    private A1Data a1Data;

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

        setToolbarSettings(toolbar);

        F1 = Fragment1.newInstance(a1Data.getE2_text());
        F2 = Fragment2.newInstance(a1Data.getS_text(), a1Data.getI_Id());

        if (savedInstanceState == null) {
            setFragmentsOrder(fragmentTransaction, F1, F2);
        }
    }

    private void getIntentExtras(Intent intent) {

        a1Data = A1Data.newBuilder()
                .setE1_text(intent.getStringExtra(A1.E1_EXTRA))
                .setE2_text(intent.getStringExtra(A1.E2_EXTRA))
                .setS_text(intent.getStringExtra(A1.S_EXTRA))
                .setI_Id(intent.getIntExtra(A1.S_POS_EXTRA, 0))
                .setC_value(intent.getBooleanExtra(A1.C_EXTRA, true))
                .build();

        System.out.println(a1Data.toString());
    }

    private void setToolbarSettings(Toolbar toolbar) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(a1Data.getE1_text());
        }
    }

    private void setFragmentsOrder(FragmentTransaction fragmentTransaction, Fragment f1, Fragment f2) {
        if (a1Data.isC_value()) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
