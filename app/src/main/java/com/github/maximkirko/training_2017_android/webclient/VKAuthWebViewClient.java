package com.github.maximkirko.training_2017_android.webclient;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Max on 07.02.2017.
 */

public class VKAuthWebViewClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("LOGIN_OLD", "TRUE");
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if(request.isRedirect()) {
            Log.i("LOGIN", "TRUE");
        }
        return false;
    }
}
