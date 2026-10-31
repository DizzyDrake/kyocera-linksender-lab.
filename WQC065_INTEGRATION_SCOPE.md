# WQC065 v0.1.4.1 — reviewed bare-domain suggestion fidelity integration

**Task:** WQ-C-20261026-017. **Source only; no APK or device acceptance.**

Frozen parent: WQC059 v0.1.4.0 SHA-256 `d8f32f8bed61ac6c6b70268d4dbf245e02c13ea06a2e43e92e98df858804db74`.

Applied exactly in this order to a disposable extracted copy with `patch -p1 --fuzz=0`:

1. WQB324 `WQB324_OPTIONAL_BARE_AUTHORITY_BOUNDARY.patch` SHA-256 `46a3c19325572516196178d467f1432cb9ce0ec5407ab88f64247cfd4aed4a02`: reject partial URL suggestions on malformed port/authority continuations such as `bit.ly:123456/path` and `bit.ly@other.example/path`.
2. WQA321 `WQA321_OPTIONAL_BARE_SUFFIX_GUARD_AFTER_WQB324.patch` SHA-256 `2374dd6d56553a3f4aea36a3659db17b482d0f2d17820a5561d59ef80cb9bfd5`: reject additional malformed continuations such as `bit.ly%2Fsecret`, `bit.ly\coupon`, and `bit.ly..other.com`.

Both original patch bytes, frozen parent manifest and parent test report are preserved under `provenance/`. The patch sources and task ownership remain A/B, not reassigned to C. C integrated both and registered their tests for Windows batch and Unix shell. The conservative heuristic is not a complete URL grammar; explicit text sending is preserved for malformed or unusual content.

**Unchanged runtime features:** explicit LSB2 UTF-8 text, LSB1 URL compatibility, Kyocera focus workaround, selected-link stale guard, receiver manual display/copy, green UI, permissions. The only new production source edit beyond version metadata is `LinkText.java` regex boundary. No SMS/contacts/Internet permission, network lookup or auto-opening added.

**Tests:** Run `bash core-tests/run.sh` or `core-tests\run.bat` with JDK 17+, then the four Python scripts listed in `WQC065_OWNER_BUILD_HANDOFF.md`. These host tests cannot establish Android build or physical behavior. Source prepared 2026-10-26; not released.
