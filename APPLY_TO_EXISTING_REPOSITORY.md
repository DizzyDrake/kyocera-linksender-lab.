# Link Sender — GitHub icon + README update

This ZIP contains the new green L-and-paper-airplane launcher icon, the public README, and the repository About text. It targets the v0.1.4.3 Android source layout at `app/src/main/`.

## Upload to the existing repository

1. Extract the ZIP, preserving its directories.
2. Copy `README.md`, `repo-assets/`, and `app/src/main/` into the matching paths in the existing repository.
3. Review `app/src/main/AndroidManifest.xml` before replacing it. The only intended change from the preserved November 1 v0.1.4.3 source is adding `android:icon` and `android:roundIcon` to `<application>`. If your current manifest has other edits, keep them and add these two attributes manually.
4. Paste `GITHUB_ABOUT_350_CHARACTERS.txt` into the repository's About/Description field, then commit the changes.
5. Run the existing **Build Link Sender debug APK** workflow to build an APK carrying the new launcher icon.

The uploaded `LinkSender-v0.1.4.3-debug-APK.zip` was inspected as the prior compiled artifact. That APK remains unchanged: applying the GitHub overlay requires a new build before the icon appears on the phone.

The ZIP includes no Java source modifications, Bluetooth protocol changes, signing keys, or compiled binaries. It does not include the separate Windows receiver source. A public repository URL/branch could not be confirmed, so the overlay was validated against the Library's canonical v0.1.4.3 Android source.
