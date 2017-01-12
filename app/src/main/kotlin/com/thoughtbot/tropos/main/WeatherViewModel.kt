package com.thoughtbot.tropos.main

import android.content.Context
import android.location.Geocoder
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.extensions.iconResId
import com.thoughtbot.tropos.extensions.labelResId
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(val context: Context, val weatherData: WeatherData) {

  val lastTimeUpdated: String = {
    val formattedDate = SimpleDateFormat("h:mm a", Locale.getDefault()).format(weatherData.date)
    context.getString(R.string.updated_at, formattedDate)
  }()

  fun city(): String = {
    val geoCoder = Geocoder(context)
    geoCoder.getFromLocation(weatherData.location.latitude, weatherData.location.longitude, 1)
        .first().locality
  }()

  val weatherSummary: String = weatherData.summary

  val icon: Int = weatherData.condition.iconResId()

  val temperatures: String = context.getString(R.string.formatted_temperature_string,
      weatherData.currentTemp, weatherData.highTemp, weatherData.lowTemp)

  val wind: String = {
    val windDirection = context.getString(weatherData.windDirection.labelResId())
    context.getString(R.string.formatted_wind_string, weatherData.windSpeed, windDirection)
  }()

}

