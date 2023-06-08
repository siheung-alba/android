package com.siheung_alba.alba.kakao_map_api

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import com.siheung_alba.alba.R
import java.net.URISyntaxException

class AddressApiActivity : AppCompatActivity() {

    private lateinit var webView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_api)

        webView = findViewById(R.id.webView)

        webView.loadUrl("C:\\Users\\jjiwo\\siheung-alba\\android\\alba\\app\\src\\main\\assets\\daum.html")
        webView.settings.javaScriptEnabled=true

        webView.webViewClient = WebViewClient()

    }
}
