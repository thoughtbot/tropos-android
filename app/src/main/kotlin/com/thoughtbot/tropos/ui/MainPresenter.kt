package com.thoughtbot.tropos.ui

import android.content.Intent
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.Presenter
import com.thoughtbot.tropos.data.WeatherDataSource
import com.thoughtbot.tropos.data.WeatherService
import com.thoughtbot.tropos.permissions.LocationPermission
import com.thoughtbot.tropos.permissions.Permission
import com.thoughtbot.tropos.permissions.PermissionResults
import com.thoughtbot.tropos.permissions.checkPermission
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.RefreshListener
import com.thoughtbot.tropos.viewmodels.ErrorToolbarViewModel
import com.thoughtbot.tropos.viewmodels.LoadingToolbarViewModel
import com.thoughtbot.tropos.viewmodels.WeatherToolbarViewModel
import io.reactivex.disposables.Disposable

class MainPresenter(override val view: MainView,
    intent: Intent?,
    val weatherDataSource: WeatherDataSource = WeatherService(view.context, intent),
    val permission: Permission = LocationPermission(view.context))
  : Presenter, RefreshListener, PermissionResults {

  var disposable: Disposable? = null

  fun onStart() {
    permission.checkPermission({ updateWeather() }, { onPermissionDenied(false) }, true)
  }

  fun updateWeather() {
    view.viewState = ViewState.Loading(LoadingToolbarViewModel(view.context))
    disposable = weatherDataSource.fetchWeather()
        .subscribe({
          view.viewState = ViewState.Weather(WeatherToolbarViewModel(view.context, it.today), it)
        }, { error ->
          val message = error.message ?: ""
          val errorMessage = view.context.getString(R.string.generic_error_message, message)
          view.viewState = ViewState.Error(ErrorToolbarViewModel(view.context), errorMessage)
        })
  }

  override fun onRefresh() {
    permission.checkPermission({ updateWeather() }, { onPermissionDenied(false) }, true)
  }

  fun onDestroy() {
    disposable?.dispose()
  }

  override fun onPermissionGranted() {
    updateWeather()
  }

  override fun onPermissionDenied(userSaidNever: Boolean) {
    val errorMessage = view.context.getString(R.string.missing_location_permission_error)
    view.viewState = ViewState.Error(ErrorToolbarViewModel(view.context), errorMessage)
  }

}
