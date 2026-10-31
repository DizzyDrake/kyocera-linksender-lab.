package org.roarincreations.linksender;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** @what Own sockets across a single explicit Bluetooth operation.
 *  @why Cancellation can occur before the worker has created or registered a socket.
 *  @how Registration and cancellation share one monitor; late resources are immediately closed.
 *  This class is pure Java and is independently testable without Android or Bluetooth.
 */
public final class OperationGate {
    private boolean cancelled;
    private final List<Closeable> tracked = new ArrayList<>();

    /** @return false when cancellation already occurred (resource is then closed). */
    public boolean track(Closeable resource) throws IOException {
        if (resource == null) throw new NullPointerException("resource");
        synchronized (this) {
            if (!cancelled) {
                tracked.add(resource);
                return true;
            }
        }
        resource.close();
        return false;
    }

    public synchronized boolean isCancelled() { return cancelled; }

    /** A closed resource needn't be closed again on later cancellation. */
    public synchronized void untrack(Closeable resource) { tracked.remove(resource); }

    /** Thread-safe, idempotent; do not hold monitor while closing a potentially slow socket. */
    public void cancel() {
        List<Closeable> closeNow;
        synchronized (this) {
            if (cancelled) return;
            cancelled = true;
            closeNow = new ArrayList<>(tracked);
            tracked.clear();
        }
        for (Closeable resource : closeNow) {
            try { resource.close(); }
            catch (IOException ignored) { /* Best effort; continue closing remaining sockets. */ }
        }
    }
}
