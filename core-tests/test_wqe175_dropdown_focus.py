"""Source-level Link Sender focus assertions. NOT a device/Android runtime test."""
from pathlib import Path
import re
root=Path(__file__).resolve().parents[1]
main=(root/'app/src/main/java/org/roarincreations/linksender/MainActivity.java').read_text()
style=(root/'app/src/main/java/org/roarincreations/linksender/LinkSenderStyle.java').read_text()
gradle=(root/'app/build.gradle').read_text()
workflow=(root/'.github/workflows/build-linksender-debug-apk.yml').read_text()
checks={
 'both spinners use state-aware background': main.count('setBackground(LinkSenderStyle.spinnerBackground())') == 2,
 'both spinners use adapter with owning picker': 'spinnerAdapter(labels, devices)' in main and 'spinnerAdapter(labels, linkChoices)' in main,
 'selected item marker in dropdown': 'committed ? "✓ " : "   "' in main,
 'committed item uses actual picker position': 'position == picker.getSelectedItemPosition()' in main,
 'closed selection visibly marked': '"✓ " + values.get(position)' in main,
 'long names wrap to two lines': main.count('setMaxLines(2)') == 2,
 'row background is state-aware': 'v.setBackground(LinkSenderStyle.dropdownRowBackground(committed))' in main,
 'no fixed dropdown CARD color': 'v.setBackgroundColor(LinkSenderStyle.CARD)' not in main,
 'spinner focus state': 'static StateListDrawable spinnerBackground()' in style and 'state_focused' in style,
 'dropdown selected state': 'state_selected' in style,
 'dropdown activated state': 'state_activated' in style,
 'dropdown focused state': 'state_focused' in style,
 'dropdown pressed state': 'state_pressed' in style,
 'committed row uses sage base': 'committed ? BUTTON : CARD' in style,
 'existing button focus retained': 'static StateListDrawable buttonBackground()' in style,
 'deferred editor focus retained': 'if (!useEntered.hasFocus()) editTextButton.requestFocus();' in main,
 'editor D-pad exit retained': 'keyCode == KeyEvent.KEYCODE_DPAD_DOWN' in main,
 'send-selected checks selected device': 'devices.getSelectedItemPosition()' in main,
 'Bluetooth service ID unchanged': '4f713f88-61ce-4f47-85b5-4e0d9a119e62' in main,
 'manual-only workflow': 'workflow_dispatch:' in workflow and 'push:' not in workflow,
 'package identity unchanged': "applicationId 'org.roarincreations.linksender'" in gradle,
 'version increment for update': 'versionCode 16' in gradle and "versionName '0.1.4.3-source-preview'" in gradle,
 'no automatic opening': 'Nothing was opened automatically.' in main,
}
for name,ok in checks.items():
 print(('PASS' if ok else 'FAIL')+' '+name)
assert all(checks.values()),'Source contract check failed'
print('PASS '+str(len(checks))+'/'+str(len(checks))+' source-level assertions; Android SDK and phone untested')
