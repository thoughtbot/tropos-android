package com.thoughtbot.tropos.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.widgets.DrawableTextLabel
import org.jetbrains.anko.find

class CurrentWeatherViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

  fun bind(weatherData: WeatherData) {
    val viewModel = CurrentWeatherViewModel(itemView.context, weatherData)

    itemView.find<ImageView>(R.id.weather_icon).setBackgroundResource(viewModel.icon)
    itemView.find<TextView>(R.id.weather_summary).text = viewModel.weatherSummary
    itemView.find<DrawableTextLabel>(R.id.temperature_label).setText(viewModel.temperatures)
    itemView.find<DrawableTextLabel>(R.id.temperature_label).setDrawable(viewModel.temperatureIcon)
    itemView.find<DrawableTextLabel>(R.id.wind_label).setText(viewModel.wind)
    itemView.find<DrawableTextLabel>(R.id.wind_label).setDrawable(viewModel.windIcon)
  }

}