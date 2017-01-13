package com.thoughtbot.tropos.main

import com.thoughtbot.tropos.data.WeatherData

sealed class ViewState {

  class Uninitialized() : ViewState()

  class Loading(val toolbarViewModel: ToolbarViewModel) : ViewState()

  class Weather(val toolbarViewModel: ToolbarViewModel, val weather: List<WeatherData>) : ViewState()

  class Error(val errorMessage: String) : ViewState()

}

