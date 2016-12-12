package com.github.maximkirko.training_2017_android.util;

/**
 * Created by MadMax on 10.12.2016.
 */

public class StringUtils {

    public static boolean isEmpty(String s) {
        if (s.replaceAll("\\s+","").isEmpty()) {
            return true;
        }
        return false;
    }
}
