package com.thoughtbot.tropos.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import com.thoughtbot.tropos.extensions.hasPermission

class LocationPermission(val context: Context) : Permission {

  override val permission: String = Manifest.permission.ACCESS_COARSE_LOCATION

  override val permissionRequestCode: Int = 123

  override fun hasPermission(): Boolean = context.hasPermission(permission)

  override fun requestPermission() {
    (context as? Activity)?.requestPermission(this)
  }

}

