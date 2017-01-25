package com.thoughtbot.tropos.refresh

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import com.thoughtbot.tropos.R

class DiagonalStripeView : View {

  private val COLUMN_WIDTH = 60
  private val paint: Paint = Paint()
  private val colors: IntArray
  val blockWidth: Int

  init {
    paint.strokeWidth = COLUMN_WIDTH.toFloat()
    colors = makeColors()
    blockWidth = COLUMN_WIDTH * colors.size
  }

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

  override fun onDraw(canvas: Canvas?) {
    canvas?.let { drawDiagonalLines(canvas) }
  }

  private fun drawDiagonalLines(canvas: Canvas) {
    //subtract column width to ensure lines fill entire screen
    val start = -COLUMN_WIDTH
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

  private fun makeColors(): IntArray {
    val tangerine = ContextCompat.getColor(context, R.color.tangerine)
    val skyBlueLight = ContextCompat.getColor(context, R.color.sky_blue_light)
    val skyBlue = ContextCompat.getColor(context, R.color.sky_blue)
    val burntOrange = ContextCompat.getColor(context, R.color.burnt_orange)

    return intArrayOf(tangerine, skyBlueLight, skyBlue, burntOrange)
  }
}

fun DiagonalStripeView.asBitmap(): Bitmap {
  val size = Point()
  (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)
  val width = size.x * 2

  val bitmap = Bitmap.createBitmap(width/*width*/, 1000/*height*/, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  draw(canvas)
  return bitmap
}

