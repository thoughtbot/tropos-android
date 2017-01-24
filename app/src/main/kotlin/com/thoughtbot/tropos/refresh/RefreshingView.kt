package com.thoughtbot.tropos.refresh

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener.Companion.INACTIVE
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener.Companion.PROGRESSING
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener.Companion.REFRESHING

class RefreshingView : FrameLayout, ProgressStateListener {

  private var refreshState: Int = INACTIVE
  val stripesOne: ImageView
  val stripesTwo: ImageView
  val progressMask: ImageView

  val stripeAnimator: Animator

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    LayoutInflater.from(context).inflate(R.layout.layout_refresh, this)
    stripesOne = findViewById(R.id.stripes_one) as ImageView
    stripesTwo = findViewById(R.id.stripes_two) as ImageView
    progressMask = findViewById(R.id.progress_mask) as ImageView

    setStripeBackgrounds()
    progressMask.background = ProgressCircleDrawable(context)
    stripeAnimator = stripesAnimator()

  }

  private fun setStripeBackgrounds() {
    val bitmap = DiagonalStripeView(context).asBitmap(screenWidth() * 2)
    val splits = bitmap.splitHorizontally()

    stripesOne.setImageBitmap(splits[0])
    stripesTwo.setImageBitmap(splits[1])
  }

  private fun screenWidth(): Int {
    val size = Point()
    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)
    return size.x
  }

  private fun DiagonalStripeView.asBitmap(width: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width/*width*/, 1000/*height*/, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
  }

  private fun Bitmap.splitHorizontally(): Array<Bitmap> {
    val center = width / 2
    val first = Bitmap.createBitmap(this, 0, 0, center, height)
    val second = Bitmap.createBitmap(this, center, 0, center, height)
    return arrayOf(first, second)
  }

  private fun stripesAnimator(): Animator {
    val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
    animator.repeatCount = ValueAnimator.INFINITE
    animator.interpolator = LinearInterpolator()
    animator.duration = 10000L
    animator.addUpdateListener { animation ->
      val progress = animation.animatedValue as Float
      val width = stripesOne.width
      val translationX = width * progress
      stripesOne.translationX = translationX
      stripesTwo.translationX = translationX - width
    }
    return animator
  }

  private fun showRefreshingState() {
    stripeAnimator.start()
    (progressMask.drawable as ProgressCircleDrawable).start()
  }

  private fun showProgressingState(percent: Float) {
    (progressMask.drawable as ProgressCircleDrawable).onProgress(percent)

    if (percent == 1f && !stripeAnimator.isRunning) {
      //animate stripes once circle is fully visible
      stripeAnimator.start()
    }

    if (percent < 1 && stripeAnimator.isRunning) {
      //stop animating stripes if circle isn't fully visible
      stripeAnimator.end()
    }
  }

  private fun showInactiveState() {
    stripeAnimator.end()
    (progressMask.drawable as ProgressCircleDrawable).stop()
  }

  // RefreshStateListener
  override fun onStateChanged(view: PullToRefreshLayout, state: Int) {
    refreshState = state
    when (state) {
      REFRESHING -> showRefreshingState()
      INACTIVE -> showInactiveState()
    }
  }

  override fun onProgress(percent: Float) {
    //only update if we are actually progressing
    if (refreshState == PROGRESSING) {
      showProgressingState(percent)
    }
  }
}

