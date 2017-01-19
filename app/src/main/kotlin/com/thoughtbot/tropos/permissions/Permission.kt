package com.thoughtbot.tropos.permissions

interface Permission {

  val permission: String

  val permissionRequestCode: Int

  fun hasPermission(): Boolean

  fun requestPermission()
}

fun Permission.checkPermission(granted: () -> Unit, denied: () -> Unit, request: Boolean) {
  if (hasPermission()) {
    granted()
  } else {
    denied()
    if (request) requestPermission()
  }
}
