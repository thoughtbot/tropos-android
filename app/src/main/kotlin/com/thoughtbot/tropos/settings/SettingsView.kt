package com.thoughtbot.tropos.settings

import com.thoughtbot.tropos.commons.View

interface SettingsView : View {

  fun checkUnitPreference(preferenceId: Int)

}


