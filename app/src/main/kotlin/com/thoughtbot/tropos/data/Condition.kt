package com.thoughtbot.tropos.data

import android.location.Location
import java.util.*

data class Condition(
    val date: Date,
    val summary: String,
    val location: Location,
    val status: Status,
    val windSpeed: Int,
    val windDirection: WindDirection,
    val lowTemp: Int,
    val currentTemp: Int,
    val highTemp: Int)
