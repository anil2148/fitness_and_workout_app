#!/usr/bin/env bash
set -e

cd "$(dirname "$0")/.."

echo "Checking Java version..."
java -version

echo "Verifying string resources..."
python3 scripts/verify_strings.py

echo "Scanning hardcoded strings..."
python3 scripts/scan_hardcoded_strings.py || true

echo "Cleaning project..."
./gradlew clean

echo "Running unit tests..."
./gradlew testDebugUnitTest

echo "Building debug APK..."
./gradlew assembleDebug

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
  echo "SUCCESS: APK generated at $APK_PATH"
else
  echo "ERROR: APK not found at $APK_PATH"
  exit 1
fi
