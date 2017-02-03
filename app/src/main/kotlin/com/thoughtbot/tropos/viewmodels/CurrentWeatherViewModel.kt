package com.thoughtbot.tropos.viewmodels

import android.content.Context
import android.support.v4.content.ContextCompat.getColor
import android.text.SpannableStringBuilder
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.R.color
import com.thoughtbot.tropos.R.drawable
import com.thoughtbot.tropos.R.string
import com.thoughtbot.tropos.data.*
import com.thoughtbot.tropos.data.TemperatureDifference.COLDER
import com.thoughtbot.tropos.data.TemperatureDifference.COOLER
import com.thoughtbot.tropos.data.TemperatureDifference.HOTTER
import com.thoughtbot.tropos.data.TemperatureDifference.SAME
import com.thoughtbot.tropos.data.TemperatureDifference.WARMER
import com.thoughtbot.tropos.data.TimeOfDay.AFTERNOON
import com.thoughtbot.tropos.data.TimeOfDay.DAY
import com.thoughtbot.tropos.data.TimeOfDay.MORNING
import com.thoughtbot.tropos.data.TimeOfDay.NIGHT
import com.thoughtbot.tropos.data.Unit
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC
import com.thoughtbot.tropos.data.local.LocalPreferences
import com.thoughtbot.tropos.extensions.colorSubString
import com.thoughtbot.tropos.extensions.convertSpeed
import com.thoughtbot.tropos.extensions.convertTemperature
import com.thoughtbot.tropos.extensions.lightenBy

class CurrentWeatherViewModel(val context: Context,
    val preferences: Preferences = LocalPreferences(context),
    val today: Condition, val yesterday: Condition) {

  fun weatherSummary(): SpannableStringBuilder {
    val adjective = tempDifference().name.toLowerCase()
    val todayDescription = context.getString(timeOfDay().asPresentDayDescription())
    val yesterdayDescription = context.getString(timeOfDay().asPreviousDayDescription())
    val format = tempDifference().summaryFormat()
    val fullSummary = context.getString(format, adjective, todayDescription, yesterdayDescription)
    return fullSummary.colorSubString(adjective, temperatureDifferenceColor())
  }

  val icon: Int = today.icon.iconResId()

  fun temperatures(): SpannableStringBuilder {
    val high = today.highTemp.convertTemperature(today.unit, preferences.unit)
    val low = today.lowTemp.convertTemperature(today.unit, preferences.unit)
    val current = today.currentTemp.convertTemperature(today.unit, preferences.unit)

    val fullString = context.getString(R.string.formatted_temperature_string, high, current, low)
    val today = context.getString(string.temperature, current)
    return fullString.colorSubString(today, temperatureDifferenceColor())
  }

  val temperatureIcon = drawable.label_thermometer

  fun wind(): String {
    val windDirection = context.getString(today.windDirection.labelResId())
    val windSpeed = today.windSpeed.convertSpeed(today.unit, preferences.unit)
    val stringFormat = if (preferences.unit == IMPERIAL) R.string.formatted_imperial_wind_string else R.string.formatted_metric_wind_string
    return context.getString(stringFormat, windSpeed, windDirection)
  }

  val windIcon = drawable.label_wind

  private fun tempDifference(): TemperatureDifference {
    return TemperatureDifference(today, yesterday)
  }

  private fun timeOfDay(): TimeOfDay {
    return TimeOfDay(today.date)
  }

  private fun temperatureDifferenceColor(): Int {
    val color = when (tempDifference()) {
      SAME -> getColor(context, android.R.color.white)
      HOTTER -> getColor(context, color.burnt_orange)
      WARMER -> getColor(context, color.tangerine)
      COOLER -> getColor(context, color.sky_blue)
      COLDER -> getColor(context, color.sky_blue_light)
    }

    if (tempDifference() == COOLER || tempDifference() == WARMER) {
      val amount = (Math.min(Math.abs(today.currentTemp - yesterday.currentTemp), 10)) / 10.0
      val lighterAmount = Math.min(1 - amount, 0.8).toFloat()

      return color.lightenBy(lighterAmount)
    } else {
      return color
    }
  }

  private fun TimeOfDay.asPresentDayDescription(): Int {
    when (this) {
      MORNING -> return string.present_morning
      DAY -> return string.present_day
      AFTERNOON -> return string.present_afternoon
      NIGHT -> return string.present_night
      else -> throw IllegalArgumentException("$this is not a valid TimeOfDay")
    }
  }

  private fun TimeOfDay.asPreviousDayDescription(): Int {
    when (this) {
      MORNING -> return string.previous_morning
      DAY -> return string.previous_day
      AFTERNOON -> return string.previous_afternoon
      NIGHT -> return string.previous_night
      else -> throw IllegalArgumentException("$this is not a valid TimeOfDay")
    }
  }

  private fun TemperatureDifference.summaryFormat(): Int {
    return if (this == SAME) string.same_temperature_format else string.different_temperature_format
  }
}
