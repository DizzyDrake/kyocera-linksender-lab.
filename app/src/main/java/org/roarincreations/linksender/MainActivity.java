package org.roarincreations.linksender;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Gravity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Foreground, user-triggered only. No SMS provider, discovery, background listener or URL auto-open. */
public final class MainActivity extends Activity {
    private static final UUID SERVICE_ID = UUID.fromString("4f713f88-61ce-4f47-85b5-4e0d9a119e62");
    private static final int PERMISSION_REQUEST = 11;
    private final Handler ui = new Handler(Looper.getMainLooper());
    private BluetoothAdapter adapter;
    private EditText input;
    private TextView status, received;
    private Spinner linkChoices, devices;
    private Button send, sendText, listen, cancel, copy, editTextButton, useEntered;
    private final List<String> choices = new ArrayList<>();
    private String choicesFromInput = "";
    private final List<BluetoothDevice> paired = new ArrayList<>();
    private volatile OperationGate activeOperation;
    private volatile boolean busy;
    private String lastReceived;
    private boolean lastReceivedIsText;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = BluetoothAdapter.getDefaultAdapter();
        buildUi();
        loadIncoming(getIntent());
        if (hasPermission()) refreshDevices();
        else requestBluetoothPermission();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        loadIncoming(intent);
    }

    /** High-contrast black labeling on green surfaces; no theme/XML dependency. */
    private TextView text(LinearLayout root, String value) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(16); t.setTextColor(LinkSenderStyle.INK);
        t.setPadding(10, 9, 10, 7);
        root.addView(t);
        return t;
    }
    private TextView infoBox(LinearLayout root, String value) {
        TextView t = text(root, value);
        t.setBackground(LinkSenderStyle.surface(LinkSenderStyle.CARD));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 4, 0, 10);
        t.setLayoutParams(lp);
        t.setPadding(14, 12, 14, 12);
        return t;
    }
    private Button button(LinearLayout root, String label, View.OnClickListener action) {
        Button b = new Button(this);
        b.setText(label); b.setAllCaps(false); b.setTextColor(LinkSenderStyle.INK);
        b.setTextSize(16); b.setFocusable(true);
        b.setBackground(LinkSenderStyle.buttonBackground());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 3, 0, 6);
        root.addView(b, lp); b.setOnClickListener(action);
        return b;
    }
    /** The closed picker shows the committed choice; yellow in the open list follows D-pad focus. */
    private ArrayAdapter<String> spinnerAdapter(List<String> values, Spinner picker) {
        ArrayAdapter<String> a = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, values) {
            @Override public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView) {
                    TextView label = (TextView) v;
                    label.setText("✓ " + values.get(position)); // committed choice, even without focus
                    label.setTextColor(LinkSenderStyle.INK);
                    label.setTextSize(16);
                    label.setSingleLine(false);
                    label.setMaxLines(2);
                    label.setGravity(Gravity.CENTER_VERTICAL);
                    label.setPadding(12, 12, 12, 12);
                }
                return v;
            }
            @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (v instanceof TextView) {
                    TextView label = (TextView) v;
                    boolean committed = position == picker.getSelectedItemPosition();
                    label.setText((committed ? "✓ " : "   ") + values.get(position));
                    label.setTextColor(LinkSenderStyle.INK);
                    label.setTextSize(16);
                    label.setSingleLine(false);
                    label.setMaxLines(2);
                    label.setGravity(Gravity.CENTER_VERTICAL);
                    label.setPadding(12, 10, 12, 10);
                    // A ListView moves selected/focused state as the D-pad moves.
                    // Do not set a fixed color: it would conceal the moving highlight.
                    v.setBackground(LinkSenderStyle.dropdownRowBackground(committed));
                }
                return v;
            }
        };
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return a;
    }
    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(LinkSenderStyle.GREEN);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(12, 10, 12, 18);
        root.setBackgroundColor(LinkSenderStyle.GREEN);
        scroll.addView(root);
        TextView heading = text(root, "LINK SENDER  •  paired Bluetooth");
        heading.setTextSize(20);
        heading.setGravity(Gravity.CENTER_VERTICAL);
        status = infoBox(root, "Choose text or one link. Nothing is sent automatically.");
        editTextButton = button(root, "Edit / type text", v -> input.requestFocus());
        input = new EditText(this); input.setSingleLine(false); input.setMinLines(2);
        input.setHint("Paste or type a link or message"); input.setTextSize(16);
        input.setTextColor(LinkSenderStyle.INK);
        input.setHintTextColor(LinkSenderStyle.HINT);
        input.setBackground(LinkSenderStyle.surface(LinkSenderStyle.EDITOR));
        input.setPadding(14, 12, 14, 12);
        // Kyocera hardware D-pad: DOWN or BACK leaves editing without closing the app.
        input.setOnKeyListener((v, keyCode, event) -> {
            // Do not intercept BACK key-down here: Activity.onBackPressed handles
            // it after Android/IME dispatch, avoiding a key-up exiting the app.
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                leaveTextEditing(); return true;
            }
            return false;
        });
        root.addView(input, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        useEntered = button(root, "Use entered text / finish editing", v -> updateChoices(input.getText().toString()));
        button(root, "Paste clipboard (only when pressed)", v -> paste());
        text(root, "Optional selected link (bare domains are shown with https://)");
        linkChoices = new Spinner(this); linkChoices.setFocusable(true);
        linkChoices.setBackground(LinkSenderStyle.spinnerBackground());
        root.addView(linkChoices);
        text(root, "Paired receiving phone");
        devices = new Spinner(this); devices.setFocusable(true);
        devices.setBackground(LinkSenderStyle.spinnerBackground());
        root.addView(devices);
        button(root, "Refresh paired phones", v -> refreshDevices());
        send = button(root, "Send selected link", v -> sendSelected());
        sendText = button(root, "Send entered text exactly", v -> sendEnteredText());
        listen = button(root, "Receive one link or text", v -> receiveOne());
        cancel = button(root, "Cancel waiting", v -> cancelOperation());
        received = infoBox(root, "Received: none");
        copy = button(root, "Copy received", v -> copyReceived());
        copy.setEnabled(false);
        text(root, "ABOUT LINK SENDER");
        infoBox(root, "Version " + BuildConfig.VERSION_NAME
                + "  •  Prepared 2026-10-31"
                + "\nBuild date (UTC): " + BuildConfig.BUILD_DATE_UTC
                + "\nRelease date (UTC): " + BuildConfig.RELEASE_DATE_UTC
                + "\nMade by GPT-6 and Aurora Williamsen"
                + "\nOwner-controlled Bluetooth transfer. No automatic sending.");
        updateChoices("");
        setContentView(scroll);
        // Initial focus on a visible button, not inside the multiline editor.
        scroll.post(() -> {
            // ACTION_SEND may have already focused Finish editing after buildUi().
            if (!useEntered.hasFocus()) editTextButton.requestFocus();
        });
    }

    private void loadIncoming(Intent intent) {
        if (intent == null || !Intent.ACTION_SEND.equals(intent.getAction())) return;
        if (!"text/plain".equals(intent.getType())) return;
        CharSequence shared = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
        if (shared == null) return;
        input.setText(shared.toString());
        updateChoices(shared.toString());
        setStatus("Shared text loaded. Send exact text or choose one link.");
        leaveTextEditing();
    }

    private void paste() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null || !clipboard.hasPrimaryClip()) { setStatus("Clipboard empty."); return; }
        ClipData clip = clipboard.getPrimaryClip();
        if (clip == null || clip.getItemCount() == 0) { setStatus("Clipboard empty."); return; }
        CharSequence value = clip.getItemAt(0).coerceToText(this);
        if (value == null) { setStatus("Clipboard has no text."); return; }
        input.setText(value);
        updateChoices(value.toString());
        leaveTextEditing();
    }

    private void updateChoices(String text) {
        choicesFromInput = text;
        choices.clear(); choices.addAll(LinkText.extract(text));
        List<String> labels = new ArrayList<>();
        if (choices.isEmpty()) labels.add("No link found (text can still be sent)");
        else for (int i = 0; i < choices.size(); i++) labels.add((i + 1) + ": " + choices.get(i));
        linkChoices.setAdapter(spinnerAdapter(labels, linkChoices));
        if (status != null) setStatus(choices.size() + " link(s) found; text sending is also available.");
    }

    private boolean hasPermission() {
        return Build.VERSION.SDK_INT < 31 || checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
    }
    private boolean ready() {
        if (adapter == null) { setStatus("Bluetooth hardware unavailable."); return false; }
        if (!hasPermission()) { requestBluetoothPermission(); return false; }
        if (!adapter.isEnabled()) { setStatus("Enable Bluetooth in system Settings, then return."); return false; }
        return true;
    }
    private void requestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 31 && !hasPermission())
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST);
    }
    @Override public void onRequestPermissionsResult(int code, String[] permissions, int[] grants) {
        super.onRequestPermissionsResult(code, permissions, grants);
        if (code == PERMISSION_REQUEST && hasPermission()) refreshDevices();
        else setStatus("Bluetooth permission not granted; nothing sent.");
    }
    private void refreshDevices() {
        if (!ready()) return;
        paired.clear();
        Set<BluetoothDevice> bonded = adapter.getBondedDevices();
        List<String> labels = new ArrayList<>();
        if (bonded != null) for (BluetoothDevice d : bonded) {
            paired.add(d); labels.add(d.getName() == null ? "Paired device" : d.getName());
        }
        if (labels.isEmpty()) labels.add("No paired phones: pair in system Settings");
        devices.setAdapter(spinnerAdapter(labels, devices));
        setStatus(paired.size() + " paired device(s). Receiver must press Receive first.");
    }
    private void setStatus(String message) { status.setText(message); }
    /** @why A queued worker result must not overwrite the UI after Cancel or a newer operation. 
     *  @how Check the active gate when the UI callback RUNS, not only when it is queued. */
    private void postStatus(OperationGate gate, String message) {
        ui.post(() -> {
            if (activeOperation == gate && !gate.isCancelled()) setStatus(message);
        });
    }
    private void postCancelledStatus(OperationGate gate) {
        ui.post(() -> {
            if (activeOperation == gate && gate.isCancelled())
                setStatus("Cancelled locally; delivery may already have occurred.");
        });
    }
    /** @why Register the cancellation gate before the background worker can allocate sockets. */
    private OperationGate beginOperation() {
        OperationGate gate = new OperationGate();
        activeOperation = gate;
        busy = true;
        send.setEnabled(false);
        sendText.setEnabled(false);
        listen.setEnabled(false);
        return gate;
    }
    private void finishOperation(OperationGate gate) {
        ui.post(() -> {
            if (activeOperation != gate) return;
            activeOperation = null;
            busy = false;
            send.setEnabled(true);
            sendText.setEnabled(true);
            listen.setEnabled(true);
        });
    }
    private void sendSelected() {
        if (busy || !ready()) return;
        // Never send a cached link after the editor has changed.
        if (!input.getText().toString().equals(choicesFromInput)) {
            updateChoices(input.getText().toString());
            setStatus("Text changed. Review the selected link before sending.");
            return;
        }
        if (choices.isEmpty()) { setStatus("No link selected. Use Send entered text for ordinary text."); return; }
        int index = linkChoices.getSelectedItemPosition();
        if (index < 0 || index >= choices.size()) return;
        int devIndex = devices.getSelectedItemPosition();
        if (devIndex < 0 || devIndex >= paired.size()) { setStatus("Choose a paired phone."); return; }
        final String url = choices.get(index);
        startSending(deviceAt(devIndex), url, false);
    }
    private BluetoothDevice deviceAt(int index) { return paired.get(index); }
    private void sendEnteredText() {
        if (busy || !ready()) return;
        final String exactText = input.getText().toString();
        if (!LinkFrame.validText(exactText)) {
            setStatus("Enter nonempty text (up to 8192 UTF-8 bytes)."); return;
        }
        int devIndex = devices.getSelectedItemPosition();
        if (devIndex < 0 || devIndex >= paired.size()) { setStatus("Choose a paired phone."); return; }
        startSending(deviceAt(devIndex), exactText, true);
    }
    private void startSending(BluetoothDevice device, String payload, boolean asText) {
        final OperationGate gate = beginOperation();
        setStatus("Connecting; receiver must be waiting…");
        new Thread(() -> {
            try (BluetoothSocket connection = device.createRfcommSocketToServiceRecord(SERVICE_ID)) {
                // Cancellation before socket allocation is not lost: late registration closes it.
                if (!gate.track(connection)) throw new IOException("Cancelled before connection");
                connection.connect();
                if (gate.isCancelled()) throw new IOException("Cancelled during connection");
                if (asText) LinkFrame.sendText(connection.getOutputStream(), payload);
                else LinkFrame.send(connection.getOutputStream(), payload);
                LinkFrame.requireAck(connection.getInputStream());
                if (!gate.isCancelled()) postStatus(gate, "Receiver validated " + (asText ? "text" : "link") + ". Nothing was opened automatically.");
            } catch (Exception e) {
                if (gate.isCancelled()) postCancelledStatus(gate);
                else postStatus(gate, "Not sent: " + e.getClass().getSimpleName() + " (check pairing/receiver)");
            } finally { finishOperation(gate); }
        }, "link-sender-send").start();
    }
    private void receiveOne() {
        if (busy || !ready()) return;
        final OperationGate gate = beginOperation();
        setStatus("Waiting for one paired sender. Cancel to stop.");
        new Thread(() -> {
            try (BluetoothServerSocket waiting = adapter.listenUsingRfcommWithServiceRecord("Link Sender", SERVICE_ID)) {
                // Closing a socket created AFTER a Cancel tap prevents an orphaned accept().
                if (!gate.track(waiting)) throw new IOException("Cancelled before listen");
                try (BluetoothSocket incoming = waiting.accept()) {
                    if (!gate.track(incoming)) throw new IOException("Cancelled before receive");
                    if (gate.isCancelled()) throw new IOException("Cancelled during receive");
                    LinkFrame.Message message = LinkFrame.receiveMessage(incoming.getInputStream());
                    if (gate.isCancelled()) throw new IOException("Cancelled during receive");
                    LinkFrame.acknowledge(incoming.getOutputStream());
                    ui.post(() -> {
                        if (activeOperation != gate || gate.isCancelled()) return;
                        lastReceived = message.value;
                        lastReceivedIsText = message.isText;
                        received.setText("Received " + (message.isText ? "text" : "link") + " (not opened): " + message.value);
                        copy.setEnabled(true);
                        setStatus("Received. Review before copying or opening elsewhere.");
                    });
                }
            } catch (Exception e) {
                if (!gate.isCancelled()) postStatus(gate, "Receive stopped: " + e.getClass().getSimpleName());
            } finally { finishOperation(gate); }
        }, "link-sender-receive").start();
    }
    private void copyReceived() {
        if (lastReceived == null) return;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText(lastReceivedIsText ? "Received text" : "Received link", lastReceived));
            setStatus("Copied. Open only if you trust the destination.");
        }
    }
    private void leaveTextEditing() {
        if (useEntered == null) return;
        // Hardware-key exit must refresh the link picker, not leave stale links.
        if (!input.getText().toString().equals(choicesFromInput))
            updateChoices(input.getText().toString());
        useEntered.requestFocus();
        InputMethodManager ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (ime != null) ime.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
    @Override public void onBackPressed() {
        if (input != null && input.hasFocus()) { leaveTextEditing(); return; }
        super.onBackPressed();
    }
    private void cancelOperation() {
        OperationGate gate = activeOperation;
        if (gate != null) {
            gate.cancel();
            setStatus("Cancel requested; delivery may already have occurred.");
        }
    }
    @Override protected void onDestroy() { cancelOperation(); super.onDestroy(); }
}
