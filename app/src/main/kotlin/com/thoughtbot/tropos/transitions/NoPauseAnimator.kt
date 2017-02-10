package com.thoughtbot.tropos.transitions

import android.animation.Animator
import android.animation.TimeInterpolator

/**
 * https://halfthought.wordpress.com/2014/11/07/reveal-transition/
 *
 * Interrupting Activity transitions can yield an OperationNotSupportedException when the
 * transition tries to pause the animator. rip. We can fix this by wrapping the
 * Animator and not calling any onPause or onResume functions
 */
class NoPauseAnimator(private val animator: Animator) : Animator() {

  override fun getStartDelay(): Long {
    return animator.startDelay
  }

  override fun setStartDelay(startDelay: Long) {
    animator.startDelay = startDelay
  }

  override fun setDuration(duration: Long): Animator {
    animator.duration = duration
    return this
  }

  override fun getDuration(): Long {
    return animator.duration
  }

  override fun setInterpolator(value: TimeInterpolator) {
    animator.interpolator = value
  }

  override fun isRunning(): Boolean {
    return animator.isRunning
  }

  override fun start() {
    animator.start()
  }

  override fun cancel() {
    animator.cancel()
  }

  override fun addListener(listener: Animator.AnimatorListener) {
    animator.addListener(listener)
  }

  override fun removeAllListeners() {
    animator.removeAllListeners()
  }

  override fun removeListener(listener: Animator.AnimatorListener) {
    animator.removeListener(listener)
  }
}
