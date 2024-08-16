package zd;

import ee.e;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: Dispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b)\u0010*J\b\u0010\u0003\u001a\u00020\u0002H\u0002J+\u0010\t\u001a\u00020\b\"\u0004\b\u0000\u0010\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\u0006\u0010\u0007\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\t\u0010\nJ\u0017\u0010\f\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u000bH\u0000¢\u0006\u0004\b\f\u0010\rJ\u001b\u0010\u000f\u001a\u00020\b2\n\u0010\u0007\u001a\u00060\u000eR\u00020\u000bH\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u0017\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u000bH\u0000¢\u0006\u0004\b\u0011\u0010\rJ\u0006\u0010\u0013\u001a\u00020\u0012R*\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00128F@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R*\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u00128F@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u001a\u0010\u0015\u001a\u0004\b\u001b\u0010\u0017\"\u0004\b\u001c\u0010\u0019R.\u0010\u001f\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001d8F@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u001f\u0010 \u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u0011\u0010(\u001a\u00020%8G¢\u0006\u0006\u001a\u0004\b&\u0010'¨\u0006+"}, d2 = {"Lzd/p;", "", "", "i", "T", "Ljava/util/Deque;", "calls", "call", "Lma/f0;", "c", "(Ljava/util/Deque;Ljava/lang/Object;)V", "Lee/e;", "a", "(Lee/e;)V", "Lee/e$a;", "d", "(Lee/e$a;)V", "e", "", "j", "maxRequests", "I", "g", "()I", "setMaxRequests", "(I)V", "maxRequestsPerHost", "h", "setMaxRequestsPerHost", "Ljava/lang/Runnable;", "<set-?>", "idleCallback", "Ljava/lang/Runnable;", "f", "()Ljava/lang/Runnable;", "setIdleCallback", "(Ljava/lang/Runnable;)V", "Ljava/util/concurrent/ExecutorService;", "b", "()Ljava/util/concurrent/ExecutorService;", "executorService", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class p {

    /* renamed from: c, reason: collision with root package name */
    private Runnable f20691c;

    /* renamed from: d, reason: collision with root package name */
    private ExecutorService f20692d;

    /* renamed from: a, reason: collision with root package name */
    private int f20689a = 64;

    /* renamed from: b, reason: collision with root package name */
    private int f20690b = 5;

    /* renamed from: e, reason: collision with root package name */
    private final ArrayDeque<e.a> f20693e = new ArrayDeque<>();

    /* renamed from: f, reason: collision with root package name */
    private final ArrayDeque<e.a> f20694f = new ArrayDeque<>();

    /* renamed from: g, reason: collision with root package name */
    private final ArrayDeque<ee.e> f20695g = new ArrayDeque<>();

    private final <T> void c(Deque<T> calls, T call) {
        Runnable f10;
        synchronized (this) {
            if (calls.remove(call)) {
                f10 = f();
                Unit unit = Unit.f15173a;
            } else {
                throw new AssertionError("Call wasn't in-flight!");
            }
        }
        if (i() || f10 == null) {
            return;
        }
        f10.run();
    }

    private final boolean i() {
        int i10;
        boolean z10;
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        ArrayList arrayList = new ArrayList();
        synchronized (this) {
            Iterator<e.a> it = this.f20693e.iterator();
            za.k.d(it, "readyAsyncCalls.iterator()");
            while (it.hasNext()) {
                e.a next = it.next();
                if (this.f20694f.size() >= g()) {
                    break;
                }
                if (next.getF11205f().get() < h()) {
                    it.remove();
                    next.getF11205f().incrementAndGet();
                    za.k.d(next, "asyncCall");
                    arrayList.add(next);
                    this.f20694f.add(next);
                }
            }
            z10 = j() > 0;
            Unit unit = Unit.f15173a;
        }
        int size = arrayList.size();
        for (i10 = 0; i10 < size; i10++) {
            ((e.a) arrayList.get(i10)).a(b());
        }
        return z10;
    }

    public final synchronized void a(ee.e call) {
        za.k.e(call, "call");
        this.f20695g.add(call);
    }

    public final synchronized ExecutorService b() {
        ExecutorService executorService;
        if (this.f20692d == null) {
            this.f20692d = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), ae.d.L(za.k.l(ae.d.f245i, " Dispatcher"), false));
        }
        executorService = this.f20692d;
        za.k.b(executorService);
        return executorService;
    }

    public final void d(e.a call) {
        za.k.e(call, "call");
        call.getF11205f().decrementAndGet();
        c(this.f20694f, call);
    }

    public final void e(ee.e call) {
        za.k.e(call, "call");
        c(this.f20695g, call);
    }

    public final synchronized Runnable f() {
        return this.f20691c;
    }

    public final synchronized int g() {
        return this.f20689a;
    }

    public final synchronized int h() {
        return this.f20690b;
    }

    public final synchronized int j() {
        return this.f20694f.size() + this.f20695g.size();
    }
}
