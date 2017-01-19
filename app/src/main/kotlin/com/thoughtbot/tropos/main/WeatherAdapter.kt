package com.thoughtbot.tropos.main

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater.from
import android.view.ViewGroup
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.viewholders.CurrentWeatherViewHolder
import com.thoughtbot.tropos.viewholders.ForecastViewHolder

class WeatherAdapter : RecyclerView.Adapter<ViewHolder>() {

  var weather: List<WeatherData> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
      return if (isCurrentWeather(position)) 3 else 1
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    when (viewType) {
      R.layout.grid_item_current_weather -> {
        val view = from(parent!!.context).inflate(R.layout.grid_item_current_weather, parent, false)
        return CurrentWeatherViewHolder(view)
      }
      R.layout.grid_item_forecast -> {
        val view = from(parent!!.context).inflate(R.layout.grid_item_forecast, parent, false)
        return ForecastViewHolder(view)
      }
      else -> throw IllegalArgumentException("$viewType is not a valid viewType")
    }
  }

  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    when (holder) {
      is CurrentWeatherViewHolder -> holder.bind(today(), yesterday())
      is ForecastViewHolder -> holder.bind(forecast(position))
    }
  }

  override fun getItemViewType(position: Int): Int {
    if (isCurrentWeather(position)) {
      return R.layout.grid_item_current_weather
    } else {
      return R.layout.grid_item_forecast
    }
  }

  override fun getItemCount(): Int {
    // 1 current weather + 3 daily forecasts
    return if (weather.isEmpty()) 0 else 4
  }

  private fun isCurrentWeather(position: Int): Boolean {
    return position == 0
  }

  private fun forecast(position: Int): WeatherData {
    //add one to account for yesterdays weather being in {@code weather}
    return weather[position + 1]
  }

  private fun today(): WeatherData {
    return weather[1]
  }

  private fun yesterday(): WeatherData {
    return weather[0]
  }

}