package com.thoughtbot.tropos.main

import com.thoughtbot.tropos.commons.View
import com.thoughtbot.tropos.main.ViewState

interface MainView : View {

  var viewState: ViewState

}

