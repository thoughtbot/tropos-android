package com.thoughtbot.tropos.ui

import com.thoughtbot.tropos.data.WeatherData
import com.thoughtbot.tropos.viewmodels.ToolbarViewModel

sealed class ViewState(val toolbarViewModel: ToolbarViewModel) {

  class Loading(toolbarViewModel: ToolbarViewModel) : ViewState(toolbarViewModel)

  class Weather(toolbarViewModel: ToolbarViewModel, val weather: List<WeatherData>) : ViewState(toolbarViewModel)

  class Error(toolbarViewModel: ToolbarViewModel, val errorMessage: String) : ViewState(toolbarViewModel)

}

