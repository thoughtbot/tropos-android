package com.thoughtbot.tropos.viewmodels

import android.content.Context
import com.thoughtbot.tropos.R.string
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.iconResId
import com.thoughtbot.tropos.data.local.LocalPreferences
import com.thoughtbot.tropos.extensions.convertTemperature
import java.text.SimpleDateFormat

class ForecastViewModel(val context: Context,
    val preferences: Preferences = LocalPreferences(context), val condition: Condition) {

  val icon: Int = condition.icon.iconResId()

  val day: String = SimpleDateFormat("EEE").format(condition.date)

  fun highTemp(): String {
    val highTemp = condition.highTemp.convertTemperature(condition.unit, preferences.unit)
    return context.getString(string.temperature, highTemp)
  }

  fun lowTemp(): String {
    val lowTemp = condition.lowTemp.convertTemperature(condition.unit, preferences.unit)
    return context.getString(string.temperature, lowTemp)
  }

}

