package com.thoughtbot.tropos.extensions

import com.thoughtbot.tropos.data.Unit
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC

fun Int.convertTemperature(from: Unit, to: Unit): Int {
  when (from) {
    IMPERIAL -> return if (to == IMPERIAL) this else ((this - 32) / 1.8).toInt()
    METRIC -> return if (to == METRIC) this else (this * 1.8 + 32).toInt()
  }
}

fun Int.convertSpeed(from: Unit, to: Unit): Int {
  when (from) {
    IMPERIAL -> return if (to == IMPERIAL) this else (this * 1.609344).toInt()
    METRIC -> return if (to == METRIC) this else (this / 1.609344).toInt()
  }
}

