> **CURRENT SOURCE-ONLY OWNER-BUILD CANDIDATE: WQA387 v0.1.4.2, 2026-10-28.** Deferred shared-text launch focus correction on top of WQC065 v0.1.4.1. The ACTION_SEND Finish button is no longer overwritten by a later default Edit-focus callback in the host model. No APK or physical focus test; preserve owner-tested v0.1.2. Read `WQA387_OWNER_BUILD_HANDOFF.md`.

> **CURRENT SOURCE-ONLY OWNER-BUILD CANDIDATE: WQC065 v0.1.4.1, 2026-10-26.** Frozen WQC059 v0.1.4.0 SHA-256 `d8f32f8bed61ac6c6b70268d4dbf245e02c13ea06a2e43e92e98df858804db74`; integrates WQB324 + WQA321 malformed bare-link boundary guards. Read `WQC065_OWNER_BUILD_HANDOFF.md` first. **Not an APK, not device-tested.** Owner-tested v0.1.2 remains the known-good fallback. Historical notes below are preserved.

> **CURRENT SOURCE-ONLY OWNER-BUILD CANDIDATE: WQC059 v0.1.4.0, 2026-10-26.** Frozen parent WQB310 v0.1.3.8 SHA-256 `3d735872da65b1e9da1c9add19a2a78f79db70d2b2411b421d82565721f8f241`; integrated WQE059 bare-domain fragment fidelity correction. Read `WQC059_OWNER_BUILD_HANDOFF.md` and `WQC059_INTEGRATION_SCOPE.md` first. **Source only: no APK, phone, Bluetooth or Android SDK build was performed.** The owner-tested v0.1.2 APK remains the known-good fallback. Historical content follows unchanged.

# WQB310 Link Sender v0.1.3.8 source-only integration (2026-10-26)

This is a new isolated source candidate based on the frozen WQA299 v0.1.3.7 ZIP (SHA-256 `ae70b2103920367ceb7fe71cb4ebf2b54fcf5cd6e14e2f0fc3586830fdbc488c`). It combines the independently reproduced WQC055 Unicode-host boundary fix (patch SHA-256 `baf5fca4627254e79df507b13b6fc7a58694193451f2790ff3dd69788b874f43`) and WQA301 documentation/Windows batch corrections (patch SHA-256 `3183c254979102c65b8b1962c4c09196154e6a384319f5740db67b47b440fce7`). Original sources and patches remain unchanged in Library.

- **Functional change:** no more partial ASCII-hostname suggestions from within Unicode letters, marks, or numbers. Does not support IDN hostnames generally. User-initiated whole-text sending remains available.
- **Owner-build readiness:** correct current manual workflow artifact label, remove invalid leading backslash from `build_apk_windows.cmd`, and register the Unicode regression in both Java test runners.
- **Source version:** `versionCode 12`, `versionName 0.1.3.8-source-preview`; no APK was built, signed, installed, or tested on phones. Original owner-tested v0.1.2 APK is preserved.
- **Known limitation:** GitHub Actions/Android SDK, Windows 10 batch runner, and actual Kyocera/S20 Bluetooth remain untested; debug signing may differ from installed APK.

---

# WQ-20261026-011 — v0.1.3.7-source-preview (not released)

- Integrates WQA281 source-only linear trailing-punctuation trim on top of v0.1.3.6. No LinkFrame protocol, permissions, UI focus, or Bluetooth changes.
- Adds WQA281 independent 10,009-case equivalence and large unmatched-punctuation regression to both host test runners.
- Bumps source-only Android versionCode 10 → 11, versionName 0.1.3.6 → 0.1.3.7, and manual GitHub artifact label. No APK built.
- Android SDK, Windows 10, actual Kyocera focus and S20 receiver trials remain owner-supervised future steps.

---

# WQ-20261025-280 — v0.1.3.6-source-preview (not released)

- Integrates Worker A WQ279 independently reproduced regex-stack correction into the isolated WQB278 v0.1.3.5 source copy. Possessive repetition avoids observed host-Java StackOverflowError on long embedded URL tokens. Source-pinned original patch retained in `provenance/`.
- Adds the WQA279 regression suite and Worker B additional deterministic stress tests to Linux/Windows host test runners. Increments source-only versionCode 9 → 10, versionName 0.1.3.5 → 0.1.3.6, and manual GitHub artifact label.
- No change to LinkFrame/Bluetooth, SMS permissions, Android focus, green UI, URL selection policy or owner-tested v0.1.2 APK. Unicode-adjacent apostrophes in embedded links remain an unresolved fidelity issue. No Android SDK build, APK, phone trial or release.

---

# WQ-20261025-278 — v0.1.3.5-source-preview (not released)

- Integrates the separately reviewed WQB270 whole-valid-URL apostrophe fidelity correction, plus a narrow URL-tokenizer fix for apostrophes **between ASCII letters/digits** in URLs embedded in ordinary prose (including scheme-less domain paths). Quotation delimiters at URL ends remain excluded.
- Adds 20 host-Java regression assertions and registers them in Linux and Windows test runners. Source-only versionCode 9 / versionName 0.1.3.5-source-preview. No change to Bluetooth framing, SMS permissions, automatic opening, phone focus or green UI.
- Ambiguous punctuation/adjacent prose is not fully solved. No Android SDK build, APK, device trial or release; owner-tested v0.1.2 APK remains unchanged.
- Parent WQC038 v0.1.3.4 ZIP SHA-256 f85b8a3c4665b49a874ceede86a46a6ec9b41dbd29ac4ceba82f0795a32fe63f; WQB270 optional patch preserved separately.

---

# WQ-C-20261025-014 — v0.1.3.4-source-preview (not released)

- Combines the owner-requested v0.1.3.3 green UI/About metadata with WQE035 narrowly reviewed selected-link correction: a whole valid explicit HTTP(S) URL retains terminal punctuation rather than being silently shortened. Embedded prose, multiple links and bare-domain trimming stay conservative. This is not a general solution to punctuation ambiguity.
- Adds 15 host-only regression assertions and registers them in shell/Windows batch test runners. Increments source-only versionCode to 8 and versionName to `0.1.3.4-source-preview`; About build date remains real Gradle UTC and release date defaults to **Not released**.
- Parent green UI ZIP SHA-256 `223183e64a1720d133e293208c22e85b1136fb159b70ab98ea13c9dbfc21e904`; reviewed narrow patch SHA-256 `23a7b0a82a06dc20dd8542dc08f097a5d4ca5be51cc4adb5cb60a944a210f88e`. Parent ZIP remains unchanged.
- **Source only.** No Android SDK/Gradle APK build, phone installation, Bluetooth trial or owner acceptance. Existing v0.1.2 APK remains the physically verified fallback.

---

# Owner-requested green theme / about metadata — 2026-10-25 (source only)

- **v0.1.3.3-source-preview**, versionCode 7; no APK or real release. Retains working v0.1.2 APK as separate owner-tested baseline.
- Visual only: sage green #79A684 background; pale-green cards #DCECDE (RGB 220,236,222), editor #F2F8F0, button #E3F0E2, distinct keyboard focus #FFF7C3, black labels #000000. Buttons, status/received fields, text editor and spinner labels/dropdown all explicitly styled. One-column scroll layout remains usable on narrow phones; hardware navigation still requires actual Kyocera verification.
- About information inside app: version; prepared October 25, 2026; Gradle-injected UTC build date; release date pending by default; credit "Made by GPT-6 and Aurora Williamsen".
- Release date is not silently invented: Gradle `-PlinkSenderReleaseDateUtc=YYYY-MM-DD` is an explicit *owner-controlled release labeling input*. Debug builds default to "Not released". This metadata does not sign, deploy or publish the app.
- Existing source `LinkFrame.java`, `LinkText.java`, `OperationGate.java`, permissions and Bluetooth procedures unchanged. Known selected-link punctuation and adjacent-URL ambiguities remain unresolved, not falsely marked fixed.
- Source package only: host Java, static source, synthetic Android API mock checks; no real Android SDK, APK, phone or Bluetooth tests.

---

# WQ204 — Link Sender v0.1.3.2 bounded bare-domain regex safety

- Integrates **WQ-C-20261024-016** reviewed one-line regex guard (`{1,127}` dotted labels) into frozen WQ202 v0.1.3.1; baseline stack overflow reproduced at 3,000 dotted labels under Java 17, patched host regression tests pass through 10,000.
- Adds `core-tests/TestRegexBounded.java` and both Linux shell and Windows batch test-runner registration. Existing 28 URL, 13 cancellation, 39 text/protocol tests unchanged.
- Changes `versionCode` 5 -> 6 and `versionName` 0.1.3.1-source -> 0.1.3.2-source, manual artifact label to match. These are build metadata only; no APK was generated.
- No changes to `MainActivity`, `LinkFrame`, Bluetooth permissions, text/URL framing or installed apps. This is a source consolidation for one owner-controlled build, not a claim of real-device readiness.

---

# WQ202 — Link Sender v0.1.3.1 stale-link picker safety

- Integrated the WQ201 independent regression fix into a separate source-only candidate.
- Reconcile edited text and displayed URL choices on D-pad DOWN / Back focus escape.
- Stop Send selected link when the editor has changed; refresh and require deliberate review before sending.
- Added independent regression test to project and optional owner-controlled APK workflow.
- Incremented Android versionCode 4 -> 5, versionName `0.1.3-source` -> `0.1.3.1-source`; not evidence of a built/signed APK or update compatibility.
- No changes to LinkFrame, LinkText, Bluetooth, permissions, storage, SMS, receiver, or installed software.

---

# WQ-20261024-200 — Link Sender v0.1.3 source candidate

## Delivered

1. **Kyocera focus path:** initial focus on visible Edit button; D-pad DOWN leaves multiline editing; Activity Back leaves editing without exiting when the editor is focused. Not verified on the physical Kyocera or with its actual IME.
2. **User-selected plain text:** explicit Send entered text action; receiver distinguishes text from links; exact UTF-8 content within 8192-byte bound, with no SMS permission and no auto-opening. New typed LSB2 frame, URL LSB1 compatibility preserved.
3. **Bare links:** `bit.ly/...`, `i.cvs.com/...`, and similar domain paths recognized in the link chooser, displayed with an explicit `https://` prefix. Exact text mode preserves original spelling and formatting.
4. **No live mutation:** parent v0.1.2 source and owner-built APK preserved. This is a complete buildable source project with manual build helpers, **not an APK**.

## Testing and limitations

- Java host tests: 28 inherited URL/protocol assertions + 13 cancellation checks + 39 new text/link/protocol checks passed.
- Static source/permission assertions: 25 passed.
- Generated Android-shaped fake API harness: actual MainActivity.java compiled under Java 17 and 3 reported behavioral probes passed. **Not Android SDK, emulator or device tests.**
- One intermediate static-check assertion became stale when Back handling moved to Activity.onBackPressed; corrected and failure noted in evidence.
- Windows `run.bat` syntax inspected/corrected but **not run on Windows**. No genuine Android APK build. Bluetooth link transfer already worked on v0.1.2 per owner's separate physical report; v0.1.3 new features remain unverified.

## Next stage

Independent code review, then owner-controlled build/installation of v0.1.3 on both devices if desired. Avoid overwriting the working v0.1.2 installation without considering debug-signature differences and rollback. Physical tests should use invented harmless text/URLs, focus navigation and both link/text modes. Report actual key sequence and screen behavior. Do not use private SMS as initial test data.
