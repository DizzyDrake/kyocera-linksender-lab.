# WQC059 coherent Link Sender owner-build source candidate (2026-10-26)

**Candidate:** v0.1.4.0-source-preview; Android `versionCode 13`; **not built, installed, signed, deployed, released, or owner-accepted.**

**Immutable parent:** WQB310 v0.1.3.8 source-only ZIP SHA-256 `3d735872da65b1e9da1c9add19a2a78f79db70d2b2411b421d82565721f8f241`.
**Integrated correction:** WQE059 `WQE059_OPTIONAL_BARE_FRAGMENT_FIDELITY.patch`, exact Library source `libfile_2c559b806fac819185274f620af42ee0`. Its narrow LinkText regex change preserves `#fragment` on scheme-less hostnames (e.g. `bit.ly#part`), with 14 host assertions added to both test runners.

**Existing feature work retained, not newly invented here:** explicit plain-text send/receive using typed LSB2 framing; old URL-only LSB1 frame for compatibility; scheme-less domain extraction and normalization for selected links; Kyocera focus button, D-pad Down and Back escape behavior; owner-requested green UI; no automatic URL opening, SMS reading, Internet permission or background message forwarding. The owner physically verified v0.1.2's `https://example.org/` transfer only; none of the new v0.1.4.0 behavior has been physically tested.

**Current release metadata:** `versionName 0.1.4.0-source-preview`, `versionCode 13`, GitHub workflow manual artifact `LinkSender-v0.1.4.0-debug-APK`. Build timestamp comes from actual Gradle build, not from this source-preparation date. Release date remains `Not released` absent owner approval.

**Source boundary:** original WQB310 ZIP and v0.1.2 APK untouched. No owner phone, F:, private SMS, repository, credentials, installation, GitHub action or automation modified. This candidate is suitable for **owner-controlled future build**, subject to actual Android compile and supervised device tests. No build or signing claims.
