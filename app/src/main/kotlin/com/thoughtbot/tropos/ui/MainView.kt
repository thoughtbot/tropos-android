package com.thoughtbot.tropos.ui

import com.thoughtbot.tropos.commons.View
import com.thoughtbot.tropos.ui.ViewState

interface MainView : View {

  var viewState: ViewState

}

