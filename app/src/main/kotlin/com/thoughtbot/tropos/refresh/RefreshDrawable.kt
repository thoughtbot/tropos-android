package com.thoughtbot.tropos.refresh

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Region
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener.Companion.INACTIVE
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener.Companion.PROGRESSING
import com.thoughtbot.tropos.refresh.PullToRefreshLayout.ProgressStateListener.Companion.REFRESHING


class RefreshDrawable(context: Context) : Drawable(), Drawable.Callback, ProgressStateListener {

  private var refreshState: Int = INACTIVE

  private val progressCircleLayer: ProgressCircleDrawable
  private val stripeBackgroundLayer: StripeDrawable
  private val layers: LayerDrawable

  init {
    val colors = initColors(context)
    stripeBackgroundLayer = StripeDrawable(colors)
    progressCircleLayer = ProgressCircleDrawable(context)

    val layers = arrayOf<Drawable>(stripeBackgroundLayer, progressCircleLayer)
    this.layers = LayerDrawable(layers)
    this.layers.callback = this
  }

  private fun initColors(context: Context): IntArray {
    val tangerine = ContextCompat.getColor(context, R.color.tangerine)
    val skyBlueLight = ContextCompat.getColor(context, R.color.sky_blue_light)
    val skyBlue = ContextCompat.getColor(context, R.color.sky_blue)
    val burntOrange = ContextCompat.getColor(context, R.color.burnt_orange)

    return intArrayOf(tangerine, skyBlueLight, skyBlue, burntOrange)
  }

  override fun draw(canvas: Canvas) {
    layers.draw(canvas)
  }

  override fun onBoundsChange(bounds: Rect) {
    layers.bounds = bounds
  }

  override fun setChangingConfigurations(configs: Int) {
    layers.changingConfigurations = configs
  }

  override fun getChangingConfigurations(): Int {
    return layers.changingConfigurations
  }

  override fun setDither(dither: Boolean) {
    layers.setDither(dither)
  }

  override fun setFilterBitmap(filter: Boolean) {
    layers.isFilterBitmap = filter
  }

  override fun setAlpha(alpha: Int) {
    layers.alpha = alpha
  }

  override fun setColorFilter(cf: ColorFilter?) {
    layers.colorFilter = cf
  }

  override fun isStateful(): Boolean {
    return layers.isStateful
  }

  override fun setState(stateSet: IntArray): Boolean {
    return layers.setState(stateSet)
  }

  override fun getState(): IntArray {
    return layers.state
  }

  override fun jumpToCurrentState() {
    DrawableCompat.jumpToCurrentState(layers)
  }

  override fun getCurrent(): Drawable {
    return layers.current
  }

  override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
    return super.setVisible(visible, restart) || layers.setVisible(visible, restart)
  }

  override fun getOpacity(): Int {
    return layers.opacity
  }

  override fun getTransparentRegion(): Region? {
    return layers.transparentRegion
  }

  override fun getIntrinsicWidth(): Int {
    return layers.intrinsicWidth
  }

  override fun getIntrinsicHeight(): Int {
    return layers.intrinsicHeight
  }

  override fun getMinimumWidth(): Int {
    return layers.minimumWidth
  }

  override fun getMinimumHeight(): Int {
    return layers.minimumHeight
  }

  override fun getPadding(padding: Rect): Boolean {
    return layers.getPadding(padding)
  }

  override fun invalidateDrawable(who: Drawable) {
    invalidateSelf()
  }

  override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
    scheduleSelf(what, `when`)
  }

  override fun unscheduleDrawable(who: Drawable, what: Runnable) {
    unscheduleSelf(what)
  }

  override fun onLevelChange(level: Int): Boolean {
    return layers.setLevel(level)
  }

  override fun setAutoMirrored(mirrored: Boolean) {
    DrawableCompat.setAutoMirrored(layers, mirrored)
  }

  override fun isAutoMirrored(): Boolean {
    return DrawableCompat.isAutoMirrored(layers)
  }

  override fun setTint(tint: Int) {
    DrawableCompat.setTint(layers, tint)
  }

  override fun setTintList(tint: ColorStateList?) {
    DrawableCompat.setTintList(layers, tint)
  }

  override fun setTintMode(tintMode: PorterDuff.Mode) {
    DrawableCompat.setTintMode(layers, tintMode)
  }

  override fun setHotspot(x: Float, y: Float) {
    DrawableCompat.setHotspot(layers, x, y)
  }

  override fun setHotspotBounds(left: Int, top: Int, right: Int, bottom: Int) {
    DrawableCompat.setHotspotBounds(layers, left, top, right, bottom)
  }

  private fun showRefreshingState() {
    stripeBackgroundLayer.start()
    progressCircleLayer.start()
  }

  private fun showProgressingState(percent: Float) {
    progressCircleLayer.onProgress(percent)

    if (percent == 1f && !stripeBackgroundLayer.isRunning) {
      //animate stripes once circle is fully visible
      stripeBackgroundLayer.start()
    }

    if (percent < 1 && stripeBackgroundLayer.isRunning) {
      //stop animating stripes if circle isn't fully visible
      stripeBackgroundLayer.stop()
    }
  }

  private fun showInactiveState() {
    stripeBackgroundLayer.stop()
    progressCircleLayer.stop()
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
