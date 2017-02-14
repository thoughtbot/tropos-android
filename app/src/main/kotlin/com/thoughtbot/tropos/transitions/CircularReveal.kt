package com.thoughtbot.tropos.transitions

import android.R.attr
import android.animation.Animator
import android.content.Context
import android.graphics.Point
import android.support.annotation.IdRes
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.View.NO_ID
import android.view.ViewAnimationUtils
import android.view.ViewGroup


/**
 * The inspiration, and a good deal of the actual code for this class, came from https://github.com/nickbutcher/plaid
 **/
class CircularReveal : Visibility {

  var startRadius: Float = 0f
  var endRadius: Float = 0f
  @IdRes var centerOnId = NO_ID
  var centerOn: View? = null

  constructor() : super()

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onAppear(sceneRoot: ViewGroup, view: View?,
      startValues: TransitionValues,
      endValues: TransitionValues): Animator? {
    if (view == null || view.height === 0 || view.width === 0) return null
    val center = ensureCenterPoint(sceneRoot, view)
    val animator = NoPauseAnimator(ViewAnimationUtils.createCircularReveal(
        view,
        center.x,
        center.y,
        startRadius,
        maxRadius(center, view)))
    animator.duration = 500
    return animator
  }

  override fun onDisappear(sceneRoot: ViewGroup, view: View?,
      startValues: TransitionValues,
      endValues: TransitionValues): Animator? {
    if (view == null || view.height == 0 || view.width == 0) return null
    val center = ensureCenterPoint(sceneRoot, view)
    val animator = NoPauseAnimator(ViewAnimationUtils.createCircularReveal(
        view,
        center.x,
        center.y,
        maxRadius(center, view),
        endRadius))
    animator.duration = 500
    return animator
  }

  private fun ensureCenterPoint(sceneRoot: ViewGroup, view: View): Point {
    if (centerOn != null || centerOnId != NO_ID) {
      val source: View = centerOn ?: sceneRoot.findViewById(centerOnId)
      // use window location to allow views in diff hierarchies
      val loc = IntArray(2)
      source.getLocationInWindow(loc)
      val srcX = loc[0] + source.getWidth() / 2
      val srcY = loc[1] + source.getHeight() / 2
      view.getLocationInWindow(loc)
      return Point(srcX - loc[0], srcY - loc[1])
    }
    // else use the upper right hand corner where the action menu button lives
    return actionMenuLocation(view)
  }

  private fun maxRadius(center: Point, view: View): Float {
    return Math.hypot(Math.max(center.x, view.width - center.x).toDouble(),
        Math.max(center.y, view.height - center.y).toDouble()).toFloat()
  }

  private fun actionMenuLocation(view: View): Point {
    val attrs = intArrayOf(attr.actionBarSize)
    val actionBarSize = view.context.theme.obtainStyledAttributes(attrs).getDimension(0, 0F)
    val x = (view.right - (actionBarSize / 2)).toInt()
    val y = (actionBarSize / 2).toInt()
    return Point(x, y)
  }
}

