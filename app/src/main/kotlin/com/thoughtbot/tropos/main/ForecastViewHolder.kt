package com.thoughtbot.tropos.main

import android.support.v7.widget.RecyclerView
import android.view.View
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.viewmodels.ForecastViewModel
import com.thoughtbot.tropos.widgets.ForecastLayout

class ForecastViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

  fun bind(weatherData: WeatherData) {
    val viewModel = ForecastViewModel(itemView.context, weatherData)
    itemView as ForecastLayout
    itemView.setIcon(viewModel.icon)
    itemView.setDay(viewModel.day)
    itemView.setHighTemp(viewModel.highTemp)
    itemView.setLowTemp(viewModel.lowTemp)
  }
}
