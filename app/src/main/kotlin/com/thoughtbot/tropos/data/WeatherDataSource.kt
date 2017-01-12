package com.thoughtbot.tropos.data

import android.location.Location
import io.reactivex.Observable
import java.util.Date

interface WeatherDataSource {

  fun fetchWeather(forLocation: Location, forDate: Date): Observable<WeatherData>

  fun fetchForecast(forLocation: Location, forNumOfDaysFromToday: Int): Observable<List<WeatherData>>

}

