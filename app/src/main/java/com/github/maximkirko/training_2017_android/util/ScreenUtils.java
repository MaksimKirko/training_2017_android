package com.github.maximkirko.training_2017_android.util;

import android.content.res.Resources;

/**
 * Created by MadMax on 06.01.2017.
 */

public final class ScreenUtils {

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
