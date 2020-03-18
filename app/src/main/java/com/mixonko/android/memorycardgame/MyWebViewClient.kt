package com.mixonko.android.memorycardgame

import android.webkit.WebViewClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.os.Build
import android.annotation.TargetApi



class MyWebViewClient: WebViewClient() {
    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        view.loadUrl(request.url.toString())
        return true
    }
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}
