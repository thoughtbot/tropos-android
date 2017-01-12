package com.thoughtbot.tropos.commons

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewBinder<M>(val function: (M) -> Unit) : ReadWriteProperty<Any, M> {
  private var mValue: M? = null

  override fun getValue(thisRef: Any, property: KProperty<*>): M {
    return mValue as M
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: M) {
    mValue = value
    function(value)
  }
}

