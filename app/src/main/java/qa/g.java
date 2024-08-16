package qa;

import qa.ContinuationInterceptor;
import ya.p;
import za.Lambda;
import za.k;

/* compiled from: CoroutineContext.kt */
/* loaded from: classes2.dex */
public interface g {

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: CoroutineContext.kt */
        /* renamed from: qa.g$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0090a extends Lambda implements p<g, b, g> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0090a f17172e = new C0090a();

            C0090a() {
                super(2);
            }

            @Override // ya.p
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final g invoke(g gVar, b bVar) {
                qa.c cVar;
                k.e(gVar, "acc");
                k.e(bVar, "element");
                g j02 = gVar.j0(bVar.getKey());
                h hVar = h.f17173e;
                if (j02 == hVar) {
                    return bVar;
                }
                ContinuationInterceptor.b bVar2 = ContinuationInterceptor.f17170a;
                ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor) j02.c(bVar2);
                if (continuationInterceptor == null) {
                    cVar = new qa.c(j02, bVar);
                } else {
                    g j03 = j02.j0(bVar2);
                    if (j03 == hVar) {
                        return new qa.c(bVar, continuationInterceptor);
                    }
                    cVar = new qa.c(new qa.c(j03, bVar), continuationInterceptor);
                }
                return cVar;
            }
        }

        public static g a(g gVar, g gVar2) {
            k.e(gVar2, "context");
            return gVar2 == h.f17173e ? gVar : (g) gVar2.i0(gVar, C0090a.f17172e);
        }
    }

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes2.dex */
    public interface b extends g {

        /* compiled from: CoroutineContext.kt */
        /* loaded from: classes2.dex */
        public static final class a {
            public static <R> R a(b bVar, R r10, p<? super R, ? super b, ? extends R> pVar) {
                k.e(pVar, "operation");
                return pVar.invoke(r10, bVar);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public static <E extends b> E b(b bVar, c<E> cVar) {
                k.e(cVar, "key");
                if (!k.a(bVar.getKey(), cVar)) {
                    return null;
                }
                k.c(bVar, "null cannot be cast to non-null type E of kotlin.coroutines.CoroutineContext.Element.get");
                return bVar;
            }

            public static g c(b bVar, c<?> cVar) {
                k.e(cVar, "key");
                return k.a(bVar.getKey(), cVar) ? h.f17173e : bVar;
            }

            public static g d(b bVar, g gVar) {
                k.e(gVar, "context");
                return a.a(bVar, gVar);
            }
        }

        @Override // qa.g
        <E extends b> E c(c<E> cVar);

        c<?> getKey();
    }

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes2.dex */
    public interface c<E extends b> {
    }

    <E extends b> E c(c<E> cVar);

    <R> R i0(R r10, p<? super R, ? super b, ? extends R> pVar);

    g j0(c<?> cVar);

    g o0(g gVar);
}
