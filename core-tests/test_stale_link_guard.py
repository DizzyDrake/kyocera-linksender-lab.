"""WQ201-origin independent fake-Android UI regression, integrated into WQ202 candidate.
The parent script creates fake SDK types, compiles actual MainActivity, and runs
probe. This wrapper adds an assertion that leaving editing refreshes link choices.
Not real Android or Bluetooth testing.
"""
from pathlib import Path
import subprocess, sys, tempfile, shutil
base = Path(__file__).resolve().parent
source = Path(sys.argv[1]) if len(sys.argv)>1 else base.parent
with tempfile.TemporaryDirectory(prefix='wq201_') as tmp:
    copy = Path(tmp) / 'source'
    shutil.copytree(source, copy)
    script = copy / 'core-tests' / 'run_mock_ui_probe.py'
    s = script.read_text()
    needle = '  System.out.println("PASS: mock initial focus, DPAD DOWN, Back focus escape; NOT physical Kyocera proof");'
    assert s.count(needle) == 1, 'Probe insertion anchor changed'
    addition = '''  call(b,"updateChoices",new Class<?>[]{String.class},"https://first.example/");
  input.setText("https://second.example/");
  input.requestFocus();
  if(!input.dispatchKey(android.view.KeyEvent.KEYCODE_DPAD_DOWN,android.view.KeyEvent.ACTION_DOWN))
      throw new AssertionError("D-pad DOWN no longer exits editing");
  Field fc=MainActivity.class.getDeclaredField("choices");fc.setAccessible(true);
  java.util.List<?> links=(java.util.List<?>)fc.get(b);
  if(!links.equals(java.util.List.of("https://second.example/")))
      throw new AssertionError("STALE LINK CHOICES: editor shows second.example but picker retains " + links);
  call(b,"updateChoices",new Class<?>[]{String.class},"https://third.example/");
  input.setText("https://fourth.example/");
  call(b,"sendSelected",new Class<?>[]{});
  if(!links.equals(java.util.List.of("https://fourth.example/")))
      throw new AssertionError("STALE LINK CHOICES: send action did not reconcile edited input: " + links);
  if(!status(b).contains("Review the selected link"))
      throw new AssertionError("Missing safety stop for changed input: " + status(b));
  System.out.println("PASS: send button stops and requires link review after unsaved edit");
  System.out.println("PASS: D-pad exit refreshes link picker after edit");
'''
    script.write_text(s.replace(needle, addition + needle))
    r = subprocess.run([sys.executable, str(script)], text=True, capture_output=True)
    print(r.stdout, end='')
    print(r.stderr, end='', file=sys.stderr)
    sys.exit(r.returncode)
