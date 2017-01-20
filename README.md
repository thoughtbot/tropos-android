# Tropos for Android [![CircleCI](https://circleci.com/gh/thoughtbot/tropos-android.svg?style=svg)](https://circleci.com/gh/thoughtbot/tropos-android)
Weather and forecasts for humans.
Information you can act on.

Most weather apps throw a lot of information at you
but that doesn't answer the question of "What does it feel like outside?".
Tropos answers this by relating the current conditions
to conditions at the same time yesterday.

# API Keys
API keys are brought in at build time via gradle.properties file. Visit https://developer.forecast.io/
to obtain a developerAPI key. Then place this key in your machine's ~/.gradle/gradle.properties file:

`DARK_SKY_FORECAST_API_KEY=123abc`

# Testing
The `test` directory contains unit tests, using JUnit and Robolectric.