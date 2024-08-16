package qa;

import java.io.Serializable;
import qa.g;
import ya.p;
import za.k;

/* compiled from: CoroutineContextImpl.kt */
/* loaded from: classes2.dex */
public final class h implements g, Serializable {

    /* renamed from: e, reason: collision with root package name */
    public static final h f17173e = new h();
    private static final long serialVersionUID = 0;

    private h() {
    }

    private final Object readResolve() {
        return f17173e;
    }

    @Override // qa.g
    public <E extends g.b> E c(g.c<E> cVar) {
        k.e(cVar, "key");
        return null;
    }

    public int hashCode() {
        return 0;
    }

    @Override // qa.g
    public <R> R i0(R r10, p<? super R, ? super g.b, ? extends R> pVar) {
        k.e(pVar, "operation");
        return r10;
    }

    @Override // qa.g
    public g j0(g.c<?> cVar) {
        k.e(cVar, "key");
        return this;
    }

    @Override // qa.g
    public g o0(g gVar) {
        k.e(gVar, "context");
        return gVar;
    }

    public String toString() {
        return "EmptyCoroutineContext";
    }
}
