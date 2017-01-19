package com.thoughtbot.tropos

import android.content.Context
import android.location.Location
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.LocationDataSource
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.data.WeatherDataSource
import com.thoughtbot.tropos.extensions.WindDirection
import com.thoughtbot.tropos.ui.MainPresenter
import com.thoughtbot.tropos.ui.MainView
import com.thoughtbot.tropos.ui.ViewState
import com.thoughtbot.tropos.permissions.Permission
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Date

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class MainPresenterTest() {

  lateinit var context: Context
  val view = mock<MainView>()
  val permission = mock<Permission>()
  val locationDataSource = mock<LocationDataSource>()
  val weatherDataSource = mock<WeatherDataSource>()

  val mockWeatherData: WeatherData = {
    val timeStamp = 1484180189 * 1000L // equivalent to Wed Jan 11 16:16:29 PST 2017
    val date = Date(timeStamp)
    val summary = "Mostly Cloudy"
    val location = Location("")
    location.longitude = -122.4375671
    location.latitude = 37.8032493
    val condition = Condition.PARTLY_CLOUDY_DAY
    val windSpeed = 4
    val windDirection = WindDirection(171.0)
    val lowTemp = 48
    val highTemp = 54
    val temp = 52

    WeatherData(date, summary, location, condition, windSpeed, windDirection, lowTemp, temp,
        highTemp)
  }()

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  fun testInit_hasPermission() {
    val presenter = MainPresenter(view, locationDataSource, weatherDataSource, permission)
    whenever(view.context).thenReturn(context)
    stubLocation()
    stubWeather()
    stubForecast()
    stubPermission(true)

    presenter.init()

    verify(view).viewState = isA<ViewState.Loading>()
    verify(view).viewState = isA<ViewState.Weather>()
  }

  @Test
  fun testInit_doesNotHavePermission() {
    val presenter = MainPresenter(view, locationDataSource, weatherDataSource, permission)
    whenever(view.context).thenReturn(context)
    stubLocation()
    stubWeather()
    stubForecast()
    stubPermission(false)

    presenter.init()

    verify(view).viewState = isA<ViewState.Error>()
  }

  fun stubLocation() {
    whenever(locationDataSource.fetchLocation()).thenReturn(Observable.just(Location("")))
  }

  fun stubWeather() {
    whenever(weatherDataSource.fetchWeather(any(), any())).thenReturn(
        Observable.just(mockWeatherData))
  }

  fun stubForecast() {
    whenever(weatherDataSource.fetchForecast(any(), any())).thenReturn(
        Observable.just(listOf(mockWeatherData)))
  }

  fun stubPermission(hasPermission: Boolean) {
    whenever(permission.hasPermission()).thenReturn(hasPermission)
  }

}
