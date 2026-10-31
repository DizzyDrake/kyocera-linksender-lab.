# Link Sender v0.1.4.0 — one owner-build-ready source preview

**Status: SOURCE ONLY. No APK was compiled, signed, installed or tested on either phone.** This source integrates the final known bare-host `#fragment` correction into the existing coherent text/link/focus/green-UI candidate. The known-good owner-tested v0.1.2 APK remains separate and must not be uninstalled merely to try this candidate.

## What the owner would be testing

- **Kyocera E4831:** focus starts on the Edit button; D-pad Down or Back from editing is intended to move focus to Finish editing without exiting. Actual keypad navigation remains unverified on hardware.
- **Send selected link:** supports `https://...` and ordinary scheme-less `bit.ly/...`, `i.cvs.com/...`; selected scheme-less links are displayed with `https://`. `bit.ly#part` should retain its fragment. No expansion or network lookup.
- **Send entered text exactly:** user deliberately copies/pastes/shares plain text, including normal SMS snippets, without SMS permission or background reading. S20 must run the matching newer receiver for LSB2 text; the v0.1.2 receiver can handle only legacy LSB1 URL frames.
- **Receive one:** displays content without opening it, allows manual copy, and requires owner initiation.

## Offline checks before any owner build

With JDK 17+ installed, `bash core-tests/run.sh` (Linux/macOS) or `core-tests\run.bat` (Windows) runs the host Java suite. `python core-tests/check_source_safety.py`, `python core-tests/run_mock_ui_probe.py`, `python core-tests/test_stale_link_guard.py`, and `python core-tests/test_green_ui_contract.py` provide source and synthetic UI checks. These do **not** establish Android SDK, Windows 10, Bluetooth, device focus or signing success.

## Future owner-controlled build (not performed here)

The included `.github/workflows/build-linksender-debug-apk.yml` is manual-only (`workflow_dispatch`), builds an Android debug APK using Android SDK and Gradle, and labels the downloadable workflow artifact `LinkSender-v0.1.4.0-debug-APK`. If Aurora chooses this route, put the **contents** of this ZIP at the root of a **private** repository/branch, including the hidden `.github` directory. The existing `BUILD_AN_APK.md` has step-by-step instructions and a Windows 10 alternative. No real SMS content or signing credentials belong in source or test fixtures.

A new debug signing key may make an in-place update fail. **Do not uninstall the working v0.1.2 APK or clear app data without a separate rollback plan.** Both devices require an owner-supervised matching-version trial for plain-text transfer. First verify the built APK and source identity, then try fictional links and text; report exact observed focus/key behavior and any errors. Do not treat a GitHub workflow success as device acceptance.

**Known limits:** no Android SDK build, no native Windows test, no actual Kyocera D-pad trial, no Bluetooth link/text validation on v0.1.4.0, no signature parity verification, no printing or external links fetched.
