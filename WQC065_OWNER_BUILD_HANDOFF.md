# Link Sender v0.1.4.1 — consolidated owner-build source, not APK

This source candidate corrects two independently reproduced cases where an automatic scheme-less URL suggestion could **silently change the intended destination** by truncating malformed characters. It is the coherent successor to WQC059 v0.1.4.0; no installed software was modified. The working, owner-phone-tested **v0.1.2 APK remains the fallback**.

## What changed and what did not

- Automatic link suggestions now reject tested ambiguous strings such as `bit.ly:123456/path`, `bit.ly:abc/path`, `bit.ly%2Fsecret`, `bit.ly\coupon`, `bit.ly..other.com`, rather than offering a truncated `https://bit.ly` variant. Normal `bit.ly/path`, `i.cvs.com/offer?q=1#frag`, and sentence-ending punctuation remain supported in host tests.
- Explicit **Send entered text exactly** remains available even for unusual strings; LSB2 typed text preserves content. Legacy LSB1 URL framing and the non-auto-opening S20 receiver are unchanged.
- Existing green UI and proposed Kyocera D-pad focus escape are unchanged and **not physically validated** on v0.1.4.1. Both phones need the matching newer receiver to exchange plain text.
- No SMS/contacts/Internet permission or background SMS reading, payments, credentials, network URL lookup, or live access.

## Host-only preflight

With JDK 17+ installed: `bash core-tests/run.sh` (Linux/macOS) or `core-tests\run.bat` (Windows 10) runs inherited and both newly registered Java regressions. Run `python core-tests/check_source_safety.py`, `python core-tests/test_stale_link_guard.py`, `python core-tests/test_green_ui_contract.py`, `python core-tests/run_mock_ui_probe.py` for additional source/mock checks. Host checks do **not** validate Android SDK compilation, APK signing, Windows execution, Bluetooth, S20 rendering, or physical keypad focus.

## Owner-controlled future build only

The included `.github/workflows/build-linksender-debug-apk.yml` uses manual `workflow_dispatch`; its expected artifact label is `LinkSender-v0.1.4.1-debug-APK`. See `BUILD_AN_APK.md` for manual GitHub/Windows instructions. No workflow was run or repository touched. A debug key mismatch may prevent in-place updating of v0.1.2; **do not uninstall or clear the working APK without an explicit backup/rollback plan**. Use fictional URLs and ordinary invented text for a supervised two-phone trial. Confirm selected-link display, D-pad/Back focus, text receive/copy, no automatic open, and any signing/install errors. Preserve observations before retrying.

**Status:** Source-only preview, not released/installed/accepted. `versionCode=14`, `versionName=0.1.4.1-source-preview` are metadata only, not proof of an APK. Parent WQC059 and independent WQB324/WQA321 reviews remain immutable.
