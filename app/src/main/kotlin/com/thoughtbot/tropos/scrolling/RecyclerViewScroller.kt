package com.thoughtbot.tropos.scrolling

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.MotionEvent
import com.thoughtbot.tropos.scrolling.OverScroller.OverScrollDirection.END

class RecyclerViewScroller(override val view: androidx.recyclerview.widget.RecyclerView) : Scroller {

  val layoutManager = view.layoutManager
  val orientation = (layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.orientation ?: (layoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager).orientation

  init {
    if (layoutManager !is androidx.recyclerview.widget.LinearLayoutManager && layoutManager !is androidx.recyclerview.widget.StaggeredGridLayoutManager) {
      throw IllegalArgumentException("Recycler views with custom layout managers are not supported")
    }
  }

  override fun isInAbsoluteStart(): Boolean {
    if (orientation == androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL) {
      return !view.canScrollHorizontally(-1)
    } else {
      return !view.canScrollVertically(-1)
    }
  }

  override fun isInAbsoluteEnd(): Boolean {
    if (orientation == androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL) {
      return !view.canScrollHorizontally(1)
    } else {
      return !view.canScrollVertically(1)
    }
  }

}

fun androidx.recyclerview.widget.RecyclerView.setVerticalEndOverScroller() {
  val overScroller = VerticalOverScroller(RecyclerViewScroller(this), END)
  addOnItemTouchListener(object : OnItemTouchListener {
    override fun onTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent) {
      overScroller.onTouch(rv, e)
    }

    override fun onInterceptTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent): Boolean {
      return overScroller.onTouch(rv, e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
  })
}
