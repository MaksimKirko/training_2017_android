package com.github.maximkirko.training_2017_android.util;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by MadMax on 04.02.2017.
 */

public class StringUtils {

    public static boolean isEmpty(String s) {
        if (s.replaceAll("\\s+", "").isEmpty()) {
            return true;
        }
        return false;
    }

    public static String bufferedReaderToString(@NonNull BufferedReader bufferedReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = bufferedReader.readLine()) != null) {
            sb.append(output);
        }
        return sb.toString();
    }
}
