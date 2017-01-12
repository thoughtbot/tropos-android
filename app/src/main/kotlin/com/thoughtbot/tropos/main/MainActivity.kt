package com.thoughtbot.tropos.main

import android.content.Context
import android.os.Bundle
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.BaseActivity
import com.thoughtbot.tropos.commons.ViewBinder
import kotlinx.android.synthetic.main.activity_main.*

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
        toolbar_title.text = it.weatherViewModel.city()
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

