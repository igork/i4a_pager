package com.example.myswipe;

import android.app.Activity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebPage {

    public static WebView getWeb(WebView webView, String url) {
        //WebView webView = (WebView) activity.findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);

        return webView;
    }

}
