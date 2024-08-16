package qa;

import java.io.Serializable;
import ma.Unit;
import qa.g;
import ya.p;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;
import za.x;

/* compiled from: CoroutineContextImpl.kt */
/* loaded from: classes2.dex */
public final class c implements g, Serializable {

    /* renamed from: e, reason: collision with root package name */
    private final g f17163e;

    /* renamed from: f, reason: collision with root package name */
    private final g.b f17164f;

    /* compiled from: CoroutineContextImpl.kt */
    /* loaded from: classes2.dex */
    private static final class a implements Serializable {

        /* renamed from: f, reason: collision with root package name */
        public static final C0088a f17165f = new C0088a(null);
        private static final long serialVersionUID = 0;

        /* renamed from: e, reason: collision with root package name */
        private final g[] f17166e;

        /* compiled from: CoroutineContextImpl.kt */
        /* renamed from: qa.c$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0088a {
            private C0088a() {
            }

            public /* synthetic */ C0088a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        public a(g[] gVarArr) {
            k.e(gVarArr, "elements");
            this.f17166e = gVarArr;
        }

        private final Object readResolve() {
            g[] gVarArr = this.f17166e;
            g gVar = h.f17173e;
            for (g gVar2 : gVarArr) {
                gVar = gVar.o0(gVar2);
            }
            return gVar;
        }
    }

    /* compiled from: CoroutineContextImpl.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements p<String, g.b, String> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f17167e = new b();

        b() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke(String str, g.b bVar) {
            k.e(str, "acc");
            k.e(bVar, "element");
            if (str.length() == 0) {
                return bVar.toString();
            }
            return str + ", " + bVar;
        }
    }

    /* compiled from: CoroutineContextImpl.kt */
    /* renamed from: qa.c$c, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    static final class C0089c extends Lambda implements p<Unit, g.b, Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ g[] f17168e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ x f17169f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C0089c(g[] gVarArr, x xVar) {
            super(2);
            this.f17168e = gVarArr;
            this.f17169f = xVar;
        }

        public final void a(Unit unit, g.b bVar) {
            k.e(unit, "<anonymous parameter 0>");
            k.e(bVar, "element");
            g[] gVarArr = this.f17168e;
            x xVar = this.f17169f;
            int i10 = xVar.f20375e;
            xVar.f20375e = i10 + 1;
            gVarArr[i10] = bVar;
        }

        @Override // ya.p
        public /* bridge */ /* synthetic */ Unit invoke(Unit unit, g.b bVar) {
            a(unit, bVar);
            return Unit.f15173a;
        }
    }

    public c(g gVar, g.b bVar) {
        k.e(gVar, "left");
        k.e(bVar, "element");
        this.f17163e = gVar;
        this.f17164f = bVar;
    }

    private final boolean a(g.b bVar) {
        return k.a(c(bVar.getKey()), bVar);
    }

    private final boolean d(c cVar) {
        while (a(cVar.f17164f)) {
            g gVar = cVar.f17163e;
            if (gVar instanceof c) {
                cVar = (c) gVar;
            } else {
                k.c(gVar, "null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
                return a((g.b) gVar);
            }
        }
        return false;
    }

    private final int e() {
        int i10 = 2;
        while (true) {
            g gVar = this.f17163e;
            this = gVar instanceof c ? (c) gVar : null;
            if (this == null) {
                return i10;
            }
            i10++;
        }
    }

    private final Object writeReplace() {
        int e10 = e();
        g[] gVarArr = new g[e10];
        x xVar = new x();
        i0(Unit.f15173a, new C0089c(gVarArr, xVar));
        if (xVar.f20375e == e10) {
            return new a(gVarArr);
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    @Override // qa.g
    public <E extends g.b> E c(g.c<E> cVar) {
        k.e(cVar, "key");
        while (true) {
            E e10 = (E) this.f17164f.c(cVar);
            if (e10 != null) {
                return e10;
            }
            g gVar = this.f17163e;
            if (gVar instanceof c) {
                this = (c) gVar;
            } else {
                return (E) gVar.c(cVar);
            }
        }
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof c) {
                c cVar = (c) obj;
                if (cVar.e() != e() || !cVar.d(this)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.f17163e.hashCode() + this.f17164f.hashCode();
    }

    @Override // qa.g
    public <R> R i0(R r10, p<? super R, ? super g.b, ? extends R> pVar) {
        k.e(pVar, "operation");
        return pVar.invoke((Object) this.f17163e.i0(r10, pVar), this.f17164f);
    }

    @Override // qa.g
    public g j0(g.c<?> cVar) {
        k.e(cVar, "key");
        if (this.f17164f.c(cVar) != null) {
            return this.f17163e;
        }
        g j02 = this.f17163e.j0(cVar);
        return j02 == this.f17163e ? this : j02 == h.f17173e ? this.f17164f : new c(j02, this.f17164f);
    }

    @Override // qa.g
    public g o0(g gVar) {
        return g.a.a(this, gVar);
    }

    public String toString() {
        return '[' + ((String) i0("", b.f17167e)) + ']';
    }
}
