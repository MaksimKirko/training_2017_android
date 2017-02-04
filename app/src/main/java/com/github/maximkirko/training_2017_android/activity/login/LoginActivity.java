package com.github.maximkirko.training_2017_android.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.activity.splash.SplashActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

/**
 * Created by MadMax on 14.01.2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        // Log.d("fingerprint", Arrays.toString(fingerprints));

        initViews();
    }

    private void initViews() {
        loginButton = (Button) findViewById(R.id.button_login_activity_login);
        loginButton.setOnClickListener(this);
    }

    private void startFriendsListActivity() {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        VKSdk.login(this, VKScope.FRIENDS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.i("AUTORIZATION", "TRUE");
                saveAccessPermission();
                startFriendsListActivity();
            }

            @Override
            public void onError(VKError error) {
                Log.i("AUTORIZATION", "ERROR");
                // TODO error view
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void saveAccessPermission() {
        SharedPreferences.Editor editor = SplashActivity.getSharedPreferences().edit();
        editor.putBoolean(SplashActivity.ACCESS_PERMISSION_PREFERENCE, true);
        editor.apply();
    }
}
