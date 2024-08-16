package kotlinx.coroutines.scheduling;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;

/* compiled from: WorkQueue.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\b\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b(\u0010)J\u0019\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u001f\u0010\n\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u0007H\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0017\u0010\u000e\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\fH\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u0011\u0010\u0010\u001a\u0004\u0018\u00010\u0002H\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u0015\u0010\u0013\u001a\u00020\u0012*\u0004\u0018\u00010\u0002H\u0002¢\u0006\u0004\b\u0013\u0010\u0014J\u000f\u0010\u0015\u001a\u0004\u0018\u00010\u0002¢\u0006\u0004\b\u0015\u0010\u0011J!\u0010\u0017\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0016\u001a\u00020\u0007¢\u0006\u0004\b\u0017\u0010\u0018J\u0015\u0010\u0019\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0000¢\u0006\u0004\b\u0019\u0010\u001aJ\u0015\u0010\u001b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0000¢\u0006\u0004\b\u001b\u0010\u001aJ\u0015\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\f¢\u0006\u0004\b\u001d\u0010\u001eR\u001c\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u001f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0017\u0010 R\u0014\u0010%\u001a\u00020\"8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b#\u0010$R\u0014\u0010'\u001a\u00020\"8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b&\u0010$¨\u0006*"}, d2 = {"Lkotlinx/coroutines/scheduling/n;", "", "Lkotlinx/coroutines/scheduling/h;", "task", "c", "(Lkotlinx/coroutines/scheduling/h;)Lkotlinx/coroutines/scheduling/h;", "victim", "", "blockingOnly", "", "m", "(Lkotlinx/coroutines/scheduling/n;Z)J", "Lkotlinx/coroutines/scheduling/d;", "queue", "j", "(Lkotlinx/coroutines/scheduling/d;)Z", "i", "()Lkotlinx/coroutines/scheduling/h;", "Lma/f0;", "d", "(Lkotlinx/coroutines/scheduling/h;)V", "h", "fair", "a", "(Lkotlinx/coroutines/scheduling/h;Z)Lkotlinx/coroutines/scheduling/h;", "l", "(Lkotlinx/coroutines/scheduling/n;)J", "k", "globalQueue", "g", "(Lkotlinx/coroutines/scheduling/d;)V", "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "buffer", "", "e", "()I", "bufferSize", "f", "size", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: kotlinx.coroutines.scheduling.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class WorkQueue {

    /* renamed from: b, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f14454b = AtomicReferenceFieldUpdater.newUpdater(WorkQueue.class, Object.class, "lastScheduledTask");

    /* renamed from: c, reason: collision with root package name */
    private static final /* synthetic */ AtomicIntegerFieldUpdater f14455c = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "producerIndex");

    /* renamed from: d, reason: collision with root package name */
    private static final /* synthetic */ AtomicIntegerFieldUpdater f14456d = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "consumerIndex");

    /* renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ AtomicIntegerFieldUpdater f14457e = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "blockingTasksInBuffer");

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final AtomicReferenceArray<h> buffer = new AtomicReferenceArray<>(128);
    private volatile /* synthetic */ Object lastScheduledTask = null;
    private volatile /* synthetic */ int producerIndex = 0;
    private volatile /* synthetic */ int consumerIndex = 0;
    private volatile /* synthetic */ int blockingTasksInBuffer = 0;

    public static /* synthetic */ h b(WorkQueue workQueue, h hVar, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return workQueue.a(hVar, z10);
    }

    private final h c(h task) {
        if (task.taskContext.getTaskMode() == 1) {
            f14457e.incrementAndGet(this);
        }
        if (e() == 127) {
            return task;
        }
        int i10 = this.producerIndex & 127;
        while (this.buffer.get(i10) != null) {
            Thread.yield();
        }
        this.buffer.lazySet(i10, task);
        f14455c.incrementAndGet(this);
        return null;
    }

    private final void d(h hVar) {
        if (hVar != null) {
            if (hVar.taskContext.getTaskMode() == 1) {
                f14457e.decrementAndGet(this);
            }
        }
    }

    private final h i() {
        h andSet;
        while (true) {
            int i10 = this.consumerIndex;
            if (i10 - this.producerIndex == 0) {
                return null;
            }
            int i11 = i10 & 127;
            if (f14456d.compareAndSet(this, i10, i10 + 1) && (andSet = this.buffer.getAndSet(i11, null)) != null) {
                d(andSet);
                return andSet;
            }
        }
    }

    private final boolean j(d queue) {
        h i10 = i();
        if (i10 == null) {
            return false;
        }
        queue.a(i10);
        return true;
    }

    private final long m(WorkQueue victim, boolean blockingOnly) {
        h hVar;
        do {
            hVar = (h) victim.lastScheduledTask;
            if (hVar == null) {
                return -2L;
            }
            if (blockingOnly) {
                if (!(hVar.taskContext.getTaskMode() == 1)) {
                    return -2L;
                }
            }
            long a10 = l.f14450e.a() - hVar.submissionTime;
            long j10 = l.f14446a;
            if (a10 < j10) {
                return j10 - a10;
            }
        } while (!f14454b.compareAndSet(victim, hVar, null));
        b(this, hVar, false, 2, null);
        return -1L;
    }

    public final h a(h task, boolean fair) {
        if (fair) {
            return c(task);
        }
        h hVar = (h) f14454b.getAndSet(this, task);
        if (hVar == null) {
            return null;
        }
        return c(hVar);
    }

    public final int e() {
        return this.producerIndex - this.consumerIndex;
    }

    public final int f() {
        Object obj = this.lastScheduledTask;
        int e10 = e();
        return obj != null ? e10 + 1 : e10;
    }

    public final void g(d globalQueue) {
        h hVar = (h) f14454b.getAndSet(this, null);
        if (hVar != null) {
            globalQueue.a(hVar);
        }
        do {
        } while (j(globalQueue));
    }

    public final h h() {
        h hVar = (h) f14454b.getAndSet(this, null);
        return hVar == null ? i() : hVar;
    }

    public final long k(WorkQueue victim) {
        int i10 = victim.consumerIndex;
        int i11 = victim.producerIndex;
        AtomicReferenceArray<h> atomicReferenceArray = victim.buffer;
        while (true) {
            if (i10 == i11) {
                break;
            }
            int i12 = i10 & 127;
            if (victim.blockingTasksInBuffer == 0) {
                break;
            }
            h hVar = atomicReferenceArray.get(i12);
            if (hVar != null) {
                if ((hVar.taskContext.getTaskMode() == 1) && atomicReferenceArray.compareAndSet(i12, hVar, null)) {
                    f14457e.decrementAndGet(victim);
                    b(this, hVar, false, 2, null);
                    return -1L;
                }
            }
            i10++;
        }
        return m(victim, true);
    }

    public final long l(WorkQueue victim) {
        h i10 = victim.i();
        if (i10 != null) {
            b(this, i10, false, 2, null);
            return -1L;
        }
        return m(victim, false);
    }
}
