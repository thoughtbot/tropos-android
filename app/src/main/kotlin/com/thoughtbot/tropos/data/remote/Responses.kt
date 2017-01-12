package com.thoughtbot.tropos.data.remote

import java.util.Date

/*
 * RemoteForecast represents the response from the Dark Sky API
 */
data class RemoteForecast(
    val latitude: Double,
    val longitude: Double,
    val currently: DataPoint,
    val daily: DataBlock
)

/*
 * A data point object contains various properties, each representing the
 * average (unless otherwise specified) of a particular weather
 * phenomenon occurring *during* a period of time.
 */
data class DataPoint(
    val time: Date,
    val temperature: Double?,
    val temperatureMax: Double?,
    val temperatureMin: Double?,
    val icon: String,
    val summary: String?,
    val windSpeed: Double?,
    val windBearing: Double?
)
/*
 * A data block object represents the various weather phenomena
 * occurring *over* a period of time.
 */

data class DataBlock(
    val data: List<DataPoint>,
    val summary: String?,
    val icon: String
)
