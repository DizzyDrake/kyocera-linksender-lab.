# WQB280 Link Sender v0.1.3.6 integration scope

- Frozen parent: WQB278 v0.1.3.5 SHA-256 `691921ed1743120b758c531b99734bd77ff649532a15e473ae0970796ac598e5`; clean ZIP CRC and 42/42 manifest.
- Exact WQA279 optional patch SHA-256 `81239310a55900987b5561c1cb4390fd7d4223ad845c43b3cb4cb47d2feeede7`, applied with `patch -p1`. This changes three quantifiers in the Java LinkText regex.
- Source copy only; version metadata and host regression runners updated. WQA279 test copied with provenance, WQB280 tests newly written.
- No Android SDK, APK, Windows 10, actual Kyocera/S20 focus, Bluetooth or installation test. Owner-tested v0.1.2 APK remains fallback.
- Known unresolved: Unicode-adjacent apostrophe fidelity in embedded prose; ambiguous punctuation and link-selection boundaries.
- Next: independent review, owner-authorized Android SDK build and safe two-phone test with fabricated text, preserving v0.1.2 fallback.
