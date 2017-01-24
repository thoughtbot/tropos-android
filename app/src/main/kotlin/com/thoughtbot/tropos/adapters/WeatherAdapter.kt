package com.thoughtbot.tropos.adapters

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater.from
import android.view.ViewGroup
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.R.layout
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.viewholders.CurrentWeatherViewHolder
import com.thoughtbot.tropos.viewholders.ForecastViewHolder

class WeatherAdapter : Adapter<ViewHolder>() {

  var weather: List<Condition> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  val spanSizeLookup = object : SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
      return if (isCurrentWeather(position)) 3 else 1
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    when (viewType) {
      layout.grid_item_current_weather -> {
        val view = from(parent!!.context).inflate(layout.grid_item_current_weather, parent, false)
        return CurrentWeatherViewHolder(view)
      }
      layout.grid_item_forecast -> {
        val view = from(parent!!.context).inflate(layout.grid_item_forecast, parent, false)
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
      return layout.grid_item_current_weather
    } else {
      return layout.grid_item_forecast
    }
  }

  override fun getItemCount(): Int {
    // 1 current weather + 3 daily forecasts
    return if (weather.isEmpty()) 0 else 4
  }

  private fun isCurrentWeather(position: Int): Boolean {
    return position == 0
  }

  private fun forecast(position: Int): Condition {
    //add one to account for yesterdays weather being in {@code weather}
    return weather[position + 1]
  }

  private fun today(): Condition {
    return weather[1]
  }

  private fun yesterday(): Condition {
    return weather[0]
  }

}