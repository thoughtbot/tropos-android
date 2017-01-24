package com.thoughtbot.tropos.viewmodels

import android.content.Context
import com.thoughtbot.tropos.R.string
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.data.iconResId
import java.text.SimpleDateFormat

class ForecastViewModel(val context: Context, val weatherData: WeatherData) {

  val icon: Int = weatherData.status.iconResId()

  val day: String = SimpleDateFormat("EEE").format(weatherData.date)

  val highTemp = context.getString(string.temperature, weatherData.highTemp)

  val lowTemp = context.getString(string.temperature, weatherData.lowTemp)
}

