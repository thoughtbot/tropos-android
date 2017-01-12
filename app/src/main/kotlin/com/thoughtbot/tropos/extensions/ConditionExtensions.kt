package com.thoughtbot.tropos.extensions

import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.Condition

fun Condition.iconResId(): Int {
  return when (this) {
    Condition.CLEAR_DAY -> R.drawable.clear_day
    Condition.CLEAR_NIGHT -> R.drawable.clear_night
    Condition.RAIN -> R.drawable.rain
    Condition.SNOW -> R.drawable.snow
    Condition.SLEET -> R.drawable.sleet
    Condition.WIND -> R.drawable.wind
    Condition.FOG -> R.drawable.fog
    Condition.CLOUDY -> R.drawable.cloudy
    Condition.PARTLY_CLOUDY_DAY -> R.drawable.partly_cloudy_day
    Condition.PARTLY_CLOUDY_NIGHT -> R.drawable.partly_cloudy_night
  }
}
