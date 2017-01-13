package com.thoughtbot.tropos.refresh

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.Path.FillType
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region.Op
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.thoughtbot.tropos.R

class ProgressCircleDrawable(context: Context) : Drawable(), Animatable {

  companion object {
    private val STROKE_WIDTH = 10f
    private val CIRCLE_RADIUS = 50f
  }

  private val MASK_COLOR: Int
  private val paint: Paint
  private var angle: Float = 0F
  private val circle: Path
  private var bounds: RectF? = null
  private var centerX: Float = 0F
  private var centerY: Float = 0F
  private var radius: Float = 0F
  private var isRunning: Boolean = false
  private var animator: ValueAnimator? = null

  init {
    MASK_COLOR = ContextCompat.getColor(context, R.color.secondary_background)
    paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = MASK_COLOR
    paint.style = Style.FILL
    circle = Path()
    circle.fillType = FillType.INVERSE_WINDING
    radius = CIRCLE_RADIUS
  }

  override fun draw(canvas: Canvas) {

    canvas.save()

    //add circle to path
    circle.reset()
    circle.addCircle(centerX, centerY, radius + STROKE_WIDTH, Direction.CW)
    circle.close()

    //write the outer rectangular bounds of that circle to #bounds
    circle.computeBounds(bounds, true)

    //put a rectangular hole in the current clip
    canvas.clipRect(bounds!!, Op.DIFFERENCE)

    //fill everything but the clip with mask color
    canvas.drawColor(MASK_COLOR)

    //restore full canvas clip for any subsequent operations
    canvas.restore()

    //draw circle inside clipped rectangle bounds
    canvas.drawPath(circle, paint)

    //inset bounds and draw inner progress circle
    if (!isRunning) {
      bounds!!.inset(STROKE_WIDTH, STROKE_WIDTH)
      canvas.drawArc(bounds!!, -90f, angle, true, paint)
    }
  }

  override fun onBoundsChange(bounds: Rect) {
    super.onBoundsChange(bounds)
    this.bounds = RectF()
    centerX = bounds.centerX().toFloat()
    centerY = bounds.centerY().toFloat()
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun setColorFilter(colorFilter: ColorFilter?) {

  }

  override fun getOpacity(): Int {
    return 0
  }

  fun onProgress(percent: Float) {
    val ringHeight = bounds!!.height() + STROKE_WIDTH * 2
    val currentHeight = getBounds().height().toFloat()
    val offset = currentHeight - ringHeight
    val max = currentHeight / percent

    // only start rotating if the entire circle is visible
    if (currentHeight > ringHeight) {
      angle = offset / (max - ringHeight) * 360 - 360
    } else {
      angle = 360f
    }
    invalidateSelf()
  }

  override fun start() {
    isRunning = true
    animator = ValueAnimator.ofFloat(CIRCLE_RADIUS + STROKE_WIDTH, 1080f)
    animator!!.duration = 200
    animator!!.addUpdateListener { animation ->
      radius = animation.animatedValue as Float
      invalidateSelf()
    }
    animator!!.start()
  }

  override fun stop() {
    if (animator != null) {
      animator!!.end()
    }
    radius = CIRCLE_RADIUS
    isRunning = false
  }

  override fun isRunning(): Boolean {
    return isRunning
  }

}
