package com.github.maximkirko.training_2017_android.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

/**
 * Created by MadMax on 10.01.2017.
 */

public final class VKService {

    public static String getFingerprint(@NonNull Context context) {
        String[] fingerprints = VKUtil.getCertificateFingerprint(context, context.getPackageName());
        return Arrays.toString(fingerprints);
    }

    public static VKParameters initVKParameters() {
        VKParameters vkParameters = new VKParameters();
        vkParameters = new VKParameters();
        vkParameters.put(VKApiConst.ACCESS_TOKEN, VKSdk.getAccessToken().accessToken);
        vkParameters.put(VKApiConst.COUNT, 50);
        vkParameters.put(VKApiConst.FIELDS, "nickname, online, last_seen, photo_100");
        return vkParameters;
    }
}
