package com.github.maximkirko.training_2017_android.navigator;

import android.content.Context;
import android.content.Intent;

import com.github.maximkirko.training_2017_android.activity.core.FriendsListActivity;
import com.github.maximkirko.training_2017_android.activity.core.UserDetailsActivity;
import com.github.maximkirko.training_2017_android.activity.intro.IntroActivity;
import com.github.maximkirko.training_2017_android.activity.login.LoginActivity;
import com.github.maximkirko.training_2017_android.activity.preference.SettingsActivity;

/**
 * Created by MadMax on 08.02.2017.
 */

public class IntentManager {

    public static Intent getIntentForUserDetailsActivity(Context context, int id) {
        Intent intent = new Intent(context, UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.USER_EXTRA, id);
        return intent;
    }

    public static Intent getIntentForLoginActivity(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Intent getIntentForIntroActivity(Context context) {
        return new Intent(context, IntroActivity.class);
    }

    public static Intent getIntentForFriendsListActivity(Context context) {
        Intent intent = new Intent(context, FriendsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static Intent getIntentForSettingsActivity(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}
