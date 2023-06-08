package com.siheung_alba.alba.address

import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.siheung_alba.alba.R

class AddressApiActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    inner class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        fun processDATA(data: String) {
            val extra = Bundle()
            val intent = Intent()
            extra.putString("data", data)
            intent.putExtras(extra)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_api)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavaScriptInterface(), "Android")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // Android -> Javascript  함수 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        //webView load
        webView.loadUrl("https://alba-b721a.web.app")
    }
}
