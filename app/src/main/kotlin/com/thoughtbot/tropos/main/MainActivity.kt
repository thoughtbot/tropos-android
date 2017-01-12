package com.thoughtbot.tropos.main

import android.content.Context
import android.os.Bundle
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.BaseActivity
import com.thoughtbot.tropos.commons.ViewBinder
import kotlinx.android.synthetic.main.activity_main.temperature_label
import kotlinx.android.synthetic.main.activity_main.toolbar_city
import kotlinx.android.synthetic.main.activity_main.toolbar_last_update
import kotlinx.android.synthetic.main.activity_main.weather_icon
import kotlinx.android.synthetic.main.activity_main.weather_summary
import kotlinx.android.synthetic.main.activity_main.wind_label

class MainActivity : BaseActivity(), MainView {

  val presenter: MainPresenter by lazy { MainPresenter(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    presenter.init()
  }

  override val context: Context = this

  override var viewState: ViewState by ViewBinder {
    when (it) {
      is ViewState.Weather -> {
        toolbar_last_update.text = it.weatherViewModel.lastTimeUpdated
        toolbar_city.text = it.weatherViewModel.city()
        weather_icon.setBackgroundResource(it.weatherViewModel.icon)
        weather_summary.text = it.weatherViewModel.weatherSummary
        temperature_label.setText(it.weatherViewModel.temperatures)
        temperature_label.setDrawable(R.drawable.label_thermometer)
        wind_label.setText(it.weatherViewModel.wind)
        wind_label.setDrawable(R.drawable.label_wind)
      }
      is ViewState.Loading -> {
        // show some loading shit
      }
      is ViewState.Error -> {
        // show error
      }
    }
  }

}

