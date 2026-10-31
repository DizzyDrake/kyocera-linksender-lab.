"""Offline static checks only; not an Android SDK compilation or security proof."""
from pathlib import Path
import xml.etree.ElementTree as ET
root = Path(__file__).resolve().parents[1]
app = (root/'app/src/main/java/org/roarincreations/linksender/MainActivity.java').read_text()
gate=(root/'app/src/main/java/org/roarincreations/linksender/OperationGate.java').read_text()
manifest=ET.parse(root/'app/src/main/AndroidManifest.xml').getroot()
keys=['if (!gate.track(connection))','connection.connect();','if (!gate.track(waiting))','waiting.accept()', 'if (!gate.track(incoming))', 'gate.cancel();', 'final OperationGate gate = beginOperation();', 'private volatile OperationGate activeOperation;']
for k in keys: assert k in app,k
assert app.index('if (!gate.track(connection))') < app.index('connection.connect();')
assert app.index('if (!gate.track(waiting))') < app.index('waiting.accept()')
assert 'cancelRequested' not in app and 'private volatile BluetoothSocket socket' not in app
assert 'private volatile BluetoothServerSocket server' not in app
assert 'public synchronized boolean isCancelled()' in gate
perms={x.attrib.get('{http://schemas.android.com/apk/res/android}name','') for x in manifest.findall('uses-permission')}
assert 'android.permission.READ_SMS' not in perms
assert 'android.permission.INTERNET' not in perms
assert 'android.permission.READ_CONTACTS' not in perms
assert 'android.permission.BLUETOOTH_CONNECT' in perms
frame=(root/'app/src/main/java/org/roarincreations/linksender/LinkFrame.java').read_text()
parser=(root/'app/src/main/java/org/roarincreations/linksender/LinkText.java').read_text()
assert 'LinkFrame.sendText(connection.getOutputStream(), payload)' in app
assert 'LinkFrame.receiveMessage(incoming.getInputStream())' in app
assert 'input.setOnKeyListener' in app and 'KEYCODE_DPAD_DOWN' in app and 'onBackPressed()' in app
assert 'scroll.post(() -> {' in app and 'if (!useEntered.hasFocus()) editTextButton.requestFocus();' in app
assert 'MAGIC_URL_V1' in frame and 'MAGIC_TYPED_V2' in frame
assert 'MAX_BYTES = 8192' in frame and 'validText' in frame
assert 'https://' in parser
assert 'android.permission.READ_SMS' not in perms and 'android.permission.INTERNET' not in perms
print('PASS 25 static source/API integration assertions; Android compilation not run')
