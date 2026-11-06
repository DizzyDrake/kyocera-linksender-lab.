# WQE175 follow-on: update only current version assertions; WQA387 ZIP remains frozen.
"""Source-only WQA387 focus integration checks; not an Android build."""
import unittest
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
class Integration(unittest.TestCase):
 def test_deferred_focus_guard(self):
  text=(ROOT/'app/src/main/java/org/roarincreations/linksender/MainActivity.java').read_text()
  self.assertIn('scroll.post(() -> {',text)
  self.assertIn('if (!useEntered.hasFocus()) editTextButton.requestFocus();',text)
  self.assertIn('leaveTextEditing()',text)
 def test_source_metadata(self):
  gradle=(ROOT/'app/build.gradle').read_text()
  workflow=(ROOT/'.github/workflows/build-linksender-debug-apk.yml').read_text()
  self.assertIn('versionCode 16',gradle)
  self.assertIn("versionName '0.1.4.3-source-preview'",gradle)
  self.assertIn('LinkSender-v0.1.4.3-debug-APK',workflow)
  self.assertIn('workflow_dispatch:',workflow)
 def test_no_sms_permission_and_receiver_still_nonautoopen(self):
  manifest=(ROOT/'app/src/main/AndroidManifest.xml').read_text()
  self.assertNotIn('READ_SMS',manifest)
  self.assertNotIn('INTERNET',manifest)
  frame=(ROOT/'app/src/main/java/org/roarincreations/linksender/LinkFrame.java').read_text()
  self.assertIn('MAGIC_TYPED_V2',frame)
  self.assertIn('MAX_BYTES = 8192',frame)
 def test_preserved_historical_manifest(self):
  self.assertTrue((ROOT/'WQC065_SOURCE_MANIFEST_FROZEN.json').is_file())
  self.assertTrue((ROOT/'WQC065_OWNER_BUILD_HANDOFF.md').is_file())
if __name__=='__main__':unittest.main()
