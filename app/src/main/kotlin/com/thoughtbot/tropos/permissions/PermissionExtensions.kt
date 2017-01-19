package com.thoughtbot.tropos.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log

fun Activity.getPermissionResults(permission: Permission, results: PermissionResults,
    requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

  if (requestCode == permission.permissionRequestCode) {
    val permissionIndex = permissions.indexOf(permission.permission)
    if (grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED) {
      results.onPermissionGranted()
    } else {
      val userSaidNever = !shouldShowRequestPermissionRationale(permission.permission)
      if (userSaidNever) {
        // user checked "never ask again"
        results.onPermissionDenied(true)
      } else {
        // we can show reasoning why
        results.onPermissionDenied(false)
      }
    }
  }
}

fun Activity.requestPermission(permission: Permission) {
  ActivityCompat.requestPermissions(this, arrayOf(permission.permission),
      permission.permissionRequestCode)
}
