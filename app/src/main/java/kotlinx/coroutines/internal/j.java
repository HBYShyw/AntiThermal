package kotlinx.coroutines.internal;

import kotlin.Metadata;
import td.CoroutineDispatcher;
import td.Delay;
import td.n0;

/* compiled from: LimitedDispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u00032\u00020\u0004B\u0017\u0012\u0006\u0010\u001c\u001a\u00020\u0001\u0012\u0006\u0010\u0011\u001a\u00020\u000e¢\u0006\u0004\b\u001d\u0010\u001eJ\b\u0010\u0006\u001a\u00020\u0005H\u0002J\u0014\u0010\b\u001a\u00020\u00052\n\u0010\u0007\u001a\u00060\u0002j\u0002`\u0003H\u0002J\b\u0010\n\u001a\u00020\tH\u0016J\u001c\u0010\r\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u000b2\n\u0010\u0007\u001a\u00060\u0002j\u0002`\u0003H\u0016R\u0014\u0010\u0011\u001a\u00020\u000e8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0012\u001a\u00020\u000e8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0012\u0010\u0010R\u001e\u0010\u0016\u001a\f\u0012\b\u0012\u00060\u0002j\u0002`\u00030\u00138\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0014\u0010\u0015R\u0018\u0010\u001b\u001a\u00060\u0017j\u0002`\u00188\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0019\u0010\u001a¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/internal/j;", "Ltd/c0;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "Ltd/o0;", "", "x0", "block", "w0", "Lma/f0;", "run", "Lqa/g;", "context", "t0", "", "h", "I", "parallelism", "runningWorkers", "Lkotlinx/coroutines/internal/o;", "j", "Lkotlinx/coroutines/internal/o;", "queue", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "k", "Ljava/lang/Object;", "workerAllocationLock", "dispatcher", "<init>", "(Ltd/c0;I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class j extends CoroutineDispatcher implements Runnable, Delay {

    /* renamed from: g, reason: collision with root package name */
    private final CoroutineDispatcher f14371g;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final int parallelism;

    /* renamed from: i, reason: collision with root package name */
    private final /* synthetic */ Delay f14373i;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private final o<Runnable> queue;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private final Object workerAllocationLock;
    private volatile int runningWorkers;

    /* JADX WARN: Multi-variable type inference failed */
    public j(CoroutineDispatcher coroutineDispatcher, int i10) {
        this.f14371g = coroutineDispatcher;
        this.parallelism = i10;
        Delay delay = coroutineDispatcher instanceof Delay ? (Delay) coroutineDispatcher : null;
        this.f14373i = delay == null ? n0.a() : delay;
        this.queue = new o<>(false);
        this.workerAllocationLock = new Object();
    }

    private final boolean w0(Runnable block) {
        this.queue.a(block);
        return this.runningWorkers >= this.parallelism;
    }

    private final boolean x0() {
        synchronized (this.workerAllocationLock) {
            if (this.runningWorkers >= this.parallelism) {
                return false;
            }
            this.runningWorkers++;
            return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x002a, code lost:
    
        r1 = r4.workerAllocationLock;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x002c, code lost:
    
        monitor-enter(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x002d, code lost:
    
        r4.runningWorkers--;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0039, code lost:
    
        if (r4.queue.c() != 0) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x003d, code lost:
    
        r4.runningWorkers++;
        r2 = ma.Unit.f15173a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x003b, code lost:
    
        monitor-exit(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x003c, code lost:
    
        return;
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void run() {
        Object obj;
        while (true) {
            int i10 = 0;
            while (true) {
                Runnable d10 = this.queue.d();
                if (d10 == null) {
                    break;
                }
                try {
                    d10.run();
                } catch (Throwable th) {
                    td.f0.a(qa.h.f17173e, th);
                }
                i10++;
                if (i10 >= 16 && this.f14371g.u0(this)) {
                    this.f14371g.t0(this, this);
                    return;
                }
            }
        }
    }

    @Override // td.CoroutineDispatcher
    public void t0(qa.g gVar, Runnable runnable) {
        if (w0(runnable) || !x0()) {
            return;
        }
        this.f14371g.t0(this, this);
    }
}
