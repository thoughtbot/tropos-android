package com.thoughtbot.tropos.viewmodels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.thoughtbot.tropos.R.string
import com.thoughtbot.tropos.data.WeatherData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface ToolbarViewModel {

  fun title(): String

  fun subtitle(): String

}

class LoadingToolbarViewModel(val context: Context) : ToolbarViewModel {

  override fun title(): String {
    return context.getString(string.checking_weather)
  }

  override fun subtitle(): String {
    val date = Date()
    val formattedDate = SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
    return context.getString(string.updated_at, formattedDate)
  }

}

class WeatherToolbarViewModel(val context: Context,
    val weatherData: WeatherData) : ToolbarViewModel {

  override fun subtitle(): String {
    val date = weatherData.date
    val formattedDate = SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
    return context.getString(string.updated_at, formattedDate)
  }

  override fun title(): String {
    val latitude = weatherData.location.latitude
    val longitude = weatherData.location.longitude
    val address: List<Address>? = Geocoder(context).getFromLocation(latitude, longitude, 1)
    val city: String? = address?.firstOrNull()?.locality
    return city ?: "$latitude, $longitude"
  }
}

class ErrorToolbarViewModel(val context: Context) : ToolbarViewModel {

  override fun title(): String {
    return context.getString(string.update_failed)
  }

  override fun subtitle(): String {
    val date = Date()
    val formattedDate = SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
    return context.getString(string.updated_at, formattedDate)
  }

}
