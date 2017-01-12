package com.thoughtbot.tropos.main

sealed class ViewState {

  class Uninitialized() : ViewState()

  class Loading() : ViewState()

  class Weather(val weatherViewModel: WeatherViewModel) : ViewState()

  class Error(val errorMessage: String) : ViewState()

}

