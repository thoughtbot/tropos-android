package com.thoughtbot.tropos.data

import android.location.Location
import io.reactivex.Observable
import java.util.Date

interface ConditionDataSource {

  fun fetchCondition(forLocation: Location, forDate: Date): Observable<Condition>

  fun fetchForecast(forLocation: Location, forNumOfDaysFromToday: Int): Observable<List<Condition>>

}

