package td;

import kotlin.Metadata;
import ma.Unit;
import qa.ContinuationInterceptor;

/* compiled from: CoroutineContext.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u00028\u00000\u0002B\u001d\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\r¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005J\u0006\u0010\n\u001a\u00020\tJ\u0012\u0010\f\u001a\u00020\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\u0005H\u0014¨\u0006\u0011"}, d2 = {"Ltd/e2;", "T", "Lkotlinx/coroutines/internal/y;", "Lqa/g;", "context", "", "oldValue", "Lma/f0;", "F0", "", "E0", "state", "z0", "Lqa/d;", "uCont", "<init>", "(Lqa/g;Lqa/d;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class e2<T> extends kotlinx.coroutines.internal.y<T> {

    /* renamed from: h, reason: collision with root package name */
    private ThreadLocal<ma.o<qa.g, Object>> f18735h;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public e2(qa.g gVar, qa.d<? super T> dVar) {
        super(gVar.c(r0) == null ? gVar.o0(r0) : gVar, dVar);
        f2 f2Var = f2.f18738e;
        this.f18735h = new ThreadLocal<>();
        if (dVar.getF18758i().c(ContinuationInterceptor.f17170a) instanceof CoroutineDispatcher) {
            return;
        }
        Object c10 = kotlinx.coroutines.internal.e0.c(gVar, null);
        kotlinx.coroutines.internal.e0.a(gVar, c10);
        F0(gVar, c10);
    }

    public final boolean E0() {
        if (this.f18735h.get() == null) {
            return false;
        }
        this.f18735h.set(null);
        return true;
    }

    public final void F0(qa.g gVar, Object obj) {
        this.f18735h.set(ma.u.a(gVar, obj));
    }

    @Override // kotlinx.coroutines.internal.y, td.AbstractCoroutine
    protected void z0(Object obj) {
        ma.o<qa.g, Object> oVar = this.f18735h.get();
        if (oVar != null) {
            kotlinx.coroutines.internal.e0.a(oVar.a(), oVar.b());
            this.f18735h.set(null);
        }
        Object a10 = z.a(obj, this.f14403g);
        qa.d<T> dVar = this.f14403g;
        qa.g f18758i = dVar.getF18758i();
        Object c10 = kotlinx.coroutines.internal.e0.c(f18758i, null);
        e2<?> g6 = c10 != kotlinx.coroutines.internal.e0.f14355a ? b0.g(dVar, f18758i, c10) : null;
        try {
            this.f14403g.resumeWith(a10);
            Unit unit = Unit.f15173a;
        } finally {
            if (g6 == null || g6.E0()) {
                kotlinx.coroutines.internal.e0.a(f18758i, c10);
            }
        }
    }
}
