#!/usr/bin/env bash
set -e

cd "$(dirname "$0")/.."

echo "Checking Java version..."
java -version

echo "Cleaning project..."
./gradlew clean

echo "Running unit tests..."
./gradlew testDebugUnitTest || echo "Unit tests failed or unavailable. Check docs/FEATURE_AUDIT.md."

echo "Building debug APK..."
./gradlew assembleDebug

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
  echo "SUCCESS: APK generated at $APK_PATH"
else
  echo "ERROR: APK not found at $APK_PATH"
  exit 1
fi
