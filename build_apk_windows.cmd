@echo off
setlocal
cd /d "%~dp0"
echo === Link Sender local Windows debug APK build ===
where java >nul 2>nul
if errorlevel 1 (
  echo MISSING: JDK 17 or compatible Java. Read BUILD_AN_APK.md
  exit /b 2
)
where gradle >nul 2>nul
if errorlevel 1 (
  echo MISSING: Gradle 8.7 or compatible Gradle. Read BUILD_AN_APK.md
  echo Alternative: upload this project to your own private GitHub repo and run the manual workflow.
  exit /b 2
)
if not defined ANDROID_HOME if defined ANDROID_SDK_ROOT set "ANDROID_HOME=%ANDROID_SDK_ROOT%"
if not defined ANDROID_HOME if exist "%LOCALAPPDATA%\Android\Sdk\platforms\android-35\android.jar" set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
if not defined ANDROID_HOME (
  echo MISSING: Android SDK location. Set ANDROID_HOME to the SDK installation folder.
  exit /b 2
)
if not exist "%ANDROID_HOME%\platforms\android-35\android.jar" (
  echo MISSING: Android SDK Platform 35 under %ANDROID_HOME% .
  exit /b 2
)
if not exist "%ANDROID_HOME%\build-tools\35.0.0" (
  echo MISSING: Android SDK Build-Tools 35.0.0 under %ANDROID_HOME% .
  exit /b 2
)
echo SDK: %ANDROID_HOME%
echo Building debug APK. No phone is connected or installed by this script.
gradle :app:assembleDebug --no-daemon
if errorlevel 1 (
  echo BUILD FAILED. Please keep the terminal output for debugging.
  exit /b 1
)
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
  echo BUILD FINISHED but APK missing. Capture command output.
  exit /b 1
)
echo BUILD SUCCEEDED: app\build\outputs\apk\debug\app-debug.apk
echo Debug signing is for temporary owner testing, not app-store releases.
exit /b 0
