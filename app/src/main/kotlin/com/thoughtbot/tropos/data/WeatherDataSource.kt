package com.thoughtbot.tropos.data

import io.reactivex.Observable

interface WeatherDataSource {

  fun fetchWeather(): Observable<Weather>
}

