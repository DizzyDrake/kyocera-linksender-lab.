@echo off
setlocal
cd /d "%~dp0\.."
if not exist "core-tests\build" mkdir "core-tests\build"
javac --release 17 -encoding UTF-8 -d "core-tests\build" "app\src\main\java\org\roarincreations\linksender\LinkText.java" "app\src\main\java\org\roarincreations\linksender\LinkFrame.java" "app\src\main\java\org\roarincreations\linksender\OperationGate.java" "core-tests\TestLinkCore.java" "core-tests\TestOperationGate.java" "core-tests\TestTextTransfer.java" "core-tests\TestRegexBounded.java" "core-tests\TestExplicitStandaloneUrl.java" "core-tests\TestApostropheLinkFidelity.java" "core-tests\TestWQA279RegexStack.java" "core-tests\TestWQB280RegexSafety.java" "core-tests\TestWQA281TrimEquivalence.java" "core-tests\TestWQC055UnicodeBoundary.java" "core-tests\TestWQE059BareFragment.java" "core-tests\TestWQC059OwnerTrialContract.java" "core-tests\TestWQB324BareAuthority.java" "core-tests\TestWQA321LinkBoundaryAndFrame.java"
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestLinkCore
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestOperationGate
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestTextTransfer
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestRegexBounded
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestExplicitStandaloneUrl
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestApostropheLinkFidelity
if errorlevel 1 exit /b 1
java -Xss256k -cp "core-tests\build" TestWQA279RegexStack
if errorlevel 1 exit /b 1
java -Xss256k -cp "core-tests\build" TestWQB280RegexSafety
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestWQA281TrimEquivalence
if errorlevel 1 exit /b 1
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestWQC055UnicodeBoundary
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestWQC055UnicodeBoundary regression
if errorlevel 1 exit /b 1

java -cp "core-tests\build" TestWQE059BareFragment
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestWQC059OwnerTrialContract
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestWQB324BareAuthority
if errorlevel 1 exit /b 1
java -cp "core-tests\build" TestWQA321LinkBoundaryAndFrame
exit /b %errorlevel%
