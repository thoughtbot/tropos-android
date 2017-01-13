package com.thoughtbot.tropos.main

import android.content.Context
import android.location.Geocoder
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WeatherData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToolbarViewModel(val context: Context, val weatherData: WeatherData?) {

  fun subtitle(): String {
    val date = weatherData?.date ?: Date()
    val formattedDate = SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
    return context.getString(R.string.updated_at, formattedDate)
  }

  fun title(): String {
    if (weatherData != null) {
      return Geocoder(context).getFromLocation(weatherData.location.latitude,
          weatherData.location.longitude, 1).first().locality
    } else {
      return context.getString(R.string.checking_weather)
    }
  }
}
