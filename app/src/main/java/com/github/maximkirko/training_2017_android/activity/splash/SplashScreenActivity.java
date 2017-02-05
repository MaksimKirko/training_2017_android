package com.github.maximkirko.training_2017_android.activity.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.activity.login.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;
    public static final String ACCESS_PERMISSION_PREFERENCE = "ACCESS_PERMISSION";

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initSharedPreferences();
        if (sharedPreferences.getBoolean(ACCESS_PERMISSION_PREFERENCE, false)) {
            startActivity(FriendsListActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
    }

    private void initSharedPreferences() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
    }

    private void startActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }
}
