package com.thoughtbot.tropos.viewholders

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.viewmodels.ForecastViewModel
import com.thoughtbot.tropos.widgets.ForecastLayout

class ForecastViewHolder(itemView: View?) : ViewHolder(itemView) {

  fun bind(condition: Condition) {
    val viewModel = ForecastViewModel(itemView.context, condition = condition)
    itemView as ForecastLayout
    itemView.setIcon(viewModel.icon)
    itemView.setDay(viewModel.day)
    itemView.setHighTemp(viewModel.highTemp())
    itemView.setLowTemp(viewModel.lowTemp())
  }
}
