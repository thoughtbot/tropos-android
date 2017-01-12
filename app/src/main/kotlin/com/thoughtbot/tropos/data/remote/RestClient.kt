package com.thoughtbot.tropos.data.remote

import com.google.gson.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.thoughtbot.tropos.BuildConfig
import retrofit2.Retrofit

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RestClient {

  fun <T> create(apiInterface: Class<T>): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(dateConverter()))
        .build()
    return retrofit.create(apiInterface)
  }

  private val baseUrl: HttpUrl = HttpUrl.Builder()
      .scheme("https")
      .host("api.forecast.io")
      .addPathSegment("forecast")
      .addPathSegment(BuildConfig.FORECAST_API_KEY)
      .addPathSegment("")
      .build()

  private val okHttpClient = OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor).build()

  private val loggingInterceptor: HttpLoggingInterceptor
    get() {
      val logging = HttpLoggingInterceptor()
      logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
      return logging
    }

  private fun dateConverter(): Gson {
    val builder = GsonBuilder()
    builder.registerTypeAdapter(Date::class.java,
        JsonDeserializer<java.util.Date> { json, typeOfT, context ->
          Date(json.asJsonPrimitive.asLong * 1000)
        })
    return builder.create()
  }
}
