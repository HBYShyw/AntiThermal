package td;

import fb._Ranges;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import kotlin.Metadata;
import td.x0;

/* compiled from: DefaultExecutor.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\bÀ\u0002\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003B\t\b\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ\b\u0010\u0005\u001a\u00020\u0004H\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0002J\b\u0010\t\u001a\u00020\bH\u0002J\b\u0010\n\u001a\u00020\u0004H\u0002J\u0014\u0010\f\u001a\u00020\u00042\n\u0010\u000b\u001a\u00060\u0002j\u0002`\u0003H\u0016J\u0018\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u000fH\u0014J\b\u0010\u0012\u001a\u00020\u0004H\u0016J\b\u0010\u0013\u001a\u00020\u0004H\u0016R\u0014\u0010\u0016\u001a\u00020\b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0018\u001a\u00020\b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0015R\u0014\u0010\u001b\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a¨\u0006\u001e"}, d2 = {"Ltd/m0;", "Ltd/x0;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "Lma/f0;", "c1", "Ljava/lang/Thread;", "Y0", "", "b1", "X0", "task", "M0", "", "now", "Ltd/x0$a;", "delayedTask", "H0", "F0", "run", "Z0", "()Z", "isShutDown", "a1", "isShutdownRequested", "G0", "()Ljava/lang/Thread;", "thread", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class m0 extends x0 implements Runnable {
    private static volatile Thread _thread;
    private static volatile int debugStatus;

    /* renamed from: l, reason: collision with root package name */
    public static final m0 f18762l;

    /* renamed from: m, reason: collision with root package name */
    private static final long f18763m;

    static {
        Long l10;
        m0 m0Var = new m0();
        f18762l = m0Var;
        w0.B0(m0Var, false, 1, null);
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        try {
            l10 = Long.getLong("kotlinx.coroutines.DefaultExecutor.keepAlive", 1000L);
        } catch (SecurityException unused) {
            l10 = 1000L;
        }
        f18763m = timeUnit.toNanos(l10.longValue());
    }

    private m0() {
    }

    private final synchronized void X0() {
        if (a1()) {
            debugStatus = 3;
            S0();
            notifyAll();
        }
    }

    private final synchronized Thread Y0() {
        Thread thread;
        thread = _thread;
        if (thread == null) {
            thread = new Thread(this, "kotlinx.coroutines.DefaultExecutor");
            _thread = thread;
            thread.setDaemon(true);
            thread.start();
        }
        return thread;
    }

    private final boolean Z0() {
        return debugStatus == 4;
    }

    private final boolean a1() {
        int i10 = debugStatus;
        return i10 == 2 || i10 == 3;
    }

    private final synchronized boolean b1() {
        if (a1()) {
            return false;
        }
        debugStatus = 1;
        notifyAll();
        return true;
    }

    private final void c1() {
        throw new RejectedExecutionException("DefaultExecutor was shut down. This error indicates that Dispatchers.shutdown() was invoked prior to completion of exiting coroutines, leaving coroutines in incomplete state. Please refer to Dispatchers.shutdown documentation for more details");
    }

    @Override // td.x0, td.w0
    public void F0() {
        debugStatus = 4;
        super.F0();
    }

    @Override // td.y0
    /* renamed from: G0 */
    protected Thread getF18736l() {
        Thread thread = _thread;
        return thread == null ? Y0() : thread;
    }

    @Override // td.y0
    protected void H0(long j10, x0.a aVar) {
        c1();
    }

    @Override // td.x0
    public void M0(Runnable runnable) {
        if (Z0()) {
            c1();
        }
        super.M0(runnable);
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean P0;
        c2.f18727a.c(this);
        c.a();
        try {
            if (!b1()) {
                if (P0) {
                    return;
                } else {
                    return;
                }
            }
            long j10 = Long.MAX_VALUE;
            while (true) {
                Thread.interrupted();
                long Q0 = Q0();
                if (Q0 == Long.MAX_VALUE) {
                    c.a();
                    long nanoTime = System.nanoTime();
                    if (j10 == Long.MAX_VALUE) {
                        j10 = f18763m + nanoTime;
                    }
                    long j11 = j10 - nanoTime;
                    if (j11 <= 0) {
                        _thread = null;
                        X0();
                        c.a();
                        if (P0()) {
                            return;
                        }
                        getF18736l();
                        return;
                    }
                    Q0 = _Ranges.g(Q0, j11);
                } else {
                    j10 = Long.MAX_VALUE;
                }
                if (Q0 > 0) {
                    if (a1()) {
                        _thread = null;
                        X0();
                        c.a();
                        if (P0()) {
                            return;
                        }
                        getF18736l();
                        return;
                    }
                    c.a();
                    LockSupport.parkNanos(this, Q0);
                }
            }
        } finally {
            _thread = null;
            X0();
            c.a();
            if (!P0()) {
                getF18736l();
            }
        }
    }
}
