# WQB310 Link Sender v0.1.3.8 source-only integration (2026-10-26)

This is a new isolated source candidate based on the frozen WQA299 v0.1.3.7 ZIP (SHA-256 `ae70b2103920367ceb7fe71cb4ebf2b54fcf5cd6e14e2f0fc3586830fdbc488c`). It combines the independently reproduced WQC055 Unicode-host boundary fix (patch SHA-256 `baf5fca4627254e79df507b13b6fc7a58694193451f2790ff3dd69788b874f43`) and WQA301 documentation/Windows batch corrections (patch SHA-256 `3183c254979102c65b8b1962c4c09196154e6a384319f5740db67b47b440fce7`). Original sources and patches remain unchanged in Library.

- **Functional change:** no more partial ASCII-hostname suggestions from within Unicode letters, marks, or numbers. Does not support IDN hostnames generally. User-initiated whole-text sending remains available.
- **Owner-build readiness:** correct current manual workflow artifact label, remove invalid leading backslash from `build_apk_windows.cmd`, and register the Unicode regression in both Java test runners.
- **Source version:** `versionCode 12`, `versionName 0.1.3.8-source-preview`; no APK was built, signed, installed, or tested on phones. Original owner-tested v0.1.2 APK is preserved.
- **Known limitation:** GitHub Actions/Android SDK, Windows 10 batch runner, and actual Kyocera/S20 Bluetooth remain untested; debug signing may differ from installed APK.

---

The original WQA299 historical notes below remain historical and are not a claim that 0.1.3.8 was built.
