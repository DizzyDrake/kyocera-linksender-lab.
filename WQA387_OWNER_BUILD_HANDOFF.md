# Link Sender v0.1.4.2 — optional owner-build source only

This ZIP is **not an APK**. It combines the WQC065 v0.1.4.1 URL-boundary and typed-text candidate with the WQE089 shared-text deferred-focus correction. Parent ZIP and optional patch are frozen in ChatGPT Library; their SHA-256 hashes are recorded in `WQA387_INTEGRATION_SCOPE.md`.

The actual owner-tested version is still v0.1.2 (harmless URL Bluetooth transfer). The new focus correction has **only** been exercised in a fake-Android host with deferred `View.post`; no Android SDK build, actual Kyocera keypad, S20 receiver, Bluetooth or native Windows 10 test has been completed.

## Optional owner-controlled trial when convenient

1. Keep the existing working v0.1.2 installation, APK, and signing/rollback details. Do not uninstall it merely to force a new debug signature. Build v0.1.4.2 only if you choose, using the manual workflow described in `BUILD_AN_APK.md` or a local Android SDK.
2. On a disposable trial with fictional text, launch from Android Share (`ACTION_SEND text/plain`). After the interface settles, observe whether **Finish editing** retains focus; then test D-pad Down, Back, exact-text send, and non-auto-opening receive on the S20. Record actual focus, exit behavior and any build/install error before retries.
3. Separately test a normal app launch and an invented bare `bit.ly/path` or `i.cvs.com/offer` suggestion, verifying the chosen destination before sending. Do not use private SMS or real account links.

The GitHub Actions workflow is **manual-only** (`workflow_dispatch`), artifact label `LinkSender-v0.1.4.2-debug-APK`, not triggered here. Source metadata: `versionCode=15`, `versionName=0.1.4.2-source-preview`, release date defaults to `Not released` and build date is generated only by an actual build. Android 10 API 29 compatibility, debug signing, real phone focus and end-to-end plain-text transfer remain owner-supervised checks.
