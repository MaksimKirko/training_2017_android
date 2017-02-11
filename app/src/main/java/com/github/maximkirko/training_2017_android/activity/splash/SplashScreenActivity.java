package com.github.maximkirko.training_2017_android.activity.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.intro.IntroActivity;
import com.github.maximkirko.training_2017_android.activity.navigator.IntentManager;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if (!isFirstLaunch()) {
            if (AppSharedPreferences.getBoolean(VKService.ACCESS_PERMISSION_PREFERENCE, false)) {
                startActivity(IntentManager.getIntentForFriendsListActivity(this));
            } else {
                startActivity(IntentManager.getIntentForLoginActivity(this));
            }
        } else {
            startActivity(IntentManager.getIntentForIntroActivity(this));
        }
        finish();
    }

    private boolean isFirstLaunch() {
        return AppSharedPreferences.getBoolean(IntroActivity.IS_FIRST_LAUNCH_PREFERENCE, true);
    }
}
