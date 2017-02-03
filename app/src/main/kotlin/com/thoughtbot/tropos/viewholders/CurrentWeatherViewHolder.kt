package com.thoughtbot.tropos.viewholders

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.thoughtbot.tropos.R.id
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.viewmodels.CurrentWeatherViewModel
import com.thoughtbot.tropos.widgets.DrawableTextLabel
import org.jetbrains.anko.find

class CurrentWeatherViewHolder(itemView: View?) : ViewHolder(itemView) {

  fun bind(today: Condition, yesterday: Condition) {
    val viewModel = CurrentWeatherViewModel(itemView.context, today = today, yesterday = yesterday)

    itemView.find<ImageView>(id.weather_icon).setBackgroundResource(viewModel.icon)
    itemView.find<TextView>(id.weather_summary).text = viewModel.weatherSummary()
    itemView.find<DrawableTextLabel>(id.temperature_label).setText(viewModel.temperatures())
    itemView.find<DrawableTextLabel>(id.temperature_label).setDrawable(viewModel.temperatureIcon)
    itemView.find<DrawableTextLabel>(id.wind_label).setText(viewModel.wind())
    itemView.find<DrawableTextLabel>(id.wind_label).setDrawable(viewModel.windIcon)
  }

}