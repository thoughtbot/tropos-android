package com.thoughtbot.tropos.main

import com.thoughtbot.tropos.commons.Presenter
import com.thoughtbot.tropos.data.LocationDataSource
import com.thoughtbot.tropos.data.WeatherDataSource
import com.thoughtbot.tropos.data.remote.LocationService
import com.thoughtbot.tropos.data.remote.WeatherDataService
import io.reactivex.disposables.Disposable
import java.util.*

class MainPresenter(override val view: MainView,
    val locationDataSource: LocationDataSource = LocationService(view.context),
    val weatherDataSource: WeatherDataSource = WeatherDataService()) : Presenter {

  lateinit var disposable: Disposable

  fun init() {
    //TODO confirm observable is completing
    view.viewState = ViewState.Loading()
    disposable = locationDataSource.fetchLocation()
        .flatMap { weatherDataSource.fetchWeather(it, Date()) }
        .doOnError { view.viewState = ViewState.Error(it.message ?: "") }
        .subscribe {
          view.viewState = ViewState.Weather(WeatherViewModel(view.context, it))
        }
  }

  fun onStop() {
    disposable.dispose()
  }
}
