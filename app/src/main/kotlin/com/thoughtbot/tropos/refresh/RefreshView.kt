package com.thoughtbot.tropos.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView

/**
 * Wrapper class created to work around issues with AnimationListeners being
 * called before the animation is actually complete :/
 **/
class RefreshView : ImageView {

  var listener: Animation.AnimationListener? = null
  private var mostRecentAnimation: Animation? = null

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  override fun onAnimationEnd() {
    super.onAnimationEnd()
    listener?.onAnimationEnd(mostRecentAnimation)
    setLayerType(View.LAYER_TYPE_NONE, null)
  }

  override fun onAnimationStart() {
    super.onAnimationStart()
    listener?.onAnimationStart(animation)
    mostRecentAnimation = animation
    setLayerType(View.LAYER_TYPE_HARDWARE, null)
  }
}

