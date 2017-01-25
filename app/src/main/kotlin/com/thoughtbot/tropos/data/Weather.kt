package com.thoughtbot.tropos.data

import android.os.Parcel
import android.os.Parcelable
import com.thoughtbot.tropos.extensions.createParcel

data class Weather(val yesterday: Condition, val today: Condition,
    val nextThreeDays: List<Condition>) : Parcelable {

  companion object {
    @JvmField val CREATOR = createParcel(::Weather)
  }

  constructor(source: Parcel) : this(
      source.readParcelable<Condition>(Condition::class.java.classLoader),
      source.readParcelable<Condition>(Condition::class.java.classLoader),
      source.createTypedArrayList(Condition.CREATOR))

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeParcelable(yesterday, 0)
    dest?.writeParcelable(today, 0)
    dest?.writeTypedList(nextThreeDays)
  }
}

