package com.github.maximkirko.training_2017_android.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.maximkirko.training_2017_android.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

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
        vkParameters.put(VKApiConst.ACCESS_TOKEN, VKSdk.getAccessToken().accessToken);
        vkParameters.put(VKApiConst.COUNT, 10);
        vkParameters.put(VKApiConst.FIELDS, "nickname, online, last_seen, photo_100");
        return vkParameters;
    }

    public static String getUrlString(VKParameters params) {
        //  method name
        String urlString = getApplicationContext().getString(R.string.base_vk_api_url) + "friends.get?";
        //  params
        for (VKParameters.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey() != "access_token" && entry.getValue() != null) {
                urlString += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        //  access token
        urlString += "access_token=" + params.get(VKApiConst.ACCESS_TOKEN);
        //  VK API version
        urlString += "&v=5.8";
        urlString = urlString.replaceAll(" ", "%20");
        return urlString;
    }
}
