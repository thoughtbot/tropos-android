package com.thoughtbot.tropos.viewmodels

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.R.drawable
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC
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
class ForecastViewModelTest() {

  private lateinit var context: Context
  private val preferences = mock<Preferences>()

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  fun testIcon() {
    val viewModel = ForecastViewModel(context, preferences, fakeCondition())
    val expected = drawable.partly_cloudy_day
    val actual = viewModel.icon

    assertEquals(expected, actual)
  }

  @Test
  fun testDay() {
    val viewModel = ForecastViewModel(context, preferences, fakeCondition())
    val expected = "Wed"
    val actual = viewModel.day

    assertEquals(expected, actual)
  }

  @Test
  fun testHighTemp_imperial_to_imperial() {
    val viewModel = ForecastViewModel(context, preferences, fakeCondition())
     whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "54°"
    val actual = viewModel.highTemp()

    assertEquals(expected, actual)
  }

  @Test
  fun testHighTemp_imperial_to_metric() {
    val viewModel = ForecastViewModel(context, preferences, fakeCondition())
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "12°"
    val actual = viewModel.highTemp()

    assertEquals(expected, actual)
  }

  @Test
  fun testHighTemp_metric_to_metric() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = ForecastViewModel(context, preferences, metricCondition)
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "54°"
    val actual = viewModel.highTemp()

    assertEquals(expected, actual)
  }

  @Test
  fun testHighTemp_metric_to_imperial() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = ForecastViewModel(context, preferences, metricCondition)
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "129°"
    val actual = viewModel.highTemp()

    assertEquals(expected, actual)
  }

  @Test
  fun testLowTemp_imperial_to_imperial() {
    val viewModel = ForecastViewModel(context, preferences, fakeCondition())
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "48°"
    val actual = viewModel.lowTemp()

    assertEquals(expected, actual)
  }

  @Test
  fun testLowTemp_imperial_to_metric() {
    val viewModel = ForecastViewModel(context, preferences, fakeCondition())
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "8°"
    val actual = viewModel.lowTemp()

    assertEquals(expected, actual)
  }

  @Test
  fun testLowTemp_metric_to_metric() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = ForecastViewModel(context, preferences, metricCondition)
    whenever(preferences.unit).thenReturn(METRIC)

    val expected = "48°"
    val actual = viewModel.lowTemp()

    assertEquals(expected, actual)
  }

  @Test
  fun testLowTemp_metric_to_imperial() {
    val metricCondition = fakeCondition(unit = METRIC)
    val viewModel = ForecastViewModel(context, preferences, metricCondition)
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val expected = "118°"
    val actual = viewModel.lowTemp()

    assertEquals(expected, actual)
  }
}

