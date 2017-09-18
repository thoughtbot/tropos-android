package com.thoughtbot.tropos.viewmodels

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.R.drawable
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC
import com.thoughtbot.tropos.testUtils.assertStringEquals
import com.thoughtbot.tropos.testUtils.fakeCondition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class CurrentWeatherViewModelTest() {

  private lateinit var context: Context
  private val preferences = mock<Preferences>()

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  fun testWeatherSummary() {
    val viewModel = CurrentWeatherViewModel(context, preferences, fakeCondition(), fakeCondition())
    val expected = "It's the same this afternoon as yesterday afternoon."
    val actual = viewModel.weatherSummary()

    assertStringEquals(expected, actual)
  }

  @Test
  fun testIcon() {
    val viewModel = CurrentWeatherViewModel(context, preferences, fakeCondition(), fakeCondition())
    val expected = drawable.partly_cloudy_day
    val actual = viewModel.icon

    assertEquals(expected, actual)
  }

  @Test
  fun testTemperatures_imperial_to_imperial() {
    val viewModel = CurrentWeatherViewModel(context, preferences, fakeCondition(), fakeCondition())
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "54° / 52° / 48°"
    val actual = viewModel.temperatures()

    assertStringEquals(expected, actual)
  }

  @Test
  fun testTemperatures_imperial_to_metric() {
    val viewModel = CurrentWeatherViewModel(context, preferences, fakeCondition(), fakeCondition())
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "12° / 11° / 8°"
    val actual = viewModel.temperatures()

    assertStringEquals(expected, actual)
  }

  @Test
  fun testTemperatures_metric_to_metric() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, fakeCondition())
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "54° / 52° / 48°"
    val actual = viewModel.temperatures()

    assertStringEquals(expected, actual)
  }

  @Test
  fun testTemperatures_metric_to_imperial() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, fakeCondition())
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "129° / 125° / 118°"
    val actual = viewModel.temperatures()

    assertStringEquals(expected, actual)
  }

  @Test
  fun testWind_imperial_to_imperial() {
    val viewModel = CurrentWeatherViewModel(context, preferences, fakeCondition(), fakeCondition())
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "4 mph S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

  @Test
  fun testWind_imperial_to_metric() {
    val viewModel = CurrentWeatherViewModel(context, preferences, fakeCondition(), fakeCondition())
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "6 km/h S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

  @Test
  fun testWind_metric_to_metric() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, fakeCondition())
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "4 km/h S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

  @Test
  fun testWind_metric_to_imperial() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = CurrentWeatherViewModel(context, preferences, metricCondition, fakeCondition())
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "2 mph S"
    val actual = viewModel.wind()

    assertEquals(expected, actual)
  }

}
