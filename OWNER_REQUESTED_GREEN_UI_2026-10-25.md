# Link Sender green UI and authorship/date — owner request 2026-10-25

**Status:** SOURCE-ONLY v0.1.3.3 candidate; no Android build, APK, installation, real phone test, published release or owner approval represented by this document.

## Visual palette

| Element | Color | Notes |
| --- | --- | --- |
| App background | `#79A684` | muted green |
| Status/received/About information boxes | `#DCECDE` | pale, desaturated mint-sage |
| Text editor and selections | `#F2F8F0` | near-white green |
| Buttons | `#E3F0E2` | light/desaturated green |
| Focused hardware D-pad button | `#FFF7C3` | distinct soft warm-yellow focus for visibility |
| Pressed button | `#B8D6B9` | gentle feedback |
| Borders | `#45734F` | muted dark-green outline |
| Text (all labels/buttons/cards/spinner rows) | `#000000` | black; input hint uses `#444C45` |

The design retains one-column scrolling for the smaller Kyocera screen. It does not assume touch or software-keyboard-only operation. Focus changes, legibility, contrast and screen fit need a physical Kyocera E4831 trial; synthetic checks cannot certify them.

## In-app About and APK date fields

- **Made by GPT-6 and Aurora Williamsen** (owner-specified credit).
- Source prepared: **2026-10-25** (fixed fact about this candidate, not the eventual release date).
- Version name comes from the Android Gradle-generated `BuildConfig.VERSION_NAME`.
- Build date UTC comes from Gradle `BuildConfig.BUILD_DATE_UTC`, set at build time.
- Release date UTC defaults to `Not released`. Only on owner-approved release build, provide `-PlinkSenderReleaseDateUtc=YYYY-MM-DD` to label that release (validated date-string shape). A build with this property does NOT itself authorize installation or publication.
- Version code 7 increments from v0.1.3.2 source code 6. Signing lineage remains unverified; Android may refuse to update v0.1.2 in place if debug keys differ.

## Preserved limits

Parent source `WQ204_LINK_SENDER_v0.1.3.2_CONSOLIDATED_OWNER_BUILD_SOURCE.zip`, SHA-256 `c31e5502d3acbf6d38444791763f8060d18f03fdcfeb9677ab0c0e7ab3cc2547`, retained unmodified. `LinkText`, `LinkFrame`, `OperationGate`, Android permissions and app ID left unchanged. Reviewer WQ205 found trailing punctuation stripped for selected links; WQD004 found joined/adjacent URLs may be ambiguous. WQD019 proposed optional standalone exact-link patch, **not integrated** in this visual-only candidate to avoid unexpectedly changing link behavior. The explicitly chosen exact-text path remains available.

## Offline tests

Run `bash core-tests/run.sh`, `python core-tests/check_source_safety.py`, `python core-tests/run_mock_ui_probe.py`, `python core-tests/test_stale_link_guard.py`, `python core-tests/test_green_ui_contract.py`. Fake Android API compilation is not an SDK build.

Do not automatically install, uninstall, pair phones, access SMS, connect to private accounts, touch F:, or change scheduled jobs.
