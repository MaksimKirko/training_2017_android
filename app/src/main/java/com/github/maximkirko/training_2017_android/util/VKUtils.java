package com.github.maximkirko.training_2017_android.util;

import android.content.Context;
import android.util.Log;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.util.VKUtil;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by MadMax on 10.01.2017.
 */

public final class VKUtils {

    public static String getFingerprint(Context context) {
        String[] fingerprints = VKUtil.getCertificateFingerprint(context, context.getPackageName());
        return Arrays.toString(fingerprints);
    }
}
