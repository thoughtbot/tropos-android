package com.thoughtbot.tropos.main

import android.content.Context
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.extensions.iconResId
import com.thoughtbot.tropos.extensions.labelResId

class CurrentWeatherViewModel(val context: Context, val today: WeatherData,
    val yesterday: WeatherData) {

  val weatherSummary: String = today.summary

  val icon: Int = today.condition.iconResId()

  val temperatures: String = context.getString(R.string.formatted_temperature_string,
      today.currentTemp, today.highTemp, today.lowTemp)

  val temperatureIcon = R.drawable.label_thermometer

  val wind: String = {
    val windDirection = context.getString(today.windDirection.labelResId())
    context.getString(R.string.formatted_wind_string, today.windSpeed, windDirection)
  }()

  val windIcon = R.drawable.label_wind

}
