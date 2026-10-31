#!/bin/sh
set -eu
cd "$(dirname "$0")/.."
mkdir -p core-tests/build
javac --release 17 -encoding UTF-8 -d core-tests/build \
 app/src/main/java/org/roarincreations/linksender/LinkText.java \
 app/src/main/java/org/roarincreations/linksender/LinkFrame.java \
 app/src/main/java/org/roarincreations/linksender/OperationGate.java \
 core-tests/TestLinkCore.java \
 core-tests/TestOperationGate.java \
 core-tests/TestTextTransfer.java \
 core-tests/TestRegexBounded.java \
 core-tests/TestExplicitStandaloneUrl.java \
 core-tests/TestApostropheLinkFidelity.java \
 core-tests/TestWQA279RegexStack.java \
 core-tests/TestWQB280RegexSafety.java \
 core-tests/TestWQA281TrimEquivalence.java \
 core-tests/TestWQC055UnicodeBoundary.java \
 core-tests/TestWQE059BareFragment.java \
 core-tests/TestWQC059OwnerTrialContract.java \
 core-tests/TestWQB324BareAuthority.java \
 core-tests/TestWQA321LinkBoundaryAndFrame.java
java -cp core-tests/build TestLinkCore
java -cp core-tests/build TestOperationGate
java -cp core-tests/build TestTextTransfer
java -cp core-tests/build TestRegexBounded
java -cp core-tests/build TestExplicitStandaloneUrl
java -cp core-tests/build TestApostropheLinkFidelity

java -Xss256k -cp core-tests/build TestWQA279RegexStack
java -Xss256k -cp core-tests/build TestWQB280RegexSafety

java -cp core-tests/build TestWQA281TrimEquivalence

java -cp core-tests/build TestWQC055UnicodeBoundary
java -cp core-tests/build TestWQC055UnicodeBoundary regression

java -cp core-tests/build TestWQE059BareFragment
java -cp core-tests/build TestWQC059OwnerTrialContract

java -cp core-tests/build TestWQB324BareAuthority
java -cp core-tests/build TestWQA321LinkBoundaryAndFrame
