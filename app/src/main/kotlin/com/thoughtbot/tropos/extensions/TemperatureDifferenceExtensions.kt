package com.thoughtbot.tropos.extensions

import com.thoughtbot.tropos.data.TemperatureDifference
import com.thoughtbot.tropos.data.TemperatureDifference.COLDER
import com.thoughtbot.tropos.data.TemperatureDifference.COOLER
import com.thoughtbot.tropos.data.TemperatureDifference.HOTTER
import com.thoughtbot.tropos.data.TemperatureDifference.SAME
import com.thoughtbot.tropos.data.TemperatureDifference.WARMER
import com.thoughtbot.tropos.data.WeatherData


fun TemperatureDifference(today: WeatherData, yesterday: WeatherData): TemperatureDifference {
  val HOTTER_LIMIT: Int = 32
  val COLDER_LIMIT: Int = 75

  val diff = today.currentTemp - yesterday.currentTemp

  return when {
    diff >= 10 && today.currentTemp > HOTTER_LIMIT -> HOTTER
    diff > 0 -> WARMER
    diff == 0 -> SAME
    diff > -10 || today.currentTemp > COLDER_LIMIT -> COOLER
    else -> COLDER
  }
}
