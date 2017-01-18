package com.thoughtbot.tropos.main

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.BaseActivity
import com.thoughtbot.tropos.commons.ViewBinder
import com.thoughtbot.tropos.refresh.PullToRefreshLayout
import com.thoughtbot.tropos.refresh.RefreshDrawable
import com.thoughtbot.tropos.refresh.setVerticalEndOverScroller
import kotlinx.android.synthetic.main.activity_main.toolbar_city
import kotlinx.android.synthetic.main.activity_main.toolbar_last_update
import org.jetbrains.anko.find

class MainActivity : BaseActivity(), MainView {

  val presenter: MainPresenter by lazy { MainPresenter(this) }
  val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) as RecyclerView }
  val layoutManager: GridLayoutManager by lazy { GridLayoutManager(this, 3) }
  val pullToRefreshLayout  by lazy { find<PullToRefreshLayout>(R.id.pull_to_refresh) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    pullToRefreshLayout.setRefreshingDrawable(RefreshDrawable(this))
    pullToRefreshLayout.refreshListener = presenter

    presenter.init()
  }

  override val context: Context = this

  override var viewState: ViewState by ViewBinder {
    when (it) {
      is ViewState.Weather -> {
        //toolbar
        toolbar_city.text = it.toolbarViewModel.title()
        toolbar_last_update.text = it.toolbarViewModel.subtitle()

        //recycler view
        val adapter = WeatherAdapter(it.weather)
        layoutManager.spanSizeLookup = adapter.spanSizeLookup
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //pull to refresh
        pullToRefreshLayout.setRefreshing(false)

        //bottom over scroll
        recyclerView.setVerticalEndOverScroller()
      }
      is ViewState.Loading -> {
        toolbar_city.text = it.toolbarViewModel.title()
        toolbar_last_update.text = it.toolbarViewModel.subtitle()
      }
      is ViewState.Error -> {
        // show error
      }
    }
  }

}

