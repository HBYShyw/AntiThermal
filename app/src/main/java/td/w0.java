package td;

import kotlin.Metadata;
import kotlinx.coroutines.internal.ArrayQueue;

/* compiled from: EventLoop.common.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\b \u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0016\u0010\u0017J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0006\u0010\u0006\u001a\u00020\u0002J\u0012\u0010\n\u001a\u00020\t2\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\u0007J\u0010\u0010\u000b\u001a\u00020\t2\b\b\u0002\u0010\u0003\u001a\u00020\u0002J\u0010\u0010\f\u001a\u00020\t2\b\b\u0002\u0010\u0003\u001a\u00020\u0002J\b\u0010\r\u001a\u00020\tH\u0016R\u0014\u0010\u0010\u001a\u00020\u00048TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0013\u001a\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0012¨\u0006\u0018"}, d2 = {"Ltd/w0;", "Ltd/c0;", "", "unconfined", "", "x0", "E0", "Ltd/r0;", "task", "Lma/f0;", "y0", "A0", "w0", "F0", "z0", "()J", "nextTime", "C0", "()Z", "isUnconfinedLoopActive", "D0", "isUnconfinedQueueEmpty", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class w0 extends CoroutineDispatcher {

    /* renamed from: g, reason: collision with root package name */
    private long f18804g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f18805h;

    /* renamed from: i, reason: collision with root package name */
    private ArrayQueue<r0<?>> f18806i;

    public static /* synthetic */ void B0(w0 w0Var, boolean z10, int i10, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: incrementUseCount");
        }
        if ((i10 & 1) != 0) {
            z10 = false;
        }
        w0Var.A0(z10);
    }

    private final long x0(boolean unconfined) {
        return unconfined ? 4294967296L : 1L;
    }

    public final void A0(boolean z10) {
        this.f18804g += x0(z10);
        if (z10) {
            return;
        }
        this.f18805h = true;
    }

    public final boolean C0() {
        return this.f18804g >= x0(true);
    }

    public final boolean D0() {
        ArrayQueue<r0<?>> arrayQueue = this.f18806i;
        if (arrayQueue != null) {
            return arrayQueue.c();
        }
        return true;
    }

    public final boolean E0() {
        r0<?> d10;
        ArrayQueue<r0<?>> arrayQueue = this.f18806i;
        if (arrayQueue == null || (d10 = arrayQueue.d()) == null) {
            return false;
        }
        d10.run();
        return true;
    }

    public void F0() {
    }

    public final void w0(boolean z10) {
        long x02 = this.f18804g - x0(z10);
        this.f18804g = x02;
        if (x02 <= 0 && this.f18805h) {
            F0();
        }
    }

    public final void y0(r0<?> r0Var) {
        ArrayQueue<r0<?>> arrayQueue = this.f18806i;
        if (arrayQueue == null) {
            arrayQueue = new ArrayQueue<>();
            this.f18806i = arrayQueue;
        }
        arrayQueue.a(r0Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long z0() {
        ArrayQueue<r0<?>> arrayQueue = this.f18806i;
        return (arrayQueue == null || arrayQueue.c()) ? Long.MAX_VALUE : 0L;
    }
}
