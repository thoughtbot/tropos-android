package com.thoughtbot.tropos.data

import com.thoughtbot.tropos.R

enum class Status {
  CLEAR_DAY, CLEAR_NIGHT, RAIN, SNOW, SLEET, WIND, FOG, CLOUDY, PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT;
}

fun Status.iconResId(): Int {
  return when (this) {
    Status.CLEAR_DAY -> R.drawable.clear_day
    Status.CLEAR_NIGHT -> R.drawable.clear_night
    Status.RAIN -> R.drawable.rain
    Status.SNOW -> R.drawable.snow
    Status.SLEET -> R.drawable.sleet
    Status.WIND -> R.drawable.wind
    Status.FOG -> R.drawable.fog
    Status.CLOUDY -> R.drawable.cloudy
    Status.PARTLY_CLOUDY_DAY -> R.drawable.partly_cloudy_day
    Status.PARTLY_CLOUDY_NIGHT -> R.drawable.partly_cloudy_night
  }
}
