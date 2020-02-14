package com.thoughtbot.tropos.scrolling


import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.thoughtbot.tropos.scrolling.OverScroller.OverScrollDirection.END

class RecyclerViewScroller(override val view: RecyclerView) : Scroller {

  val layoutManager = view.layoutManager
  val orientation = (layoutManager as? LinearLayoutManager)?.orientation ?: (layoutManager as StaggeredGridLayoutManager).orientation

  init {
    if (layoutManager !is LinearLayoutManager && layoutManager !is StaggeredGridLayoutManager) {
      throw IllegalArgumentException("Recycler views with custom layout managers are not supported")
    }
  }

  override fun isInAbsoluteStart(): Boolean {
    if (orientation == LinearLayoutManager.HORIZONTAL) {
      return !view.canScrollHorizontally(-1)
    } else {
      return !view.canScrollVertically(-1)
    }
  }

  override fun isInAbsoluteEnd(): Boolean {
    if (orientation == LinearLayoutManager.HORIZONTAL) {
      return !view.canScrollHorizontally(1)
    } else {
      return !view.canScrollVertically(1)
    }
  }

}

fun RecyclerView.setVerticalEndOverScroller() {
  val overScroller = VerticalOverScroller(RecyclerViewScroller(this), END)
  addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
      overScroller.onTouch(rv, e)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
      return overScroller.onTouch(rv, e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
  })
}