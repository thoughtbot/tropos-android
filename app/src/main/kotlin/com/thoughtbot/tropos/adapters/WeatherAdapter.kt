package com.thoughtbot.tropos.adapters

//import androidx.appcompat.widget.GridLayoutManager.SpanSizeLookup
//import androidx.appcompat.widget.RecyclerView.Adapter
//import androidx.appcompat.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thoughtbot.tropos.R.layout
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Weather
import com.thoughtbot.tropos.viewholders.CurrentWeatherViewHolder
import com.thoughtbot.tropos.viewholders.ForecastViewHolder

class WeatherAdapter : RecyclerView.Adapter<ViewHolder>() {

  var weather: Weather? = null
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
      return if (isCurrentWeather(position)) 3 else 1
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    when (holder) {
      is CurrentWeatherViewHolder -> weather?.let { holder.bind(it.today, it.yesterday) }
      is ForecastViewHolder -> weather?.let { holder.bind(forecast(it, position)) }
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
    return if (weather == null) 0 else 4
  }

  private fun isCurrentWeather(position: Int): Boolean {
    return position == 0
  }

  private fun forecast(weather: Weather, position: Int): Condition {
    //subtract one to account for the current condition
    return weather.nextThreeDays[position - 1]
  }

}