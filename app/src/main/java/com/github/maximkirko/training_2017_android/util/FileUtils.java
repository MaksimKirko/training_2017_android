package com.github.maximkirko.training_2017_android.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;

import java.io.File;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

/**
 * Created by MadMax on 16.01.2017.
 */

public class FileUtils {

    public static File getImageDirectory(String url) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        return cw.getDir("imageDir", Context.MODE_PRIVATE);
    }

    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    public static void removeOldest(File directory) {
        File oldest = getOldestModified(directory);
        for (File file : directory.listFiles()) {
            if (file.getName().equals(oldest.getName())) {
                file.delete();
                return;
            }
        }
    }

    public static File getOldestModified(File directory) {
        File oldest = null;
        boolean isFirst = true;
        for (File file : directory.listFiles()) {
            if (isFirst) {
                isFirst = false;
                oldest = file;
                continue;
            }
            if (file.lastModified() < oldest.lastModified()) {
                oldest = file;
            }
        }
        return oldest;
    }
}
