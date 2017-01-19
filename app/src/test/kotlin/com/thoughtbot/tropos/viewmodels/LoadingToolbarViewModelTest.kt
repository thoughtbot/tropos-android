package com.thoughtbot.tropos.viewmodels

import android.content.Context
import com.thoughtbot.tropos.BuildConfig
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.test.assertEquals

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class LoadingToolbarViewModelTest() {

  lateinit var context: Context

  @Before
  fun setup() {
    RuntimeEnvironment.application.let { context = it }
  }

  @Test
  fun testTitle() {
    val viewModel = LoadingToolbarViewModel(context)
    val expected = "Checking Weather…"
    val actual = viewModel.title()

    assertEquals(expected, actual)
  }

  @Test
  fun testSubtitle() {
    val viewModel = LoadingToolbarViewModel(context)
    val formattedDate = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
    val expected = "Updated at $formattedDate"
    val actual = viewModel.subtitle()

    assertEquals(expected, actual)
  }
}

