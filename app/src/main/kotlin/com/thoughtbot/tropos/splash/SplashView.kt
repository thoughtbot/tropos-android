package com.thoughtbot.tropos.splash

import android.content.Intent
import com.thoughtbot.tropos.commons.View

interface SplashView : View {
  fun navigate(intent: Intent)
}
