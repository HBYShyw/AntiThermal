package qa;

import qa.g;
import za.k;

/* compiled from: ContinuationInterceptor.kt */
/* renamed from: qa.e, reason: use source file name */
/* loaded from: classes2.dex */
public interface ContinuationInterceptor extends g.b {

    /* renamed from: a, reason: collision with root package name */
    public static final b f17170a = b.f17171e;

    /* compiled from: ContinuationInterceptor.kt */
    /* renamed from: qa.e$a */
    /* loaded from: classes2.dex */
    public static final class a {
        public static <E extends g.b> E a(ContinuationInterceptor continuationInterceptor, g.c<E> cVar) {
            k.e(cVar, "key");
            if (cVar instanceof qa.b) {
                qa.b bVar = (qa.b) cVar;
                if (!bVar.a(continuationInterceptor.getKey())) {
                    return null;
                }
                E e10 = (E) bVar.b(continuationInterceptor);
                if (e10 instanceof g.b) {
                    return e10;
                }
                return null;
            }
            if (ContinuationInterceptor.f17170a != cVar) {
                return null;
            }
            k.c(continuationInterceptor, "null cannot be cast to non-null type E of kotlin.coroutines.ContinuationInterceptor.get");
            return continuationInterceptor;
        }

        public static g b(ContinuationInterceptor continuationInterceptor, g.c<?> cVar) {
            k.e(cVar, "key");
            if (!(cVar instanceof qa.b)) {
                return ContinuationInterceptor.f17170a == cVar ? h.f17173e : continuationInterceptor;
            }
            qa.b bVar = (qa.b) cVar;
            return (!bVar.a(continuationInterceptor.getKey()) || bVar.b(continuationInterceptor) == null) ? continuationInterceptor : h.f17173e;
        }
    }

    /* compiled from: ContinuationInterceptor.kt */
    /* renamed from: qa.e$b */
    /* loaded from: classes2.dex */
    public static final class b implements g.c<ContinuationInterceptor> {

        /* renamed from: e, reason: collision with root package name */
        static final /* synthetic */ b f17171e = new b();

        private b() {
        }
    }

    <T> d<T> L(d<? super T> dVar);

    void u(d<?> dVar);
}
