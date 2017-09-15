package com.thoughtbot.tropos.viewmodels

import android.content.Context
import com.thoughtbot.tropos.BuildConfig
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.testUtils.FakeCondition
import com.thoughtbot.tropos.testUtils.MockGeocoder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class WeatherToolbarViewModelTest() {

  private lateinit var context: Context
  private val fakeCondition: Condition = FakeCondition.getInstance()

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  @Config(shadows = arrayOf(MockGeocoder::class))
  fun testTitle() {
    val viewModel = WeatherToolbarViewModel(context, fakeCondition)
    val expected = "San Francisco"
    val actual = viewModel.title()

    assertEquals(expected, actual)
  }

  @Test
  fun testSubtitle() {
    val viewModel = WeatherToolbarViewModel(context, fakeCondition)
    val expected = "Updated at 4:16 PM"
    val actual = viewModel.subtitle()

    assertEquals(expected, actual)
  }
}
