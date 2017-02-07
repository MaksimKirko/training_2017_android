package com.github.maximkirko.training_2017_android.activity.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.activity.login.LoginActivity;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.service.VKService;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        initSharedPreferences();
        if (VKSimpleChatApplication.getSharedPreferences().getBoolean(VKService.ACCESS_PERMISSION_PREFERENCE, false)) {
            startActivity(FriendsListActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
    }

    private void initSharedPreferences() {
        VKSimpleChatApplication.setSharedPreferences(getPreferences(Context.MODE_PRIVATE));
    }

    private void startActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }
}
