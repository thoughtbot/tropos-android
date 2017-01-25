package com.thoughtbot.tropos.data

data class Weather(val yesterday: Condition, val today: Condition,
    val nextThreeDays: List<Condition>)

