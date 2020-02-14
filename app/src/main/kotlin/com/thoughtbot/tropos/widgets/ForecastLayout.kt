package com.thoughtbot.tropos.widgets

import android.content.Context
import androidx.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.thoughtbot.tropos.R

class ForecastLayout : LinearLayout {
  private val icon: ImageView
  private val day: TextView
  private val highTemp: TextView
  private val lowTemp: TextView

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    LayoutInflater.from(context).inflate(R.layout.layout_forecast, this)
    icon = findViewById(R.id.image_forecast_icon) as ImageView
    day = findViewById(R.id.text_forecast_day) as TextView
    highTemp = findViewById(R.id.text_forecast_high) as TextView
    lowTemp = findViewById(R.id.text_forecast_low) as TextView
  }


  fun setIcon(@DrawableRes imageId: Int) {
    this.icon.setImageResource(imageId)
  }

  fun setDay(day: String) {
    this.day.text = day
  }

  fun setHighTemp(temp: String) {
    this.highTemp.text = temp
  }

  fun setLowTemp(temp: String) {
    this.lowTemp.text = temp
  }
}


