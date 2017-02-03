package com.thoughtbot.tropos.data

import android.location.Location
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.data.Icon.PARTLY_CLOUDY_DAY
import com.thoughtbot.tropos.data.TemperatureDifference.COLDER
import com.thoughtbot.tropos.data.TemperatureDifference.COOLER
import com.thoughtbot.tropos.data.TemperatureDifference.HOTTER
import com.thoughtbot.tropos.data.TemperatureDifference.SAME
import com.thoughtbot.tropos.data.TemperatureDifference.WARMER
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import java.util.Date
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class TemperatureDifferenceTest {

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

  @Test
  fun testGetTempDiff_SAME() {
    val today = mockCondition.copy(currentTemp = 0)
    val yesterday = mockCondition.copy(currentTemp = 0)

    val actual = TemperatureDifference(today, yesterday)
    val expected = SAME
    assertEquals(actual, expected)

  }

  @Test
  fun testGetTempDiff_WARMER() {
    val today = mockCondition.copy(currentTemp = 5)
    val yesterday = mockCondition.copy(currentTemp = 0)

    val actual = TemperatureDifference(today, yesterday)
    val expected = WARMER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_HOTTER() {
    val today = mockCondition.copy(currentTemp = 100)
    val yesterday = mockCondition.copy(currentTemp = 80)

    val actual = TemperatureDifference(today, yesterday)
    val expected = HOTTER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_COLDER() {
    val today = mockCondition.copy(currentTemp = 0)
    val yesterday = mockCondition.copy(currentTemp = 10)

    val actual = TemperatureDifference(today, yesterday)
    val expected = COLDER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_COOLER() {
    val today = mockCondition.copy(currentTemp = 0)
    val yesterday = mockCondition.copy(currentTemp = 9)

    val actual = TemperatureDifference(today, yesterday)
    val expected = COOLER
    assertEquals(actual, expected)
  }

}

