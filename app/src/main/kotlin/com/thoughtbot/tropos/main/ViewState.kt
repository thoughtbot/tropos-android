package com.thoughtbot.tropos.main

sealed class ViewState {

  class Uninitialized() : ViewState()

  class Loading(val toolbarViewModel: ToolbarViewModel) : ViewState()

  class Weather(val weatherViewModel: WeatherViewModel) : ViewState()

  class Error(val errorMessage: String) : ViewState()

}

