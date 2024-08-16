package td;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fb._Ranges;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;

/* compiled from: EventLoop.common.kt */
@Metadata(bv = {}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0017\b \u0018\u00002\u00020\u00012\u00020\u0002:\u000201B\u0007¢\u0006\u0004\b/\u0010\rJ\u001b\u0010\u0007\u001a\u00020\u00062\n\u0010\u0005\u001a\u00060\u0003j\u0002`\u0004H\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u0017\u0010\t\u001a\n\u0018\u00010\u0003j\u0004\u0018\u0001`\u0004H\u0002¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\f\u001a\u00020\u000bH\u0002¢\u0006\u0004\b\f\u0010\rJ\u0017\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u000eH\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u001f\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u000eH\u0002¢\u0006\u0004\b\u0015\u0010\u0016J\u000f\u0010\u0017\u001a\u00020\u000bH\u0002¢\u0006\u0004\b\u0017\u0010\rJ\u000f\u0010\u0018\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u0018\u0010\rJ\u000f\u0010\u0019\u001a\u00020\u0011H\u0016¢\u0006\u0004\b\u0019\u0010\u001aJ!\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001b2\n\u0010\u001d\u001a\u00060\u0003j\u0002`\u0004¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010 \u001a\u00020\u000b2\n\u0010\u0005\u001a\u00060\u0003j\u0002`\u0004H\u0016¢\u0006\u0004\b \u0010!J\u001d\u0010\"\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u000e¢\u0006\u0004\b\"\u0010#J\u000f\u0010$\u001a\u00020\u000bH\u0004¢\u0006\u0004\b$\u0010\rR$\u0010*\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u00068B@BX\u0082\u000e¢\u0006\f\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\u0014\u0010,\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b+\u0010'R\u0014\u0010.\u001a\u00020\u00118TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b-\u0010\u001a¨\u00062"}, d2 = {"Ltd/x0;", "Ltd/y0;", "Ltd/o0;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "task", "", "N0", "(Ljava/lang/Runnable;)Z", "L0", "()Ljava/lang/Runnable;", "Lma/f0;", "K0", "()V", "Ltd/x0$a;", "W0", "(Ltd/x0$a;)Z", "", "now", "delayedTask", "", "U0", "(JLtd/x0$a;)I", "R0", "F0", "Q0", "()J", "Lqa/g;", "context", "block", "t0", "(Lqa/g;Ljava/lang/Runnable;)V", "M0", "(Ljava/lang/Runnable;)V", "T0", "(JLtd/x0$a;)V", "S0", ThermalBaseConfig.Item.ATTR_VALUE, "O0", "()Z", "V0", "(Z)V", "isCompleted", "P0", "isEmpty", "z0", "nextTime", "<init>", "a", "b", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class x0 extends y0 implements Delay {

    /* renamed from: j, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f18807j = AtomicReferenceFieldUpdater.newUpdater(x0.class, Object.class, "_queue");

    /* renamed from: k, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f18808k = AtomicReferenceFieldUpdater.newUpdater(x0.class, Object.class, "_delayed");
    private volatile /* synthetic */ Object _queue = null;
    private volatile /* synthetic */ Object _delayed = null;
    private volatile /* synthetic */ int _isCompleted = 0;

    /* compiled from: EventLoop.common.kt */
    @Metadata(bv = {}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\b \u0018\u00002\u00060\u0001j\u0002`\u00022\b\u0012\u0004\u0012\u00020\u00000\u00032\u00020\u00042\u00020\u0005J\u0011\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0000H\u0096\u0002J\u000e\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\tJ\u001e\u0010\u0011\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u000fJ\u0006\u0010\u0013\u001a\u00020\u0012J\b\u0010\u0015\u001a\u00020\u0014H\u0016R0\u0010\u001c\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00162\f\u0010\u0017\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00168V@VX\u0096\u000e¢\u0006\f\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\"\u0010\u001d\u001a\u00020\u00078\u0016@\u0016X\u0096\u000e¢\u0006\u0012\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"¨\u0006#"}, d2 = {"Ltd/x0$a;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "", "Ltd/u0;", "Lkotlinx/coroutines/internal/g0;", "other", "", "f", "", "now", "", "h", "Ltd/x0$b;", "delayed", "Ltd/x0;", "eventLoop", "g", "Lma/f0;", "a", "", "toString", "Lkotlinx/coroutines/internal/f0;", ThermalBaseConfig.Item.ATTR_VALUE, "c", "()Lkotlinx/coroutines/internal/f0;", "b", "(Lkotlinx/coroutines/internal/f0;)V", "heap", ThermalBaseConfig.Item.ATTR_INDEX, "I", "j", "()I", "e", "(I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static abstract class a implements Runnable, Comparable<a>, u0, kotlinx.coroutines.internal.g0 {
        private volatile Object _heap;

        /* renamed from: e, reason: collision with root package name */
        public long f18809e;

        /* renamed from: f, reason: collision with root package name */
        private int f18810f;

        @Override // td.u0
        public final synchronized void a() {
            Symbol symbol;
            Symbol symbol2;
            Object obj = this._heap;
            symbol = a1.f18716a;
            if (obj == symbol) {
                return;
            }
            b bVar = obj instanceof b ? (b) obj : null;
            if (bVar != null) {
                bVar.g(this);
            }
            symbol2 = a1.f18716a;
            this._heap = symbol2;
        }

        @Override // kotlinx.coroutines.internal.g0
        public void b(kotlinx.coroutines.internal.f0<?> f0Var) {
            Symbol symbol;
            Object obj = this._heap;
            symbol = a1.f18716a;
            if (obj != symbol) {
                this._heap = f0Var;
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        @Override // kotlinx.coroutines.internal.g0
        public kotlinx.coroutines.internal.f0<?> c() {
            Object obj = this._heap;
            if (obj instanceof kotlinx.coroutines.internal.f0) {
                return (kotlinx.coroutines.internal.f0) obj;
            }
            return null;
        }

        @Override // kotlinx.coroutines.internal.g0
        public void e(int i10) {
            this.f18810f = i10;
        }

        @Override // java.lang.Comparable
        /* renamed from: f, reason: merged with bridge method [inline-methods] */
        public int compareTo(a other) {
            long j10 = this.f18809e - other.f18809e;
            if (j10 > 0) {
                return 1;
            }
            return j10 < 0 ? -1 : 0;
        }

        public final synchronized int g(long now, b delayed, x0 eventLoop) {
            Symbol symbol;
            Object obj = this._heap;
            symbol = a1.f18716a;
            if (obj == symbol) {
                return 2;
            }
            synchronized (delayed) {
                a b10 = delayed.b();
                if (eventLoop.O0()) {
                    return 1;
                }
                if (b10 == null) {
                    delayed.f18811b = now;
                } else {
                    long j10 = b10.f18809e;
                    if (j10 - now < 0) {
                        now = j10;
                    }
                    if (now - delayed.f18811b > 0) {
                        delayed.f18811b = now;
                    }
                }
                long j11 = this.f18809e;
                long j12 = delayed.f18811b;
                if (j11 - j12 < 0) {
                    this.f18809e = j12;
                }
                delayed.a(this);
                return 0;
            }
        }

        public final boolean h(long now) {
            return now - this.f18809e >= 0;
        }

        @Override // kotlinx.coroutines.internal.g0
        /* renamed from: j, reason: from getter */
        public int getF18810f() {
            return this.f18810f;
        }

        public String toString() {
            return "Delayed[nanos=" + this.f18809e + ']';
        }
    }

    /* compiled from: EventLoop.common.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0004\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Ltd/x0$b;", "Lkotlinx/coroutines/internal/f0;", "Ltd/x0$a;", "", "timeNow", "<init>", "(J)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class b extends kotlinx.coroutines.internal.f0<a> {

        /* renamed from: b, reason: collision with root package name */
        public long f18811b;

        public b(long j10) {
            this.f18811b = j10;
        }
    }

    private final void K0() {
        Symbol symbol;
        Symbol symbol2;
        while (true) {
            Object obj = this._queue;
            if (obj == null) {
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = f18807j;
                symbol = a1.f18717b;
                if (atomicReferenceFieldUpdater.compareAndSet(this, null, symbol)) {
                    return;
                }
            } else {
                if (obj instanceof kotlinx.coroutines.internal.p) {
                    ((kotlinx.coroutines.internal.p) obj).d();
                    return;
                }
                symbol2 = a1.f18717b;
                if (obj == symbol2) {
                    return;
                }
                kotlinx.coroutines.internal.p pVar = new kotlinx.coroutines.internal.p(8, true);
                pVar.a((Runnable) obj);
                if (f18807j.compareAndSet(this, obj, pVar)) {
                    return;
                }
            }
        }
    }

    private final Runnable L0() {
        Symbol symbol;
        while (true) {
            Object obj = this._queue;
            if (obj == null) {
                return null;
            }
            if (obj instanceof kotlinx.coroutines.internal.p) {
                kotlinx.coroutines.internal.p pVar = (kotlinx.coroutines.internal.p) obj;
                Object j10 = pVar.j();
                if (j10 != kotlinx.coroutines.internal.p.f14387h) {
                    return (Runnable) j10;
                }
                f18807j.compareAndSet(this, obj, pVar.i());
            } else {
                symbol = a1.f18717b;
                if (obj == symbol) {
                    return null;
                }
                if (f18807j.compareAndSet(this, obj, null)) {
                    return (Runnable) obj;
                }
            }
        }
    }

    private final boolean N0(Runnable task) {
        Symbol symbol;
        while (true) {
            Object obj = this._queue;
            if (O0()) {
                return false;
            }
            if (obj == null) {
                if (f18807j.compareAndSet(this, null, task)) {
                    return true;
                }
            } else if (obj instanceof kotlinx.coroutines.internal.p) {
                kotlinx.coroutines.internal.p pVar = (kotlinx.coroutines.internal.p) obj;
                int a10 = pVar.a(task);
                if (a10 == 0) {
                    return true;
                }
                if (a10 == 1) {
                    f18807j.compareAndSet(this, obj, pVar.i());
                } else if (a10 == 2) {
                    return false;
                }
            } else {
                symbol = a1.f18717b;
                if (obj == symbol) {
                    return false;
                }
                kotlinx.coroutines.internal.p pVar2 = new kotlinx.coroutines.internal.p(8, true);
                pVar2.a((Runnable) obj);
                pVar2.a(task);
                if (f18807j.compareAndSet(this, obj, pVar2)) {
                    return true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v1, types: [int, boolean] */
    public final boolean O0() {
        return this._isCompleted;
    }

    private final void R0() {
        a i10;
        c.a();
        long nanoTime = System.nanoTime();
        while (true) {
            b bVar = (b) this._delayed;
            if (bVar == null || (i10 = bVar.i()) == null) {
                return;
            } else {
                H0(nanoTime, i10);
            }
        }
    }

    private final int U0(long now, a delayedTask) {
        if (O0()) {
            return 1;
        }
        b bVar = (b) this._delayed;
        if (bVar == null) {
            f18808k.compareAndSet(this, null, new b(now));
            Object obj = this._delayed;
            za.k.b(obj);
            bVar = (b) obj;
        }
        return delayedTask.g(now, bVar, this);
    }

    private final void V0(boolean z10) {
        this._isCompleted = z10 ? 1 : 0;
    }

    private final boolean W0(a task) {
        b bVar = (b) this._delayed;
        return (bVar != null ? bVar.e() : null) == task;
    }

    @Override // td.w0
    public void F0() {
        c2.f18727a.b();
        V0(true);
        K0();
        do {
        } while (Q0() <= 0);
        R0();
    }

    public void M0(Runnable task) {
        if (N0(task)) {
            I0();
        } else {
            m0.f18762l.M0(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean P0() {
        Symbol symbol;
        if (!D0()) {
            return false;
        }
        b bVar = (b) this._delayed;
        if (bVar != null && !bVar.d()) {
            return false;
        }
        Object obj = this._queue;
        if (obj != null) {
            if (obj instanceof kotlinx.coroutines.internal.p) {
                return ((kotlinx.coroutines.internal.p) obj).g();
            }
            symbol = a1.f18717b;
            if (obj != symbol) {
                return false;
            }
        }
        return true;
    }

    public long Q0() {
        a aVar;
        if (E0()) {
            return 0L;
        }
        b bVar = (b) this._delayed;
        if (bVar != null && !bVar.d()) {
            c.a();
            long nanoTime = System.nanoTime();
            do {
                synchronized (bVar) {
                    a b10 = bVar.b();
                    if (b10 != null) {
                        a aVar2 = b10;
                        aVar = aVar2.h(nanoTime) ? N0(aVar2) : false ? bVar.h(0) : null;
                    }
                }
            } while (aVar != null);
        }
        Runnable L0 = L0();
        if (L0 != null) {
            L0.run();
            return 0L;
        }
        return z0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void S0() {
        this._queue = null;
        this._delayed = null;
    }

    public final void T0(long now, a delayedTask) {
        int U0 = U0(now, delayedTask);
        if (U0 == 0) {
            if (W0(delayedTask)) {
                I0();
            }
        } else if (U0 == 1) {
            H0(now, delayedTask);
        } else if (U0 != 2) {
            throw new IllegalStateException("unexpected result".toString());
        }
    }

    @Override // td.CoroutineDispatcher
    public final void t0(qa.g context, Runnable block) {
        M0(block);
    }

    @Override // td.w0
    protected long z0() {
        a e10;
        long d10;
        Symbol symbol;
        if (super.z0() == 0) {
            return 0L;
        }
        Object obj = this._queue;
        if (obj != null) {
            if (!(obj instanceof kotlinx.coroutines.internal.p)) {
                symbol = a1.f18717b;
                return obj == symbol ? Long.MAX_VALUE : 0L;
            }
            if (!((kotlinx.coroutines.internal.p) obj).g()) {
                return 0L;
            }
        }
        b bVar = (b) this._delayed;
        if (bVar == null || (e10 = bVar.e()) == null) {
            return Long.MAX_VALUE;
        }
        long j10 = e10.f18809e;
        c.a();
        d10 = _Ranges.d(j10 - System.nanoTime(), 0L);
        return d10;
    }
}
