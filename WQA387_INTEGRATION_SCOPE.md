# WQA387 — Link Sender v0.1.4.2 deferred shared-text focus integration

**Task:** WQ-20261028-005. **Status:** offline source-only owner-build candidate, not an APK or installed release.

## Exact source and narrow change

- Frozen parent WQC065 v0.1.4.1 ZIP SHA-256: `9013e00d8d57543f2196650b14a780374b58c46c61a81fbae5a00d145dcfda6d`.
- Independent WQE089 optional patch SHA-256: `eeecd07c99aa4fc7b756bcfbe5ef6416b5bd017944cc28a495d3ad1810af9059`. Strict `patch -p1 --fuzz=0` applied to frozen source, no source rewrite of LinkText, LinkFrame or receiver.
- Source-only metadata versionCode 15 / versionName `0.1.4.2-source-preview`; manual workflow artifact label `LinkSender-v0.1.4.2-debug-APK`. Historical WQC065 notes and manifest preserved, not silently rewritten.

The functional source change checks whether ACTION_SEND already focused **Finish editing** before executing the deferred initial Edit-button focus request. In the host model this prevents the shared-text focus from being stolen. The patch also corrects the fake Android host to model deferred `View.post` and updates its source assertions. No new permission, network operation, auto-open, SMS read, URL normalization or Bluetooth protocol change.

**Important:** This does not prove that the owner-observed original E4831 edit-field escape problem is solved. D-pad, Back and ACTION_SEND focus must be tested on the physical phone, and both ends need matching newer APKs for plain text. The known-good v0.1.2 remains installed/untouched. Do not uninstall or clear it merely to resolve a debug signature mismatch. No final build/flash/install was authorized.
