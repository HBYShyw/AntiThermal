package ee;

import de.TaskRunner;
import ee.e;
import ie.Platform;
import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.k;
import zd.Address;
import zd.d0;

/* compiled from: RealConnectionPool.kt */
@Metadata(bv = {}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0011B'\u0012\u0006\u0010\u0017\u001a\u00020\u0016\u0012\u0006\u0010\u0018\u001a\u00020\u0006\u0012\u0006\u0010\u0019\u001a\u00020\u0004\u0012\u0006\u0010\u001b\u001a\u00020\u001a¢\u0006\u0004\b\u001c\u0010\u001dJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J.\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\n2\u000e\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\f2\u0006\u0010\u0010\u001a\u00020\u000fJ\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\u001e"}, d2 = {"Lee/g;", "", "Lee/f;", "connection", "", "now", "", "d", "Lzd/a;", "address", "Lee/e;", "call", "", "Lzd/d0;", "routes", "", "requireMultiplexed", "a", "Lma/f0;", "e", "c", "b", "Lde/e;", "taskRunner", "maxIdleConnections", "keepAliveDuration", "Ljava/util/concurrent/TimeUnit;", "timeUnit", "<init>", "(Lde/e;IJLjava/util/concurrent/TimeUnit;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class RealConnectionPool {

    /* renamed from: f, reason: collision with root package name */
    public static final a f11232f = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final int f11233a;

    /* renamed from: b, reason: collision with root package name */
    private final long f11234b;

    /* renamed from: c, reason: collision with root package name */
    private final de.d f11235c;

    /* renamed from: d, reason: collision with root package name */
    private final b f11236d;

    /* renamed from: e, reason: collision with root package name */
    private final ConcurrentLinkedQueue<RealConnection> f11237e;

    /* compiled from: RealConnectionPool.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lee/g$a;", "", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ee.g$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: RealConnectionPool.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004"}, d2 = {"ee/g$b", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ee.g$b */
    /* loaded from: classes2.dex */
    public static final class b extends de.a {
        b(String str) {
            super(str, false, 2, null);
        }

        @Override // de.a
        public long f() {
            return RealConnectionPool.this.b(System.nanoTime());
        }
    }

    public RealConnectionPool(TaskRunner taskRunner, int i10, long j10, TimeUnit timeUnit) {
        k.e(taskRunner, "taskRunner");
        k.e(timeUnit, "timeUnit");
        this.f11233a = i10;
        this.f11234b = timeUnit.toNanos(j10);
        this.f11235c = taskRunner.i();
        this.f11236d = new b(k.l(ae.d.f245i, " ConnectionPool"));
        this.f11237e = new ConcurrentLinkedQueue<>();
        if (!(j10 > 0)) {
            throw new IllegalArgumentException(k.l("keepAliveDuration <= 0: ", Long.valueOf(j10)).toString());
        }
    }

    private final int d(RealConnection connection, long now) {
        if (ae.d.f244h && !Thread.holdsLock(connection)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + connection);
        }
        List<Reference<e>> n10 = connection.n();
        int i10 = 0;
        while (i10 < n10.size()) {
            Reference<e> reference = n10.get(i10);
            if (reference.get() != null) {
                i10++;
            } else {
                Platform.f12870a.g().l("A connection to " + connection.getF11211d().getF20560a().getF20493i() + " was leaked. Did you forget to close a response body?", ((e.b) reference).getF11207a());
                n10.remove(i10);
                connection.C(true);
                if (n10.isEmpty()) {
                    connection.B(now - this.f11234b);
                    return 0;
                }
            }
        }
        return n10.size();
    }

    public final boolean a(Address address, e call, List<d0> routes, boolean requireMultiplexed) {
        k.e(address, "address");
        k.e(call, "call");
        Iterator<RealConnection> it = this.f11237e.iterator();
        while (it.hasNext()) {
            RealConnection next = it.next();
            k.d(next, "connection");
            synchronized (next) {
                if (requireMultiplexed) {
                    if (!next.v()) {
                        Unit unit = Unit.f15173a;
                    }
                }
                if (next.t(address, routes)) {
                    call.c(next);
                    return true;
                }
                Unit unit2 = Unit.f15173a;
            }
        }
        return false;
    }

    public final long b(long now) {
        Iterator<RealConnection> it = this.f11237e.iterator();
        int i10 = 0;
        long j10 = Long.MIN_VALUE;
        RealConnection realConnection = null;
        int i11 = 0;
        while (it.hasNext()) {
            RealConnection next = it.next();
            k.d(next, "connection");
            synchronized (next) {
                if (d(next, now) > 0) {
                    i11++;
                } else {
                    i10++;
                    long f11226s = now - next.getF11226s();
                    if (f11226s > j10) {
                        realConnection = next;
                        j10 = f11226s;
                    }
                    Unit unit = Unit.f15173a;
                }
            }
        }
        long j11 = this.f11234b;
        if (j10 < j11 && i10 <= this.f11233a) {
            if (i10 > 0) {
                return j11 - j10;
            }
            if (i11 > 0) {
                return j11;
            }
            return -1L;
        }
        k.b(realConnection);
        synchronized (realConnection) {
            if (!realConnection.n().isEmpty()) {
                return 0L;
            }
            if (realConnection.getF11226s() + j10 != now) {
                return 0L;
            }
            realConnection.C(true);
            this.f11237e.remove(realConnection);
            ae.d.m(realConnection.D());
            if (this.f11237e.isEmpty()) {
                this.f11235c.a();
            }
            return 0L;
        }
    }

    public final boolean c(RealConnection connection) {
        k.e(connection, "connection");
        if (ae.d.f244h && !Thread.holdsLock(connection)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + connection);
        }
        if (!connection.getF11219l() && this.f11233a != 0) {
            de.d.j(this.f11235c, this.f11236d, 0L, 2, null);
            return false;
        }
        connection.C(true);
        this.f11237e.remove(connection);
        if (!this.f11237e.isEmpty()) {
            return true;
        }
        this.f11235c.a();
        return true;
    }

    public final void e(RealConnection realConnection) {
        k.e(realConnection, "connection");
        if (ae.d.f244h && !Thread.holdsLock(realConnection)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST hold lock on " + realConnection);
        }
        this.f11237e.add(realConnection);
        de.d.j(this.f11235c, this.f11236d, 0L, 2, null);
    }
}
