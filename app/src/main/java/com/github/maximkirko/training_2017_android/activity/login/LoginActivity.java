package com.github.maximkirko.training_2017_android.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.activity.splash.SplashScreenActivity;
import com.github.maximkirko.training_2017_android.asynctask.AuthorizationTask;
import com.github.maximkirko.training_2017_android.asynctask.AsyncTaskCallback;
import com.github.maximkirko.training_2017_android.util.StringUtils;

/**
 * Created by MadMax on 14.01.2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCallback<String> {

    // input https://oauth.vk.com/authorize?client_id=5814025&redirect_uri=http://vk.com&display=page&scope=friends&response_type=token&v=5.62
    // output http://api.vk.com/blank.html#access_token=3d6c7afdfe96c571f0809ea79950d0b8038cbafd480b09e2a181d8d24d5e8f43650d50ca5e757199d470c&expires_in=86400&user_id=181965790

    // region Views
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;
    // endregion

    private AuthorizationTask authorizationTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        // Log.d("fingerprint", Arrays.toString(fingerprints));

        initViews();
        initAuthorizationTask();
    }

    private void initViews() {
        loginEditText = (EditText) findViewById(R.id.edittext_login_activity_login);
        passwordEditText = (EditText) findViewById(R.id.edittext_login_activity_password);
        loginButton = (Button) findViewById(R.id.button_login_activity_login);
        loginButton.setOnClickListener(this);
    }

    private void initAuthorizationTask() {
        authorizationTask = new AuthorizationTask(this);
    }

    @Override
    public void onClick(View view) {
        String[] cridentials = getCridentials();
        if (!isValidCridentials(cridentials)) {
            return;
        } else {
            startAuthorizationTask();
        }
    }

    private String[] getCridentials() {
        return new String[]{loginEditText.getText().toString(), passwordEditText.getText().toString()};
    }

    private boolean isValidCridentials(String[] cridentials) {
        boolean flag = true;
        if (StringUtils.isEmpty(cridentials[0])) {
            showToast(getString(R.string.login_activity_empty_login_field_message));
            flag = false;
        }
        if (StringUtils.isEmpty(cridentials[1])) {
            showToast(getString(R.string.login_activity_empty_password_field_message));
            flag = false;
        }
        return flag;
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void startAuthorizationTask() {
        authorizationTask.execute();
    }

    @Override
    public void onAsyncTaskFinished(String result) {
        if (result.startsWith("http://REDIRECT_URI#access_token=")) {

        }
    }

    private void saveAccessPermission() {
        SharedPreferences.Editor editor = SplashScreenActivity.getSharedPreferences().edit();
        editor.putBoolean(SplashScreenActivity.ACCESS_PERMISSION_PREFERENCE, true);
        editor.apply();
    }

    private void startFriendsListActivity() {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
        finish();
    }
}
