package com.thoughtbot.tropos.extensions

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasPermission(permission: String): Boolean {
  return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
  val editor = edit()
  editor.func()
  editor.apply()
}
