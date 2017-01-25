package com.thoughtbot.tropos.refresh

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.SystemClock

class StripeDrawable(context: Context) : Drawable(), Animatable, Runnable, Drawable.Callback {

  private val DURATION = 1000  // in ms
  private val FRAME_DELAY = (1000 / 60).toLong() // 60 fps

  private var running = false
  private var startTime: Long = 0

  private var stripes: DiagonalStripeView = DiagonalStripeView(context)
  private var bitmap: Bitmap = stripes.asBitmap()
  private var paint: Paint = Paint()

  override fun draw(canvas: Canvas) {
    val bounds = bounds
    if (isRunning) {
      //animation in progress

      val save = canvas.save()
      canvas.clipRect(bounds)

      val timeDiff = SystemClock.uptimeMillis() - startTime
      val progress = timeDiff.toFloat() / DURATION.toFloat() // 0..1
      val xPos = (0 - progress * stripes.blockWidth)

      canvas.drawBitmap(bitmap, xPos, 0F, paint)
      canvas.restoreToCount(save)
    } else {
      canvas.drawBitmap(bitmap, 0F, 0F, paint)
    }
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun setColorFilter(colorFilter: ColorFilter?) {

  }

  override fun getOpacity(): Int {
    return 0
  }

  override fun start() {
    if (isRunning) {
      stop()
    }
    running = true
    startTime = SystemClock.uptimeMillis()
    invalidateSelf()
    scheduleSelf(this, startTime + FRAME_DELAY)
  }

  override fun stop() {
    unscheduleSelf(this)
    running = false
  }

  override fun isRunning(): Boolean {
    return running
  }

  override fun run() {
    invalidateSelf()
    val uptimeMillis = SystemClock.uptimeMillis()
    if (uptimeMillis + FRAME_DELAY < startTime + DURATION) {
      scheduleSelf(this, uptimeMillis + FRAME_DELAY)
    } else {
      running = false
      start()
    }
  }

  override fun invalidateDrawable(who: Drawable) {
    val callback = callback
    callback?.invalidateDrawable(this)
  }

  override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
    val callback = callback
    callback?.scheduleDrawable(this, what, `when`)
  }

  override fun unscheduleDrawable(who: Drawable, what: Runnable) {
    val callback = callback
    callback?.unscheduleDrawable(this, what)
  }
}
