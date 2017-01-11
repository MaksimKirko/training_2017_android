package com.github.maximkirko.training_2017_android.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

/**
 * Created by MadMax on 10.01.2017.
 */

public final class VKUtils {

    public static String getFingerprint(@NonNull Context context) {
        String[] fingerprints = VKUtil.getCertificateFingerprint(context, context.getPackageName());
        return Arrays.toString(fingerprints);
    }
}
