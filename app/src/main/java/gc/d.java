package gc;

import gd.TypeConstructor;
import gd.TypeProjection;
import gd.Variance;
import gd.a0;
import gd.c1;
import gd.d0;
import gd.d1;
import gd.g0;
import gd.h0;
import gd.i0;
import gd.n0;
import gd.o0;
import gd.s0;
import gd.s1;
import gd.u1;
import gd.v1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ma.NoWhenBranchMatchedException;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;

/* compiled from: typeEnhancement.kt */
/* loaded from: classes2.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    private final bc.c f11651a;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: typeEnhancement.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final g0 f11652a;

        /* renamed from: b, reason: collision with root package name */
        private final int f11653b;

        public a(g0 g0Var, int i10) {
            this.f11652a = g0Var;
            this.f11653b = i10;
        }

        public final int a() {
            return this.f11653b;
        }

        public final g0 b() {
            return this.f11652a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: typeEnhancement.kt */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final o0 f11654a;

        /* renamed from: b, reason: collision with root package name */
        private final int f11655b;

        /* renamed from: c, reason: collision with root package name */
        private final boolean f11656c;

        public b(o0 o0Var, int i10, boolean z10) {
            this.f11654a = o0Var;
            this.f11655b = i10;
            this.f11656c = z10;
        }

        public final boolean a() {
            return this.f11656c;
        }

        public final int b() {
            return this.f11655b;
        }

        public final o0 c() {
            return this.f11654a;
        }
    }

    public d(bc.c cVar) {
        za.k.e(cVar, "javaResolverSettings");
        this.f11651a = cVar;
    }

    private final b b(o0 o0Var, ya.l<? super Integer, e> lVar, int i10, o oVar, boolean z10, boolean z11) {
        ClassifierDescriptor f10;
        Boolean h10;
        TypeConstructor W0;
        int u7;
        int u10;
        c cVar;
        List o10;
        qb.g e10;
        int u11;
        int u12;
        boolean z12;
        boolean z13;
        a aVar;
        TypeProjection s7;
        ya.l<? super Integer, e> lVar2 = lVar;
        boolean a10 = p.a(oVar);
        boolean z14 = (z11 && z10) ? false : true;
        g0 g0Var = null;
        if (!a10 && o0Var.U0().isEmpty()) {
            return new b(null, 1, false);
        }
        ClassifierDescriptor v7 = o0Var.W0().v();
        if (v7 == null) {
            return new b(null, 1, false);
        }
        e invoke = lVar2.invoke(Integer.valueOf(i10));
        f10 = r.f(v7, invoke, oVar);
        h10 = r.h(invoke, oVar);
        if (f10 == null || (W0 = f10.n()) == null) {
            W0 = o0Var.W0();
        }
        TypeConstructor typeConstructor = W0;
        za.k.d(typeConstructor, "enhancedClassifier?.typeConstructor ?: constructor");
        int i11 = i10 + 1;
        List<TypeProjection> U0 = o0Var.U0();
        List<TypeParameterDescriptor> parameters = typeConstructor.getParameters();
        za.k.d(parameters, "typeConstructor.parameters");
        Iterator<T> it = U0.iterator();
        Iterator<T> it2 = parameters.iterator();
        u7 = kotlin.collections.s.u(U0, 10);
        u10 = kotlin.collections.s.u(parameters, 10);
        ArrayList arrayList = new ArrayList(Math.min(u7, u10));
        while (it.hasNext() && it2.hasNext()) {
            Object next = it.next();
            TypeParameterDescriptor typeParameterDescriptor = (TypeParameterDescriptor) it2.next();
            TypeProjection typeProjection = (TypeProjection) next;
            if (!z14) {
                z13 = z14;
                aVar = new a(g0Var, 0);
            } else {
                z13 = z14;
                if (!typeProjection.b()) {
                    aVar = d(typeProjection.getType().Z0(), lVar2, i11, z11);
                } else if (lVar2.invoke(Integer.valueOf(i11)).d() == h.FORCE_FLEXIBILITY) {
                    v1 Z0 = typeProjection.getType().Z0();
                    aVar = new a(h0.d(d0.c(Z0).a1(false), d0.d(Z0).a1(true)), 1);
                } else {
                    aVar = new a(null, 1);
                }
            }
            i11 += aVar.a();
            if (aVar.b() != null) {
                g0 b10 = aVar.b();
                Variance a11 = typeProjection.a();
                za.k.d(a11, "arg.projectionKind");
                s7 = ld.a.f(b10, a11, typeParameterDescriptor);
            } else if (f10 == null || typeProjection.b()) {
                s7 = f10 != null ? s1.s(typeParameterDescriptor) : null;
            } else {
                g0 type = typeProjection.getType();
                za.k.d(type, "arg.type");
                Variance a12 = typeProjection.a();
                za.k.d(a12, "arg.projectionKind");
                s7 = ld.a.f(type, a12, typeParameterDescriptor);
            }
            arrayList.add(s7);
            lVar2 = lVar;
            z14 = z13;
            g0Var = null;
        }
        int i12 = i11 - i10;
        if (f10 == null && h10 == null) {
            if (!arrayList.isEmpty()) {
                Iterator it3 = arrayList.iterator();
                while (it3.hasNext()) {
                    if (!(((TypeProjection) it3.next()) == null)) {
                        z12 = false;
                        break;
                    }
                }
            }
            z12 = true;
            if (z12) {
                return new b(null, i12, false);
            }
        }
        qb.g[] gVarArr = new qb.g[3];
        gVarArr[0] = o0Var.i();
        cVar = r.f11737b;
        if (!(f10 != null)) {
            cVar = null;
        }
        gVarArr[1] = cVar;
        qb.g g6 = r.g();
        if (!(h10 != null)) {
            g6 = null;
        }
        gVarArr[2] = g6;
        o10 = kotlin.collections.r.o(gVarArr);
        e10 = r.e(o10);
        c1 b11 = d1.b(e10);
        List<TypeProjection> U02 = o0Var.U0();
        Iterator it4 = arrayList.iterator();
        Iterator<T> it5 = U02.iterator();
        u11 = kotlin.collections.s.u(arrayList, 10);
        u12 = kotlin.collections.s.u(U02, 10);
        ArrayList arrayList2 = new ArrayList(Math.min(u11, u12));
        while (it4.hasNext() && it5.hasNext()) {
            Object next2 = it4.next();
            TypeProjection typeProjection2 = (TypeProjection) it5.next();
            TypeProjection typeProjection3 = (TypeProjection) next2;
            if (typeProjection3 != null) {
                typeProjection2 = typeProjection3;
            }
            arrayList2.add(typeProjection2);
        }
        o0 j10 = h0.j(b11, typeConstructor, arrayList2, h10 != null ? h10.booleanValue() : o0Var.X0(), null, 16, null);
        if (invoke.b()) {
            j10 = e(j10);
        }
        return new b(j10, i12, h10 != null && invoke.e());
    }

    static /* synthetic */ b c(d dVar, o0 o0Var, ya.l lVar, int i10, o oVar, boolean z10, boolean z11, int i11, Object obj) {
        return dVar.b(o0Var, lVar, i10, oVar, (i11 & 8) != 0 ? false : z10, (i11 & 16) != 0 ? false : z11);
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0096, code lost:
    
        if (r11 == null) goto L40;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final a d(v1 v1Var, ya.l<? super Integer, e> lVar, int i10, boolean z10) {
        g0 c10;
        g0 g0Var = null;
        if (i0.a(v1Var)) {
            return new a(null, 1);
        }
        if (v1Var instanceof a0) {
            boolean z11 = v1Var instanceof n0;
            a0 a0Var = (a0) v1Var;
            b b10 = b(a0Var.e1(), lVar, i10, o.FLEXIBLE_LOWER, z11, z10);
            b b11 = b(a0Var.f1(), lVar, i10, o.FLEXIBLE_UPPER, z11, z10);
            b10.b();
            b11.b();
            if (b10.c() != null || b11.c() != null) {
                if (b10.a() || b11.a()) {
                    o0 c11 = b11.c();
                    if (c11 != null) {
                        o0 c12 = b10.c();
                        if (c12 == null) {
                            c12 = c11;
                        }
                        c10 = h0.d(c12, c11);
                    }
                    c10 = b10.c();
                    za.k.b(c10);
                    g0Var = u1.d(v1Var, c10);
                } else if (z11) {
                    o0 c13 = b10.c();
                    if (c13 == null) {
                        c13 = a0Var.e1();
                    }
                    o0 c14 = b11.c();
                    if (c14 == null) {
                        c14 = a0Var.f1();
                    }
                    g0Var = new dc.h(c13, c14);
                } else {
                    o0 c15 = b10.c();
                    if (c15 == null) {
                        c15 = a0Var.e1();
                    }
                    o0 c16 = b11.c();
                    if (c16 == null) {
                        c16 = a0Var.f1();
                    }
                    g0Var = h0.d(c15, c16);
                }
            }
            return new a(g0Var, b10.b());
        }
        if (!(v1Var instanceof o0)) {
            throw new NoWhenBranchMatchedException();
        }
        b c17 = c(this, (o0) v1Var, lVar, i10, o.INFLEXIBLE, false, z10, 8, null);
        return new a(c17.a() ? u1.d(v1Var, c17.c()) : c17.c(), c17.b());
    }

    private final o0 e(o0 o0Var) {
        if (this.f11651a.a()) {
            return s0.h(o0Var, true);
        }
        return new g(o0Var);
    }

    public final g0 a(g0 g0Var, ya.l<? super Integer, e> lVar, boolean z10) {
        za.k.e(g0Var, "<this>");
        za.k.e(lVar, "qualifiers");
        return d(g0Var.Z0(), lVar, 0, z10).b();
    }
}
