package com.thoughtbot.tropos.viewmodels

import android.content.Context
import android.location.Location
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.data.Icon.PARTLY_CLOUDY_DAY
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Unit
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.WindDirection
import com.thoughtbot.tropos.testUtils.MockGeocoder
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
class WeatherToolbarViewModelTest() {

  lateinit var context: Context

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
  @Config(shadows = arrayOf(MockGeocoder::class))
  fun testTitle() {
    val viewModel = WeatherToolbarViewModel(context, mockCondition)
    val expected = "San Francisco"
    val actual = viewModel.title()

    assertEquals(expected, actual)
  }

  @Test
  fun testSubtitle() {
    val viewModel = WeatherToolbarViewModel(context, mockCondition)
    val expected = "Updated at 4:16 PM"
    val actual = viewModel.subtitle()

    assertEquals(expected, actual)
  }
}
