package com.thoughtbot.tropos.commons

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import com.thoughtbot.tropos.R
import org.jetbrains.anko.find

class WebviewActivity : BaseActivity() {

  companion object {
    val URL_EXTRA = "url_extra"

    fun createIntent(context: Context, url: String?): Intent {
      val intent = Intent(context, WebviewActivity::class.java)
      url?.let { intent.putExtra(URL_EXTRA, it) }
      return intent
    }
  }

  private val DEFAULT_URL = "https://www.thoughtbot.com"
  private val webview: WebView by lazy { find<WebView>(R.id.webview) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview)

    val url: String = intent?.getStringExtra(URL_EXTRA) ?: DEFAULT_URL
    webview.loadUrl(url)
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    // Check if the key event was the Back button and if there's history
    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
      webview.goBack()
      return true
    }
    // If it wasn't the Back key or there's no web page history, bubble up to the default
    // system behavior (probably exit the activity)
    return super.onKeyDown(keyCode, event)
  }
}
