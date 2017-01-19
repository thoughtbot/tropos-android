package com.thoughtbot.tropos.scrolling

import android.view.MotionEvent
import android.view.View

class VerticalOverScroller(scroller: Scroller,
    direction: OverScrollDirection) : OverScroller(scroller, direction) {

  override fun translateView(view: View, offset: Float) {
    view.translationY = offset
  }

  override fun translateViewAndEvent(view: View, offset: Float, event: MotionEvent) {
    view.translationY = offset
    event.offsetLocation(offset - event.getY(0), 0f)
  }

  override fun setMotionAttributes(view: View, event: MotionEvent): Boolean {
    // We must have history available to calc the dx. Normally it's there - if it isn't temporarily,
    // we declare the event 'invalid' and expect it in consequent events.
    if (event.historySize == 0) {
      return false
    }

    // Allow for counter-orientation-direction operations (e.g. item swiping) to run fluently.
    val dy = event.getY(0) - event.getHistoricalY(0, 0)
    val dx = event.getX(0) - event.getHistoricalX(0, 0)
    if (Math.abs(dx) > Math.abs(dy)) {
      return false
    }

    absOffset = view.translationY
    deltaOffset = dy
    dir = deltaOffset > 0

    return true
  }

  override fun setAnimationAttributes(view: View) {
    animProperty = View.TRANSLATION_Y
    absOffset = view.translationY
    maxOffset = view.height.toFloat()
  }

}
