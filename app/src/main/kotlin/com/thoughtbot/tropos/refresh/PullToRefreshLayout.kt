package com.thoughtbot.tropos.refresh

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation
import android.widget.AbsListView
import android.widget.ImageView

class PullToRefreshLayout : ViewGroup {

  companion object {
    private val INVALID_POINTER = -1
    private val DRAG_RATE = .5f
    private val DRAG_MAX_DISTANCE = 64
    private val TO_START_ANIMATION_DURATION = 200
    private val TO_EXPANDED_ANIMATION_DURATION = 400
    private val DECELERATE_INTERPOLATION_FACTOR = 2f
  }

  interface ProgressStateListener {

    companion object {
      val INACTIVE = 0
      val PROGRESSING = 2
      val REFRESHING = 1
    }

    fun onStateChanged(view: PullToRefreshLayout, state: Int)

    fun onProgress(percent: Float)
  }

  interface RefreshListener {
    fun onRefresh()
  }

  //views
  private var target: View? = null
  private var refreshView: ImageView? = null

  //touch event
  private var activePointerId: Int = 0
  private val touchSlop: Int
  private var isBeingDragged: Boolean = false
  private var lastDownY: Float = 0.toFloat()
  private var lastScrollState: Int = 0

  //scroll / drag
  private var maxDragDistance = -1f
  private var currentOffsetTop: Int = 0
  private var expandedPositionOffset = 25

  //refreshing
  private var isRefreshing = false
  private val decelerateInterpolator: DecelerateInterpolator

  //listeners
  var stateListener: ProgressStateListener? = null
  var refreshListener: RefreshListener? = null

  //nested scrolling
  private var totalUnconsumed: Float = 0.toFloat()
  private val nestedScrollingParentHelper: NestedScrollingParentHelper
  private val nestedScrollingChildHelper: NestedScrollingChildHelper
  private val parentScrollConsumed = IntArray(2)
  private val parentOffsetInWindow = IntArray(2)
  private var nestedScrollInProgress: Boolean = false

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    createProgressView()
    touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    val metrics = resources.displayMetrics
    maxDragDistance = DRAG_MAX_DISTANCE * metrics.density
    decelerateInterpolator = DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR)

    nestedScrollingParentHelper = NestedScrollingParentHelper(this)
    nestedScrollingChildHelper = NestedScrollingChildHelper(this)
    isNestedScrollingEnabled = true
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    if (childCount == 0) {
      return
    }

    if (target == null) {
      ensureTarget()
    }
    if (target == null) {
      return
    }
    val height = measuredHeight
    val width = measuredWidth
    val left = paddingLeft
    val top = paddingTop
    val right = paddingRight
    val bottom = paddingBottom

    target?.let {
      it.layout(left, top + it.top, left + width - right, top + height - bottom + it.top)
    }
    refreshView?.layout(left, top, left + width - right, currentOffsetTop)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var widthMeasureSpec = widthMeasureSpec
    var heightMeasureSpec = heightMeasureSpec
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    if (target == null) {
      ensureTarget()
    }
    if (target == null) {
      return
    }
    widthMeasureSpec = makeMeasureSpec(measuredWidth - paddingRight - paddingLeft, EXACTLY)
    heightMeasureSpec = makeMeasureSpec(measuredHeight - paddingTop - paddingBottom, EXACTLY)

    target?.measure(widthMeasureSpec, heightMeasureSpec)
    refreshView?.measure(widthMeasureSpec, heightMeasureSpec)
  }

  private fun createProgressView() {
    refreshView = ImageView(context)
    addView(refreshView, 0)
  }

  private fun ensureTarget() {
    if (target == null) {
      for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child != refreshView) {
          target = child
          break
        }
      }
    }
  }

  override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
    return isEnabled
        && !isRefreshing
        && nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
  }

  override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
    // Reset the counter of how much leftover scroll needs to be consumed.
    nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes)
    // Dispatch up to the nested parent
    startNestedScroll(axes and ViewCompat.SCROLL_AXIS_VERTICAL)
    totalUnconsumed = 0f
    nestedScrollInProgress = true
  }

  override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
    // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
    // before allowing the list to scroll
    if (dy > 0 && totalUnconsumed > 0) {
      if (dy > totalUnconsumed) {
        consumed[1] = dy - totalUnconsumed.toInt()
        totalUnconsumed = 0f
      } else {
        totalUnconsumed -= dy.toFloat()
        consumed[1] = dy
      }
      onDrag(totalUnconsumed)
    }

    // Now let our nested parent consume the leftovers
    val parentConsumed = parentScrollConsumed
    if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
      consumed[0] += parentConsumed[0]
      consumed[1] += parentConsumed[1]
    }
  }

  override fun getNestedScrollAxes(): Int {
    return nestedScrollingParentHelper.nestedScrollAxes
  }

  override fun onStopNestedScroll(target: View) {
    nestedScrollingParentHelper.onStopNestedScroll(target)
    nestedScrollInProgress = false
    // Finish the spinner for nested scrolling if we ever consumed any
    // unconsumed nested scroll
    if (totalUnconsumed > 0) {
      onRelease(totalUnconsumed)
      totalUnconsumed = 0f
    }
    // Dispatch up our nested parent
    stopNestedScroll()
  }

  override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int,
      dxUnconsumed: Int, dyUnconsumed: Int) {
    // Dispatch up to the nested parent first
    dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, parentOffsetInWindow)

    // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
    // sometimes between two nested scrolling views, we need a way to be able to know when any
    // nested scrolling parent has stopped handling events. We do that by using the
    // 'offset in window 'functionality to see if we have been moved from the event.
    // This is a decent indication of whether we should take over the event stream or not.
    val dy = dyUnconsumed + parentOffsetInWindow[1]
    if (dy < 0) {
      totalUnconsumed += Math.abs(dy).toFloat()
      onDrag(totalUnconsumed)
    }
  }

  override fun setNestedScrollingEnabled(enabled: Boolean) {
    nestedScrollingChildHelper.isNestedScrollingEnabled = enabled
  }

  override fun isNestedScrollingEnabled(): Boolean {
    return nestedScrollingChildHelper.isNestedScrollingEnabled
  }

  override fun startNestedScroll(axes: Int): Boolean {
    return nestedScrollingChildHelper.startNestedScroll(axes)
  }

  override fun stopNestedScroll() {
    nestedScrollingChildHelper.stopNestedScroll()
  }

  override fun hasNestedScrollingParent(): Boolean {
    return nestedScrollingChildHelper.hasNestedScrollingParent()
  }

  override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
      dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
    return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed, offsetInWindow)
  }

  override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?,
      offsetInWindow: IntArray?): Boolean {
    return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
  }

  override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
    return dispatchNestedPreFling(velocityX, velocityY)
  }

  override fun onNestedFling(target: View, velocityX: Float, velocityY: Float,
      consumed: Boolean): Boolean {
    return dispatchNestedFling(velocityX, velocityY, consumed)
  }

  override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
    return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
  }

  override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
    return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
  }

  private fun getMotionEventY(ev: MotionEvent, activePointerId: Int): Float {
    val index = MotionEventCompat.findPointerIndex(ev, activePointerId)
    if (index < 0) {
      return -1f
    }
    return MotionEventCompat.getY(ev, index)
  }

  private fun onSecondaryPointerUp(event: MotionEvent) {
    val pointerIndex = MotionEventCompat.getActionIndex(event)
    val pointerId = MotionEventCompat.getPointerId(event, pointerIndex)
    if (pointerId == activePointerId) {
      // This was our active pointer going up. Choose a new
      // active pointer and adjust accordingly.
      val newPointerIndex = if (pointerIndex == 0) 1 else 0
      activePointerId = MotionEventCompat.getPointerId(event, newPointerIndex)
    }
  }

  private fun calculateOverscroll(event: MotionEvent): Float {
    val y = getMotionEventY(event, activePointerId)
    if (y == -1f) {
      return -1f
    }
    return (y - lastDownY) * DRAG_RATE
  }

  private fun canTargetViewScrollUp(): Boolean {
    if (android.os.Build.VERSION.SDK_INT < 14) {
      if (target is AbsListView) {
        val absListView = target as AbsListView
        return absListView.childCount > 0 && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(
            0).top < absListView.paddingTop)
      } else {
        return ViewCompat.canScrollVertically(target, -1) || target?.scrollY ?: 0 > 0
      }
    } else {
      return ViewCompat.canScrollVertically(target, -1)
    }
  }

  /*
   * This method JUST determines whether we want to intercept the motion.
   * If we return true, onTouchEvent will be called and we do the actual
   * scrolling there.
   */
  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    val action = MotionEventCompat.getActionMasked(ev)

    if (canTargetViewScrollUp() || nestedScrollInProgress) {
      return false
    }

    when (action) {
      MotionEvent.ACTION_DOWN -> {
        activePointerId = MotionEventCompat.getPointerId(ev, 0)
        isBeingDragged = false

        val downY = getMotionEventY(ev, activePointerId)
        if (downY == -1f) {
          return false
        }
        lastDownY = downY
      }
      MotionEvent.ACTION_MOVE -> {
        if (activePointerId == INVALID_POINTER) {
          return false
        }

        val y = getMotionEventY(ev, activePointerId)
        if (y == -1f) {
          return false
        }

        //calculate difference from initial down to now
        val dy = y - lastDownY

        // If the user has dragged their finger vertically more than
        // the touch slop, start the scroll
        if (dy > touchSlop && !isBeingDragged) {
          isBeingDragged = true
          lastDownY = y + touchSlop
          return true
        }
      }
      MotionEvent.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        isBeingDragged = false
        activePointerId = INVALID_POINTER
      }
    }

    return isBeingDragged
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val action = MotionEventCompat.getActionMasked(event)
    when (action) {
      MotionEvent.ACTION_DOWN -> {
        activePointerId = MotionEventCompat.getPointerId(event, 0)
        isBeingDragged = false
      }
      MotionEvent.ACTION_MOVE -> {
        val overscroll = calculateOverscroll(event)
        if (overscroll > 0 && !isRefreshing) {
          onDrag(overscroll)
        } else {
          return false
        }
      }
      MotionEvent.ACTION_POINTER_DOWN -> {
        val pointerIndex = MotionEventCompat.getActionIndex(event)
        if (pointerIndex < 0) {
          return false
        }
        //reset our activePointerId
        activePointerId = MotionEventCompat.getPointerId(event, pointerIndex)
      }
      MotionEvent.ACTION_POINTER_UP -> onSecondaryPointerUp(event)
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        val overscroll = calculateOverscroll(event)
        if (overscroll == -1f) {
          return false
        }
        onRelease(overscroll)
        isBeingDragged = false
        activePointerId = INVALID_POINTER
        return false
      }
    }
    return true
  }

  override fun requestDisallowInterceptTouchEvent(b: Boolean) {
    // if this is a List < L or another view that doesn't support nested
    // scrolling, ignore this request so that the vertical scroll event
    // isn't stolen
    if (android.os.Build.VERSION.SDK_INT < 21 && target is AbsListView || target != null && !ViewCompat.isNestedScrollingEnabled(
        target)) {
      // Nope.
    } else {
      super.requestDisallowInterceptTouchEvent(b)
    }
  }

  private fun onDrag(overscroll: Float) {
    val targetOffset = calculateTargetOffset(overscroll)
    offsetTarget(targetOffset - currentOffsetTop, true)
    reportRefreshStateChange(ProgressStateListener.PROGRESSING)
  }

  private fun onRelease(overscroll: Float) {
    if (overscroll > maxDragDistance) {
      setRefreshing(true)
    } else {
      // cancel refresh
      isRefreshing = false
      animateOffsetToStartPosition()
    }
  }

  private fun calculateTargetOffset(overscroll: Float): Int {
    val originalDragPercent = overscroll / maxDragDistance
    val dragPercent = Math.min(1f, Math.abs(originalDragPercent))
    val extraOS = Math.abs(overscroll) - maxDragDistance
    val slingshotDist = maxDragDistance
    val tensionSlingshotPercent = Math.max(0f, Math.min(extraOS, slingshotDist * 2) / slingshotDist)
    val tensionPercent = (tensionSlingshotPercent / 4 - Math.pow(
        (tensionSlingshotPercent / 4).toDouble(), 2.0)).toFloat() * 2f
    val extraMove = slingshotDist * tensionPercent * 2f

    return (slingshotDist * dragPercent + extraMove).toInt()
  }

  private fun offsetTarget(offset: Int, requiresUpdate: Boolean) {
    target?.offsetTopAndBottom(offset)
    updateHeightOfRefreshView(offset.toFloat())
    currentOffsetTop = target?.top ?: 0
    if (stateListener != null) {
      stateListener?.onProgress(offsetAsPercent(currentOffsetTop.toFloat()))
    }

    if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
      invalidate()
    }
  }

  private fun updateHeightOfRefreshView(offset: Float) {
    val params = refreshView?.layoutParams
    params?.height = offset.toInt()
    refreshView?.layoutParams = params
  }

  private fun offsetAsPercent(overscrollTop: Float): Float {
    val originalDragPercent = overscrollTop / maxDragDistance
    return Math.min(1f, Math.abs(originalDragPercent))
  }

  private fun calculateDistanceToStartPosition(interpolatedTime: Float): Int {
    val targetTop = currentOffsetTop - (currentOffsetTop * interpolatedTime)
    val roundedTop = Math.floor(targetTop.toDouble()).toInt()
    return roundedTop - (target?.top ?: 0)
  }

  private fun calculateDistanceToExpandedPosition(interpolatedTime: Float): Int {
    val targetTop = currentOffsetTop + ((expandedPositionOffset - currentOffsetTop) * interpolatedTime)
    val roundedTop = Math.floor(targetTop.toDouble()).toInt()
    return roundedTop - (target?.top ?: 0)
  }

  private fun animateOffsetToStartPosition() {
    toStartPositionAnimation.reset()
    toStartPositionAnimation.duration = TO_START_ANIMATION_DURATION.toLong()
    toStartPositionAnimation.interpolator = decelerateInterpolator
    toStartPositionAnimation.setAnimationListener(toStartListener)
    target?.clearAnimation()
    target?.startAnimation(toStartPositionAnimation)
  }

  private fun animateOffsetToExpandedPosition() {
    toExpandedPositionAnimation.reset()
    toExpandedPositionAnimation.duration = TO_EXPANDED_ANIMATION_DURATION.toLong()
    toExpandedPositionAnimation.interpolator = decelerateInterpolator
    toExpandedPositionAnimation.setAnimationListener(refreshingListener)
    target?.clearAnimation()
    target?.startAnimation(toExpandedPositionAnimation)
  }

  private val toStartPositionAnimation = object : Animation() {
    public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
      val offset = calculateDistanceToStartPosition(interpolatedTime)
      offsetTarget(offset, false)
    }
  }

  private val toExpandedPositionAnimation = object : Animation() {
    public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
      val offset = calculateDistanceToExpandedPosition(interpolatedTime)
      offsetTarget(offset, false)
    }
  }

  private val toStartListener = object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation) {}

    override fun onAnimationRepeat(animation: Animation) {}

    override fun onAnimationEnd(animation: Animation) {
      reportRefreshStateChange(ProgressStateListener.INACTIVE)
    }
  }

  private val refreshingListener = object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation) {}

    override fun onAnimationRepeat(animation: Animation) {}

    override fun onAnimationEnd(animation: Animation) {
      if (!isRefreshing) {
        offsetTarget(-currentOffsetTop, true)
      }
    }
  }

  /**
   * Fires an "on refresh state changed" event to the registered
   * [RefreshStateListener], if any. The state change
   * is fired only if the specified state is different from the previously known state.

   * @param newState The new refresh state.
   */
  private fun reportRefreshStateChange(newState: Int) {
    if (newState != lastScrollState) {
      lastScrollState = newState
      stateListener?.onStateChanged(this, newState)
      if (newState == ProgressStateListener.REFRESHING) {
        refreshListener?.onRefresh()
      }
    }
  }

  fun setRefreshing(refreshing: Boolean) {
    if (isRefreshing != refreshing) {
      isRefreshing = refreshing
      if (isRefreshing) {
        animateOffsetToExpandedPosition()
        reportRefreshStateChange(ProgressStateListener.REFRESHING)
      } else {
        animateOffsetToStartPosition()
      }
    }
  }

  fun setRefreshingDrawable(drawable: Drawable) {
    if (refreshView != null) {
      refreshView?.setImageDrawable(drawable)
    }
    if (drawable is ProgressStateListener) {
      stateListener = drawable
    }
  }
}
