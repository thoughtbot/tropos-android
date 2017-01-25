package com.thoughtbot.tropos.data

import android.content.Context
import com.thoughtbot.tropos.data.remote.ConditionDataService
import com.thoughtbot.tropos.data.remote.LocationService
import com.thoughtbot.tropos.extensions.dayBefore
import io.reactivex.Observable
import java.util.Date

class WeatherService(
    val context: Context,
    val locationDataSource: LocationDataSource = LocationService(context),
    val conditionDataSource: ConditionDataSource = ConditionDataService()) : WeatherDataSource {

  override fun fetchWeather(): Observable<Weather> {
    return locationDataSource.fetchLocation()
        .flatMap { conditionDataSource.fetchForecast(it, 3) }
        .flatMap({ forecast ->
          conditionDataSource.fetchCondition(forecast[0].location, Date().dayBefore())
        }, { forecast, yesterday ->
          return@flatMap Weather(yesterday, forecast[0], forecast.drop(1))
        })
  }

}

