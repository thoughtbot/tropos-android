package com.thoughtbot.tropos

import android.location.Location
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.TemperatureDifference
import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.extensions.TemperatureDifference
import com.thoughtbot.tropos.extensions.WindDirection
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import java.util.Date
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class TemperatureDifferenceTest {

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

  @Test
  fun testGetTempDiff_SAME() {
    //Test SAME
    val todayWD = mockWeatherData.copy(currentTemp = 0)
    val yesterdayWD = mockWeatherData.copy(currentTemp = 0)

    val actual = TemperatureDifference(todayWD, yesterdayWD)
    val expected = TemperatureDifference.SAME
    assertEquals(actual, expected)

  }

  @Test
  fun testGetTempDiff_WARMER() {
    val todayWD = mockWeatherData.copy(currentTemp = 5)
    val yesterdayWD = mockWeatherData.copy(currentTemp = 0)

    val actual = TemperatureDifference(todayWD, yesterdayWD)
    val expected = TemperatureDifference.WARMER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_HOTTER() {
    val todayWD = mockWeatherData.copy(currentTemp = 100)
    val yesterdayWD = mockWeatherData.copy(currentTemp = 80)

    val actual = TemperatureDifference(todayWD, yesterdayWD)
    val expected = TemperatureDifference.HOTTER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_COLDER() {
    val todayWD = mockWeatherData.copy(currentTemp = 0)
    val yesterdayWD = mockWeatherData.copy(currentTemp = 10)

    val actual = TemperatureDifference(todayWD, yesterdayWD)
    val expected = TemperatureDifference.COLDER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_COOLER() {
    val todayWD = mockWeatherData.copy(currentTemp = 0)
    val yesterdayWD = mockWeatherData.copy(currentTemp = 9)

    val actual = TemperatureDifference(todayWD, yesterdayWD)
    val expected = TemperatureDifference.COOLER
    assertEquals(actual, expected)
  }

}

