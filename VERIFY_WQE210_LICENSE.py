"""Read-only integrity check for WQE210 licensed Link Sender source tree.

The WQE175 historical source manifest is verified as a subset, and the new
WQE210 manifest verifies all payload members including the additive license.
"""
from pathlib import Path
import hashlib, json, sys
root=Path(__file__).resolve().parent
sha=lambda p:hashlib.sha256(p.read_bytes()).hexdigest()
old=json.loads((root/'SOURCE_MANIFEST.json').read_text())['files']
new=json.loads((root/'WQE210_LICENSE_MANIFEST.json').read_text())
actual={str(p.relative_to(root)).replace('\\','/'):sha(p) for p in root.rglob('*') if p.is_file() and not any(x in p.parts for x in ('__pycache__','build','.git')) and p.name!='WQE210_LICENSE_MANIFEST.json'}
expected=new['payload_sha256']
missing=sorted(set(expected)-set(actual));extra=sorted(set(actual)-set(expected))
mismatch=sorted(k for k in expected.keys() & actual.keys() if expected[k]!=actual[k])
old_mismatch=sorted(k for k,v in old.items() if actual.get(k)!=v)
license_ok=actual.get('LICENSE')=='cfc7749b96f63bd31c3c42b5c471bf756814053e847c10f3eb003417bc523d30'
result={'ok':not (missing or extra or mismatch or old_mismatch) and license_ok,'payload_files':len(expected),'historical_source_files':len(old),'missing':missing,'extra':extra,'mismatch':mismatch,'historical_mismatch':old_mismatch,'official_license_sha256_match':license_ok}
print(json.dumps(result,indent=2));sys.exit(0 if result['ok'] else 1)
