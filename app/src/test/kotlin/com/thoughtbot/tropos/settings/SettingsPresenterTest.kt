package com.thoughtbot.tropos.settings

import android.content.Context
import android.location.Location
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Icon
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC
import com.thoughtbot.tropos.data.Weather
import com.thoughtbot.tropos.data.WindDirection
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Date

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class SettingsPresenterTest() {

  lateinit var context: Context
  val view = mock<SettingsView>()
  val preferences = mock<Preferences>()

  val mockCondition: Condition = {
    val timeStamp = 1484180189 * 1000L // equivalent to Wed Jan 11 16:16:29 PST 2017
    val date = Date(timeStamp)
    val summary = "Mostly Cloudy"
    val location = Location("")
    location.longitude = -122.4375671
    location.latitude = 37.8032493
    val icon = Icon.PARTLY_CLOUDY_DAY
    val windSpeed = 4
    val windDirection = WindDirection(171.0)
    val unit = IMPERIAL
    val lowTemp = 48
    val highTemp = 54
    val temp = 52

    Condition(date, summary, location, icon, windSpeed, windDirection, unit, lowTemp, temp,
        highTemp)
  }()

  val mockWeather: Weather = Weather(mockCondition, mockCondition,
      listOf(mockCondition, mockCondition, mockCondition))

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


