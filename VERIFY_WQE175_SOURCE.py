"""Read-only SHA-256 verification of this versioned source tree."""
from pathlib import Path
import hashlib,json,sys
root=Path(__file__).resolve().parent
m=json.loads((root/'SOURCE_MANIFEST.json').read_text())
expected=m['files']
actual={str(p.relative_to(root)).replace('\\','/'):hashlib.sha256(p.read_bytes()).hexdigest() for p in root.rglob('*') if p.is_file() and p.name!='SOURCE_MANIFEST.json' and not any(x in p.parts for x in ('__pycache__','build','.git'))}
missing=sorted(set(expected)-set(actual));extra=sorted(set(actual)-set(expected));mismatches=sorted(k for k in expected.keys()&actual.keys() if expected[k]!=actual[k]);ok=not(missing or extra or mismatches)
print(json.dumps({'ok':ok,'verified':len(expected)-len(mismatches),'expected':len(expected),'missing':missing,'extra':extra,'mismatches':mismatches},indent=2));sys.exit(0 if ok else 1)
