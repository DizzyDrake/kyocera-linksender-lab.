# CURRENT WQA387 OWNER-BUILD HANDOFF — v0.1.4.2 SOURCE ONLY

Read `WQA387_OWNER_BUILD_HANDOFF.md` first. This is source-only, NOT an APK. The current manual workflow artifact label is `LinkSender-v0.1.4.2-debug-APK`. Historical labels below are superseded. Preserve the owner-tested v0.1.2 APK and signing/rollback state. No owner devices, repositories or accounts were touched.

---

# CURRENT WQC065 OWNER-BUILD HANDOFF — v0.1.4.1 SOURCE ONLY

Read `WQC065_OWNER_BUILD_HANDOFF.md` first. This is source-only, NOT an APK. The current manual workflow artifact label is `LinkSender-v0.1.4.1-debug-APK`. Preserve the working owner-tested v0.1.2 APK. No owner devices, repositories or accounts have been touched.

---

## Historical WQC059 build guidance (artifact label below is superseded)

# CURRENT WQC059 OWNER-BUILD HANDOFF — v0.1.4.0 SOURCE ONLY

Read `WQC059_OWNER_BUILD_HANDOFF.md` first. This is source-only, NOT an APK. The current manual workflow artifact label is `LinkSender-v0.1.4.0-debug-APK`. The older WQB310 notes below are historical and must not override this label. Preserve the working v0.1.2 APK. No owner devices, repositories or accounts have been touched.

---

# HISTORICAL WQB310 BUILD HANDOFF — v0.1.3.8 SOURCE ONLY

That prior candidate was not compiled as an APK. Its manual GitHub Actions artifact is **LinkSender-v0.1.3.8-debug-APK**. The dated v0.1.3.3 and v0.1.3.2 notes below describe earlier source candidates; do not use their artifact names for this build. Preserve the owner-tested v0.1.2 APK and its signing/rollback plan.

---

**Historical visual edition (2026-10-25, v0.1.3.3):** v0.1.3.3 **SOURCE ONLY**, *not a new release/APK*. The source-prepared date is 2026-10-25. Every eventual APK build will show its actual UTC build date in the in-app About card and the credit **Made by GPT-6 and Aurora Williamsen**. The release date displays **Not released** by default. When and only when the owner explicitly approves issuing a release, a Gradle build may include `-PlinkSenderReleaseDateUtc=YYYY-MM-DD` (for example `gradle :app:assembleDebug -PlinkSenderReleaseDateUtc=2026-10-25` ONLY if that date really is the approved release date). The parameter is display metadata, not release approval. The historical workflow artifact was `LinkSender-v0.1.3.3-debug-APK`; the workflow has not been triggered.

**Known issues carried forward:** standalone URL punctuation/adjacent HTTP URL ambiguity. Do not install over working v0.1.2 automatically; preserve debug-signing fallback instructions below.

---

**WQ204 build note (2026-10-24):** This ancestor WQ204 archive was v0.1.3.2 *source*, not a built APK. The manual workflow artifact name is `LinkSender-v0.1.3.3-debug-APK`. Build/install only under owner supervision; first test with fabricated text and links, and preserve the working v0.1.2 rollback. A newly generated debug APK may not share the old signing key; Android can refuse an in-place update. Do not uninstall the working version merely to force an update without backing up the plan.

# Link Sender — build a test APK without Android Studio

**This ZIP is source plus build helpers, NOT a compiled APK.** Current owner trial target:
- Kyocera DuraXA Equip E4831: Android 10, API 29, physical D-pad and soft keys.
- Samsung Galaxy S20 on LineageOS (newer Android): Thor installer available.
- One app, paired Bluetooth, manually selected link or text; no SMS access, background forwarding or auto-open.

## Recommended: private GitHub Actions build (no Android Studio needed)

1. On GitHub, create an **empty private repository** you control, for example `kyocera-linksender-lab`.
2. Put the **contents of the LinkSender folder**, including hidden `.github/workflows/`, at the **repository root**. GitHub Desktop plus your desktop file manager is a reasonable route; commit and push. Do **not** upload this ZIP as one undissected blob.
3. In GitHub, select **Actions > Build Link Sender debug APK > Run workflow**. The workflow runs only when you ask; it doesn't build on every push.
4. When the run succeeds, open it and download artifact **LinkSender-v0.1.4.2-debug-APK**, unzip the artifact on your computer, and locate `app-debug.apk`.
5. Keep the debug APK as an **unaccepted test build** until source and install steps are reviewed. Never send real SMS content to the build; the repository should contain only the invented sample tests and app source.

The workflow installs JDK 17, Android SDK Platform 35 and Build-Tools 35, Gradle 8.7 and runs `gradle :app:assembleDebug`. Android Gradle Plugin is 8.6.1. Android Gradle `assembleDebug` outputs an **automatically debug-signed APK** usable for a private trial. *The workflow has not been run by the assistant.* GitHub Actions can consume private-repository usage quota.

If the run fails, copy the **first actual Gradle/Android compile error** into chat; I can correct the source and provide a new versioned build kit. A successful workflow run is the first genuine Android SDK build validation; it does not prove device interaction.

## Alternative: build on Windows 10, no Android Studio UI

One-time prerequisites: JDK 17, Android SDK command-line tools with Platform 35 + Build-Tools 35.0.0, Gradle 8.7, available on PATH. You can use Android Studio's SDK Manager as an easier installer if you prefer, but the IDE is not required to build. Set `ANDROID_HOME` if you use a nonstandard SDK path. From the extracted LinkSender project, double-click `build_apk_windows.cmd` or run it from an existing Command Prompt. It reports missing prerequisites instead of making system changes or installing anything. The output is `app\build\outputs\apk\debug\app-debug.apk`. **This script has not run on Windows.**

If you'd prefer not to set up a local SDK or upload code, I can guide you one command at a time after you choose a path.

## Installing the produced APK (separate, owner-controlled step)

- **On the LineageOS S20:** Your Thor app supports installing APKs via standard Android installation, Root, Shizuku or Dhizuku where configured. A standard debug-signed APK should normally not need a privileged bypass; Thor cannot compile source into an APK. You can use Thor if that's more convenient.
- **On the Android 10 Kyocera:** ADB from Windows is a practical route if touchscreen-oriented installation dialogs are difficult. *After you explicitly decide to install and enable USB debugging*, a single connected device can be installed with `adb install -r app-debug.apk`. Verify the target by `adb devices` first; with multiple devices use `adb -s DEVICE_SERIAL install -r app-debug.apk`. We have not performed this operation.
- Both phones need the app for its current Bluetooth sender/receiver design. For the new text payload, BOTH ends must use v0.1.3 or later; v0.1.3 link mode deliberately retains the older v0.1.2 URL wire frame. The older app cannot receive v0.1.3 text; pair devices through system Bluetooth settings, keep receiver foreground and explicitly press Receive before Send.
- A debug APK is already **signed** with a debug key; Thor's install overrides do **not** turn a non-APK ZIP, `.java` files, or an unsigned invalid APK into an Android application.
- The GitHub runner normally creates a temporary debug key. Rebuilding later may give a *different signature*. To replace an installed test APK, uninstall the old debug build first if installation reports signature mismatch; this discards local app data. Do not use this signing method for a long-term released app or assume a future debug APK can update in place.

## What remains unverified

No Android SDK or Gradle is installed in the assistant's isolated container. The earlier v0.1.3.2 candidate passed only Java host checks, mock Android UI tests and static inspection. Neither phone has been altered by this task. Real v0.1.3.2 APK compilation, focus behavior on the 240×320 D-pad, new text transfer, and scheme-less links still need owner-controlled build and physical trial. If the Kyocera messaging app does not permit Share or Copy for received links, this specific app will need a better intake method.

## Source provenance

This WQ-204 v0.1.3.2 candidate consolidates WQ-200/WQ-202 and Worker C's regex fix. Its ancestor WQ-200 was an isolated modification of frozen WQ-169 v0.1.2 source, with WQ-171 manual build helpers. The v0.1.2 owner-built APK successfully transferred `https://example.org/` in an owner-reported physical trial; this new source has NOT been built as an APK or tested on either phone. Original source, owner APK and all parent manifests are preserved separately.

## Official links

- Android debug builds: https://developer.android.com/build/building-cmdline
- AGP/Gradle requirements: https://developer.android.com/build/releases/about-agp
- Android SDK setup action: https://github.com/android-actions/setup-android
- Gradle GitHub action: https://github.com/gradle/actions
- GitHub manual runs: https://docs.github.com/en/actions/how-tos/manage-workflow-runs
- Thor: https://github.com/trinadhthatakula/Thor
