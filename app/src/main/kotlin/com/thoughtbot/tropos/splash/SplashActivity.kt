package com.thoughtbot.tropos.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat.finishAfterTransition
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.BaseActivity
import com.thoughtbot.tropos.permissions.getPermissionResults

class SplashActivity : BaseActivity(), SplashView {

  val presenter: SplashPresenter by lazy { SplashPresenter(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    presenter.init()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onDestroy()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    getPermissionResults(presenter.permission, presenter, requestCode, permissions, grantResults)
  }

  override val context: Context = this

  override fun navigate(intent: Intent) {
    startActivity(intent)
    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    finishAfterTransition(this)
  }

}
