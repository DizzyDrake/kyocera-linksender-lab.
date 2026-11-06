# Link Sender v0.1.4.3 — paired-device and link-picker focus repair

**Worker E:** `WQ-E-20261031-006` · October 31, 2026 · **source-only offline candidate**. This is a continuation of frozen WQA387 v0.1.4.2 (parent ZIP SHA-256 `bddc5a639725c95ad38ea8ae58b72dde60811da22ac08d160a42821bd218d349`), not an installed APK. The owner-reported newer Link Sender app is installed and visually liked; exact installed APK identity is inferred, not independently confirmed. No new-version Bluetooth transfer trial has occurred.

## What changed

The **Paired receiving phone** and **Optional selected link** dropdowns now use a pale-yellow focus/selection background when navigated by the D-pad. The currently committed selection carries a visible `✓` in the closed picker and beside its row in the expanded list; that row has a pale-sage base when it is not the moving focused row. Both lists wrap labels to two lines, rather than relying on a fixed-color single-line row. This aims to make selection visible while preserving the owner-liked green/sage palette and current scroll/editing controls.

**Important:** Native Android Spinner popup selection/focus state differs by device/theme. The state-aware drawable is a source-level repair candidate, not proof of visible focus on the E4831. In particular, a list selector rendered behind an opaque row may behave differently; the owner phone trial decides. The selected-link issue was not observed by the owner; it received the analogous preventative change, not a confirmed defect label.

Version metadata: `versionCode=16`, `versionName=0.1.4.3-source-preview`; Android package `org.roarincreations.linksender` unchanged. The manual-only GitHub Actions build artifact name is `LinkSender-v0.1.4.3-debug-APK`. Bluetooth protocol, permissions, send/receive code, and existing project data handling were not modified. The source ZIP includes historical WQA387 documents; this file supersedes their *version/build* instructions for this candidate, not their past evidence.

## Optional owner-controlled Windows/GitHub trial

1. Preserve the existing installed app and its data. If/when ready, upload **this complete source tree** to the existing private repository in a new branch or commit, and run the manual workflow. Do not uninstall the current app to force a signature mismatch. If an update fails with `INSTALL_FAILED_UPDATE_INCOMPATIBLE`, preserve the error and existing app; a data-preserving signing strategy or explicit uninstall/data-loss decision is needed.
2. On the Kyocera, use D-pad to focus **Paired receiving phone**, open it, move between rows and confirm a yellow highlight tracks the current row while the committed choice remains marked with `✓`. Check the closed picker also shows the chosen phone and a yellow background when focused. Repeat for **Optional selected link** if convenient; it was not previously owner-tested.
3. Report the installed version if available, whether the highlight visibly moves, whether selection sticks after closing, whether scrolling/text-editing remains good, and any build/install error verbatim. Do not test Bluetooth with private links. The S20 Bluetooth test may wait; it is independent of this UI change.

## Evidence and limits

- Original source ZIP SHA-256 independently matched before patching. All original source files remain in the prior Library candidate; this new source tree is separately versioned.
- `core-tests/run.sh` host Java tests cover URL/protocol logic, not Android UI. `core-tests/test_wqe175_dropdown_focus.py` verifies source-level focus states and retention of key behaviors; existing WQA387 metadata assertions were updated to this candidate version and remain source-only.
- No Android SDK or Gradle build available in this environment. No APK compiled, no on-device D-pad/scrolling/selected-row rendering tested, no S20 transfer, no owner file/system/GitHub access, no deployment.
