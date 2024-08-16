package td;

import kotlin.Metadata;

/* compiled from: CoroutineExceptionHandler.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0018\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u001a\u0018\u0010\b\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0000¨\u0006\t"}, d2 = {"Lqa/g;", "context", "", "exception", "Lma/f0;", "a", "originalException", "thrownException", "b", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class f0 {
    public static final void a(qa.g gVar, Throwable th) {
        try {
            d0 d0Var = (d0) gVar.c(d0.f18730c);
            if (d0Var != null) {
                d0Var.N(gVar, th);
            } else {
                e0.a(gVar, th);
            }
        } catch (Throwable th2) {
            e0.a(gVar, b(th, th2));
        }
    }

    public static final Throwable b(Throwable th, Throwable th2) {
        if (th == th2) {
            return th;
        }
        RuntimeException runtimeException = new RuntimeException("Exception while trying to handle coroutine exception", th2);
        ma.b.a(runtimeException, th);
        return runtimeException;
    }
}
