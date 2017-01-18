package com.thoughtbot.tropos.refresh

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Property
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.thoughtbot.tropos.refresh.OverScroller.DragState
import com.thoughtbot.tropos.refresh.OverScroller.DragState.STATE_BOUNCE_BACK
import com.thoughtbot.tropos.refresh.OverScroller.DragState.STATE_DRAG_END_SIDE
import com.thoughtbot.tropos.refresh.OverScroller.DragState.STATE_DRAG_START_SIDE
import com.thoughtbot.tropos.refresh.OverScroller.DragState.STATE_IDLE
import com.thoughtbot.tropos.refresh.OverScroller.OverScrollDirection.BOTH
import com.thoughtbot.tropos.refresh.OverScroller.OverScrollDirection.END
import com.thoughtbot.tropos.refresh.OverScroller.OverScrollDirection.START
import com.thoughtbot.tropos.refresh.OverScroller.ScrollState

/**
 * This class was inspired by https://github.com/EverythingMe/overscroll-decor
 **/

abstract class OverScroller(
    val scroller: Scroller,
    val overScrollDirection: OverScrollDirection = BOTH,
    val decelerateFactor: Float = DEFAULT_DECELERATE_FACTOR,
    val touchDragRatioBck: Float = DEFAULT_TOUCH_DRAG_MOVE_RATIO_BCK,
    val touchDragRatioFwd: Float = DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD) : View.OnTouchListener {

  //initial attributes
  var initialPointerId: Int = 0
  var initialAbsOffset: Float = 0F
  var initialDir: Boolean = false // true = 'forward', false = 'backwards'

  // motion attributes
  var absOffset: Float = 0F
  var deltaOffset: Float = 0F
  var dir: Boolean = false // True = 'forward', false = 'backwards'
  var velocity: Float = 0F

  // animation attributes
  var animProperty: Property<View, Float>? = null
  var maxOffset: Float = 0F
  var bounceBackInterpolator = DecelerateInterpolator()

  // listeners
  var scrollListener: ScrollListener? = null

  // state variables
  var currentScrollState: ScrollState = ScrollState.IDLE
  var currentDragState: DragState = STATE_IDLE

  companion object {
    val MAX_BOUNCE_BACK_DURATION_MS: Int = 800
    val MIN_BOUNCE_BACK_DURATION_MS: Int = 200
    val DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD = 3f
    val DEFAULT_TOUCH_DRAG_MOVE_RATIO_BCK = 1f
    val DEFAULT_DECELERATE_FACTOR = -2f
  }

  enum class OverScrollDirection {
    START, END, BOTH
  }

  // TODO make this sealed inner class when following kotlin language bug is fixed:
  // https://youtrack.jetbrains.com/oauth?state=%2Fissue%2FKT-15788
  enum class ScrollState {
    IDLE, OVER_SCROLL, BOUNCE_BACK
  }

  enum class DragState {
    // No over-scroll is in-effect
    STATE_IDLE,

    // User is actively touch-dragging, thus enabling over-scroll at the view's *start* side
    STATE_DRAG_START_SIDE,

    // User is actively touch-dragging, thus enabling over-scroll at the view's *end* side
    STATE_DRAG_END_SIDE,

    // User has released their touch, thus throwing the view back into place via bounce-back animation
    STATE_BOUNCE_BACK;
  }


  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    when (event?.action) {
      MotionEvent.ACTION_MOVE -> return handleMoveTouchEvent(event)
      MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> return handleUpOrCancelTouchEvent(event)
    }
    return false
  }

  private fun handleMoveTouchEvent(event: MotionEvent?): Boolean {
    if (event != null) {
      return when (currentScrollState) {
        ScrollState.IDLE -> handleIdleMoveTouchEvent(event)
        ScrollState.OVER_SCROLL -> handleOverScrollMoveTouchEvent(event)
        ScrollState.BOUNCE_BACK -> handleBounceBackMoveTouchEvent(event)
      }
    } else {
      return false
    }
  }

  private fun handleUpOrCancelTouchEvent(event: MotionEvent?): Boolean {
    if (event != null) {
      return when (currentScrollState) {
        ScrollState.IDLE -> handleIdleUpOrCancelTouchEvent(event)
        ScrollState.OVER_SCROLL -> handleOverScrollUpOrCancelTouchEvent(event)
        ScrollState.BOUNCE_BACK -> handleBounceBackUpOrCancelTouchEvent(event)
      }
    } else {
      return false
    }
  }

  // idle events

  private fun handleIdleMoveTouchEvent(event: MotionEvent): Boolean {
    if (!setMotionAttributes(scroller.view, event)) {
      return false
    }

    if (hasOverScrollingBegun()) {
      // Save initial over-scroll attributes for future reference.
      initialPointerId = event.getPointerId(0)
      initialAbsOffset == absOffset
      initialDir = dir

      switchState(ScrollState.OVER_SCROLL)
      return handleOverScrollMoveTouchEvent(event)
    }
    return false
  }

  private fun handleIdleUpOrCancelTouchEvent(event: MotionEvent): Boolean {
    return false
  }

  // over scroll events

  private fun handleOverScrollMoveTouchEvent(event: MotionEvent): Boolean {
    // Switching 'pointers' (e.g. fingers) on-the-fly isn't supported -- abort over-scroll
    // smoothly using the default bounce-back animation in this case.
    if (initialPointerId != event.getPointerId(0)) {
      switchState(ScrollState.BOUNCE_BACK)
      return true
    }

    if (!setMotionAttributes(scroller.view, event)) {
      // Keep intercepting the touch event as long as we're still over-scrolling...
      return true
    }

    val deltaOffset = deltaOffset / if (dir == initialDir) touchDragRatioFwd else touchDragRatioBck
    val newOffset = absOffset + deltaOffset

    // If moved in counter direction onto a potential under-scroll state -- don't. Instead, abort
    // over-scrolling abruptly, thus returning control to which-ever touch handlers there
    // are waiting (e.g. regular scroller handlers).
    if (initialDir && !dir && newOffset <= initialAbsOffset || !initialDir && dir && newOffset >= initialAbsOffset) {
      translateViewAndEvent(scroller.view, initialAbsOffset, event)
      scrollListener?.onOverScroll(ScrollState.OVER_SCROLL, currentDragState, 0f)

      switchState(ScrollState.IDLE)
      return true
    }

    if (scroller.view.parent != null) {
      scroller.view.parent.requestDisallowInterceptTouchEvent(true)
    }

    val dt = event.eventTime - event.getHistoricalEventTime(0)
    if (dt > 0) {
      velocity = deltaOffset / dt
    }

    translateView(scroller.view, newOffset)
    scrollListener?.onOverScroll(ScrollState.BOUNCE_BACK, currentDragState, newOffset)

    return true
  }

  private fun handleOverScrollUpOrCancelTouchEvent(event: MotionEvent): Boolean {
    switchState(ScrollState.BOUNCE_BACK)
    return false
  }

  // bounce back events

  private fun handleBounceBackMoveTouchEvent(event: MotionEvent): Boolean {
    // Flush all touches down the drain till animation is over.
    return true
  }

  private fun handleBounceBackUpOrCancelTouchEvent(event: MotionEvent?): Boolean {
    //Flush all touches down the drain till animation is over
    return true
  }

  private fun hasOverScrollingBegun(): Boolean {
    return when (overScrollDirection) {
      START -> scroller.isInAbsoluteStart() && dir
      END -> scroller.isInAbsoluteEnd() && !dir
      BOTH -> (scroller.isInAbsoluteStart() && dir) || (scroller.isInAbsoluteEnd() && !dir)
    }
  }

  private fun switchState(toState: ScrollState) {
    currentScrollState = toState
    when (currentScrollState) {
      ScrollState.IDLE -> scrollListener?.onDragStateChange(currentDragState, STATE_IDLE)
      ScrollState.OVER_SCROLL -> {
        val oldDragState = currentDragState
        currentDragState = if (initialDir) STATE_DRAG_START_SIDE else STATE_DRAG_END_SIDE
        scrollListener?.onDragStateChange(oldDragState, currentDragState)
      }
      ScrollState.BOUNCE_BACK -> {
        scrollListener?.onDragStateChange(currentDragState, STATE_BOUNCE_BACK)

        val bounceBackAnim = bounceBackAnim()
        bounceBackAnim.addListener(bounceBackAnimationListener)

        bounceBackAnim.start()
      }
    }
  }

  private fun bounceBackAnim(): Animator {

    setAnimationAttributes(scroller.view)

    // Set up a low-duration slow-down animation IN the drag direction.

    // Exception: If wasn't dragging in 'forward' direction (or velocity=0 -- i.e. not dragging at all),
    // skip slow-down anim directly to the bounce-back.
    if (velocity === 0f || velocity < 0 && initialDir || velocity > 0 && !initialDir) {
      return bounceBackAnimator(absOffset)
    }

    // dt = (Vt - Vo) / a; Vt=0 ==> dt = -Vo / a
    var slowdownDuration = -velocity / decelerateFactor
    slowdownDuration = if (slowdownDuration < 0) 0F else slowdownDuration // Happens in counter-direction dragging

    // dx = (Vt^2 - Vo^2) / 2a; Vt=0 ==> dx = -Vo^2 / 2a
    val doubleDecelerateFactor = decelerateFactor * 2F
    val slowdownDistance = -velocity * velocity / doubleDecelerateFactor
    val slowdownEndOffset = absOffset + slowdownDistance

    val slowdownAnim = slowdownAnimator(scroller.view, slowdownDuration.toInt(),
        slowdownEndOffset)

    // Set up the bounce back animation, bringing the view back into the original, pre-overscroll position (translation=0).
    val bounceBackAnim = bounceBackAnimator(slowdownEndOffset)

    // Play the 2 animations as a sequence.
    val wholeAnim = AnimatorSet()
    wholeAnim.playSequentially(slowdownAnim, bounceBackAnim)
    return wholeAnim
  }

  private fun bounceBackAnimator(startOffset: Float): ObjectAnimator {
    // Duration is proportional to the view's size.
    val bounceBackDuration = Math.abs(
        startOffset) / maxOffset * MAX_BOUNCE_BACK_DURATION_MS
    val bounceBackAnim = ObjectAnimator.ofFloat(scroller.view, animProperty,
        initialAbsOffset)
    bounceBackAnim.duration = Math.max(bounceBackDuration.toInt(),
        MIN_BOUNCE_BACK_DURATION_MS).toLong()
    bounceBackAnim.interpolator = bounceBackInterpolator
    bounceBackAnim.addUpdateListener(bounceBackAnimationUpdateListener)
    return bounceBackAnim
  }

  private fun slowdownAnimator(view: View, slowdownDuration: Int,
      slowdownEndOffset: Float): ObjectAnimator {
    val slowdownAnim = ObjectAnimator.ofFloat(view, animProperty, slowdownEndOffset)
    slowdownAnim.duration = slowdownDuration.toLong()
    slowdownAnim.interpolator = bounceBackInterpolator
    slowdownAnim.addUpdateListener(bounceBackAnimationUpdateListener)
    return slowdownAnim
  }

  private val bounceBackAnimationUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
    scrollListener?.onOverScroll(ScrollState.BOUNCE_BACK, STATE_BOUNCE_BACK,
        animation.animatedValue as Float)
  }

  private val bounceBackAnimationListener = object : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator?) {
      switchState(ScrollState.IDLE)
    }
  }

  // this is not called by default on initialization in case calling class wants to manually
  // attach or use the `onTouch` independent of a `View.OnTouchListener`
  fun attach() {
    scroller.view.setOnTouchListener(this)
    scroller.view.overScrollMode = View.OVER_SCROLL_NEVER
  }

  fun detach() {
    scroller.view.setOnTouchListener(null)
    scroller.view.overScrollMode = View.OVER_SCROLL_ALWAYS
  }

  abstract fun translateView(view: View, offset: Float)
  abstract fun translateViewAndEvent(view: View, offset: Float, event: MotionEvent)
  abstract fun setMotionAttributes(view: View, event: MotionEvent): Boolean
  abstract fun setAnimationAttributes(view: View)
}

interface ScrollListener {
  fun onDragStateChange(oldState: DragState, newState: DragState)

  fun onOverScroll(state: ScrollState, dragState: DragState, offset: Float)
}

interface Scroller {
  val view: View
  fun isInAbsoluteStart(): Boolean
  fun isInAbsoluteEnd(): Boolean
}
