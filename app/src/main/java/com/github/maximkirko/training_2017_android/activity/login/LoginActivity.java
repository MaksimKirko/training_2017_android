package com.github.maximkirko.training_2017_android.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.webclient.VKAuthWebViewClient;
import com.github.maximkirko.training_2017_android.webclient.WebClientCallback;

/**
 * Created by MadMax on 14.01.2017.
 */

public class LoginActivity extends AppCompatActivity implements WebClientCallback<String> {

    // input https://oauth.vk.com/authorize?client_id=5814025&redirect_uri=http://vk.com&display=page&scope=friends&response_type=token&v=5.62
    // output http://api.vk.com/blank.html#access_token=3d6c7afdfe96c571f0809ea79950d0b8038cbafd480b09e2a181d8d24d5e8f43650d50ca5e757199d470c&expires_in=86400&user_id=181965790
    // error http://REDIRECT_URI#error=access_denied&error_description=The+user+or+authorization+server+denied+the+request.

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initWebView();
        webView.loadUrl(VKService.getAuthUrl());
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
        SharedPreferences sharedPreferences = VKSimpleChatApplication.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(VKService.ACCESS_PERMISSION_PREFERENCE, true);
        editor.putString(VKService.ACCESS_TOKEN_PREFERENCE, accessToken);
        editor.putString(VKService.USER_ID_PREFERENCE, userId);
        editor.apply();
    }

    private void startFriendsListActivity() {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
        finish();
    }
}