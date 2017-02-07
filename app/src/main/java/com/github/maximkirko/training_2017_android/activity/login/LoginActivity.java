package com.github.maximkirko.training_2017_android.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.activity.splash.SplashScreenActivity;
import com.github.maximkirko.training_2017_android.asynctask.AsyncTaskCallback;
import com.github.maximkirko.training_2017_android.asynctask.AuthorizationTask;
import com.github.maximkirko.training_2017_android.util.VKUtils;
import com.github.maximkirko.training_2017_android.webclient.VKAuthWebViewClient;

/**
 * Created by MadMax on 14.01.2017.
 */

public class LoginActivity extends AppCompatActivity implements AsyncTaskCallback<String> {

    // input https://oauth.vk.com/authorize?client_id=5814025&redirect_uri=http://vk.com&display=page&scope=friends&response_type=token&v=5.62
    // output http://api.vk.com/blank.html#access_token=3d6c7afdfe96c571f0809ea79950d0b8038cbafd480b09e2a181d8d24d5e8f43650d50ca5e757199d470c&expires_in=86400&user_id=181965790
    // error http://REDIRECT_URI#error=access_denied&error_description=The+user+or+authorization+server+denied+the+request.

    // region Views
    private WebView webView;
    // endregion

    private AuthorizationTask authorizationTask;

    private static final String AUTH_REDIRECT_URL_SUCCESS = VKUtils.REDIRECT_URI + "#access_token=";
    private static final String AUTH_REDIRECT_URL_ERROR = VKUtils.REDIRECT_URI + "#error=access_denied&error_description=";

    public static final String ACCESS_TOKEN_PREFERENCE = "ACCESS_TOKEN";
    public static final String USER_ID_PREFERENCE = "USER_ID";
    public static final String ACCESS_PERMISSION_PREFERENCE = "ACCESS_PERMISSION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initWebView();
        webView.loadUrl(VKUtils.getAuthUrl());
        initAuthorizationTask();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webview_login_activity);
        webView.setWebViewClient(new VKAuthWebViewClient());
    }

    private void initAuthorizationTask() {
        authorizationTask = new AuthorizationTask(this);
    }

    private void startAuthorizationTask() {
        authorizationTask.execute();
    }

    @Override
    public void onAsyncTaskFinished(String result) {
        if (result.startsWith(AUTH_REDIRECT_URL_SUCCESS)) {
            String accessToken = VKUtils.getAccessTokenFromRedirectUrl(result);
            String userId = VKUtils.getUserIdFromRedirectUrl(result);
            saveAccessParamsToSharedPreferences(accessToken, userId);
        } else if (result.startsWith(AUTH_REDIRECT_URL_ERROR)) {
            // TODO error handling
        }
    }

    private void saveAccessParamsToSharedPreferences(String accessToken, String userId) {
        SharedPreferences.Editor editor = SplashScreenActivity.getSharedPreferences().edit();
        editor.putString(ACCESS_TOKEN_PREFERENCE, accessToken);
        editor.putString(USER_ID_PREFERENCE, userId);
        editor.apply();
    }

    private void saveAccessPermission() {
        SharedPreferences.Editor editor = SplashScreenActivity.getSharedPreferences().edit();
        editor.putBoolean(ACCESS_PERMISSION_PREFERENCE, true);
        editor.apply();
    }

    private void startFriendsListActivity() {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
        finish();
    }
}