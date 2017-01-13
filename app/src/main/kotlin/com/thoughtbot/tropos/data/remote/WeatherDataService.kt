package com.thoughtbot.tropos.data.remote

import android.location.Location
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.data.WeatherDataSource
import com.thoughtbot.tropos.extensions.WindDirection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Date

class WeatherDataService(val api: ApiService = RestClient().create(
    ApiService::class.java)) : WeatherDataSource {

  override fun fetchWeather(forLocation: Location, forDate: Date): Observable<WeatherData> {
    return api.fetchRemoteForecast(forLocation.latitude, forLocation.longitude, forDate.time / 1000)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { it.mapToWeatherData(0) }
  }

  override fun fetchForecast(forLocation: Location,
      forNumOfDaysFromToday: Int): Observable<List<WeatherData>> {
    return api.fetchRemoteForecast(forLocation.latitude, forLocation.longitude)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { it.mapToWeatherDataList(forNumOfDaysFromToday) }
  }

  private fun RemoteForecast.mapToWeatherDataList(forNumOfDaysFromToday: Int): List<WeatherData> {
    var list = emptyList<WeatherData>()
    for (i in 0..forNumOfDaysFromToday) {
      list = list.plus(this.mapToWeatherData(i))
    }
    return list
  }

  private fun RemoteForecast.mapToWeatherData(dayOffset: Int): WeatherData {
    return when (dayOffset) {
    //today
      0 -> {
        val date = this.currently.time
        val summary = this.currently.summary ?: ""
        val location = Location("")
        location.longitude = this.longitude
        location.latitude = this.latitude
        val condition = this.currently.icon.mapToCondition()
        val windSpeed = this.currently.windSpeed?.toInt() ?: 0
        val windDirection = WindDirection(this.currently.windBearing ?: 0.0)
        val lowTemp = this.daily.data[0].temperatureMin?.toInt() ?: 0
        val highTemp = this.daily.data[0].temperatureMax?.toInt() ?: 0
        val temp = this.currently.temperature?.toInt() ?: 0

        WeatherData(date, summary, location, condition, windSpeed, windDirection, lowTemp, temp,
            highTemp)
      }
    //this week
      in 1..7 -> {
        val date = this.daily.data[dayOffset].time
        val summary = this.daily.data[dayOffset].summary ?: ""
        val location = Location("")
        location.longitude = this.longitude
        location.latitude = this.latitude
        val condition = this.daily.data[dayOffset].icon.mapToCondition()
        val windSpeed = this.daily.data[dayOffset].windSpeed?.toInt() ?: 0
        val windDirection = WindDirection(this.daily.data[dayOffset].windBearing ?: 0.0)
        val lowTemp = this.daily.data[dayOffset].temperatureMin?.toInt() ?: 0
        val highTemp = this.daily.data[dayOffset].temperatureMax?.toInt() ?: 0
        val temp = this.daily.data[dayOffset].temperature?.toInt() ?: 0

        WeatherData(date, summary, location, condition, windSpeed, windDirection, lowTemp, temp,
            highTemp)
      }
      else -> throw IllegalArgumentException("dayOffset must be <= 7")
    }
  }

  private fun String.mapToCondition(): Condition {
    val formatted = this.replace('-', '_').toUpperCase()
    return Condition.values()
        .find { it.name.contentEquals(formatted) }
        ?: throw IllegalArgumentException("$formatted is not a valid Condition")
  }

}

