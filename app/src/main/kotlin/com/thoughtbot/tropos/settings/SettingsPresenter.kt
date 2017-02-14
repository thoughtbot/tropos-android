package com.thoughtbot.tropos.settings

import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.Presenter
import com.thoughtbot.tropos.commons.WebviewActivity
import com.thoughtbot.tropos.data.Preferences
import com.thoughtbot.tropos.data.Unit
import com.thoughtbot.tropos.data.Unit.IMPERIAL
import com.thoughtbot.tropos.data.Unit.METRIC
import com.thoughtbot.tropos.data.local.LocalPreferences

class SettingsPresenter(override val view: SettingsView,
    val preferences: Preferences = LocalPreferences(view.context))
  : Presenter, OnCheckedChangeListener {

  fun init() {
    view.checkUnitPreference(preferences.unit.resId())
  }

  fun onPrivacyClicked() {
    val privacyUrl = "http://troposweather.com/privacy"
    val intent = WebviewActivity.createIntent(view.context, privacyUrl)
    view.context.startActivity(intent)
  }

  override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
    when (checkedId) {
      R.id.settings_imperial_button -> preferences.unit = IMPERIAL
      R.id.settings_metric_button -> preferences.unit = METRIC
    }
  }

  private fun Unit.resId(): Int {
    return when (this) {
      IMPERIAL -> R.id.settings_imperial_button
      METRIC -> R.id.settings_metric_button
    }
  }

}


