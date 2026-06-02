#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "Java version:"
java -version

echo "Cleaning project..."
./gradlew clean

echo "Running JVM unit tests..."
./gradlew testDebugUnitTest

echo "Building debug APK..."
./gradlew assembleDebug

APK="app/build/outputs/apk/debug/app-debug.apk"
if [[ ! -f "$APK" ]]; then
  echo "ERROR: APK missing at $APK" >&2
  exit 1
fi

echo "SUCCESS: APK generated at $APK"
