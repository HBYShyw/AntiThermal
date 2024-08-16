package qa;

import qa.g;
import qa.g.b;
import ya.l;
import za.k;

/* compiled from: CoroutineContextImpl.kt */
/* loaded from: classes2.dex */
public abstract class b<B extends g.b, E extends B> implements g.c<E> {

    /* renamed from: e, reason: collision with root package name */
    private final l<g.b, E> f17161e;

    /* renamed from: f, reason: collision with root package name */
    private final g.c<?> f17162f;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [qa.g$c<?>] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r3v0, types: [ya.l<qa.g$b, E extends B>, ya.l<? super qa.g$b, ? extends E extends B>, java.lang.Object] */
    public b(g.c<B> cVar, l<? super g.b, ? extends E> lVar) {
        k.e(cVar, "baseKey");
        k.e(lVar, "safeCast");
        this.f17161e = lVar;
        this.f17162f = cVar instanceof b ? (g.c<B>) ((b) cVar).f17162f : cVar;
    }

    public final boolean a(g.c<?> cVar) {
        k.e(cVar, "key");
        return cVar == this || this.f17162f == cVar;
    }

    /* JADX WARN: Incorrect return type in method signature: (Lqa/g$b;)TE; */
    public final g.b b(g.b bVar) {
        k.e(bVar, "element");
        return (g.b) this.f17161e.invoke(bVar);
    }
}
