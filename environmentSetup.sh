# Environment Setup which is required for Circle CI

function copy_env_vars_to_gradle_properties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
      echo "Gradle Properties does not exist"

      echo "Creating Gradle Properties directory and file..."
      mkdir -p "$HOME/.gradle/"
      touch "$GRADLE_PROPERTIES"

      echo "Writing DARK_SKY_FORECAST_API_KEY to gradle.properties..."
      echo "DARK_SKY_FORECAST_API_KEY=$DARK_SKY_FORECAST_API_KEY_ENV_VAR" >> $GRADLE_PROPERTIES
    fi
}

function affirmative_android_update {
  echo y | android update sdk --no-ui --all --filter "$1"
}

function get_android_sdk_25 {
  # fix the CircleCI path
  # export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

  DEPS="$ANDROID_HOME/installed-dependencies"

  if [ ! -e $DEPS ]; then
    echo Fetch and install Android SDK 25
    echo y | android update sdk --no-ui --all --filter tools,platform-tools,build-tools-25.0.2,android-25,extra-google-m2repository,extra-google-google_play_services,extra-android-m2repository,extra-android-support
    # create the folder so we won't do this again (this is soooo Apache Ant right here)
    touch $DEPS
  fi
}
