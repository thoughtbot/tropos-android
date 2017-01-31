# Tropos for Android [![CircleCI](https://circleci.com/gh/thoughtbot/tropos-android/tree/master.svg?style=svg&circle-token=6998942c05bf1870244c7cc7d9a71424480e5e49)](https://circleci.com/gh/thoughtbot/tropos-android/tree/master)

<a href='https://play.google.com/store/apps/details?id=com.thoughtbot.tropos'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=175/></a>

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

Contributing
------------

See the [CONTRIBUTING] document.
Thank you, [contributors]!

[CONTRIBUTING]: CONTRIBUTING.md
[contributors]: https://github.com/thoughtbot/tropos-android/graphs/contributors


License
-------

Tropos is Copyright (c) 2017 thoughtbot, inc. It is free software,
and may be redistributed under the terms specified in the [LICENSE] file.

[LICENSE]: /LICENSE

About
-----

![thoughtbot](https://thoughtbot.com/logo.png)

Tropos is maintained and funded by thoughtbot, inc.
The names and logos for thoughtbot are trademarks of thoughtbot, inc.

We love open source software!
See [our other projects][community]
or [hire us][hire] to help build your product.

[community]: https://thoughtbot.com/community?utm_source=github
[hire]: https://thoughtbot.com/hire-us?utm_source=github