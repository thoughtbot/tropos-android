package com.thoughtbot.tropos.data

import android.location.Location
import io.reactivex.Observable

interface LocationDataSource {

  fun fetchLocation(): Observable<Location>
}

