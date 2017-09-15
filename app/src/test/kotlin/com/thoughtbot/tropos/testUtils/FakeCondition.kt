package com.thoughtbot.tropos.testUtils

import android.location.Location
import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.data.Icon
import com.thoughtbot.tropos.data.Unit
import com.thoughtbot.tropos.data.WindDirection
import java.util.*


class FakeCondition {
    companion object {
        fun getInstance(): Condition {
            val cal = Calendar.getInstance()
            cal.set(2017, 0, 11, 16, 16, 29)
            val date = cal.getTime()
            val summary = "Mostly Cloudy"
            val location = Location("")
            location.longitude = -122.4375671
            location.latitude = 37.8032493
            val icon = Icon.PARTLY_CLOUDY_DAY
            val windSpeed = 4
            val windDirection = WindDirection(171.0)
            val unit = Unit.IMPERIAL
            val lowTemp = 48
            val highTemp = 54
            val temp = 52

            return Condition(date, summary, location, icon, windSpeed, windDirection, unit, lowTemp, temp,
                    highTemp)
        }
    }
}