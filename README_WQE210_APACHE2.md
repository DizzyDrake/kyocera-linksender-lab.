# Link Sender v0.1.4.3 — Apache-2.0 licensed source candidate (WQE210)

**Current candidate:** the WQE175 source tree with an **additive** Apache-2.0 license overlay. All 81 prior ZIP members are byte-identical, including the original historical `SOURCE_MANIFEST.json` and source verification script. The original WQE175 ZIP remains separately preserved in the ChatGPT Library.

## Quick start

1. Read `README_WQE175_FIRST.md` for the actual v0.1.4.3 behavior and owner-build context. Read `LICENSE_SCOPE.md` and `LICENSE` for licensing.
2. To verify the entire licensed source tree: `python VERIFY_WQE210_LICENSE.py` (Python 3.9+). The old `VERIFY_WQE175_SOURCE.py` checks only the original 80-member source tree and **will intentionally flag these five added licensing files as extras**. It is preserved as historical evidence, not the correct verifier for this overlay.
3. For offline logic and synthetic UI checks, run `bash core-tests/run.sh` with JDK 17, then `python core-tests/check_source_safety.py`, `python core-tests/run_mock_ui_probe.py`, `python core-tests/test_stale_link_guard.py`, `python core-tests/test_green_ui_contract.py`, `python core-tests/test_wqa387_focus_integration.py`, `python core-tests/test_wqe175_dropdown_focus.py`. These are not native Android SDK/device tests.
4. Optional future owner-controlled Android build uses the existing `BUILD_AN_APK.md` and manual GitHub workflow. No build, upload, installation, deployment, public repository or release was performed here.

**Version metadata and functionality unchanged:** `org.roarincreations.linksender`, `versionCode 16`, `versionName 0.1.4.3-source-preview`. Green/sage UI, focus markers, Bluetooth protocol and application permissions are unchanged. Do not uninstall an existing working Kyocera app merely to test signing compatibility.

**Provenance:** WQE175 ZIP SHA-256 `a6068645fbdee4e20c5e3627ad2c6e6a35dc1926b7ce8864f73cd314ea015a46`; official Apache-2.0 text SHA-256 `cfc7749b96f63bd31c3c42b5c471bf756814053e847c10f3eb003417bc523d30`.
