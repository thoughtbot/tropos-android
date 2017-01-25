package com.thoughtbot.tropos.ui

import com.thoughtbot.tropos.viewmodels.ToolbarViewModel

sealed class ViewState(val toolbarViewModel: ToolbarViewModel) {

  class Loading(toolbarViewModel: ToolbarViewModel) : ViewState(toolbarViewModel)

  class Weather(toolbarViewModel: ToolbarViewModel,
      val weather: com.thoughtbot.tropos.data.Weather) : ViewState(toolbarViewModel)

  class Error(toolbarViewModel: ToolbarViewModel, val errorMessage: String) : ViewState(
      toolbarViewModel)

}

