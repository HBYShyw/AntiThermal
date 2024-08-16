package qa;

import qa.g;
import ya.p;
import za.k;

/* compiled from: CoroutineContextImpl.kt */
/* loaded from: classes2.dex */
public abstract class a implements g.b {

    /* renamed from: e, reason: collision with root package name */
    private final g.c<?> f17160e;

    public a(g.c<?> cVar) {
        k.e(cVar, "key");
        this.f17160e = cVar;
    }

    @Override // qa.g.b, qa.g
    public <E extends g.b> E c(g.c<E> cVar) {
        return (E) g.b.a.b(this, cVar);
    }

    @Override // qa.g.b
    public g.c<?> getKey() {
        return this.f17160e;
    }

    @Override // qa.g
    public <R> R i0(R r10, p<? super R, ? super g.b, ? extends R> pVar) {
        return (R) g.b.a.a(this, r10, pVar);
    }

    @Override // qa.g
    public g j0(g.c<?> cVar) {
        return g.b.a.c(this, cVar);
    }

    @Override // qa.g
    public g o0(g gVar) {
        return g.b.a.d(this, gVar);
    }
}
