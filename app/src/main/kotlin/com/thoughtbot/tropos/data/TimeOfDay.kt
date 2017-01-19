package com.thoughtbot.tropos.data

import com.thoughtbot.tropos.data.TimeOfDay.AFTERNOON
import com.thoughtbot.tropos.data.TimeOfDay.DAY
import com.thoughtbot.tropos.data.TimeOfDay.MORNING
import com.thoughtbot.tropos.data.TimeOfDay.NIGHT
import java.util.Date

enum class TimeOfDay {
  MORNING, DAY, AFTERNOON, NIGHT;
}

fun TimeOfDay(date: Date): TimeOfDay {
  val hour = date.hours
  return when (hour) {
    in 0..4 -> NIGHT
    in 4..9 -> MORNING
    in 9..14 -> DAY
    in 14..17 -> AFTERNOON
    else -> NIGHT
  }
}
