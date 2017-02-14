package com.github.maximkirko.training_2017_android.activity.core;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.fragment.FavoriteFriendsFragment;

/**
 * Created by MadMax on 14.02.2017.
 */

public class FavoriteFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_friends_activity);

        FavoriteFriendsFragment favoriteFriendsFragment = FavoriteFriendsFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.activity_favorite_friends, favoriteFriendsFragment);
        fragmentTransaction.commit();
    }
}
