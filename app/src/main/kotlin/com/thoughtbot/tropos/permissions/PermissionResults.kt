package com.thoughtbot.tropos.permissions

interface PermissionResults {

  fun onPermissionGranted()

  fun onPermissionDenied(userSaidNever: Boolean)
}

