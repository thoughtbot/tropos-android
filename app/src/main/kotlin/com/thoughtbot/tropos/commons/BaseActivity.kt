package com.thoughtbot.tropos.commons

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thoughtbot.tropos.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
        .setDefaultFontPath("DINNextLTPro-Light.otf")
        .setFontAttrId(R.attr.fontPath)
        .build()
    )
  }

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
  }
}

