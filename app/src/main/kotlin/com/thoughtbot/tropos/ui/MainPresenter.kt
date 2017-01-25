package com.thoughtbot.tropos.ui

import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.Presenter
import com.thoughtbot.tropos.data.ConditionDataSource
import com.thoughtbot.tropos.data.LocationDataSource
import com.thoughtbot.tropos.data.Weather
import com.thoughtbot.tropos.data.remote.ConditionDataService
import com.thoughtbot.tropos.data.remote.LocationService
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
    val conditionDataSource: ConditionDataSource = ConditionDataService(),
    val permission: Permission = LocationPermission(view.context))
  : Presenter, RefreshListener, PermissionResults {

  lateinit var disposable: Disposable

  fun init() {
    permission.checkPermission({ updateWeather() }, { onPermissionDenied(false) }, true)
  }

  fun updateWeather() {
    view.viewState = ViewState.Loading(LoadingToolbarViewModel(view.context))
    disposable = locationDataSource.fetchLocation()
        .flatMap { conditionDataSource.fetchForecast(it, 3) }
        .flatMap({ forecast ->
          conditionDataSource.fetchCondition(forecast[0].location, Date().dayBefore())
        }, { forecast, yesterday ->
          return@flatMap Weather(yesterday, forecast[0], forecast.drop(1))
        })
        .doOnError {
          val errorMessage = it.message ?: ""
          view.viewState = ViewState.Error(ErrorToolbarViewModel(view.context), errorMessage)
        }
        .subscribe {
          view.viewState = ViewState.Weather(WeatherToolbarViewModel(view.context, it.today), it)
        }
  }

  override fun onRefresh() {
    permission.checkPermission({ updateWeather() }, { onPermissionDenied(false) }, true)
  }

  fun onDestroy() {
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
