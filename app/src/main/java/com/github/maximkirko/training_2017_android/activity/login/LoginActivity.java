package com.github.maximkirko.training_2017_android.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;
import com.github.maximkirko.training_2017_android.webclient.VKAuthWebViewClient;
import com.github.maximkirko.training_2017_android.webclient.WebClientCallback;

/**
 * Created by MadMax on 14.01.2017.
 */

public class LoginActivity extends AppCompatActivity implements WebClientCallback<String> {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initWebView();
        webView.loadUrl(VKService.getAuthUrl(getBaseContext()));
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webview_login_activity);
        webView.setWebViewClient(new VKAuthWebViewClient(this));
    }

    @Override
    public void onUrlLoading(String result) {
        String accessToken = VKService.getAccessTokenFromRedirectUrl(result);
        String userId = VKService.getUserIdFromRedirectUrl(result);
        saveAccessParamsToSharedPreferences(accessToken, userId);
        startFriendsListActivity();
    }

    private void saveAccessParamsToSharedPreferences(String accessToken, String userId) {
        AppSharedPreferences.putBoolean(VKService.ACCESS_PERMISSION_PREFERENCE, true);
        AppSharedPreferences.putString(VKService.ACCESS_TOKEN_PREFERENCE, accessToken);
        AppSharedPreferences.putInt(VKService.USER_ID_PREFERENCE, Integer.parseInt(userId));
    }

    private void startFriendsListActivity() {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
        finish();
    }
}