package com.thoughtbot.tropos.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.adapters.WeatherAdapter
import com.thoughtbot.tropos.commons.BaseActivity
import com.thoughtbot.tropos.commons.ViewBinder
import com.thoughtbot.tropos.data.Weather
import com.thoughtbot.tropos.extensions.attachSnapHelper
import com.thoughtbot.tropos.permissions.getPermissionResults
import com.thoughtbot.tropos.refresh.PullToRefreshLayout
import com.thoughtbot.tropos.refresh.RefreshDrawable
import com.thoughtbot.tropos.scrolling.WeatherSnapHelper
import com.thoughtbot.tropos.scrolling.setVerticalEndOverScroller
import com.thoughtbot.tropos.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.error_text
import kotlinx.android.synthetic.main.activity_main.footer
import kotlinx.android.synthetic.main.activity_main.toolbar_city
import kotlinx.android.synthetic.main.activity_main.toolbar_last_update
import org.jetbrains.anko.find

class MainActivity : BaseActivity(), MainView {
  companion object {
    val WEATHER_EXTRA = "weather_extra"

    fun createIntent(context: Context, weather: Weather?): Intent {
      val intent = Intent(context, MainActivity::class.java)
      weather?.let { intent.putExtra(WEATHER_EXTRA, it) }
      return intent
    }
  }

  val presenter: MainPresenter by lazy { MainPresenter(this, intent) }
  val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) as RecyclerView }
  val adapter: WeatherAdapter by lazy { WeatherAdapter() }
  val layoutManager: GridLayoutManager by lazy { GridLayoutManager(this, 3) }
  val pullToRefreshLayout  by lazy { find<PullToRefreshLayout>(R.id.pull_to_refresh) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val toolbar = find<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)

    recyclerView.adapter = adapter
    layoutManager.spanSizeLookup = adapter.spanSizeLookup
    recyclerView.layoutManager = layoutManager
    recyclerView.attachSnapHelper(WeatherSnapHelper())
    recyclerView.setVerticalEndOverScroller()

    pullToRefreshLayout.setRefreshingDrawable(RefreshDrawable(this))
    pullToRefreshLayout.refreshListener = presenter
  }

  override fun onResume() {
    super.onResume()
    presenter.onResume()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onDestroy()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    getPermissionResults(presenter.permission, presenter, requestCode, permissions, grantResults)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.settings_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.action_settings -> {
        startActivity(Intent(this, SettingsActivity::class.java))
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override val context: Context = this

  override var viewState: ViewState by ViewBinder {
    when (it) {
      is ViewState.Weather -> {
        toolbar_city.text = it.toolbarViewModel.title()
        toolbar_last_update.text = it.toolbarViewModel.subtitle()

        adapter.weather = it.weather
        recyclerView.itemAnimator.isRunning {
          pullToRefreshLayout.setRefreshing(false)
        }

        footer.visibility = View.VISIBLE
        error_text.visibility = View.GONE
      }
      is ViewState.Loading -> {
        toolbar_city.text = it.toolbarViewModel.title()
        toolbar_last_update.text = it.toolbarViewModel.subtitle()

        footer.visibility = View.GONE
        error_text.visibility = View.GONE
      }
      is ViewState.Error -> {
        toolbar_city.text = it.toolbarViewModel.title()
        toolbar_last_update.text = it.toolbarViewModel.subtitle()

        pullToRefreshLayout.setRefreshing(false)

        footer.visibility = View.GONE
        error_text.visibility = View.VISIBLE
        error_text.text = it.errorMessage
      }
    }
  }

}

