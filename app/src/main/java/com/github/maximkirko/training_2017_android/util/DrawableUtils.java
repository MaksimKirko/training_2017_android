package com.github.maximkirko.training_2017_android.util;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by MadMax on 12.12.2016.
 */

public class DrawableUtils {

    public static int getResourceIdByName(String name, AppCompatActivity activity) {

        return activity.getResources().getIdentifier(name, null, activity.getPackageName());
    }
}
