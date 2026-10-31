import org.roarincreations.linksender.OperationGate;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class TestOperationGate {
    private static int checks;
    private static void yes(boolean value, String name) { checks++; if (!value) throw new AssertionError(name); }
    static final class Fake implements Closeable {
        int closes;
        boolean fail;
        @Override public void close() throws IOException { closes++; if (fail) throw new IOException("simulated close failure"); }
    }
    public static void main(String[] args) throws Exception {
        OperationGate g = new OperationGate();
        yes(!g.isCancelled(), "initial not canceled");
        Fake a = new Fake(), b = new Fake();
        yes(g.track(a) && g.track(b), "track two resources");
        g.cancel();
        yes(g.isCancelled(), "cancelled state");
        yes(a.closes == 1 && b.closes == 1, "both closed on cancel");
        g.cancel();
        yes(a.closes == 1 && b.closes == 1, "idempotent");
        Fake late = new Fake();
        yes(!g.track(late) && late.closes == 1, "late registration closes immediately");
        Fake removed = new Fake();
        OperationGate g2 = new OperationGate();
        yes(g2.track(removed), "track before release");
        g2.untrack(removed); g2.cancel();
        yes(removed.closes == 0, "untracked resource not closed again");
        OperationGate g3 = new OperationGate();
        Fake failure = new Fake(); failure.fail = true;
        Fake survivor = new Fake();
        g3.track(failure); g3.track(survivor); g3.cancel();
        yes(failure.closes == 1 && survivor.closes == 1, "failed close doesn't block rest");
        OperationGate g4 = new OperationGate();
        CountDownLatch start = new CountDownLatch(1), done = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        Thread worker = new Thread(() -> {
            try { start.await(); Fake later = new Fake(); boolean tracked = g4.track(later);
                  yes(!tracked && later.closes == 1, "late socket created on worker after cancellation"); }
            catch (Throwable t) { error.set(t); }
            finally { done.countDown(); }
        });
        worker.start(); g4.cancel(); start.countDown();
        yes(done.await(3, TimeUnit.SECONDS), "worker completes without hanging");
        if (error.get() != null) throw new AssertionError("worker failed", error.get());
        OperationGate g5 = new OperationGate();
        Fake live = new Fake();
        yes(g5.track(live), "track live resource before concurrent cancel");
        CountDownLatch cstart = new CountDownLatch(1);
        Thread canceller = new Thread(() -> { try { cstart.await(); g5.cancel(); } catch(InterruptedException e) { Thread.currentThread().interrupt(); } });
        canceller.start(); cstart.countDown(); canceller.join(3000);
        yes(!canceller.isAlive() && live.closes == 1 && g5.isCancelled(), "cancel from another thread closes tracked resource");
        System.out.println("PASS " + checks + " cancellation-gate checks; no Bluetooth or Android runtime");
    }
}
