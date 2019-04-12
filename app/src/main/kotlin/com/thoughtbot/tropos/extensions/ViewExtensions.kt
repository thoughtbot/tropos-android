package com.thoughtbot.tropos.extensions

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

fun Int.lightenBy(amount: Float): Int {
  val red = ((Color.red(this) * (1 - amount) / 255 + amount) * 255).toInt()
  val green = ((Color.green(this) * (1 - amount) / 255 + amount) * 255).toInt()
  val blue = ((Color.blue(this) * (1 - amount) / 255 + amount) * 255).toInt()
  return Color.rgb(red, green, blue)
}

fun String.colorSubString(subString: String, color: Int): SpannableStringBuilder {
  val stringBuilder = SpannableStringBuilder(this)
  val colorSpan = ForegroundColorSpan(color)
  val start = this.indexOf(subString)
  val end = start + subString.length
  stringBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

  return stringBuilder
}

fun androidx.recyclerview.widget.RecyclerView.attachSnapHelper(snapHelper: androidx.recyclerview.widget.SnapHelper) {
  snapHelper.attachToRecyclerView(this)
}
