package kotlinx.coroutines.scheduling;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import db.Random;
import fb._Ranges;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import kotlin.Metadata;
import kotlinx.coroutines.internal.ResizableAtomicArray;
import kotlinx.coroutines.internal.Symbol;
import ma.Unit;
import td.DebugStrings;

/* compiled from: CoroutineScheduler.kt */
@Metadata(bv = {}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0000\u0018\u0000 U2\u00020\u00012\u00020\u0002:\u0003V\u0019WB+\u0012\u0006\u0010>\u001a\u00020\f\u0012\u0006\u0010@\u001a\u00020\f\u0012\b\b\u0002\u0010C\u001a\u00020\u0013\u0012\b\b\u0002\u0010F\u001a\u000207¢\u0006\u0004\bS\u0010TJ\u0017\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0015\u0010\t\u001a\b\u0018\u00010\bR\u00020\u0000H\u0002¢\u0006\u0004\b\t\u0010\nJ\u001b\u0010\r\u001a\u00020\f2\n\u0010\u000b\u001a\u00060\bR\u00020\u0000H\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u0017\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u0019\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0014\u001a\u00020\u0013H\u0002¢\u0006\u0004\b\u0015\u0010\u0016J\u000f\u0010\u0017\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\u0017\u0010\u0018J\u000f\u0010\u0019\u001a\u00020\fH\u0002¢\u0006\u0004\b\u0019\u0010\u001aJ+\u0010\u001c\u001a\u0004\u0018\u00010\u0003*\b\u0018\u00010\bR\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ\u0015\u0010\u001e\u001a\b\u0018\u00010\bR\u00020\u0000H\u0002¢\u0006\u0004\b\u001e\u0010\nJ)\u0010!\u001a\u00020\u00102\n\u0010\u000b\u001a\u00060\bR\u00020\u00002\u0006\u0010\u001f\u001a\u00020\f2\u0006\u0010 \u001a\u00020\f¢\u0006\u0004\b!\u0010\"J\u0019\u0010#\u001a\u00020\u00052\n\u0010\u000b\u001a\u00060\bR\u00020\u0000¢\u0006\u0004\b#\u0010$J\u001b\u0010(\u001a\u00020\u00102\n\u0010'\u001a\u00060%j\u0002`&H\u0016¢\u0006\u0004\b(\u0010)J\u000f\u0010*\u001a\u00020\u0010H\u0016¢\u0006\u0004\b*\u0010+J\u0015\u0010-\u001a\u00020\u00102\u0006\u0010,\u001a\u00020\u0013¢\u0006\u0004\b-\u0010.J-\u00102\u001a\u00020\u00102\n\u0010/\u001a\u00060%j\u0002`&2\b\b\u0002\u00101\u001a\u0002002\b\b\u0002\u0010\u001b\u001a\u00020\u0005¢\u0006\u0004\b2\u00103J!\u00104\u001a\u00020\u00032\n\u0010/\u001a\u00060%j\u0002`&2\u0006\u00101\u001a\u000200¢\u0006\u0004\b4\u00105J\r\u00106\u001a\u00020\u0010¢\u0006\u0004\b6\u0010+J\u000f\u00108\u001a\u000207H\u0016¢\u0006\u0004\b8\u00109J\u0015\u0010:\u001a\u00020\u00102\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b:\u0010;R\u0014\u0010>\u001a\u00020\f8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b<\u0010=R\u0014\u0010@\u001a\u00020\f8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b?\u0010=R\u0014\u0010C\u001a\u00020\u00138\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\bA\u0010BR\u0014\u0010F\u001a\u0002078\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\bD\u0010ER\u0014\u0010J\u001a\u00020G8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\bH\u0010IR\u0014\u0010L\u001a\u00020G8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\bK\u0010IR\u001e\u0010P\u001a\f\u0012\b\u0012\u00060\bR\u00020\u00000M8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\bN\u0010OR\u0011\u0010R\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\bQ\u0010\u0018¨\u0006X"}, d2 = {"Lkotlinx/coroutines/scheduling/a;", "Ljava/util/concurrent/Executor;", "Ljava/io/Closeable;", "Lkotlinx/coroutines/scheduling/h;", "task", "", "b", "(Lkotlinx/coroutines/scheduling/h;)Z", "Lkotlinx/coroutines/scheduling/a$c;", "S", "()Lkotlinx/coroutines/scheduling/a$c;", "worker", "", "N", "(Lkotlinx/coroutines/scheduling/a$c;)I", "skipUnpark", "Lma/f0;", "h0", "(Z)V", "", "state", "m0", "(J)Z", "t0", "()Z", "c", "()I", "tailDispatch", "j0", "(Lkotlinx/coroutines/scheduling/a$c;Lkotlinx/coroutines/scheduling/h;Z)Lkotlinx/coroutines/scheduling/h;", "u", "oldIndex", "newIndex", "X", "(Lkotlinx/coroutines/scheduling/a$c;II)V", "U", "(Lkotlinx/coroutines/scheduling/a$c;)Z", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "command", "execute", "(Ljava/lang/Runnable;)V", "close", "()V", "timeout", "e0", "(J)V", "block", "Lkotlinx/coroutines/scheduling/i;", "taskContext", "v", "(Ljava/lang/Runnable;Lkotlinx/coroutines/scheduling/i;Z)V", "m", "(Ljava/lang/Runnable;Lkotlinx/coroutines/scheduling/i;)Lkotlinx/coroutines/scheduling/h;", "i0", "", "toString", "()Ljava/lang/String;", "a0", "(Lkotlinx/coroutines/scheduling/h;)V", "e", "I", "corePoolSize", "f", "maxPoolSize", "g", "J", "idleWorkerKeepAliveNs", "h", "Ljava/lang/String;", "schedulerName", "Lkotlinx/coroutines/scheduling/d;", "i", "Lkotlinx/coroutines/scheduling/d;", "globalCpuQueue", "j", "globalBlockingQueue", "Lkotlinx/coroutines/internal/x;", "k", "Lkotlinx/coroutines/internal/x;", "workers", "L", "isTerminated", "<init>", "(IIJLjava/lang/String;)V", "l", "a", "d", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: kotlinx.coroutines.scheduling.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class CoroutineScheduler implements Executor, Closeable {
    private volatile /* synthetic */ int _isTerminated;
    volatile /* synthetic */ long controlState;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    public final int corePoolSize;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    public final int maxPoolSize;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    public final long idleWorkerKeepAliveNs;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    public final String schedulerName;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    public final kotlinx.coroutines.scheduling.d globalCpuQueue;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    public final kotlinx.coroutines.scheduling.d globalBlockingQueue;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    public final ResizableAtomicArray<c> workers;
    private volatile /* synthetic */ long parkedWorkersStack;

    /* renamed from: p, reason: collision with root package name */
    public static final Symbol f14410p = new Symbol("NOT_IN_STACK");

    /* renamed from: m, reason: collision with root package name */
    private static final /* synthetic */ AtomicLongFieldUpdater f14407m = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "parkedWorkersStack");

    /* renamed from: n, reason: collision with root package name */
    static final /* synthetic */ AtomicLongFieldUpdater f14408n = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "controlState");

    /* renamed from: o, reason: collision with root package name */
    private static final /* synthetic */ AtomicIntegerFieldUpdater f14409o = AtomicIntegerFieldUpdater.newUpdater(CoroutineScheduler.class, "_isTerminated");

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* renamed from: kotlinx.coroutines.scheduling.a$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f14418a;

        static {
            int[] iArr = new int[d.values().length];
            iArr[d.PARKING.ordinal()] = 1;
            iArr[d.BLOCKING.ordinal()] = 2;
            iArr[d.CPU_ACQUIRED.ordinal()] = 3;
            iArr[d.DORMANT.ordinal()] = 4;
            iArr[d.TERMINATED.ordinal()] = 5;
            f14418a = iArr;
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/scheduling/a$d;", "", "<init>", "(Ljava/lang/String;I)V", "CPU_ACQUIRED", "BLOCKING", "PARKING", "DORMANT", "TERMINATED", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* renamed from: kotlinx.coroutines.scheduling.a$d */
    /* loaded from: classes2.dex */
    public enum d {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        DORMANT,
        TERMINATED
    }

    public CoroutineScheduler(int i10, int i11, long j10, String str) {
        this.corePoolSize = i10;
        this.maxPoolSize = i11;
        this.idleWorkerKeepAliveNs = j10;
        this.schedulerName = str;
        if (!(i10 >= 1)) {
            throw new IllegalArgumentException(("Core pool size " + i10 + " should be at least 1").toString());
        }
        if (!(i11 >= i10)) {
            throw new IllegalArgumentException(("Max pool size " + i11 + " should be greater than or equals to core pool size " + i10).toString());
        }
        if (!(i11 <= 2097150)) {
            throw new IllegalArgumentException(("Max pool size " + i11 + " should not exceed maximal supported number of threads 2097150").toString());
        }
        if (j10 > 0) {
            this.globalCpuQueue = new kotlinx.coroutines.scheduling.d();
            this.globalBlockingQueue = new kotlinx.coroutines.scheduling.d();
            this.parkedWorkersStack = 0L;
            this.workers = new ResizableAtomicArray<>(i10 + 1);
            this.controlState = i10 << 42;
            this._isTerminated = 0;
            return;
        }
        throw new IllegalArgumentException(("Idle worker keep alive time " + j10 + " must be positive").toString());
    }

    private final int N(c worker) {
        Object nextParkedWorker = worker.getNextParkedWorker();
        while (nextParkedWorker != f14410p) {
            if (nextParkedWorker == null) {
                return 0;
            }
            c cVar = (c) nextParkedWorker;
            int indexInArray = cVar.getIndexInArray();
            if (indexInArray != 0) {
                return indexInArray;
            }
            nextParkedWorker = cVar.getNextParkedWorker();
        }
        return -1;
    }

    private final c S() {
        while (true) {
            long j10 = this.parkedWorkersStack;
            c b10 = this.workers.b((int) (2097151 & j10));
            if (b10 == null) {
                return null;
            }
            long j11 = (2097152 + j10) & (-2097152);
            int N = N(b10);
            if (N >= 0 && f14407m.compareAndSet(this, j10, N | j11)) {
                b10.p(f14410p);
                return b10;
            }
        }
    }

    private final boolean b(h task) {
        if (task.taskContext.getTaskMode() == 1) {
            return this.globalBlockingQueue.a(task);
        }
        return this.globalCpuQueue.a(task);
    }

    private final int c() {
        int c10;
        synchronized (this.workers) {
            if (L()) {
                return -1;
            }
            long j10 = this.controlState;
            int i10 = (int) (j10 & 2097151);
            c10 = _Ranges.c(i10 - ((int) ((j10 & 4398044413952L) >> 21)), 0);
            if (c10 >= this.corePoolSize) {
                return 0;
            }
            if (i10 >= this.maxPoolSize) {
                return 0;
            }
            int i11 = ((int) (this.controlState & 2097151)) + 1;
            if (i11 > 0 && this.workers.b(i11) == null) {
                c cVar = new c(this, i11);
                this.workers.c(i11, cVar);
                if (i11 == ((int) (2097151 & f14408n.incrementAndGet(this)))) {
                    cVar.start();
                    return c10 + 1;
                }
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    private final void h0(boolean skipUnpark) {
        long addAndGet = f14408n.addAndGet(this, 2097152L);
        if (skipUnpark || t0() || m0(addAndGet)) {
            return;
        }
        t0();
    }

    private final h j0(c cVar, h hVar, boolean z10) {
        if (cVar == null || cVar.state == d.TERMINATED) {
            return hVar;
        }
        if (hVar.taskContext.getTaskMode() == 0 && cVar.state == d.BLOCKING) {
            return hVar;
        }
        cVar.mayHaveLocalTasks = true;
        return cVar.localQueue.a(hVar, z10);
    }

    private final boolean m0(long state) {
        int c10;
        c10 = _Ranges.c(((int) (2097151 & state)) - ((int) ((state & 4398044413952L) >> 21)), 0);
        if (c10 < this.corePoolSize) {
            int c11 = c();
            if (c11 == 1 && this.corePoolSize > 1) {
                c();
            }
            if (c11 > 0) {
                return true;
            }
        }
        return false;
    }

    static /* synthetic */ boolean o0(CoroutineScheduler coroutineScheduler, long j10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            j10 = coroutineScheduler.controlState;
        }
        return coroutineScheduler.m0(j10);
    }

    private final boolean t0() {
        c S;
        do {
            S = S();
            if (S == null) {
                return false;
            }
        } while (!c.f14419l.compareAndSet(S, -1, 0));
        LockSupport.unpark(S);
        return true;
    }

    private final c u() {
        Thread currentThread = Thread.currentThread();
        c cVar = currentThread instanceof c ? (c) currentThread : null;
        if (cVar == null || !za.k.a(CoroutineScheduler.this, this)) {
            return null;
        }
        return cVar;
    }

    public static /* synthetic */ void w(CoroutineScheduler coroutineScheduler, Runnable runnable, i iVar, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            iVar = l.f14451f;
        }
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        coroutineScheduler.v(runnable, iVar, z10);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [int, boolean] */
    public final boolean L() {
        return this._isTerminated;
    }

    public final boolean U(c worker) {
        long j10;
        int indexInArray;
        if (worker.getNextParkedWorker() != f14410p) {
            return false;
        }
        do {
            j10 = this.parkedWorkersStack;
            indexInArray = worker.getIndexInArray();
            worker.p(this.workers.b((int) (2097151 & j10)));
        } while (!f14407m.compareAndSet(this, j10, ((2097152 + j10) & (-2097152)) | indexInArray));
        return true;
    }

    public final void X(c worker, int oldIndex, int newIndex) {
        while (true) {
            long j10 = this.parkedWorkersStack;
            int i10 = (int) (2097151 & j10);
            long j11 = (2097152 + j10) & (-2097152);
            if (i10 == oldIndex) {
                i10 = newIndex == 0 ? N(worker) : newIndex;
            }
            if (i10 >= 0 && f14407m.compareAndSet(this, j10, j11 | i10)) {
                return;
            }
        }
    }

    public final void a0(h task) {
        try {
            task.run();
        } finally {
            try {
            } finally {
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        e0(10000L);
    }

    public final void e0(long timeout) {
        int i10;
        h d10;
        if (f14409o.compareAndSet(this, 0, 1)) {
            c u7 = u();
            synchronized (this.workers) {
                i10 = (int) (this.controlState & 2097151);
            }
            if (1 <= i10) {
                int i11 = 1;
                while (true) {
                    c b10 = this.workers.b(i11);
                    za.k.b(b10);
                    c cVar = b10;
                    if (cVar != u7) {
                        while (cVar.isAlive()) {
                            LockSupport.unpark(cVar);
                            cVar.join(timeout);
                        }
                        cVar.localQueue.g(this.globalBlockingQueue);
                    }
                    if (i11 == i10) {
                        break;
                    } else {
                        i11++;
                    }
                }
            }
            this.globalBlockingQueue.b();
            this.globalCpuQueue.b();
            while (true) {
                if (u7 != null) {
                    d10 = u7.f(true);
                    if (d10 != null) {
                        continue;
                        a0(d10);
                    }
                }
                d10 = this.globalCpuQueue.d();
                if (d10 == null && (d10 = this.globalBlockingQueue.d()) == null) {
                    break;
                }
                a0(d10);
            }
            if (u7 != null) {
                u7.s(d.TERMINATED);
            }
            this.parkedWorkersStack = 0L;
            this.controlState = 0L;
        }
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        w(this, command, null, false, 6, null);
    }

    public final void i0() {
        if (t0() || o0(this, 0L, 1, null)) {
            return;
        }
        t0();
    }

    public final h m(Runnable block, i taskContext) {
        long a10 = l.f14450e.a();
        if (block instanceof h) {
            h hVar = (h) block;
            hVar.submissionTime = a10;
            hVar.taskContext = taskContext;
            return hVar;
        }
        return new k(block, a10, taskContext);
    }

    public String toString() {
        ArrayList arrayList = new ArrayList();
        int a10 = this.workers.a();
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        for (int i15 = 1; i15 < a10; i15++) {
            c b10 = this.workers.b(i15);
            if (b10 != null) {
                int f10 = b10.localQueue.f();
                int i16 = b.f14418a[b10.state.ordinal()];
                if (i16 == 1) {
                    i12++;
                } else if (i16 == 2) {
                    i11++;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(f10);
                    sb2.append('b');
                    arrayList.add(sb2.toString());
                } else if (i16 == 3) {
                    i10++;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(f10);
                    sb3.append('c');
                    arrayList.add(sb3.toString());
                } else if (i16 == 4) {
                    i13++;
                    if (f10 > 0) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(f10);
                        sb4.append('d');
                        arrayList.add(sb4.toString());
                    }
                } else if (i16 == 5) {
                    i14++;
                }
            }
        }
        long j10 = this.controlState;
        return this.schedulerName + '@' + DebugStrings.b(this) + "[Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {CPU = " + i10 + ", blocking = " + i11 + ", parked = " + i12 + ", dormant = " + i13 + ", terminated = " + i14 + "}, running workers queues = " + arrayList + ", global CPU queue size = " + this.globalCpuQueue.c() + ", global blocking queue size = " + this.globalBlockingQueue.c() + ", Control State {created workers= " + ((int) (2097151 & j10)) + ", blocking tasks = " + ((int) ((4398044413952L & j10) >> 21)) + ", CPUs acquired = " + (this.corePoolSize - ((int) ((9223367638808264704L & j10) >> 42))) + "}]";
    }

    public final void v(Runnable block, i taskContext, boolean tailDispatch) {
        td.c.a();
        h m10 = m(block, taskContext);
        c u7 = u();
        h j02 = j0(u7, m10, tailDispatch);
        if (j02 != null && !b(j02)) {
            throw new RejectedExecutionException(this.schedulerName + " was terminated");
        }
        boolean z10 = tailDispatch && u7 != null;
        if (m10.taskContext.getTaskMode() != 0) {
            h0(z10);
        } else {
            if (z10) {
                return;
            }
            i0();
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u000e\b\u0080\u0004\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b@\u0010AB\u0011\b\u0016\u0012\u0006\u0010'\u001a\u00020\u000e¢\u0006\u0004\b@\u0010BJ\u000f\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\u000f\u0010\u0006\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u000f\u0010\b\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\b\u0010\u0007J\u000f\u0010\t\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\t\u0010\u0004J\u0017\u0010\f\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\f\u0010\rJ\u0017\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u000eH\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u0017\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u000eH\u0002¢\u0006\u0004\b\u0012\u0010\u0011J\u000f\u0010\u0013\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\u0013\u0010\u0007J\u000f\u0010\u0014\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\u0014\u0010\u0007J\u0017\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u000eH\u0002¢\u0006\u0004\b\u0016\u0010\u0011J\u0019\u0010\u0018\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0017\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\u0011\u0010\u001a\u001a\u0004\u0018\u00010\nH\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ\u0019\u0010\u001d\u001a\u0004\u0018\u00010\n2\u0006\u0010\u001c\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u001d\u0010\u0019J\u0015\u0010 \u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u001e¢\u0006\u0004\b \u0010!J\u000f\u0010\"\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\"\u0010\u0007J\u0015\u0010$\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020\u000e¢\u0006\u0004\b$\u0010%J\u0017\u0010&\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0017\u001a\u00020\u0002¢\u0006\u0004\b&\u0010\u0019R*\u0010(\u001a\u00020\u000e2\u0006\u0010'\u001a\u00020\u000e8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b(\u0010)\u001a\u0004\b*\u0010+\"\u0004\b,\u0010\u0011R\u0014\u0010/\u001a\u00020-8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0018\u0010.R\u0016\u00101\u001a\u00020\u001e8\u0006@\u0006X\u0087\u000e¢\u0006\u0006\n\u0004\b&\u00100R\u0016\u00104\u001a\u0002028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b*\u00103R$\u00106\u001a\u0004\u0018\u0001058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b6\u00107\u001a\u0004\b8\u00109\"\u0004\b:\u0010;R\u0016\u0010<\u001a\u0002028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b8\u00103R\u0016\u0010=\u001a\u00020\u000e8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0016\u0010)R\u0016\u0010?\u001a\u00020\u00028\u0006@\u0006X\u0087\u000e¢\u0006\u0006\n\u0004\b\t\u0010>¨\u0006C"}, d2 = {"Lkotlinx/coroutines/scheduling/a$c;", "Ljava/lang/Thread;", "", "q", "()Z", "Lma/f0;", "n", "()V", "r", "j", "Lkotlinx/coroutines/scheduling/h;", "task", "d", "(Lkotlinx/coroutines/scheduling/h;)V", "", "taskMode", "c", "(I)V", "b", "l", "u", "mode", "i", "scanLocalQueue", "e", "(Z)Lkotlinx/coroutines/scheduling/h;", "m", "()Lkotlinx/coroutines/scheduling/h;", "blockingOnly", "t", "Lkotlinx/coroutines/scheduling/a$d;", "newState", "s", "(Lkotlinx/coroutines/scheduling/a$d;)Z", "run", "upperBound", "k", "(I)I", "f", ThermalBaseConfig.Item.ATTR_INDEX, "indexInArray", "I", "g", "()I", "o", "Lkotlinx/coroutines/scheduling/n;", "Lkotlinx/coroutines/scheduling/n;", "localQueue", "Lkotlinx/coroutines/scheduling/a$d;", "state", "", "J", "terminationDeadline", "", "nextParkedWorker", "Ljava/lang/Object;", "h", "()Ljava/lang/Object;", "p", "(Ljava/lang/Object;)V", "minDelayUntilStealableTaskNs", "rngState", "Z", "mayHaveLocalTasks", "<init>", "(Lkotlinx/coroutines/scheduling/a;)V", "(Lkotlinx/coroutines/scheduling/a;I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* renamed from: kotlinx.coroutines.scheduling.a$c */
    /* loaded from: classes2.dex */
    public final class c extends Thread {

        /* renamed from: l, reason: collision with root package name */
        static final /* synthetic */ AtomicIntegerFieldUpdater f14419l = AtomicIntegerFieldUpdater.newUpdater(c.class, "workerCtl");

        /* renamed from: e, reason: collision with root package name and from kotlin metadata */
        public final WorkQueue localQueue;

        /* renamed from: f, reason: collision with root package name and from kotlin metadata */
        public d state;

        /* renamed from: g, reason: collision with root package name and from kotlin metadata */
        private long terminationDeadline;

        /* renamed from: h, reason: collision with root package name and from kotlin metadata */
        private long minDelayUntilStealableTaskNs;

        /* renamed from: i, reason: collision with root package name and from kotlin metadata */
        private int rngState;
        private volatile int indexInArray;

        /* renamed from: j, reason: collision with root package name and from kotlin metadata */
        public boolean mayHaveLocalTasks;
        private volatile Object nextParkedWorker;
        volatile /* synthetic */ int workerCtl;

        private c() {
            setDaemon(true);
            this.localQueue = new WorkQueue();
            this.state = d.DORMANT;
            this.workerCtl = 0;
            this.nextParkedWorker = CoroutineScheduler.f14410p;
            this.rngState = Random.f10892e.b();
        }

        private final void b(int taskMode) {
            if (taskMode == 0) {
                return;
            }
            CoroutineScheduler.f14408n.addAndGet(CoroutineScheduler.this, -2097152L);
            if (this.state != d.TERMINATED) {
                this.state = d.DORMANT;
            }
        }

        private final void c(int taskMode) {
            if (taskMode != 0 && s(d.BLOCKING)) {
                CoroutineScheduler.this.i0();
            }
        }

        private final void d(h task) {
            int taskMode = task.taskContext.getTaskMode();
            i(taskMode);
            c(taskMode);
            CoroutineScheduler.this.a0(task);
            b(taskMode);
        }

        private final h e(boolean scanLocalQueue) {
            h m10;
            h m11;
            if (scanLocalQueue) {
                boolean z10 = k(CoroutineScheduler.this.corePoolSize * 2) == 0;
                if (z10 && (m11 = m()) != null) {
                    return m11;
                }
                h h10 = this.localQueue.h();
                if (h10 != null) {
                    return h10;
                }
                if (!z10 && (m10 = m()) != null) {
                    return m10;
                }
            } else {
                h m12 = m();
                if (m12 != null) {
                    return m12;
                }
            }
            return t(false);
        }

        private final void i(int mode) {
            this.terminationDeadline = 0L;
            if (this.state == d.PARKING) {
                this.state = d.BLOCKING;
            }
        }

        private final boolean j() {
            return this.nextParkedWorker != CoroutineScheduler.f14410p;
        }

        private final void l() {
            if (this.terminationDeadline == 0) {
                this.terminationDeadline = System.nanoTime() + CoroutineScheduler.this.idleWorkerKeepAliveNs;
            }
            LockSupport.parkNanos(CoroutineScheduler.this.idleWorkerKeepAliveNs);
            if (System.nanoTime() - this.terminationDeadline >= 0) {
                this.terminationDeadline = 0L;
                u();
            }
        }

        private final h m() {
            if (k(2) == 0) {
                h d10 = CoroutineScheduler.this.globalCpuQueue.d();
                return d10 != null ? d10 : CoroutineScheduler.this.globalBlockingQueue.d();
            }
            h d11 = CoroutineScheduler.this.globalBlockingQueue.d();
            return d11 != null ? d11 : CoroutineScheduler.this.globalCpuQueue.d();
        }

        private final void n() {
            loop0: while (true) {
                boolean z10 = false;
                while (!CoroutineScheduler.this.L() && this.state != d.TERMINATED) {
                    h f10 = f(this.mayHaveLocalTasks);
                    if (f10 != null) {
                        this.minDelayUntilStealableTaskNs = 0L;
                        d(f10);
                    } else {
                        this.mayHaveLocalTasks = false;
                        if (this.minDelayUntilStealableTaskNs == 0) {
                            r();
                        } else if (z10) {
                            s(d.PARKING);
                            Thread.interrupted();
                            LockSupport.parkNanos(this.minDelayUntilStealableTaskNs);
                            this.minDelayUntilStealableTaskNs = 0L;
                        } else {
                            z10 = true;
                        }
                    }
                }
            }
            s(d.TERMINATED);
        }

        private final boolean q() {
            boolean z10;
            if (this.state != d.CPU_ACQUIRED) {
                CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
                while (true) {
                    long j10 = coroutineScheduler.controlState;
                    if (((int) ((9223367638808264704L & j10) >> 42)) == 0) {
                        z10 = false;
                        break;
                    }
                    if (CoroutineScheduler.f14408n.compareAndSet(coroutineScheduler, j10, j10 - 4398046511104L)) {
                        z10 = true;
                        break;
                    }
                }
                if (!z10) {
                    return false;
                }
                this.state = d.CPU_ACQUIRED;
            }
            return true;
        }

        private final void r() {
            if (!j()) {
                CoroutineScheduler.this.U(this);
                return;
            }
            this.workerCtl = -1;
            while (j() && this.workerCtl == -1 && !CoroutineScheduler.this.L() && this.state != d.TERMINATED) {
                s(d.PARKING);
                Thread.interrupted();
                l();
            }
        }

        private final h t(boolean blockingOnly) {
            long l10;
            int i10 = (int) (CoroutineScheduler.this.controlState & 2097151);
            if (i10 < 2) {
                return null;
            }
            int k10 = k(i10);
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            long j10 = Long.MAX_VALUE;
            for (int i11 = 0; i11 < i10; i11++) {
                k10++;
                if (k10 > i10) {
                    k10 = 1;
                }
                c b10 = coroutineScheduler.workers.b(k10);
                if (b10 != null && b10 != this) {
                    if (blockingOnly) {
                        l10 = this.localQueue.k(b10.localQueue);
                    } else {
                        l10 = this.localQueue.l(b10.localQueue);
                    }
                    if (l10 == -1) {
                        return this.localQueue.h();
                    }
                    if (l10 > 0) {
                        j10 = Math.min(j10, l10);
                    }
                }
            }
            if (j10 == Long.MAX_VALUE) {
                j10 = 0;
            }
            this.minDelayUntilStealableTaskNs = j10;
            return null;
        }

        private final void u() {
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            synchronized (coroutineScheduler.workers) {
                if (coroutineScheduler.L()) {
                    return;
                }
                if (((int) (coroutineScheduler.controlState & 2097151)) <= coroutineScheduler.corePoolSize) {
                    return;
                }
                if (f14419l.compareAndSet(this, -1, 1)) {
                    int i10 = this.indexInArray;
                    o(0);
                    coroutineScheduler.X(this, i10, 0);
                    int andDecrement = (int) (CoroutineScheduler.f14408n.getAndDecrement(coroutineScheduler) & 2097151);
                    if (andDecrement != i10) {
                        c b10 = coroutineScheduler.workers.b(andDecrement);
                        za.k.b(b10);
                        c cVar = b10;
                        coroutineScheduler.workers.c(i10, cVar);
                        cVar.o(i10);
                        coroutineScheduler.X(cVar, andDecrement, i10);
                    }
                    coroutineScheduler.workers.c(andDecrement, null);
                    Unit unit = Unit.f15173a;
                    this.state = d.TERMINATED;
                }
            }
        }

        public final h f(boolean scanLocalQueue) {
            h d10;
            if (q()) {
                return e(scanLocalQueue);
            }
            if (scanLocalQueue) {
                d10 = this.localQueue.h();
                if (d10 == null) {
                    d10 = CoroutineScheduler.this.globalBlockingQueue.d();
                }
            } else {
                d10 = CoroutineScheduler.this.globalBlockingQueue.d();
            }
            return d10 == null ? t(true) : d10;
        }

        /* renamed from: g, reason: from getter */
        public final int getIndexInArray() {
            return this.indexInArray;
        }

        /* renamed from: h, reason: from getter */
        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final int k(int upperBound) {
            int i10 = this.rngState;
            int i11 = i10 ^ (i10 << 13);
            int i12 = i11 ^ (i11 >> 17);
            int i13 = i12 ^ (i12 << 5);
            this.rngState = i13;
            int i14 = upperBound - 1;
            return (i14 & upperBound) == 0 ? i14 & i13 : (Integer.MAX_VALUE & i13) % upperBound;
        }

        public final void o(int i10) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(CoroutineScheduler.this.schedulerName);
            sb2.append("-worker-");
            sb2.append(i10 == 0 ? "TERMINATED" : String.valueOf(i10));
            setName(sb2.toString());
            this.indexInArray = i10;
        }

        public final void p(Object obj) {
            this.nextParkedWorker = obj;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            n();
        }

        public final boolean s(d newState) {
            d dVar = this.state;
            boolean z10 = dVar == d.CPU_ACQUIRED;
            if (z10) {
                CoroutineScheduler.f14408n.addAndGet(CoroutineScheduler.this, 4398046511104L);
            }
            if (dVar != newState) {
                this.state = newState;
            }
            return z10;
        }

        public c(CoroutineScheduler coroutineScheduler, int i10) {
            this();
            o(i10);
        }
    }
}
