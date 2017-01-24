package com.thoughtbot.tropos.viewmodels

import android.content.Context
import com.thoughtbot.tropos.R.string
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.iconResId
import java.text.SimpleDateFormat

class ForecastViewModel(val context: Context, val condition: Condition) {

  val icon: Int = condition.status.iconResId()

  val day: String = SimpleDateFormat("EEE").format(condition.date)

  val highTemp = context.getString(string.temperature, condition.highTemp)

  val lowTemp = context.getString(string.temperature, condition.lowTemp)
}

