package com.thoughtbot.tropos.scrolling

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import android.view.View

/**
 * Implementation of {@link LinearSnapHelper} that is designed to work exclusively
 * with {@link WeatherAdapter} and its' attached {@link RecyclerView}
 *
 * The implementation will snap to either completely show or hide the {@link ForecastViewHolder}
 * depending on how much of it is visible on when user releases their touch
 **/
class WeatherSnapHelper : androidx.recyclerview.widget.LinearSnapHelper() {

  override fun findSnapView(layoutManager: LayoutManager?): View? {
    layoutManager as androidx.recyclerview.widget.LinearLayoutManager

    val lastChildIndex = layoutManager.findLastVisibleItemPosition()
    if (lastChildIndex == androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
      return null
    }

    val lastChild = layoutManager.findViewByPosition(lastChildIndex)
    val orientationHelper = androidx.recyclerview.widget.OrientationHelper.createVerticalHelper(layoutManager)
    val lastChildStart = orientationHelper.getDecoratedStart(lastChild)
    val lastChildHeight = orientationHelper.getDecoratedMeasurement(lastChild)
    val recyclerViewHeight = layoutManager.height

    //amount of the the forecast (last child) height that is visible on screen
    val visibleHeight = recyclerViewHeight - lastChildStart

    //if more than half of the forecast is visible, snap to it, else snap to the weather
    if (visibleHeight > (lastChildHeight / 2)) {
      return lastChild
    } else {
      return layoutManager.findViewByPosition(0)
    }
  }

  override fun calculateDistanceToFinalSnap(layoutManager: LayoutManager, target: View): IntArray? {
    val out = IntArray(2)
    out[0] = 0

    val targetViewType = layoutManager.getItemViewType(target)
    val weatherViewType = layoutManager.getItemViewType(layoutManager.findViewByPosition(0)!!)
    val orientationHelper = androidx.recyclerview.widget.OrientationHelper.createVerticalHelper(layoutManager)

    if (targetViewType == weatherViewType) {
      // snap to show weather
      out[1] = orientationHelper.getDecoratedStart(target) - orientationHelper.startAfterPadding
    } else {
      // snap to show forecasts
      out[1] = orientationHelper.getDecoratedEnd(target) - orientationHelper.endAfterPadding
    }
    return out
  }

}
