package com.github.maximkirko.training_2017_android.activity.splash;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.intro.IntroActivity;
import com.github.maximkirko.training_2017_android.navigator.IntentManager;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;
import com.github.maximkirko.training_2017_android.util.NetworkUtils;
import com.github.maximkirko.training_2017_android.util.StringUtils;

import java.io.IOException;

public class SplashScreenActivity extends AppCompatActivity {

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        checkAccessTokenStatus();
    }

    private boolean isFirstLaunch() {
        return AppSharedPreferences.getBoolean(IntroActivity.IS_FIRST_LAUNCH_PREFERENCE, true);
    }

    private void chooseStartingActivity() {
        if (!isFirstLaunch()) {
            if (AppSharedPreferences.getBoolean(VKService.ACCESS_PERMISSION_PREFERENCE, false) && flag) {
                startActivity(IntentManager.getIntentForFriendsListActivity(this));
            } else {
                startActivity(IntentManager.getIntentForLoginActivity(this));
            }
        } else {
            startActivity(IntentManager.getIntentForIntroActivity(this));
        }
        finish();
    }

    private void checkAccessTokenStatus() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String response = StringUtils.bufferedReaderToString(NetworkUtils.getConnectionInputStream(VKService.getFriendsRequestUrl()));
                    if (response.contains("User authorization failed")) {
                        flag = false;
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                flag = true;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                chooseStartingActivity();
            }
        }.execute();
    }
}
