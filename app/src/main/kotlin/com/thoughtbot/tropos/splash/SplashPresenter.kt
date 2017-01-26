package com.thoughtbot.tropos.splash

import com.thoughtbot.tropos.commons.Presenter
import com.thoughtbot.tropos.data.WeatherDataSource
import com.thoughtbot.tropos.data.WeatherService
import com.thoughtbot.tropos.permissions.LocationPermission
import com.thoughtbot.tropos.permissions.Permission
import com.thoughtbot.tropos.permissions.PermissionResults
import com.thoughtbot.tropos.permissions.checkPermission
import com.thoughtbot.tropos.ui.MainActivity
import io.reactivex.disposables.Disposable

class SplashPresenter(override val view: SplashView,
    val permission: Permission = LocationPermission(view.context),
    val weatherDataSource: WeatherDataSource = WeatherService(view.context, null))
  : Presenter, PermissionResults {

  var disposable: Disposable? = null

  fun init() {
    permission.checkPermission({ fetchWeather() }, { onPermissionDenied(false) }, true)
  }

  fun onDestroy() {
    disposable?.dispose()
  }

  override fun onPermissionGranted() {
    fetchWeather()
  }

  override fun onPermissionDenied(userSaidNever: Boolean) {
    view.navigate(MainActivity.createIntent(view.context, null))
  }

  private fun fetchWeather() {
    disposable = weatherDataSource.fetchWeather()
        .subscribe({
          view.navigate(MainActivity.createIntent(view.context, it))
        }, { error ->
          view.navigate(MainActivity.createIntent(view.context, null))
        })
  }
}
