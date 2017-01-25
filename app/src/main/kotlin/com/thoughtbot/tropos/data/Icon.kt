package com.thoughtbot.tropos.data

import com.thoughtbot.tropos.R

enum class Icon {
  CLEAR_DAY, CLEAR_NIGHT, RAIN, SNOW, SLEET, WIND, FOG, CLOUDY, PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT;
}

fun Icon.iconResId(): Int {
  return when (this) {
    Icon.CLEAR_DAY -> R.drawable.clear_day
    Icon.CLEAR_NIGHT -> R.drawable.clear_night
    Icon.RAIN -> R.drawable.rain
    Icon.SNOW -> R.drawable.snow
    Icon.SLEET -> R.drawable.sleet
    Icon.WIND -> R.drawable.wind
    Icon.FOG -> R.drawable.fog
    Icon.CLOUDY -> R.drawable.cloudy
    Icon.PARTLY_CLOUDY_DAY -> R.drawable.partly_cloudy_day
    Icon.PARTLY_CLOUDY_NIGHT -> R.drawable.partly_cloudy_night
  }
}
