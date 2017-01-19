package com.thoughtbot.tropos.ui

import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.Presenter
import com.thoughtbot.tropos.data.LocationDataSource
import com.thoughtbot.tropos.data.WeatherDataSource
import com.thoughtbot.tropos.data.remote.LocationService
import com.thoughtbot.tropos.data.remote.WeatherDataService
import com.thoughtbot.tropos.extensions.dayBefore
import com.thoughtbot.tropos.permissions.LocationPermission
import com.thoughtbot.tropos.permissions.Permission
import com.thoughtbot.tropos.permissions.PermissionResults
import com.thoughtbot.tropos.permissions.checkPermission
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.RefreshListener
import com.thoughtbot.tropos.viewmodels.ErrorToolbarViewModel
import com.thoughtbot.tropos.viewmodels.LoadingToolbarViewModel
import com.thoughtbot.tropos.viewmodels.WeatherToolbarViewModel
import io.reactivex.disposables.Disposable
import java.util.Date

class MainPresenter(override val view: MainView,
    val locationDataSource: LocationDataSource = LocationService(view.context),
    val weatherDataSource: WeatherDataSource = WeatherDataService(),
    val permission: Permission = LocationPermission(view.context))
  : Presenter, RefreshListener, PermissionResults {

  lateinit var disposable: Disposable

  fun init() {
    permission.checkPermission({ updateWeather() }, { onPermissionDenied(false) }, true)
  }

  fun updateWeather() {
    //TODO confirm observable is completing
    view.viewState = ViewState.Loading(LoadingToolbarViewModel(view.context))
    disposable = locationDataSource.fetchLocation()
        .flatMap { weatherDataSource.fetchForecast(it, 3) }
        .flatMap({ forecast ->
          weatherDataSource.fetchWeather(forecast[0].location, Date().dayBefore())
        }, { forecast, yesterday -> return@flatMap listOf(yesterday).plus(forecast) })
        .doOnError {
          val errorMessage = it.message ?: ""
          view.viewState = ViewState.Error(ErrorToolbarViewModel(view.context), errorMessage)
        }
        .subscribe {
          view.viewState = ViewState.Weather(WeatherToolbarViewModel(view.context, it[0]), it)
        }
  }

  override fun onRefresh() {
    permission.checkPermission({ updateWeather() }, { onPermissionDenied(false) }, true)
  }

  fun onStop() {
    disposable.dispose()
  }

  override fun onPermissionGranted() {
    updateWeather()
  }

  override fun onPermissionDenied(userSaidNever: Boolean) {
    val errorMessage = view.context.getString(R.string.missing_location_permission_error)
    view.viewState = ViewState.Error(ErrorToolbarViewModel(view.context), errorMessage)
  }

}
