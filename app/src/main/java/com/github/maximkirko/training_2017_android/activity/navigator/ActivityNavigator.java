package com.github.maximkirko.training_2017_android.activity.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MadMax on 08.02.2017.
 */

public class ActivityNavigator {

    public static void startActivity(Context context, Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }

    public static void startActivityWithDestroy(Activity currentActivity, Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }
}
