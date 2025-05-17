#!/bin/bash

set -e

echo "Installing Flutter SDK..."

FLUTTER_DIR="$HOME/flutter"
ANDROID_SDK_DIR="$HOME/Android/Sdk"
FLUTTER_URL="https://storage.googleapis.com/flutter_infra_release/releases/stable/windows/flutter_windows_3.22.1-stable.zip"

if [ -d "$FLUTTER_DIR" ]; then
    echo "Flutter already exists at $FLUTTER_DIR"
else
    mkdir -p "$FLUTTER_DIR"
    echo "Downloading Flutter SDK..."
    if [ -f flutter.zip ]; then
        echo "flutter.zip already exists. Skipping download."
    else
        curl -o flutter.zip "$FLUTTER_URL"
    fi
    unzip flutter.zip -d "$HOME"
    rm flutter.zip
fi

echo "Setting up environment variables..."

grep -qxF 'export PATH="$PATH:$HOME/flutter/bin"' ~/.bashrc || echo 'export PATH="$PATH:$HOME/flutter/bin"' >> ~/.bashrc
source ~/.bashrc

echo "Installing Android Command Line Tools..."

mkdir -p "$ANDROID_SDK_DIR/cmdline-tools"
cd "$ANDROID_SDK_DIR/cmdline-tools"

ANDROID_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip"

if [ -f cmdline-tools.zip ]; then
    echo "cmdline-tools.zip already exists. Skipping download."
else
    curl -o cmdline-tools.zip "$ANDROID_TOOLS_URL"
fi

unzip -o cmdline-tools.zip

if [ -d latest ]; then
    echo "'latest' directory already exists. Skipping mv."
else
    mv -f cmdline-tools latest
fi

rm -f cmdline-tools.zip

grep -qxF "export ANDROID_HOME=$ANDROID_SDK_DIR" ~/.bashrc || echo "export ANDROID_HOME=$ANDROID_SDK_DIR" >> ~/.bashrc
grep -qxF "export PATH=\$PATH:$ANDROID_SDK_DIR/cmdline-tools/latest/bin" ~/.bashrc || echo "export PATH=\$PATH:$ANDROID_SDK_DIR/cmdline-tools/latest/bin" >> ~/.bashrc
grep -qxF "export PATH=\$PATH:$ANDROID_SDK_DIR/platform-tools" ~/.bashrc || echo "export PATH=\$PATH:$ANDROID_SDK_DIR/platform-tools" >> ~/.bashrc
grep -qxF "export PATH=\$PATH:$ANDROID_SDK_DIR/emulator" ~/.bashrc || echo "export PATH=\$PATH:$ANDROID_SDK_DIR/emulator" >> ~/.bashrc

source ~/.bashrc

echo "Installing Android SDK components..."

yes | $ANDROID_SDK_DIR/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_SDK_DIR --licenses
$ANDROID_SDK_DIR/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_SDK_DIR "platform-tools" "platforms;android-33" "build-tools;33.0.2"

echo "Checking Flutter Doctor..."

flutter doctor

echo "DONE. Restart terminal or run 'source ~/.bashrc' to apply changes."
