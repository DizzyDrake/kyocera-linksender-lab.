# WQ-20261025-278 — Link Sender apostrophe URL fidelity integration

This is an isolated, source-only successor to WQC038 v0.1.3.4, not an APK or a release.

- Parent ZIP SHA-256: `f85b8a3c4665b49a874ceede86a46a6ec9b41dbd29ac4ceba82f0795a32fe63f`.
- Parent `LinkText.java` SHA-256: `e3169dc22c6d564f754dc2bc82008454f24c89652da889f5a4edb482a481aab1`.
- WQB270 optional whole-URL correction: remove CANDIDATE gate when `valid(wholeUrl)` accepts the entire explicitly prefixed URL.
- New correction: inside URL tokens in prose, accept a raw apostrophe only when it is between ASCII alphanumeric characters. The delimiter at the end of a single-quoted link remains excluded. Scheme-less link paths share the same rule.
- Unchanged: HTTP(S) host/userinfo/port/length validation, conservative trailing punctuation trimming for prose, text-frame size/typing, link-frame compatibility, Bluetooth, SMS permissions, no-auto-opening, green UI and D-pad behavior.
- Remaining ambiguity: quoted URLs immediately followed by prose without whitespace, non-ASCII apostrophe neighbors, punctuation that may be part of a prose URL, and rare intentional terminal apostrophes. Use the exact-text transfer mode if bytewise preservation matters.
- Host Java tests and fake-Android probes are not evidence of actual Android SDK compilation, Windows 10 batch execution, Kyocera D-pad behavior or Bluetooth transfer.
- No owner devices, GitHub repository, credentials, live archive, or F: accessed.
