package com.thoughtbot.tropos.settings

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC
import com.thoughtbot.tropos.data.Weather
import com.thoughtbot.tropos.testUtils.FakeCondition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class SettingsPresenterTest() {

  lateinit var context: Context
  val view = mock<SettingsView>()
  val preferences = mock<Preferences>()
  val fakeCondition: Condition = FakeCondition.getInstance()
  val mockWeather: Weather = Weather(fakeCondition, fakeCondition,
      listOf(fakeCondition, fakeCondition, fakeCondition))

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  fun testInitialValue_metric() {
    whenever(preferences.unit).thenReturn(METRIC)

    val presenter = SettingsPresenter(view, preferences)

    presenter.init()

    verify(view).checkUnitPreference(R.id.settings_metric_button)
  }

  @Test
  fun testInitialValue_imperial() {
    whenever(preferences.unit).thenReturn(IMPERIAL)

    val presenter = SettingsPresenter(view, preferences)

    presenter.init()

    verify(view).checkUnitPreference(R.id.settings_imperial_button)
  }

  @Test
  fun testCheckChange_toImperial() {
    val presenter = SettingsPresenter(view, preferences)

    presenter.onCheckedChanged(null, R.id.settings_imperial_button)

    verify(preferences).unit = IMPERIAL
  }

  @Test
  fun testCheckChange_toMetric() {
    val presenter = SettingsPresenter(view, preferences)

    presenter.onCheckedChanged(null, R.id.settings_metric_button)

    verify(preferences).unit = METRIC
  }

}


