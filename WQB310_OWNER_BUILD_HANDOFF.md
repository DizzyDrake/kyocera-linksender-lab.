# Link Sender v0.1.3.8 — owner-controlled source build handoff

**Source-only preview, not a built APK.** This candidate combines the WQC055 Unicode-hostname partial-match guard with WQA301 corrections to build instructions and the Windows batch launcher. It also adds the Unicode regression to both host test runners. Parent v0.1.3.7 SHA-256: `ae70b2103920367ceb7fe71cb4ebf2b54fcf5cd6e14e2f0fc3586830fdbc488c`.

The working, owner-tested **v0.1.2** APK is not replaced. If you later choose to build this source, extract the ZIP, place the **contents** at the root of a separate private GitHub trial branch (including hidden `.github`), then manually run the included GitHub Actions workflow. Expected artifact label: `LinkSender-v0.1.3.8-debug-APK`. Never upload private messages, signing keys or credentials. A different debug signing key may prevent in-place update; do not uninstall the known-good version without a separately reviewed backup and rollback plan.

Run `bash core-tests/run.sh` (Linux/macOS with JDK 17+) or `core-tests\run.bat` (Windows with JDK 17+) for host-only Java checks. They cannot prove Android compilation, Bluetooth behavior, Kyocera focus navigation, or Windows 10 execution. `BUILD_AN_APK.md` contains additional historical context and safety warnings. No Android APK, firmware, phone access, or GitHub action was performed in this queue run.

Version: `versionCode 12`, `versionName 0.1.3.8-source-preview`. The build-date metadata is generated only during a real build; release date remains `Not released` unless the owner explicitly sets it.
