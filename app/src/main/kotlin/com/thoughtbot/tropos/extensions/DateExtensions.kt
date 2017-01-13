package com.thoughtbot.tropos.extensions

import java.util.Date

fun Date.dayBefore(): Date {
  val newTime = this.time - 24 * 60 * 60 * 1000
  return Date(newTime)
}

