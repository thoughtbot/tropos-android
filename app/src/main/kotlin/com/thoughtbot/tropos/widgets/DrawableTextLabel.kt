package com.thoughtbot.tropos.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.thoughtbot.tropos.R


class DrawableTextLabel : ViewGroup {

  private val image: ImageView
  private val titleTextView: TextView
  private val spacing: Int
  private val imageHeight: Int
  private val imageWidth: Int

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    LayoutInflater.from(context).inflate(R.layout.label_drawable_text, this)
    image = findViewById(R.id.label_image) as ImageView
    titleTextView = findViewById(R.id.label_title) as TextView

    val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextLabel)

    val drawable: Drawable? = a.getDrawable(R.styleable.DrawableTextLabel_label_drawable)
    val title: String? = a.getString(R.styleable.DrawableTextLabel_label_title_text)

    spacing = a.getDimension(R.styleable.DrawableTextLabel_label_spacing, 0F).toInt()
    imageHeight = a.getDimension(R.styleable.DrawableTextLabel_label_drawable_height, -1F).toInt()
    imageWidth = a.getDimension(R.styleable.DrawableTextLabel_label_drawable_width, -1F).toInt()

    drawable?.let { setDrawable(it) }
    title?.let { setText(it) }

    a.recycle()
  }

  override fun checkLayoutParams(p: LayoutParams?): Boolean {
    return p is MarginLayoutParams
  }

  override fun generateDefaultLayoutParams(): LayoutParams {
    return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
  }

  override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
    return MarginLayoutParams(context, attrs)
  }

  override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
    return generateDefaultLayoutParams()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var widthUsed: Int = 0
    var heightUsed: Int = 0

    // Image goes to the left: measure and reserve horizontal space
    measureChildWithMargins(image, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
    widthUsed += widthWithMargins(image, getImageWidth())

    // Use remaining space center the TextView vertically
    var textWidth: Int = 0
    measureChildWithMargins(titleTextView, widthMeasureSpec, widthUsed, heightMeasureSpec,
        heightUsed)
    heightUsed += heightWithMargins(titleTextView, titleTextView.measuredHeight)
    textWidth = Math.max(textWidth, widthWithMargins(titleTextView, titleTextView.measuredWidth))

    widthUsed += textWidth

    // handle the case where the image is taller than the TextView
    heightUsed = Math.max(heightWithMargins(image, getImageHeight()), heightUsed)

    widthUsed += paddingLeft + paddingRight
    heightUsed += paddingTop + paddingBottom

    setMeasuredDimension(resolveSize(widthUsed, widthMeasureSpec),
        resolveSize(heightUsed, heightMeasureSpec))
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var x = paddingLeft
    var y = paddingTop

    val innerHeight = height - paddingTop - paddingBottom

    // center image vertically
    val dy = Math.max(0, (innerHeight - heightWithMargins(image, getImageHeight())) / 2)
    placeChild(image, getImageWidth(), getImageHeight(), x, y + dy)

    x += widthWithMargins(image, getImageWidth())

    // center text
    var textHeight = 0
    textHeight += heightWithMargins(titleTextView, titleTextView.measuredHeight)

    y += Math.max(0, (innerHeight - textHeight) / 2)

    placeChild(titleTextView, titleTextView.measuredWidth, titleTextView.measuredHeight,
        x + spacing, y)
  }

  private fun placeChild(child: View, width: Int, height: Int, left: Int, top: Int) {
    val lp: MarginLayoutParams = child.layoutParams as MarginLayoutParams
    child.layout(left + lp.leftMargin, top + lp.topMargin, left + lp.leftMargin + width,
        top + lp.topMargin + height)
  }

  private fun getImageHeight(): Int {
    return if (this.imageHeight == -1) this.image.measuredHeight else this.imageHeight
  }

  private fun getImageWidth(): Int {
    return if (this.imageWidth == -1) this.image.measuredWidth else this.imageWidth
  }

  private fun widthWithMargins(child: View, width: Int): Int {
    val lp: MarginLayoutParams = child.layoutParams as MarginLayoutParams
    return width + lp.leftMargin + lp.rightMargin
  }

  private fun heightWithMargins(child: View, height: Int): Int {
    val lp: MarginLayoutParams = child.layoutParams as MarginLayoutParams
    return height + lp.topMargin + lp.bottomMargin
  }

  fun setDrawable(image: Drawable) {
    this.image.setImageDrawable(image)
  }

  fun setDrawable(@DrawableRes drawableResId: Int) {
    this.image.setImageResource(drawableResId)
  }

  fun setText(caption: String) {
    this.titleTextView.text = caption
  }

}

