# WQE175 follow-on: update only current version assertions; WQA387 ZIP remains frozen.
"""Owner-theme source/API contract checks; NOT an Android SDK or phone render test."""
from pathlib import Path
import re
root = Path(__file__).resolve().parents[1]
ui = (root/'app/src/main/java/org/roarincreations/linksender/MainActivity.java').read_text()
style = (root/'app/src/main/java/org/roarincreations/linksender/LinkSenderStyle.java').read_text()
gradle = (root/'app/build.gradle').read_text()
manifest=(root/'app/src/main/AndroidManifest.xml').read_text()
expected = {'GREEN':(121,166,132), 'CARD':(220,236,222), 'EDITOR':(242,248,240), 'BUTTON':(227,240,226), 'FOCUS':(255,247,195), 'INK':None}
for k,rgb in expected.items():
    assert k+' = ' in style, k
    if rgb: assert re.search(r'\b'+k+r' = Color\.rgb\('+r'\s*,\s*'.join(map(str,rgb))+r'\)',style), k
    else: assert 'INK = Color.BLACK' in style
assert 'new int[]{android.R.attr.state_focused}' in style
for marker in ['status = infoBox(', 'received = infoBox(', 'BuildConfig.VERSION_NAME', 'BuildConfig.BUILD_DATE_UTC', 'BuildConfig.RELEASE_DATE_UTC', 'Made by GPT-6 and Aurora Williamsen', 'getDropDownView', 'setTextColor(LinkSenderStyle.INK)', 'scroll.post(() -> {']:
    assert marker in ui, marker
assert 'if (!useEntered.hasFocus()) editTextButton.requestFocus();' in ui
assert "versionCode 16" in gradle and "versionName '0.1.4.3-source-preview'" in gradle
assert 'linkSenderReleaseDateUtc' in gradle and "'Not released'" in gradle
assert 'buildConfigField' in gradle and 'new Date().format' in gradle
assert 'android:label="Link Sender"' in manifest and 'READ_SMS' not in manifest
print('PASS 22 UI/style/metadata source contract checks; NOT Android build or visual acceptance')
