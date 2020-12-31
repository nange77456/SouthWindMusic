package com.dss.swmusic.util;

import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

/**
 * WebViewClient的继承类，为了实现拦截非http、https的网络请求
 */
public class MyWebViewClient extends WebViewClient {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri uri = request.getUrl();
        String scheme = uri.getScheme();
        if(!scheme.equals("http") && !scheme.equals("https")){
            return true;
        }
        return super.shouldOverrideUrlLoading(view,request);
    }
}
