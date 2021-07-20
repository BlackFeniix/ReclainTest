package com.hito.nikolay.reclaintest

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        this.setTitle(R.string.webView_activity_name)

        val progressBar = findViewById<ProgressBar>(R.id.progressBarWebView)

        webView = findViewById(R.id.webView)
        webView.let {
            it.settings.javaScriptEnabled = true
            it.settings.domStorageEnabled = true
            it.loadUrl("https://www.igromania.ru")

            it.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    // Page loading started
                    progressBar.visibility = ProgressBar.VISIBLE
                }

                override fun onPageFinished(view: WebView, url: String) {
                    // Page loading finished
                    progressBar.visibility = ProgressBar.INVISIBLE
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.canGoBack()) {
            // If web view have back history, then go to the web view back history
            webView.goBack()
        }
    }
}