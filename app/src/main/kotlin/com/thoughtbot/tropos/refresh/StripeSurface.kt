package com.thoughtbot.tropos.refresh

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.thoughtbot.tropos.R

class StripeSurface : SurfaceView {

  private val colors: IntArray
  private val paint: Paint = Paint()
  private var startPosition = 0

  companion object {
    private val COLUMN_WIDTH = 60
  }

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    colors = initColors(context)
    val thread = StripeThread(this)
    val surfaceHolder = holder

    surfaceHolder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
      }

      override fun surfaceDestroyed(holder: SurfaceHolder?) {
        var retry: Boolean = true
        thread.running = false
        while (retry) {
          try {
            thread.join()
            retry = false
          } catch (e: InterruptedException) {
          }
        }
      }

      override fun surfaceCreated(holder: SurfaceHolder?) {
        thread.running = true
        thread.start()
      }
    })
  }

  private fun initColors(context: Context): IntArray {
    val tangerine = ContextCompat.getColor(context, R.color.tangerine)
    val skyBlueLight = ContextCompat.getColor(context, R.color.sky_blue_light)
    val skyBlue = ContextCompat.getColor(context, R.color.sky_blue)
    val burntOrange = ContextCompat.getColor(context, R.color.burnt_orange)

    return intArrayOf(tangerine, skyBlueLight, skyBlue, burntOrange)
  }

  fun drawDiagonalLines(canvas: Canvas) {
    val numberOfColumns = canvas.width / COLUMN_WIDTH
    val columnWidth = (canvas.width / numberOfColumns).toFloat()

    for (index: Int in 0..numberOfColumns) {
      val colorIndex = startPosition / COLUMN_WIDTH % colors.size
      paint.color = colors[colorIndex]

      val left = columnWidth * index
      val right = columnWidth + (columnWidth * index)
      val bottom = canvas.height.toFloat()
      val top = 0f
      canvas.drawRect(left, top, right, bottom, paint)
      startPosition += COLUMN_WIDTH
    }
  }

}

class StripeThread(val stripeSurface: StripeSurface, var running: Boolean = false) : Thread() {

  override fun run() {
    super.run()
    while (running) {
      val canvas = stripeSurface.holder.lockCanvas()
      canvas?.let {
        synchronized(stripeSurface.holder) {
          stripeSurface.drawDiagonalLines(canvas)
        }
        stripeSurface.holder.unlockCanvasAndPost(canvas)
      }

      try {
        Thread.sleep(100)
      } catch (e: InterruptedException) {
        e.printStackTrace()
      }

    }
  }
}
