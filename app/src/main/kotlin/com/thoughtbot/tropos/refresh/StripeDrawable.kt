package com.thoughtbot.tropos.refresh

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.SystemClock

class StripeDrawable(
    private val colors: IntArray) : Drawable(), Animatable, Runnable, Drawable.Callback {

  private val COLUMN_WIDTH = 60
  private val DURATION = 1000  // in ms
  private val FRAME_DELAY = (1000 / 60).toLong() // 60 fps

  private var running = false
  private var startTime: Long = 0

  private val paint: Paint

  init {
    paint = Paint()
    paint.strokeWidth = COLUMN_WIDTH.toFloat()
  }

  override fun draw(canvas: Canvas) {
    val bounds = bounds
    if (isRunning) {
      //animation in progress

      val save = canvas.save()
      canvas.clipRect(bounds)

      val timeDiff = SystemClock.uptimeMillis() - startTime
      val progress = timeDiff.toFloat() / DURATION.toFloat() // 0..1
      val width = COLUMN_WIDTH * colors.size

      val xPos = (0 - progress * width).toInt()

      drawDiagonalLines(canvas, xPos)
      canvas.restoreToCount(save)
    } else {
      //draw normal
      drawDiagonalLines(canvas, 0)
    }
  }

  private fun drawDiagonalLines(canvas: Canvas, startPosition: Int) {
    //subtract column width to ensure lines fill entire screen
    val start = startPosition - COLUMN_WIDTH
    val width = canvas.width - start + COLUMN_WIDTH

    val x = start
    var y = 0
    while (y - width < width + COLUMN_WIDTH) {
      val colorIndex = y / COLUMN_WIDTH % colors.size
      paint.color = colors[colorIndex]
      canvas.drawLine(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y - width).toFloat(), paint)
      y += COLUMN_WIDTH
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
