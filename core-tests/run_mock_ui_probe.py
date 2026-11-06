"""Synthetic Android-shaped UI state probe. Compiles with generated fake SDK types,
NOT Android SDK or a deployable APK. Requires Python 3.9+, JDK 17+.
Tests that queued success from a cancelled/stale Bluetooth operation is suppressed.
"""
from pathlib import Path
import shutil, subprocess, os, textwrap, sys, tempfile, atexit
base=Path(__file__).resolve().parents[1]
work=Path(tempfile.mkdtemp(prefix='linksender_fake_android_'))
atexit.register(lambda: shutil.rmtree(work,ignore_errors=True))
stubs=work/'test_stub_src'; out=work/'mock_compile'
stubs.mkdir();out.mkdir()
source={
'android.Manifest':'''public final class Manifest { public static final class permission { public static final String BLUETOOTH_CONNECT="android.permission.BLUETOOTH_CONNECT"; }}''',
'android.content.pm.PackageManager':'''public class PackageManager { public static final int PERMISSION_GRANTED=0; }''',
'android.os.Build':'''public class Build { public static class VERSION { public static int SDK_INT=29; }}''',
'android.os.Bundle':'''public class Bundle {}''',
'android.graphics.Color':'''public class Color { public static final int BLACK=0xff000000; public static int rgb(int r,int g,int b){return 0xff000000|(r<<16)|(g<<8)|b;} }''',
'android.graphics.drawable.Drawable':'''public class Drawable {}''',
'android.graphics.drawable.GradientDrawable':'''public class GradientDrawable extends Drawable { public void setColor(int c){} public void setCornerRadius(float f){} public void setStroke(int w,int c){} }''',
'android.graphics.drawable.StateListDrawable':'''public class StateListDrawable extends Drawable {public void addState(int[] s,Drawable b){} }''',
'org.roarincreations.linksender.BuildConfig':'''public final class BuildConfig { public static final String VERSION_NAME="0.1.3.3-source-preview",BUILD_DATE_UTC="2026-10-25",RELEASE_DATE_UTC="Not released"; }''',
'android.os.Looper':'''public class Looper {public static Looper getMainLooper(){ return new Looper(); }}''',
'android.os.Handler':'''import java.util.*; public class Handler { private static final List<Runnable> Q = new ArrayList<>(); public Handler(Looper l){} public void post(Runnable r){Q.add(r);} public static void flush(){ while(!Q.isEmpty()) Q.remove(0).run(); } }''',
'android.content.Context':'''public class Context { public static final String CLIPBOARD_SERVICE="clipboard", INPUT_METHOD_SERVICE="input_method"; public Object getSystemService(String s){return INPUT_METHOD_SERVICE.equals(s) ? new android.view.inputmethod.InputMethodManager() : new ClipboardManager();} }''',
'android.content.Intent':'''public class Intent {public static final String ACTION_SEND="ACTION_SEND",EXTRA_TEXT="extra"; private final String action,type; private final CharSequence shared; public Intent(){this(null,null,null);} public Intent(String a,String t,CharSequence v){action=a;type=t;shared=v;} public String getAction(){return action;} public String getType(){return type;} public CharSequence getCharSequenceExtra(String k){return EXTRA_TEXT.equals(k)?shared:null;} }''',
'android.content.ClipData':'''public class ClipData { public static class Item { public CharSequence coerceToText(Context c){return "";} } public static ClipData newPlainText(String l, String v){return new ClipData();} public int getItemCount(){return 0;} public Item getItemAt(int i){return new Item();} }''',
'android.content.ClipboardManager':'''public class ClipboardManager { public boolean hasPrimaryClip(){return false;} public ClipData getPrimaryClip(){return null;} public void setPrimaryClip(ClipData clip){} }''',
'android.app.Activity':'''import android.content.*; import android.os.*; import android.view.*; public class Activity extends Context { protected void onCreate(Bundle b){} protected void onDestroy(){} protected void onNewIntent(Intent i){} private static Intent launchIntent=new Intent(); private Intent currentIntent; public static void setLaunchIntent(Intent i){launchIntent=i;} public Intent getIntent(){return currentIntent==null?launchIntent:currentIntent;} public void setIntent(Intent i){currentIntent=i;} public void setContentView(View v){} public void onBackPressed(){ throw new AssertionError("Unexpected activity exit"); } public int checkSelfPermission(String s){return 0;} public void requestPermissions(String[] p,int c){} public void onRequestPermissionsResult(int c,String[] p,int[] g){} }''',
'android.view.Gravity':'''public class Gravity {public static final int CENTER_VERTICAL=16;}''',
'android.view.View':'''public class View { public interface OnClickListener {void onClick(View v);} public interface OnKeyListener {boolean onKey(View v,int keyCode,KeyEvent event);} private static View focused; private OnKeyListener key; public boolean requestFocus(){focused=this;return true;} public boolean hasFocus(){return focused==this;} private static final java.util.List<Runnable> POSTS=new java.util.ArrayList<>(); public void post(Runnable r){POSTS.add(r);} public static void flushPosted(){while(!POSTS.isEmpty())POSTS.remove(0).run();} public Object getWindowToken(){return new Object();} public void setOnKeyListener(OnKeyListener k){key=k;} public void setBackground(android.graphics.drawable.Drawable d){} public void setBackgroundColor(int c){} public void setLayoutParams(ViewGroup.LayoutParams p){} public boolean dispatchKey(int code,int action){return key!=null && key.onKey(this,code,new KeyEvent(action));} }''',
'android.view.ViewGroup':'''public class ViewGroup extends View { public static class LayoutParams { public static final int MATCH_PARENT=-1,WRAP_CONTENT=-2; public LayoutParams(int w,int h){} }}''',
'android.widget.LinearLayout':'''import android.content.*; import android.view.*; public class LinearLayout extends ViewGroup {public static final int VERTICAL=1; public LinearLayout(Context c){} public void setOrientation(int v){} public void setPadding(int a,int b,int c,int d){} public void addView(View v){} public void addView(View v,ViewGroup.LayoutParams l){} public static class LayoutParams extends ViewGroup.LayoutParams { public LayoutParams(int w,int h){super(w,h);} public void setMargins(int l,int t,int r,int b){} }}''',
'android.widget.TextView':'''import android.content.*; import android.view.*; public class TextView extends View { private String txt=""; public TextView(Context c){} public void setText(CharSequence s){txt=s.toString();} public String getText(){return txt;} public void setTextSize(int s){} public void setPadding(int a,int b,int c,int d){} public void setTextColor(int c){} public void setHintTextColor(int c){} public void setGravity(int g){} public void setSingleLine(boolean b){} public void setMaxLines(int n){} }''',
'android.widget.Button':'''import android.content.*; public class Button extends TextView {public Button(Context c){super(c);} public void setAllCaps(boolean b){} public void setFocusable(boolean b){} public void setOnClickListener(android.view.View.OnClickListener a){} public void setEnabled(boolean b){} }''',
'android.view.KeyEvent':'''public class KeyEvent { public static final int ACTION_DOWN=0, ACTION_UP=1, KEYCODE_DPAD_DOWN=20, KEYCODE_BACK=4; private final int action; public KeyEvent(int action){this.action=action;} public int getAction(){return action;} }''',
'android.view.inputmethod.InputMethodManager':'''public class InputMethodManager { public boolean hideSoftInputFromWindow(Object token,int flags){return true;} }''',
'android.widget.EditText':'''import android.content.*; public class EditText extends TextView {public EditText(Context c){super(c);} public void setSingleLine(boolean b){} public void setMinLines(int n){} public void setHint(String s){} }''',
'android.widget.ScrollView':'''import android.content.*; import android.view.*; public class ScrollView extends ViewGroup {public ScrollView(Context c){} public void addView(View v){} }''',
'android.widget.Spinner':'''import android.content.*; import android.view.*; public class Spinner extends View {public Spinner(Context c){} public void setFocusable(boolean b){} public void setAdapter(Object a){} public int getSelectedItemPosition(){return 0;} }''',
'android.widget.ArrayAdapter':'''import java.util.*;import android.content.*;import android.view.*;public class ArrayAdapter<T> { public ArrayAdapter(Context c,int id,List<T> labels){} public View getView(int i,View cv,ViewGroup p){return new android.widget.TextView(new android.content.Context());} public View getDropDownView(int i,View cv,ViewGroup p){return getView(i,cv,p);} public void setDropDownViewResource(int id){} }''',
'android.R':'''public final class R { public static final class layout {public static final int simple_spinner_dropdown_item=1,simple_spinner_item=2;} public static final class attr {public static final int state_focused=1,state_pressed=2,state_selected=3,state_activated=4;} }''',
'android.bluetooth.BluetoothAdapter':'''import java.util.*; public class BluetoothAdapter { public static BluetoothAdapter getDefaultAdapter(){return new BluetoothAdapter();} public boolean isEnabled(){return true;} public Set<BluetoothDevice> getBondedDevices(){return Collections.emptySet();} public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String s,java.util.UUID u){return new BluetoothServerSocket();} }''',
'android.bluetooth.BluetoothDevice':'''public class BluetoothDevice { public String getName(){return "device";} public BluetoothSocket createRfcommSocketToServiceRecord(java.util.UUID u){return new BluetoothSocket();} }''',
'android.bluetooth.BluetoothSocket':'''import java.io.*; public class BluetoothSocket implements Closeable { public void connect() throws IOException {} public InputStream getInputStream(){return new ByteArrayInputStream(new byte[0]);} public OutputStream getOutputStream(){return new ByteArrayOutputStream();} public void close() throws IOException {} }''',
'android.bluetooth.BluetoothServerSocket':'''import java.io.*; public class BluetoothServerSocket implements Closeable { public BluetoothSocket accept() throws IOException {return new BluetoothSocket();} public void close() throws IOException {} }''',
}
for cls,source_text in source.items():
 pkg,name=cls.rsplit('.',1); path=stubs/pkg.replace('.','/');path.mkdir(parents=True,exist_ok=True);(path/(name+'.java')).write_text('package '+pkg+';\n'+source_text+'\n')
probe=stubs/'org/roarincreations/linksender/StatusRaceProbe.java';probe.parent.mkdir(parents=True,exist_ok=True)
probe.write_text('''package org.roarincreations.linksender;
import android.os.*;import android.widget.*;import java.lang.reflect.*;
public class StatusRaceProbe {
 static Object call(Object target,String name,Class<?>[] classes,Object... values) throws Exception {
    Method m=MainActivity.class.getDeclaredMethod(name,classes);m.setAccessible(true);return m.invoke(target,values);
 }
 static String status(MainActivity a) throws Exception {
   Field f=MainActivity.class.getDeclaredField("status");f.setAccessible(true);return ((TextView)f.get(a)).getText();
 }
 public static void main(String[] ignored) throws Exception {
  MainActivity a=new MainActivity();a.onCreate(null);
  OperationGate gate=(OperationGate)call(a,"beginOperation",new Class<?>[]{});
  try {call(a,"postStatus",new Class<?>[]{OperationGate.class,String.class},gate,"Delivered (stale)");}
  catch(NoSuchMethodException missing) {call(a,"postStatus",new Class<?>[]{String.class},"Delivered (stale)");}
  call(a,"cancelOperation",new Class<?>[]{});
  Handler.flush();
  if(status(a).contains("Delivered"))throw new AssertionError("CANCEL stale success overwrote cancellation!");
  if(!status(a).contains("Cancel"))throw new AssertionError("Expected cancellation status:"+status(a));
  System.out.println("PASS: queued Bluetooth success suppressed after Cancel");
  OperationGate other=(OperationGate)call(a,"beginOperation",new Class<?>[]{});
  call(a,"postStatus",new Class<?>[]{OperationGate.class,String.class},gate,"Old op result");
  Handler.flush();
  if(status(a).contains("Old op"))throw new AssertionError("Old op overwrote newer status");
  call(a,"postStatus",new Class<?>[]{OperationGate.class,String.class},other,"New op status");
  Handler.flush();
  if(!status(a).contains("New op"))throw new AssertionError("Current op status missing");
  System.out.println("PASS: old operation cannot overwrite newer session; current one works");
  MainActivity b=new MainActivity();b.onCreate(null);
  Field fInput=MainActivity.class.getDeclaredField("input");fInput.setAccessible(true);
  Field fEdit=MainActivity.class.getDeclaredField("editTextButton");fEdit.setAccessible(true);
  Field fDone=MainActivity.class.getDeclaredField("useEntered");fDone.setAccessible(true);
  EditText input=(EditText)fInput.get(b);
  Button edit=(Button)fEdit.get(b), done=(Button)fDone.get(b);
  android.view.View.flushPosted();
  if(!edit.hasFocus())throw new AssertionError("Initial focus not on Edit text button");
  input.requestFocus();
  if(!input.dispatchKey(android.view.KeyEvent.KEYCODE_DPAD_DOWN,android.view.KeyEvent.ACTION_DOWN))
      throw new AssertionError("DPAD DOWN not handled");
  if(!done.hasFocus())throw new AssertionError("DPAD DOWN failed to exit input");
  input.requestFocus();b.onBackPressed();
  if(!done.hasFocus())throw new AssertionError("Back failed to leave edit without exiting");
  System.out.println("PASS: mock initial focus, DPAD DOWN, Back focus escape; NOT physical Kyocera proof");
  android.app.Activity.setLaunchIntent(new android.content.Intent(android.content.Intent.ACTION_SEND,"text/plain","Example copied text bit.ly/demo"));
  MainActivity shared=new MainActivity();shared.onCreate(null);
  Button sharedDone=(Button)fDone.get(shared);
  if(!sharedDone.hasFocus())throw new AssertionError("Share focus not on Finish before post");
  android.view.View.flushPosted();
  if(!sharedDone.hasFocus())throw new AssertionError("Delayed initial focus stole shared-text Finish focus");
  System.out.println("PASS: mock shared-text Finish focus survives deferred initial View.post");
 }
}
''')
target=base/'app/src/main/java'
files=[str(p) for p in stubs.rglob('*.java')]+[str(p) for p in target.rglob('*.java')]
run=subprocess.run(['javac','--release','17','-encoding','UTF-8','-d',str(out)]+files,capture_output=True,text=True);print('JAVAC',run.returncode,run.stderr[:5000]);sys.exit(run.returncode) if run.returncode else None
run=subprocess.run(['java','-cp',str(out),'org.roarincreations.linksender.StatusRaceProbe'],capture_output=True,text=True);print('PROBE',run.returncode,run.stdout,run.stderr[:3000]);sys.exit(run.returncode)
