package com.thoughtbot.tropos.data.remote

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.thoughtbot.tropos.data.LocationDataSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocationService(val context: Context) : LocationDataSource {

  val locationRequest: LocationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setNumUpdates(1)

  override fun fetchLocation(): Observable<Location> {
    val rxLocation = RxLocation(context)
    //TODO checkAndHandleResolution is not working
    return rxLocation.settings().checkAndHandleResolution(locationRequest)
        .flatMapObservable { hasPermission ->
          if (hasPermission) {
            return@flatMapObservable rxLocation.location().updates(locationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
          } else {
            return@flatMapObservable rxLocation.location().lastLocation()
                .flatMapObservable { Observable.just(it) }
          }
        }
  }
}


