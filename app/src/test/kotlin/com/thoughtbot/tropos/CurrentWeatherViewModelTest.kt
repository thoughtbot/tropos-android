package com.thoughtbot.tropos

import android.content.Context
import android.location.Location
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.extensions.WindDirection
import com.thoughtbot.tropos.main.CurrentWeatherViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Date
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class CurrentWeatherViewModelTest() {

  lateinit var context: Context

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
  fun testWeatherSummary() {
    val viewModel = CurrentWeatherViewModel(context, mockWeatherData)
    val expected = "Mostly Cloudy"
    val actual = viewModel.weatherSummary

    assertEquals(expected, actual)
  }

  @Test
  fun testIcon() {
    val viewModel = CurrentWeatherViewModel(context, mockWeatherData)
    val expected = R.drawable.partly_cloudy_day
    val actual = viewModel.icon

    assertEquals(expected, actual)
  }

  @Test
  fun testTemperatures() {
    val viewModel = CurrentWeatherViewModel(context, mockWeatherData)
    val expected = "52° / 54° / 48°"
    val actual = viewModel.temperatures

    assertEquals(expected, actual)
  }

  @Test
  fun testWind() {
    val viewModel = CurrentWeatherViewModel(context, mockWeatherData)
    val expected = "4 mph S"
    val actual = viewModel.wind

    assertEquals(expected, actual)
  }

}
