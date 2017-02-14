package com.thoughtbot.tropos.data

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.thoughtbot.tropos.extensions.createParcel
import java.util.*

data class Condition(
    val date: Date,
    val summary: String,
    val location: Location,
    val icon: Icon,
    val windSpeed: Int,
    val windDirection: WindDirection,
    val unit: Unit,
    val lowTemp: Int,
    val currentTemp: Int,
    val highTemp: Int) : Parcelable {

  companion object {
    @JvmField val CREATOR = createParcel(::Condition)
  }

  constructor(source: Parcel) : this(
      Date(source.readLong()),
      source.readString(),
      source.readParcelable<Location>(Location::class.java.classLoader),
      Icon.values()[source.readInt()],
      source.readInt(),
      WindDirection.values()[source.readInt()],
      Unit.values()[source.readInt()],
      source.readInt(),
      source.readInt(),
      source.readInt())

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeLong(date.time)
    dest?.writeString(summary)
    dest?.writeParcelable(location, 0)
    dest?.writeInt(icon.ordinal)
    dest?.writeInt(windSpeed)
    dest?.writeInt(windDirection.ordinal)
    dest?.writeInt(unit.ordinal)
    dest?.writeInt(lowTemp)
    dest?.writeInt(currentTemp)
    dest?.writeInt(highTemp)
  }
}
