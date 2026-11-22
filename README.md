# Link Sender

![Link Sender icon](repo-assets/link-sender-icon-512.png)

**Send a link. Send a note. Keep the buttons.**

Link Sender moves links and text between paired Android devices over Bluetooth. It was designed around the Kyocera DuraXA Equip's physical keypad and also supports touch-enabled Android phones.

**Designed and built by Aurora and GPT-6.**

## Using Link Sender

Pair the phones in Android's Bluetooth settings. On the receiving phone, choose **Receive one link or text**. On the sending phone, choose the paired receiver, then **Send selected link** or **Send entered text exactly**.

You can paste text from the clipboard or share text from another app. Link Sender picks out links so you can choose one, and it can send the full entered text as well. The receiver offers **Copy received** and an explicit option to open a received link.

The green interface has D-pad-friendly buttons, dropdowns and selection indicators. Transfers are manually initiated and acknowledged over Bluetooth. The app works offline.

## Compatibility

- **Android:** Android 10 (API 29) and newer; Bluetooth Classic on both devices
- **Original device:** Kyocera DuraXA Equip E4831
- **Package:** `org.roarincreations.linksender`
- **Current Android source:** v0.1.4.3 (versionCode 16)

There is also a **separate Windows desktop receiver source project** for receiving Bluetooth transfers. Its native Windows build and real-device interoperability require their own testing.

## Build the Android app

The repository includes a manual GitHub Actions workflow:

1. Open **Actions → Build Link Sender debug APK**.
2. Choose **Run workflow**.
3. Download `LinkSender-v0.1.4.3-debug-APK` from the completed run and extract `app-debug.apk`.

The workflow uses Java 17, Gradle 8.7, and Android SDK 35. You can also build locally with the same toolchain using `gradle :app:assembleDebug`.

Debug APKs use a build-specific signing certificate. Android requires a matching certificate to update an installed copy while preserving its data, so check your existing installation before updating.

## Project notes

The app requests Bluetooth access. It keeps the connection user-controlled, and received links open only when you select that action. The source includes protocol, URL parsing, cancellation, and source-level UI tests under `core-tests/`.

The v0.1.4.3 source and its tests are documented in `README_WQE175_FIRST.md` and `README_WQE210_APACHE2.md`. The icon resources added in this update work with Android 10's adaptive launcher icons.

## License

Apache License 2.0 for the original project material where contributors hold the necessary rights. See [LICENSE](LICENSE) and [LICENSE_SCOPE.md](LICENSE_SCOPE.md) for details.
