package com.github.maximkirko.training_2017_android.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.maximkirko.training_2017_android.R;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

/**
 * Created by MadMax on 10.01.2017.
 */

public final class VKUtils {

    // region request params
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    private static final String SCOPE_PARAM = "scope";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String API_VERSION_PARAM = "v";
    // endregion

    // region request values
    public static final String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    public static final String SCOPE = "friends";
    public static final String RESPONSE_TYPE = "token";
    public static final String API_VERSION = "5.62";
    // endregion

    public static String getFingerprint(@NonNull Context context) {
        String[] fingerprints = VKUtil.getCertificateFingerprint(context, context.getPackageName());
        return Arrays.toString(fingerprints);
    }

    public static String getAuthUrl() {
        String url = getApplicationContext().getString(R.string.vk_oauth_url);
        url += "client_id=" + getApplicationContext().getResources().getInteger(R.integer.com_vk_sdk_AppId);
        url += "&" + REDIRECT_URI_PARAM + "=" + REDIRECT_URI;
        url += "&" + SCOPE_PARAM + "=" + SCOPE;
        url += "&" + RESPONSE_TYPE_PARAM + "=" + RESPONSE_TYPE;
        url += "&" + API_VERSION_PARAM + "=" + API_VERSION;
        return url;
    }

    public static String getAccessTokenFromRedirectUrl(String url) {
        return parseSuccessRedirectUri(url)[1];
    }

    public static String getExpiresInFromRedirectUrl(String url) {
        return parseSuccessRedirectUri(url)[2];
    }

    public static String getUserIdFromRedirectUrl(String url) {
        return parseSuccessRedirectUri(url)[3];
    }

    /*
    * @returns matches[1] - access token, matches[2] - expires in, matches[3] - user id
     */
    private static String[] parseSuccessRedirectUri(String url) {
        // http://api.vk.com/blank.html#access_token=3d6c7afdfe96c571f0809ea79950d0b8038cbafd480b09e2a181d8d24d5e8f43650d50ca5e757199d470c&expires_in=86400&user_id=181965790
        String[] matches = url.split("http://api.vk.com/blank.html#access_token=|&expires_in=|&user_id=");
        for (int i = 0; i < matches.length; i++) {
            System.out.println("I=" + i + " " + matches[i]);
        }
        return matches;
    }
}
