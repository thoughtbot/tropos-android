package com.thoughtbot.tropos.data.local

import android.content.Context
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.Unit
import com.thoughtbot.tropos.extensions.edit
import org.jetbrains.anko.defaultSharedPreferences

class LocalPreferences(val context: Context) : Preferences {

  val UNIT = "unit"

  override var unit: Unit
    get() {
      //default to Imperial
      return Unit.values()[context.defaultSharedPreferences.getInt(UNIT, 0)]
    }
    set(value) {
      context.defaultSharedPreferences.edit { putInt(UNIT, value.ordinal) }
    }
}

