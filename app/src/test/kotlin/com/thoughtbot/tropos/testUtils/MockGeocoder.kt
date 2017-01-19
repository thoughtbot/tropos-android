package com.thoughtbot.tropos.testUtils

import android.location.Address
import android.location.Geocoder
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.maps.ShadowGeocoder
import java.util.Locale


@Implements(Geocoder::class)
class MockGeocoder : ShadowGeocoder() {

  @Implementation
  override fun getFromLocation(latitude: Double, longitude: Double, maxResults: Int): List<Address> {
    val address: Address = Address(Locale.ENGLISH)
    address.locality = "San Francisco"
    return listOf(address)
  }

}


