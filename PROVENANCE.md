> **CURRENT SOURCE-ONLY OWNER-BUILD CANDIDATE: WQC059 v0.1.4.0, 2026-10-26.** Frozen parent WQB310 v0.1.3.8 SHA-256 `3d735872da65b1e9da1c9add19a2a78f79db70d2b2411b421d82565721f8f241`; integrated WQE059 bare-domain fragment fidelity correction. Read `WQC059_OWNER_BUILD_HANDOFF.md` and `WQC059_INTEGRATION_SCOPE.md` first. **Source only: no APK, phone, Bluetooth or Android SDK build was performed.** The owner-tested v0.1.2 APK remains the known-good fallback. Historical content follows unchanged.

# WQB310 Link Sender v0.1.3.8 source-only integration (2026-10-26)

This is a new isolated source candidate based on the frozen WQA299 v0.1.3.7 ZIP (SHA-256 `ae70b2103920367ceb7fe71cb4ebf2b54fcf5cd6e14e2f0fc3586830fdbc488c`). It combines the independently reproduced WQC055 Unicode-host boundary fix (patch SHA-256 `baf5fca4627254e79df507b13b6fc7a58694193451f2790ff3dd69788b874f43`) and WQA301 documentation/Windows batch corrections (patch SHA-256 `3183c254979102c65b8b1962c4c09196154e6a384319f5740db67b47b440fce7`). Original sources and patches remain unchanged in Library.

- **Functional change:** no more partial ASCII-hostname suggestions from within Unicode letters, marks, or numbers. Does not support IDN hostnames generally. User-initiated whole-text sending remains available.
- **Owner-build readiness:** correct current manual workflow artifact label, remove invalid leading backslash from `build_apk_windows.cmd`, and register the Unicode regression in both Java test runners.
- **Source version:** `versionCode 12`, `versionName 0.1.3.8-source-preview`; no APK was built, signed, installed, or tested on phones. Original owner-tested v0.1.2 APK is preserved.
- **Known limitation:** GitHub Actions/Android SDK, Windows 10 batch runner, and actual Kyocera/S20 Bluetooth remain untested; debug signing may differ from installed APK.

---

## WQ-20261026-011 v0.1.3.7 source-only integration

- Parent frozen WQB280 v0.1.3.6 ZIP SHA256 `e0a7ed2d6fa0dc2d0eae39161ea7d1ba739c785d4d92da4940afe68471e5f5d9`.
- Applied WQA281 optional linear trim patch SHA256 `2d8ffb7e62d436d80b2160b7e2eb3cc4fc02eddb446e8e0921c7ef597ce0cb58`.
- Added independent WQA281 test; owner-tested v0.1.2 APK remains untouched.
- This is not compiled firmware/APK, no device or Bluetooth verification.

## WQ-20261025-280 v0.1.3.6 source-only integration

Frozen parent: WQB278 v0.1.3.5 ZIP SHA-256 `691921ed1743120b758c531b99734bd77ff649532a15e473ae0970796ac598e5` (42/42 embedded hashes verified). Worker A WQ279 optional patch SHA-256 `81239310a55900987b5561c1cb4390fd7d4223ad845c43b3cb4cb47d2feeede7`, applied unchanged to `LinkText.java` in a new scratch copy. Worker A synthetic regression source `TestWQA279RegexStack.java` retained with attribution; additional Worker B stress tests separately written. No owner-source, v0.1.2 APK, phones, or installed application changed. No Android SDK compilation was performed.

# WQ204 source provenance (2026-10-24)

- Immediate immutable parent: WQ202 v0.1.3.1 source ZIP, SHA-256 `850511f9e957f652bb524e87ad2805d32c9abd8677f9ee374f747bb9359c0a58`, independently materialized from ChatGPT Library and byte-compared with prior local copy.
- Independent Worker C finding: WQ-C-20261024-016; source patch SHA-256 `26640e3890e178352dc5f5246c97a15bf193bf1ef7d66f67d3625e37f4101a6f`; the exact patch is copied into `provenance/WQC016_REGEX_STACK_FIX_SOURCE_ONLY.patch`.
- Owner physical test is limited to v0.1.2, SHA-256 `c65d1dbbc1ddc3dbf7ab83abcab1bfbfd3899b0e1826d943b38cbe7b6f8329fa`, and one harmless URL transfer. No owner personal SMS data included.
- This candidate was reconstructed in an isolated scratch directory. The canonical owner APK, WQ200, WQ202 and Worker C's review remain unchanged. No third-party source was copied.
- Only product behavior change: bounded LinkText bare-host regex. Other changes are host test registration, build version metadata, release/build notes, manifest, test report and provenance.
- No Android SDK/Gradle compilation, real Windows 10 test, GitHub Actions run, installation, device access, Bluetooth transfer, F: access, automation edit or live data access.

---

# WQ202 source provenance (2026-10-24)

- **Immediate frozen parent:** WQ200 Link Sender v0.1.3 source-only ZIP, SHA-256 `4ec21f270f0e01b3308c06677d72dd8a0e12f9394f46528d9f22910266cf3031`.
- **Independent defect/review:** WQ201 `WQ201_STALE_LINK_GUARD_PROPOSAL.patch` and `WQ201_test_stale_link.py` (Worker A); proposal applied cleanly to an isolated copy. The original patch is included under `provenance/`, test adapted only for project-root execution.
- **Older source lineage:** WQ169 v0.1.2 source SHA-256 `bd2ea3a989b7c479eb9c353f90caf83f5604f8dd3cff8353e4390e1f7c2b006e`; WQ171 build helper SHA-256 `dae506f5da9f898a95a85b9ddfe8be77199609174be29888cb116aa82e2f7fe7`.
- **Owner working APK (NOT included):** v0.1.2 SHA-256 `c65d1dbbc1ddc3dbf7ab83abcab1bfbfd3899b0e1826d943b38cbe7b6f8329fa`. Owner confirmed a harmless URL transfer on that older APK only.
- **Historical metadata:** Original WQ200 manifest/test report are preserved under `provenance/`. The top-level manifest and test report describe this candidate.
- **Changes:** `MainActivity.java`, `app/build.gradle`, workflow, new regression test, source documentation/metadata. No source copy from third-party repositories.
- **Not performed:** Android SDK build, physical keypad or Bluetooth testing, APK installation, owner Windows/phones/GitHub/Discord/F: access, or automations changes.

# WQ-20261024-200 source provenance

- Canonical parent: `/FOSS/Kyocera SMS Link Bridge to LineageOS S20/WQ169_LINK_SENDER_v0.1.2_UI_STATUS_GUARD_SOURCE_ONLY.zip` (SHA-256 `bd2ea3a989b7c479eb9c353f90caf83f5604f8dd3cff8353e4390e1f7c2b006e`).
- Build helper basis: `/FOSS/Kyocera SMS Link Bridge to LineageOS S20/WQ171_LINK_SENDER_v0.1.2_APK_BUILD_HELPER_PRIVATE_GITHUB_OR_WINDOWS.zip` (SHA-256 `dae506f5da9f898a95a85b9ddfe8be77199609174be29888cb116aa82e2f7fe7`).
- Owner physical test: v0.1.2 genuine APK SHA-256 `c65d1dbbc1ddc3dbf7ab83abcab1bfbfd3899b0e1826d943b38cbe7b6f8329fa`; URL transfer passed for `https://example.org/` only. APK not included or modified.
- Owner feedback: `/Work Queue/OWNER_LINK_SENDER_REAL_PHONE_TEST_AND_TEXT_TRANSFER_FEEDBACK_2026-10-24.md`.
- Changed source files: `LinkText.java`, `LinkFrame.java`, `MainActivity.java`, `app/build.gradle`, Java and fake Android tests, README/build-helper documentation. No third-party source copied. Existing WQ171 GitHub build-helper configuration retained except candidate artifact name.
- This archive contains the **entire independently modified project** and immutable parent manifest copies under `provenance/`. It is not a compiled APK, an installed build, or evidence of live Bluetooth behavior.


## WQ-20261025-278 offline apostrophe link fidelity
Parent WQC038 ZIP SHA-256 f85b8a3c4665b49a874ceede86a46a6ec9b41dbd29ac4ceba82f0795a32fe63f; original source manifest copied to provenance. WQB270 reviewed one-line correction copied to provenance and integrated into isolated source. New bounded interior-apostrophe tokenizer correction and tests are original WQ-B work. No source, APK or device deployed.
