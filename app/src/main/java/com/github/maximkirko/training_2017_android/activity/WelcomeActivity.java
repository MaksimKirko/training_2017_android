package com.github.maximkirko.training_2017_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.maximkirko.training_2017_android.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

/**
 * Created by MadMax on 14.01.2017.
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;

    private SharedPreferences sharedPreferences;

    private static final String ACCESS_PERMISSION_PREFERENCE = "ACCESS_PERMISSION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("fingerprint", Arrays.toString(fingerprints));

        initViews();
        initSharedPreferences();

        if (sharedPreferences.getBoolean(ACCESS_PERMISSION_PREFERENCE, false)) {
            startFriendsListActivity();
        }
    }

    private void initViews() {
        loginButton = (Button) findViewById(R.id.button_welcome_activity_login);
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
//                TODO error view
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initSharedPreferences() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
    }

    public void saveAccessPermission() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ACCESS_PERMISSION_PREFERENCE, true);
        editor.apply();
    }
}
