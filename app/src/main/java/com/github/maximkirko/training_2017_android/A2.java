package com.github.maximkirko.training_2017_android;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.github.maximkirko.training_2017_android.util.DrawableUtils;
import com.github.maximkirko.training_2017_android.util.PropertyReader;

import java.util.Properties;

public class A2 extends AppCompatActivity {

    private String E1_text;
    private String E2_text;
    private String S_text;
    private int S_pos;
    private boolean C_value;
    private int imageId;

    //    views
    private Fragment F1;
    private Fragment F2;

    private String F1_tag;
    private String F2_tag;


    //    pictures from spinner
    private PropertyReader propertyReader;
    private Properties prop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        propertyReader = new PropertyReader(this);
        prop = propertyReader.getMyProperties("spinner.properties");

        Intent intent = getIntent();

        E1_text = intent.getStringExtra("E1");
        E2_text = intent.getStringExtra("E2");
        S_text = intent.getStringExtra("S");
        S_pos = intent.getIntExtra("S_pos", 0);
        C_value = intent.getBooleanExtra("C", true);

        getSupportActionBar().setTitle(E1_text);
        imageId = DrawableUtils.getResourceIdByName(prop.getProperty("" + S_pos), this);

        F1_tag = getResources().getString(R.string.fragment_f1_tag);
        F2_tag = getResources().getString(R.string.fragment_f2_tag);

        if (savedInstanceState == null) {
            if (C_value) {
                buildF2();
                buildF1();
            } else {
                buildF1();
                buildF2();
            }
        }


//        Fragment1 = getFragmentManager().findFragmentByTag(F1_tag);
//        ((TextView) Fragment1.getView().findViewById(R.id.T1))
//                .setText(E2_text);
////
//        Fragment2 = getFragmentManager().findFragmentByTag(F2_tag);
//        ((TextView) Fragment2.getView().findViewById(R.id.T2))
//                .setText(S_text);
//        ((ImageView) Fragment2.getView().findViewById(R.id.I))
//                .setImageURI(Uri.parse(prop.getProperty(S_text)));

    }

    private void buildF1() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        F1 = Fragment1.newInstance(E2_text);
        ft.add(R.id.fragment_container, F1, F1_tag);
        ft.commit();
    }

    private void buildF2() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        F2 = Fragment2.newInstance(S_text, imageId);
        ft.add(R.id.fragment_container, F2, F2_tag);
        ft.commit();
    }

}
