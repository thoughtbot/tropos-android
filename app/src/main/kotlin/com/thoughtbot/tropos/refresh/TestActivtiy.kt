package com.thoughtbot.tropos.refresh

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.BaseActivity
import kotlinx.android.synthetic.main.activity_test.stripes_1
import kotlinx.android.synthetic.main.activity_test.stripes_2


class TestActivtiy : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_test)

    val stripes1 = stripes_1
    val stripes2 = stripes_2


//    val bitmap = DiagonalStripeView(this).asBitmap(screenWidth() * 2)
//    val splits = bitmap.splitHorizontally()
//
//    stripes_1.setImageBitmap(splits[0])
//    stripes_2.setImageBitmap(splits[1])
//
//    val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
//    animator.repeatCount = ValueAnimator.INFINITE
//    animator.interpolator = LinearInterpolator()
//    animator.duration = 10000L
//    animator.addUpdateListener { animation ->
//      val progress = animation.animatedValue as Float
//      val width = stripes1.width
//      val translationX = width * progress
//      stripes1.translationX = translationX
//      stripes2.translationX = translationX - width
//    }
//    animator.start()
  }

}

