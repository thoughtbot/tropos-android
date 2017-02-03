package com.thoughtbot.tropos.viewmodels

import android.content.Context
import android.location.Location
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.R.drawable
import com.thoughtbot.tropos.data.Icon.PARTLY_CLOUDY_DAY
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.Unit
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC
import com.thoughtbot.tropos.data.WindDirection
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class CurrentWeatherViewModelTest() {

  lateinit var context: Context
  val preferences = mock<Preferences>()

  val mockCondition: Condition = {
    val timeStamp = 1484180189 * 1000L // equivalent to Wed Jan 11 16:16:29 PST 2017
    val date = Date(timeStamp)
    val summary = "Mostly Cloudy"
    val location = Location("")
    location.longitude = -122.4375671
    location.latitude = 37.8032493
    val icon = PARTLY_CLOUDY_DAY
    val windSpeed = 4
    val windDirection = WindDirection(171.0)
    val unit = IMPERIAL
    val lowTemp = 48
    val highTemp = 54
    val temp = 52

    Condition(date, summary, location, icon, windSpeed, windDirection, unit, lowTemp, temp,
        highTemp)
  }()

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  fun testWeatherSummary() {
    val viewModel = CurrentWeatherViewModel(context, preferences, mockCondition, mockCondition)
    val expected = "It's the same this afternoon as yesterday afternoon."
    val actual = viewModel.weatherSummary()

    assertTrue { expected.contentEquals(actual) }
  }

  @Test
  fun testIcon() {
    val viewModel = CurrentWeatherViewModel(context, preferences, mockCondition, mockCondition)
    val expected = drawable.partly_cloudy_day
    val actual = viewModel.icon

    assertEquals(expected, actual)
  }

  @Test
  fun testTemperatures_imperial_to_imperial() {
    val viewModel = CurrentWeatherViewModel(context, preferences, mockCondition, mockCondition)
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "54° / 52° / 48°"
    val actual = viewModel.temperatures()

    assertTrue { expected.contentEquals(actual) }
  }

  @Test
  fun testTemperatures_imperial_to_metric() {
    val viewModel = CurrentWeatherViewModel(context, preferences, mockCondition, mockCondition)
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "12° / 11° / 8°"
    val actual = viewModel.temperatures()

    assertTrue { expected.contentEquals(actual) }
  }

  @Test
  fun testTemperatures_metric_to_metric() {
    val metricCondition = mockCondition.copy(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, mockCondition)
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "54° / 52° / 48°"
    val actual = viewModel.temperatures()

    assertTrue { expected.contentEquals(actual) }
  }

  @Test
  fun testTemperatures_metric_to_imperial() {
    val metricCondition = mockCondition.copy(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, mockCondition)
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "129° / 125° / 118°"
    val actual = viewModel.temperatures()

    assertTrue { expected.contentEquals(actual) }
  }

  @Test
  fun testWind_imperial_to_imperial() {
    val viewModel = CurrentWeatherViewModel(context, preferences, mockCondition, mockCondition)
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "4 mph S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

  @Test
  fun testWind_imperial_to_metric() {
    val viewModel = CurrentWeatherViewModel(context, preferences, mockCondition, mockCondition)
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "6 km/h S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

  @Test
  fun testWind_metric_to_metric() {
    val metricCondition = mockCondition.copy(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, mockCondition)
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "4 km/h S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

  @Test
  fun testWind_metric_to_imperial() {
    val metricCondition = mockCondition.copy(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, mockCondition)
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "2 mph S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

}
