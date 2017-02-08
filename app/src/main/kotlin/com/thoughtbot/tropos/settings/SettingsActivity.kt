package com.thoughtbot.tropos.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.settings_privacy_policy
import kotlinx.android.synthetic.main.activity_settings.settings_unit_radio_group
import org.jetbrains.anko.find

class SettingsActivity : BaseActivity(), SettingsView {

  val presenter: SettingsPresenter by lazy { SettingsPresenter(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)

    val toolbar = find<Toolbar>(R.id.settings_toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

    settings_unit_radio_group.setOnCheckedChangeListener(presenter)
    settings_privacy_policy.setOnClickListener { presenter.onPrivacyClicked() }

    presenter.init()
  }

  override fun onSupportNavigateUp(): Boolean {
    finish()
    return false
  }

  override val context: Context = this

  override fun checkUnitPreference(preferenceId: Int) {
    settings_unit_radio_group.check(preferenceId)
  }

}

