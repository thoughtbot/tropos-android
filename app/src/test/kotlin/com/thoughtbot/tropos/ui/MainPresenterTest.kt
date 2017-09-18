package com.thoughtbot.tropos.ui

import android.content.Context
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Weather
import com.thoughtbot.tropos.data.WeatherDataSource
import com.thoughtbot.tropos.permissions.Permission
import com.thoughtbot.tropos.testUtils.fakeCondition
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class MainPresenterTest() {

  private lateinit var context: Context
  private val view = mock<MainView>()
  private val permission = mock<Permission>()
  private val weatherDataSource = mock<WeatherDataSource>()
  private val mockWeather: Weather = Weather(fakeCondition(), fakeCondition(),
      listOf(fakeCondition(), fakeCondition(), fakeCondition()))

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  fun testOnResume_hasPermission() {
    val presenter = MainPresenter(view, null, weatherDataSource, permission)
    whenever(view.context).thenReturn(context)
    stubWeather()
    stubPermission(true)

    presenter.onStart()

    verify(view).viewState = isA<ViewState.Loading>()
    verify(view).viewState = isA<ViewState.Weather>()
  }

  @Test
  fun testOnResume_doesNotHavePermission() {
    val presenter = MainPresenter(view, null, weatherDataSource, permission)
    whenever(view.context).thenReturn(context)
    stubWeather()
    stubPermission(false)

    presenter.onStart()

    verify(view).viewState = isA<ViewState.Error>()
  }

  fun stubWeather() {
    whenever(weatherDataSource.fetchWeather()).thenReturn(Observable.just(mockWeather))
  }

  fun stubPermission(hasPermission: Boolean) {
    whenever(permission.hasPermission()).thenReturn(hasPermission)
  }

}
