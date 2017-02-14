package com.thoughtbot.tropos.data.remote

import android.location.Location
import com.thoughtbot.tropos.data.Icon
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.ConditionDataSource
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.WindDirection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Date

class ConditionDataService(val api: ApiService = RestClient().create(
    ApiService::class.java)) : ConditionDataSource {

  override fun fetchCondition(forLocation: Location, forDate: Date): Observable<Condition> {
    return api.fetchRemoteForecast(forLocation.latitude, forLocation.longitude, forDate.time / 1000)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { it.mapToCondition(0) }
  }

  override fun fetchForecast(forLocation: Location,
      forNumOfDaysFromToday: Int): Observable<List<Condition>> {
    return api.fetchRemoteForecast(forLocation.latitude, forLocation.longitude)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { it.mapToConditionList(forNumOfDaysFromToday) }
  }

  private fun RemoteForecast.mapToConditionList(forNumOfDaysFromToday: Int): List<Condition> {
    var list = emptyList<Condition>()
    for (i in 0..forNumOfDaysFromToday) {
      list = list.plus(this.mapToCondition(i))
    }
    return list
  }

  private fun RemoteForecast.mapToCondition(dayOffset: Int): Condition {
    return when (dayOffset) {
    //today
      0 -> {
        val date = this.currently.time
        val summary = this.currently.summary ?: ""
        val location = Location("")
        location.longitude = this.longitude
        location.latitude = this.latitude
        val icon = this.currently.icon.mapToIcon()
        val windSpeed = this.currently.windSpeed?.toInt() ?: 0
        val windDirection = WindDirection(this.currently.windBearing ?: 0.0)
        val unit = IMPERIAL // API returns imperial by default
        val lowTemp = this.daily.data[0].temperatureMin?.toInt() ?: 0
        val highTemp = this.daily.data[0].temperatureMax?.toInt() ?: 0
        val temp = this.currently.temperature?.toInt() ?: 0

        Condition(date, summary, location, icon, windSpeed, windDirection, unit, lowTemp, temp,
            highTemp)
      }
    //this week
      in 1..7 -> {
        val date = this.daily.data[dayOffset].time
        val summary = this.daily.data[dayOffset].summary ?: ""
        val location = Location("")
        location.longitude = this.longitude
        location.latitude = this.latitude
        val icon = this.daily.data[dayOffset].icon.mapToIcon()
        val windSpeed = this.daily.data[dayOffset].windSpeed?.toInt() ?: 0
        val windDirection = WindDirection(this.daily.data[dayOffset].windBearing ?: 0.0)
        val unit = IMPERIAL // API returns imperial by default
        val lowTemp = this.daily.data[dayOffset].temperatureMin?.toInt() ?: 0
        val highTemp = this.daily.data[dayOffset].temperatureMax?.toInt() ?: 0
        val temp = this.daily.data[dayOffset].temperature?.toInt() ?: 0

        Condition(date, summary, location, icon, windSpeed, windDirection, unit, lowTemp, temp,
            highTemp)
      }
      else -> throw IllegalArgumentException("dayOffset must be <= 7")
    }
  }

  private fun String.mapToIcon(): Icon {
    val formatted = this.replace('-', '_').toUpperCase()
    return Icon.values()
        .find { it.name.contentEquals(formatted) }
        ?: throw IllegalArgumentException("$formatted is not a valid Icon")
  }

}

