> **WQE175 v0.1.4.3 follow-on:** Read `README_WQE175_FIRST.md` first. The WQA387 v0.1.4.2 instructions below are historical for that source candidate.

> **CURRENT SOURCE-ONLY OWNER-BUILD CANDIDATE: WQA387 v0.1.4.2, 2026-10-28.** Based on byte-pinned WQC065 v0.1.4.1 and independently reproduced WQE089 deferred ACTION_SEND focus correction. Read `WQA387_OWNER_BUILD_HANDOFF.md` first. **Not an APK, not device-tested.** The owner-tested v0.1.2 APK remains the known-good fallback. Historical notes below are preserved.

> **CURRENT SOURCE-ONLY OWNER-BUILD CANDIDATE: WQC065 v0.1.4.1, 2026-10-26.** Frozen WQC059 v0.1.4.0 SHA-256 `d8f32f8bed61ac6c6b70268d4dbf245e02c13ea06a2e43e92e98df858804db74`; integrates WQB324 + WQA321 malformed bare-link boundary guards. Read `WQC065_OWNER_BUILD_HANDOFF.md` first. **Not an APK, not device-tested.** Owner-tested v0.1.2 remains the known-good fallback. Historical notes below are preserved.

> **CURRENT SOURCE-ONLY OWNER-BUILD CANDIDATE: WQC059 v0.1.4.0, 2026-10-26.** Frozen parent WQB310 v0.1.3.8 SHA-256 `3d735872da65b1e9da1c9add19a2a78f79db70d2b2411b421d82565721f8f241`; integrated WQE059 bare-domain fragment fidelity correction. Read `WQC059_OWNER_BUILD_HANDOFF.md` and `WQC059_INTEGRATION_SCOPE.md` first. **Source only: no APK, phone, Bluetooth or Android SDK build was performed.** The owner-tested v0.1.2 APK remains the known-good fallback. Historical content follows unchanged.

# WQB310 Link Sender v0.1.3.8 source-only integration (2026-10-26)

This is a new isolated source candidate based on the frozen WQA299 v0.1.3.7 ZIP (SHA-256 `ae70b2103920367ceb7fe71cb4ebf2b54fcf5cd6e14e2f0fc3586830fdbc488c`). It combines the independently reproduced WQC055 Unicode-host boundary fix (patch SHA-256 `baf5fca4627254e79df507b13b6fc7a58694193451f2790ff3dd69788b874f43`) and WQA301 documentation/Windows batch corrections (patch SHA-256 `3183c254979102c65b8b1962c4c09196154e6a384319f5740db67b47b440fce7`). Original sources and patches remain unchanged in Library.

- **Functional change:** no more partial ASCII-hostname suggestions from within Unicode letters, marks, or numbers. Does not support IDN hostnames generally. User-initiated whole-text sending remains available.
- **Owner-build readiness:** correct current manual workflow artifact label, remove invalid leading backslash from `build_apk_windows.cmd`, and register the Unicode regression in both Java test runners.
- **Source version:** `versionCode 12`, `versionName 0.1.3.8-source-preview`; no APK was built, signed, installed, or tested on phones. Original owner-tested v0.1.2 APK is preserved.
- **Known limitation:** GitHub Actions/Android SDK, Windows 10 batch runner, and actual Kyocera/S20 Bluetooth remain untested; debug signing may differ from installed APK.

---

# WQA299 source-only integration — v0.1.3.7

**Current candidate:** Link Sender v0.1.3.7-source-preview (WQ-20261026-011). Based on immutable v0.1.3.6 and WQA281 optional linear URL-trimming correction. No APK, phone installation, or Android SDK build has occurred. **Owner-tested v0.1.2 APK remains fallback.**

**Change:** a linear-time URL punctuation trim, preserving the old trimming decisions in 10,009 host comparisons and avoiding repeated scans on long unmatched closing punctuation. Source-only evidence is not device proof. See `WQA299_INTEGRATION_SCOPE.md`.

---

# Owner-requested green UI candidate — 2026-10-25

**Candidate:** Link Sender v0.1.3.6-source-preview (WQ-20261025-280), derived from immutable WQB278 v0.1.3.5 plus the WQA279 independent regex-stack correction. **Not built into an APK; not released or installed.**

The requested visual redesign uses a sage-green background, desaturated pale-green information panels, pale buttons/editor/selectors, and **black text** throughout, including spinner list items. A contrasting warm-ivory keyboard focus indicator helps navigation on the Kyocera E4831. See `OWNER_REQUESTED_GREEN_UI_2026-10-25.md` for exact colors, About metadata, and limitations. This package inherits the green UI and Bluetooth/text framing from v0.1.3.3 and integrates the narrow, source-reviewed explicit-URL selection correction from WQE035.

**About panel displayed in the app:** version, source-prepared date (2026-10-25), *actual Gradle build date UTC* for each APK, *release date* (default **Not released**), and `Made by GPT-6 and Aurora Williamsen`.

**Important:** the owner-tested Link Sender v0.1.2 APK remains the working baseline. The newer v0.1.3.6 candidate is SOURCE ONLY. It preserves complete valid HTTP(S) URLs, including apostrophes and trailing punctuation, when entered alone. When embedded in prose, it preserves apostrophes strictly between ASCII letters/digits inside links, including scheme-less domains, while keeping conservative terminal punctuation trimming and quote boundaries. Ambiguous adjacent prose, unmatched punctuation and quoted URL boundaries remain unresolved; verify selected links before sending. Use Send entered text exactly whenever preserving the full original text is essential. Do not install, uninstall, or update phones automatically.

**New safety correction:** The prior v0.1.3.5 URL extractor can overflow the Java regex stack on long embedded URL tokens; the WQA279 possessive-quantifier patch is integrated and host-tested at small Java stack sizes. This is not proof of Android runtime safety. Test links must be reviewed before sending, and the owner-tested v0.1.2 APK remains the fallback. See `WQB280_INTEGRATION_SCOPE.md` for evidence and limitations.

See `BUILD_AN_APK.md` for the owner-controlled build procedure, including optional release-date metadata.

---

# Link Sender v0.1.3.2 — consolidated source for owner-controlled APK build (WQ-20261024-204)

**Status: source only. No APK, Android SDK build, installed update or physical phone test was performed for v0.1.3.2.** The owner's existing v0.1.2 APK is the only version reported to have transferred a harmless URL on the actual two phones.

This full source project carries forward the coherent v0.1.3 features (Kyocera focus escape, explicitly chosen ordinary-text transfer, scheme-less links) and v0.1.3.1 stale-link selection protection. It now integrates Worker C's independently reproduced regex stack-overflow correction: pasted SMS-like text containing thousands of dotted fragments must not crash URL extraction. The fix does **not** change the Bluetooth frame formats, permissions or data storage.

**Owner-controlled build:** Read `BUILD_AN_APK.md`; the manual GitHub Actions workflow is included but is **not triggered** by this package. On Windows with Java 17, `core-tests\run.bat` exercises the host-only tests. A real APK build, signing compatibility, D-pad focus, two-device transfer and the Discord-independent application behavior still require actual owner-controlled validation. Use harmless fabricated content for initial trials. Do not replace the known-working v0.1.2 on either phone without a rollback plan; debug APK signatures can differ across builds.

---

# Link Sender v0.1.3.1 — source-only stale-link safety revision (WQ-20261024-202)

**This is a follow-up to WQ200 v0.1.3, not a built APK or owner-tested release.** It integrates Worker A's WQ201 independently reproduced stale-link-picker correction. The original WQ200 candidate and owner's working v0.1.2 APK remain unchanged.

**Corrected behavior:** If the user changes editor text and exits with D-pad DOWN or Back, the link picker refreshes. If the user edits the text and presses **Send selected link** before refreshing, the app refreshes the picker, **does not send**, and displays `Text changed. Review the selected link before sending.` The user must deliberately select and press Send again. Exact-text transfer remains a separate action.

**Verification:** 80 Java host assertions, 25 static source assertions, 3 inherited fake-Android UI checks, and 2 additional stale-link regression assertions passed. The regression harness compiles the real MainActivity against synthetic Android API stubs, **not** the Android SDK. Actual E4831 D-pad, Bluetooth, receiver, APK signing/build, and Windows 10 remain unverified.

**Offline regression:** From the project root, run `python core-tests/test_stale_link_guard.py` after `sh core-tests/run.sh` or `core-tests\run.bat`. The GitHub Actions workflow now includes this test when an owner chooses to build. Do not install, uninstall or replace the existing app automatically.

---

# Link Sender v0.1.3 — source-only owner-trial candidate

**Task:** WQ-20261024-200. **Status:** Java host tested; mock Android API compilation and simulated focus tested; **NO v0.1.3 APK, Android SDK build, Kyocera/S20 installation or real Bluetooth test performed**. The owner separately confirmed that genuine v0.1.2 APK transferred `https://example.org/` between paired Kyocera E4831 and LineageOS S20 on October 24, 2026. Do not confuse that success with testing this candidate.

## Changes based on actual owner feedback

- **Focus escape:** Initial focus is placed on **Edit / type text**, not automatically inside the editor. When editing, hardware **D-pad DOWN** or **Back** moves focus to **Use entered text / finish editing** rather than closing the app; the text remains intact. This is an unverified physical-key hypothesis requiring the owner's next harmless trial, not a demonstrated fix on E4831. Other controls remain in the scrollable view.
- **Deliberate text transfer:** **Send entered text exactly** transmits the current typed, pasted, or `ACTION_SEND text/plain` shared text. It requires a nonempty payload of at most **8192 UTF-8 bytes**, and preserves whitespace, Unicode, and line breaks. It never reads SMS directly. The receiver shows the content as **text**, not a link, and provides **Copy received**. Neither payload type auto-opens anything.
- **Bare-domain links:** **Use entered text** extracts HTTP(S) links plus common hostnames such as `bit.ly/abc` and `i.cvs.com/offer`. Bare hostnames are visibly presented with an added `https://` prefix in the link chooser; the owner can instead send the exact original text. No URL fetching, expansion, browser opening or external requests.
- **Two explicit actions:** **Send selected link** sends one displayed URL, while **Send entered text exactly** sends the complete entered text. Receiver must press **Receive one link or text** first. A URL link frame retains the old LSB1 format for compatibility with the v0.1.2 receiver; new plain-text frames use a typed LSB2 format and require a **v0.1.3 receiver**. Update both ends before testing text. Do not expect an older receiver to accept LSB2.

## What is deliberately unchanged

Paired-device secure Bluetooth RFCOMM UUID, manual Send/Receive, one payload per connection, one-byte validation acknowledgment, cancellation gate, no automatic SMS/clipboard reading, no URL auto-open, no cloud or persistent history, no `READ_SMS`, `INTERNET`, location, contacts or notification permissions. The app keeps no message history; received content stays in memory and can be manually copied.

## Offline checks

From the extracted project with JDK 17+: `sh core-tests/run.sh` (Linux/macOS) or `core-tests\run.bat` (Windows); `python core-tests/check_source_safety.py`; `python core-tests/run_mock_ui_probe.py`. The fake Android harness compiles **actual MainActivity.java** against generated stubs and checks stale status suppression, initial focus and D-pad/Back focus escape. It is not the Android SDK and cannot prove real Kyocera behavior. Check `TEST_REPORT.json` for exact results.

## Owner-supervised build and trial (not done by worker)

`BUILD_AN_APK.md` explains the existing optional manual GitHub Actions or Windows SDK build helpers. The user decides if/when to build and install. Preserve the functioning v0.1.2 APK and avoid changing the real phones until a deliberate test. An Android debug APK built on a different runner may use a different debug signing certificate and might not update in place; do not uninstall a working app automatically.

A safe future physical trial would use fabricated `bit.ly/example`, `i.cvs.com/test`, `https://example.org/` and plain text such as `Testing 1\nTesting 2`, with both phones nearby. Verify whether focus starts on the button and whether D-pad DOWN/Back leaves editing without exiting; report exactly what happened. Never test with actual private SMS until comfortable with the UI and privacy implications.

## Source provenance and safety

Parent frozen WQ169 source SHA-256 `bd2ea3a989b7c479eb9c353f90caf83f5604f8dd3cff8353e4390e1f7c2b006e`; build helper WQ171 SHA-256 `dae506f5da9f898a95a85b9ddfe8be77199609174be29888cb116aa82e2f7fe7`. Genuine owner v0.1.2 APK SHA-256 `c65d1dbbc1ddc3dbf7ab83abcab1bfbfd3899b0e1826d943b38cbe7b6f8329fa` (NOT contained here). See `PROVENANCE.md` and manifests for reproducibility. No owner device, GitHub repository, account, SMS, F: drive, or automation was accessed or changed.
