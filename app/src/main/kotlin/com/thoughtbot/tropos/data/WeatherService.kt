package com.thoughtbot.tropos.data

import android.content.Context
import android.content.Intent
import com.thoughtbot.tropos.data.remote.ConditionDataService
import com.thoughtbot.tropos.data.remote.LocationService
import com.thoughtbot.tropos.extensions.dayBefore
import com.thoughtbot.tropos.ui.MainActivity
import io.reactivex.Observable
import java.util.Date

class WeatherService(
    val context: Context,
    intent: Intent?,
    val locationDataSource: LocationDataSource = LocationService(context),
    val conditionDataSource: ConditionDataSource = ConditionDataService()) : WeatherDataSource {

  val weather: Weather? = intent?.getParcelableExtra(MainActivity.WEATHER_EXTRA)
  var initialRequest = true

  override fun fetchWeather(): Observable<Weather> {
    if (weather != null && initialRequest) {
      //only return the Weather from Intent on initial request, subsequent calls should hit the API
      initialRequest = false
      return Observable.just(weather)
    } else {
      return locationDataSource.fetchLocation()
          .flatMap { conditionDataSource.fetchForecast(it, 3) }
          .flatMap({ forecast ->
            conditionDataSource.fetchCondition(forecast[0].location, Date().dayBefore())
          }, { forecast, yesterday ->
            return@flatMap Weather(yesterday, forecast[0], forecast.drop(1))
          })
    }
  }
}
