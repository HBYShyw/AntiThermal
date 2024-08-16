package gd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import kotlin.collections._Collections;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import za.Lambda;
import zc.TypeIntersectionScope;

/* compiled from: IntersectionTypeConstructor.kt */
/* renamed from: gd.f0, reason: use source file name */
/* loaded from: classes2.dex */
public final class IntersectionTypeConstructor implements TypeConstructor, kd.h {

    /* renamed from: a, reason: collision with root package name */
    private g0 f11771a;

    /* renamed from: b, reason: collision with root package name */
    private final LinkedHashSet<g0> f11772b;

    /* renamed from: c, reason: collision with root package name */
    private final int f11773c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntersectionTypeConstructor.kt */
    /* renamed from: gd.f0$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<hd.g, o0> {
        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke(hd.g gVar) {
            za.k.e(gVar, "kotlinTypeRefiner");
            return IntersectionTypeConstructor.this.u(gVar).d();
        }
    }

    /* compiled from: Comparisons.kt */
    /* renamed from: gd.f0$b */
    /* loaded from: classes2.dex */
    public static final class b<T> implements Comparator {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.l f11775e;

        public b(ya.l lVar) {
            this.f11775e = lVar;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Comparator
        public final int compare(T t7, T t10) {
            int a10;
            g0 g0Var = (g0) t7;
            ya.l lVar = this.f11775e;
            za.k.d(g0Var, "it");
            String obj = lVar.invoke(g0Var).toString();
            g0 g0Var2 = (g0) t10;
            ya.l lVar2 = this.f11775e;
            za.k.d(g0Var2, "it");
            a10 = pa.b.a(obj, lVar2.invoke(g0Var2).toString());
            return a10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntersectionTypeConstructor.kt */
    /* renamed from: gd.f0$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<g0, String> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f11776e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke(g0 g0Var) {
            za.k.e(g0Var, "it");
            return g0Var.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntersectionTypeConstructor.kt */
    /* renamed from: gd.f0$d */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.l<g0, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.l<g0, Object> f11777e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        d(ya.l<? super g0, ? extends Object> lVar) {
            super(1);
            this.f11777e = lVar;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(g0 g0Var) {
            ya.l<g0, Object> lVar = this.f11777e;
            za.k.d(g0Var, "it");
            return lVar.invoke(g0Var).toString();
        }
    }

    public IntersectionTypeConstructor(Collection<? extends g0> collection) {
        za.k.e(collection, "typesToIntersect");
        collection.isEmpty();
        LinkedHashSet<g0> linkedHashSet = new LinkedHashSet<>(collection);
        this.f11772b = linkedHashSet;
        this.f11773c = linkedHashSet.hashCode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ String g(IntersectionTypeConstructor intersectionTypeConstructor, ya.l lVar, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            lVar = c.f11776e;
        }
        return intersectionTypeConstructor.f(lVar);
    }

    public final zc.h c() {
        return TypeIntersectionScope.f20479d.a("member scope for intersection type", this.f11772b);
    }

    public final o0 d() {
        List j10;
        c1 h10 = c1.f11749f.h();
        j10 = kotlin.collections.r.j();
        return h0.l(h10, this, j10, false, c(), new a());
    }

    public final g0 e() {
        return this.f11771a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IntersectionTypeConstructor) {
            return za.k.a(this.f11772b, ((IntersectionTypeConstructor) obj).f11772b);
        }
        return false;
    }

    public final String f(ya.l<? super g0, ? extends Object> lVar) {
        List u02;
        String c02;
        za.k.e(lVar, "getProperTypeRelatedToStringify");
        u02 = _Collections.u0(this.f11772b, new b(lVar));
        c02 = _Collections.c0(u02, " & ", "{", "}", 0, null, new d(lVar), 24, null);
        return c02;
    }

    @Override // gd.TypeConstructor
    public List<TypeParameterDescriptor> getParameters() {
        List<TypeParameterDescriptor> j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // gd.TypeConstructor
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public IntersectionTypeConstructor u(hd.g gVar) {
        int u7;
        za.k.e(gVar, "kotlinTypeRefiner");
        Collection<g0> q10 = q();
        u7 = kotlin.collections.s.u(q10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = q10.iterator();
        boolean z10 = false;
        while (it.hasNext()) {
            arrayList.add(((g0) it.next()).g1(gVar));
            z10 = true;
        }
        IntersectionTypeConstructor intersectionTypeConstructor = null;
        if (z10) {
            g0 e10 = e();
            intersectionTypeConstructor = new IntersectionTypeConstructor(arrayList).i(e10 != null ? e10.g1(gVar) : null);
        }
        return intersectionTypeConstructor == null ? this : intersectionTypeConstructor;
    }

    public int hashCode() {
        return this.f11773c;
    }

    public final IntersectionTypeConstructor i(g0 g0Var) {
        return new IntersectionTypeConstructor(this.f11772b, g0Var);
    }

    @Override // gd.TypeConstructor
    public Collection<g0> q() {
        return this.f11772b;
    }

    @Override // gd.TypeConstructor
    public KotlinBuiltIns t() {
        KotlinBuiltIns t7 = this.f11772b.iterator().next().W0().t();
        za.k.d(t7, "intersectedTypes.iterato…xt().constructor.builtIns");
        return t7;
    }

    public String toString() {
        return g(this, null, 1, null);
    }

    @Override // gd.TypeConstructor
    public ClassifierDescriptor v() {
        return null;
    }

    @Override // gd.TypeConstructor
    public boolean w() {
        return false;
    }

    private IntersectionTypeConstructor(Collection<? extends g0> collection, g0 g0Var) {
        this(collection);
        this.f11771a = g0Var;
    }
}
