package com.thoughtbot.tropos.main

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater.from
import android.view.ViewGroup
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WeatherData

class WeatherAdapter(val weather: List<WeatherData>) : RecyclerView.Adapter<ViewHolder>() {

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
      is CurrentWeatherViewHolder -> holder.bind(weather[position])
      is ForecastViewHolder -> holder.bind(weather[position])
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
    return 4
  }

  fun isCurrentWeather(position: Int): Boolean {
    return position == 0
  }

}