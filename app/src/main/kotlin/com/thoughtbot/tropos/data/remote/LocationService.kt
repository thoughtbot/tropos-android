package com.thoughtbot.tropos.data.remote

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.thoughtbot.tropos.data.LocationDataSource
import io.reactivex.Observable

class LocationService(val context: Context) : LocationDataSource {

  val locationRequest: LocationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setNumUpdates(1)

  override fun fetchLocation(): Observable<Location> {
    val location = Location("")
    location.latitude = 37.7885901
    location.longitude = -122.4000474
    return Observable.just(location)

//    val rxLocation = RxLocation(context)
//    //TODO checkAndHandleResolution is not working
//    return rxLocation.settings().checkAndHandleResolution(locationRequest)
//        .flatMapObservable { hasPermission ->
//          if (hasPermission) {
//            return@flatMapObservable rxLocation.location().updates(locationRequest)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//          } else {
//            return@flatMapObservable rxLocation.location().lastLocation()
//                .flatMapObservable { Observable.just(it) }
//          }
//        }
  }
}


