package com.thoughtbot.tropos.main

import android.content.Context
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.extensions.iconResId
import java.text.SimpleDateFormat

class ForecastViewModel(val context: Context, val weatherData: WeatherData) {

  val icon: Int = weatherData.condition.iconResId()

  val day: String = SimpleDateFormat("EEE").format(weatherData.date)

  val highTemp = context.getString(R.string.temperature, weatherData.highTemp)

  val lowTemp = context.getString(R.string.temperature, weatherData.lowTemp)
}

