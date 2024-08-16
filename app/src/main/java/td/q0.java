package td;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.Metadata;
import ra.IntrinsicsJvm;

/* compiled from: Builders.common.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u00028\u00000\u0002B\u001d\u0012\u0006\u0010\u0010\u001a\u00020\u000f\u0012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00000\u0011¢\u0006\u0004\b\u0013\u0010\u0014J\u000f\u0010\u0004\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u000f\u0010\u0006\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\u0006\u0010\u0005J\u0019\u0010\n\u001a\u00020\t2\b\u0010\b\u001a\u0004\u0018\u00010\u0007H\u0014¢\u0006\u0004\b\n\u0010\u000bJ\u0019\u0010\f\u001a\u00020\t2\b\u0010\b\u001a\u0004\u0018\u00010\u0007H\u0014¢\u0006\u0004\b\f\u0010\u000bJ\u000f\u0010\r\u001a\u0004\u0018\u00010\u0007¢\u0006\u0004\b\r\u0010\u000e¨\u0006\u0015"}, d2 = {"Ltd/q0;", "T", "Lkotlinx/coroutines/internal/y;", "", "G0", "()Z", "F0", "", "state", "Lma/f0;", "o", "(Ljava/lang/Object;)V", "z0", "E0", "()Ljava/lang/Object;", "Lqa/g;", "context", "Lqa/d;", "uCont", "<init>", "(Lqa/g;Lqa/d;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class q0<T> extends kotlinx.coroutines.internal.y<T> {

    /* renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ AtomicIntegerFieldUpdater f18778h = AtomicIntegerFieldUpdater.newUpdater(q0.class, "_decision");
    private volatile /* synthetic */ int _decision;

    public q0(qa.g gVar, qa.d<? super T> dVar) {
        super(gVar, dVar);
        this._decision = 0;
    }

    private final boolean F0() {
        do {
            int i10 = this._decision;
            if (i10 != 0) {
                if (i10 == 1) {
                    return false;
                }
                throw new IllegalStateException("Already resumed".toString());
            }
        } while (!f18778h.compareAndSet(this, 0, 2));
        return true;
    }

    private final boolean G0() {
        do {
            int i10 = this._decision;
            if (i10 != 0) {
                if (i10 == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!f18778h.compareAndSet(this, 0, 1));
        return true;
    }

    public final Object E0() {
        Object c10;
        if (G0()) {
            c10 = ra.d.c();
            return c10;
        }
        Object h10 = q1.h(J());
        if (h10 instanceof v) {
            throw ((v) h10).f18800a;
        }
        return h10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.internal.y, td.p1
    public void o(Object state) {
        z0(state);
    }

    @Override // kotlinx.coroutines.internal.y, td.AbstractCoroutine
    protected void z0(Object state) {
        qa.d b10;
        if (F0()) {
            return;
        }
        b10 = IntrinsicsJvm.b(this.f14403g);
        kotlinx.coroutines.internal.f.c(b10, z.a(state, this.f14403g), null, 2, null);
    }
}
