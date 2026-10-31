# WQC038 Link Sender green UI + exact standalone HTTP(S) link candidate

**Owner intent:** Green interface/About credit and deliberate text/link transfer. **Worker proposal:** integrate WQE035 narrow explicit URL preservation into the green source-only parent. This is not a new owner-approved universal punctuation rule.

**Source parent:** owner-prepared v0.1.3.3 ZIP SHA-256 `223183e64a1720d133e293208c22e85b1136fb159b70ab98ea13c9dbfc21e904`. **Patch parent:** WQE035 SHA-256 `23a7b0a82a06dc20dd8542dc08f097a5d4ca5be51cc4adb5cb60a944a210f88e`. Both immutable.

**Change:** In `LinkText.extract`, when the complete trimmed input is a valid explicitly prefixed HTTP(S) URL, preserve its exact trailing punctuation. Prose and bare-domain links still use conservative trimming. No change to `LinkFrame`, Bluetooth, permissions, SMS access, opening behavior, focus navigation or green styling.

**Limitations:** A standalone URL ending in punctuation can itself be ambiguous; the selected-link picker must still be reviewed. Links in prose may still be shortened. Android/physical keyboard behavior, installed update signature compatibility and both-phone transfer require owner-controlled trial. Source-only metadata is not an APK or release.

**Safety:** Do not deploy or overwrite v0.1.2 on either phone automatically. No live data, F:, accounts, GitHub writes or automations.
