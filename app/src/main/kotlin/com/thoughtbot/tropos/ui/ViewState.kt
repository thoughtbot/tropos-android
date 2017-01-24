package com.thoughtbot.tropos.ui

import com.thoughtbot.tropos.data.Condition
import com.thoughtbot.tropos.viewmodels.ToolbarViewModel

sealed class ViewState(val toolbarViewModel: ToolbarViewModel) {

  class Loading(toolbarViewModel: ToolbarViewModel) : ViewState(toolbarViewModel)

  class Weather(toolbarViewModel: ToolbarViewModel, val weather: List<Condition>) : ViewState(toolbarViewModel)

  class Error(toolbarViewModel: ToolbarViewModel, val errorMessage: String) : ViewState(toolbarViewModel)

}

