package com.github.maximkirko.training_2017_android.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

/**
 * Created by MadMax on 08.02.2017.
 */

@Singleton
public class AppSharedPreferences {

    private static final String APP_PREFERENCES = "com.github.maximkirko.training_2017_android_sprint2_preferences";

    private static SharedPreferences preferences;

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static void init(Context context) {
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }


    public static void removePreference(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
