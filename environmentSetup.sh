# Environment Setup which is required for Circle CI

function copy_env_vars_to_gradle_properties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
      echo "Gradle Properties does not exist"

      echo "Creating Gradle Properties directory and file..."
      mkdir -p "$HOME/.gradle/"
      touch "$GRADLE_PROPERTIES"

      echo "Writing API_BASE_URL_PROD to gradle.properties..."
      echo "API_BASE_URL_PROD=$API_BASE_URL_PROD" >> "$GRADLE_PROPERTIES"
      echo "API_BASE_URL_DEV=$API_BASE_URL_DEV" >> "$GRADLE_PROPERTIES"
    fi
}

function affirmative_android_update {
  echo y | android update sdk --no-ui --all --filter "$1"
}

function get_android_sdk_24 {
  # fix the CircleCI path
  # export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

  DEPS="$ANDROID_HOME/installed-dependencies"

  if [ ! -e $DEPS ]; then
    echo Fetch and install Android SDK 24
    echo y | android update sdk --no-ui --all --filter tools,platform-tools,build-tools-24.0.2,android-24,extra-google-m2repository,extra-google-google_play_services,extra-android-m2repository,extra-android-support
    # create the folder so we won't do this again (this is soooo Apache Ant right here)
    touch $DEPS
  fi
}
