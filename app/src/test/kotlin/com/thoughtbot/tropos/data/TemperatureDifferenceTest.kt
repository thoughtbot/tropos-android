package com.thoughtbot.tropos.data

import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.data.TemperatureDifference.COLDER
import com.thoughtbot.tropos.data.TemperatureDifference.COOLER
import com.thoughtbot.tropos.data.TemperatureDifference.HOTTER
import com.thoughtbot.tropos.data.TemperatureDifference.SAME
import com.thoughtbot.tropos.data.TemperatureDifference.WARMER
import com.thoughtbot.tropos.testUtils.fakeCondition
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class TemperatureDifferenceTest {
  @Test
  fun testGetTempDiff_SAME() {
    val today = fakeCondition(currentTemp = 0)
    val yesterday = fakeCondition(currentTemp = 0)

    val actual = TemperatureDifference(today, yesterday)
    val expected = SAME
    assertEquals(actual, expected)

  }

  @Test
  fun testGetTempDiff_WARMER() {
    val today = fakeCondition(currentTemp = 5)
    val yesterday = fakeCondition(currentTemp = 0)

    val actual = TemperatureDifference(today, yesterday)
    val expected = WARMER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_HOTTER() {
    val today = fakeCondition(currentTemp = 100)
    val yesterday = fakeCondition(currentTemp = 80)

    val actual = TemperatureDifference(today, yesterday)
    val expected = HOTTER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_COLDER() {
    val today = fakeCondition(currentTemp = 0)
    val yesterday = fakeCondition(currentTemp = 10)

    val actual = TemperatureDifference(today, yesterday)
    val expected = COLDER
    assertEquals(actual, expected)
  }

  @Test
  fun testGetTempDiff_COOLER() {
    val today = fakeCondition(currentTemp = 0)
    val yesterday = fakeCondition(currentTemp = 9)

    val actual = TemperatureDifference(today, yesterday)
    val expected = COOLER
    assertEquals(actual, expected)
  }

}

