package com.thoughtbot.tropos.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

  @GET("{latitude},{longitude}")
  fun fetchRemoteForecast(@Path("latitude") latitude: Double,
      @Path("longitude") longitude: Double): Observable<RemoteForecast>

  @GET("{latitude},{longitude},{time}")
  fun fetchRemoteForecast(@Path("latitude") latitude: Double,
      @Path("longitude") longitude: Double, @Path("time") time: Long): Observable<RemoteForecast>
}

