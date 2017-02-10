package com.github.maximkirko.training_2017_android.webclient;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.maximkirko.training_2017_android.service.VKService;

import java.lang.ref.WeakReference;

/**
 * Created by Max on 07.02.2017.
 */

public class VKAuthWebViewClient extends WebViewClient {

    private WeakReference<WebClientCallback<String>> webClientCallbackWeakReference;

    public VKAuthWebViewClient(WebClientCallback<String> webClientCallback) {
        webClientCallbackWeakReference = new WeakReference<>(webClientCallback);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        checkRedirectUrl(url);
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (request.isRedirect()) {
            checkRedirectUrl(request.getUrl().toString());
        }
        return false;
    }

    private void checkRedirectUrl(String url) {
        if (url.startsWith(VKService.REDIRECT_URI + "#" + VKService.ACCESS_TOKEN_PARAM)) {
            if (webClientCallbackWeakReference != null) {
                webClientCallbackWeakReference.get().onUrlLoading(url);
            }
            clearCookies();
        }
    }

    private void clearCookies() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.CookieManager.getInstance().removeAllCookie();
        } else {
            android.webkit.CookieManager.getInstance().removeAllCookies(null);
        }

    }
}
