package md;

import gd.TypeConstructor;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.TypeSubstitutor;
import gd.Variance;
import gd.d0;
import gd.g0;
import gd.h0;
import gd.h1;
import gd.o0;
import gd.o1;
import gd.s1;
import gd.u1;
import gd.v1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import kotlin.collections.s;
import ma.NoWhenBranchMatchedException;
import ma.o;
import mb.KotlinBuiltIns;
import pb.TypeParameterDescriptor;
import tc.d;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: CapturedTypeApproximation.kt */
/* loaded from: classes2.dex */
public final class b {

    /* compiled from: CapturedTypeApproximation.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f15457a;

        static {
            int[] iArr = new int[Variance.values().length];
            try {
                iArr[Variance.INVARIANT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[Variance.IN_VARIANCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[Variance.OUT_VARIANCE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f15457a = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CapturedTypeApproximation.kt */
    /* renamed from: md.b$b, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0078b extends Lambda implements l<v1, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final C0078b f15458e = new C0078b();

        C0078b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(v1 v1Var) {
            k.d(v1Var, "it");
            return Boolean.valueOf(d.d(v1Var));
        }
    }

    /* compiled from: CapturedTypeApproximation.kt */
    /* loaded from: classes2.dex */
    public static final class c extends h1 {
        c() {
        }

        @Override // gd.h1
        public TypeProjection k(TypeConstructor typeConstructor) {
            k.e(typeConstructor, "key");
            tc.b bVar = typeConstructor instanceof tc.b ? (tc.b) typeConstructor : null;
            if (bVar == null) {
                return null;
            }
            if (bVar.b().b()) {
                return new TypeProjectionImpl(Variance.OUT_VARIANCE, bVar.b().getType());
            }
            return bVar.b();
        }
    }

    public static final md.a<g0> a(g0 g0Var) {
        List<o> G0;
        Object e10;
        k.e(g0Var, "type");
        if (d0.b(g0Var)) {
            md.a<g0> a10 = a(d0.c(g0Var));
            md.a<g0> a11 = a(d0.d(g0Var));
            return new md.a<>(u1.b(h0.d(d0.c(a10.c()), d0.d(a11.c())), g0Var), u1.b(h0.d(d0.c(a10.d()), d0.d(a11.d())), g0Var));
        }
        TypeConstructor W0 = g0Var.W0();
        if (d.d(g0Var)) {
            k.c(W0, "null cannot be cast to non-null type org.jetbrains.kotlin.resolve.calls.inference.CapturedTypeConstructor");
            TypeProjection b10 = ((tc.b) W0).b();
            g0 type = b10.getType();
            k.d(type, "typeProjection.type");
            g0 b11 = b(type, g0Var);
            int i10 = a.f15457a[b10.a().ordinal()];
            if (i10 == 2) {
                o0 I = ld.a.i(g0Var).I();
                k.d(I, "type.builtIns.nullableAnyType");
                return new md.a<>(b11, I);
            }
            if (i10 == 3) {
                o0 H = ld.a.i(g0Var).H();
                k.d(H, "type.builtIns.nothingType");
                return new md.a<>(b(H, g0Var), b11);
            }
            throw new AssertionError("Only nontrivial projections should have been captured, not: " + b10);
        }
        if (!g0Var.U0().isEmpty() && g0Var.U0().size() == W0.getParameters().size()) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            List<TypeProjection> U0 = g0Var.U0();
            List<TypeParameterDescriptor> parameters = W0.getParameters();
            k.d(parameters, "typeConstructor.parameters");
            G0 = _Collections.G0(U0, parameters);
            for (o oVar : G0) {
                TypeProjection typeProjection = (TypeProjection) oVar.a();
                TypeParameterDescriptor typeParameterDescriptor = (TypeParameterDescriptor) oVar.b();
                k.d(typeParameterDescriptor, "typeParameter");
                md.c g6 = g(typeProjection, typeParameterDescriptor);
                if (typeProjection.b()) {
                    arrayList.add(g6);
                    arrayList2.add(g6);
                } else {
                    md.a<md.c> d10 = d(g6);
                    md.c a12 = d10.a();
                    md.c b12 = d10.b();
                    arrayList.add(a12);
                    arrayList2.add(b12);
                }
            }
            boolean z10 = true;
            if (!arrayList.isEmpty()) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (!((md.c) it.next()).d()) {
                        break;
                    }
                }
            }
            z10 = false;
            if (z10) {
                e10 = ld.a.i(g0Var).H();
                k.d(e10, "type.builtIns.nothingType");
            } else {
                e10 = e(g0Var, arrayList);
            }
            return new md.a<>(e10, e(g0Var, arrayList2));
        }
        return new md.a<>(g0Var, g0Var);
    }

    private static final g0 b(g0 g0Var, g0 g0Var2) {
        g0 q10 = s1.q(g0Var, g0Var2.X0());
        k.d(q10, "makeNullableIfNeeded(this, type.isMarkedNullable)");
        return q10;
    }

    public static final TypeProjection c(TypeProjection typeProjection, boolean z10) {
        if (typeProjection == null) {
            return null;
        }
        if (typeProjection.b()) {
            return typeProjection;
        }
        g0 type = typeProjection.getType();
        k.d(type, "typeProjection.type");
        if (!s1.c(type, C0078b.f15458e)) {
            return typeProjection;
        }
        Variance a10 = typeProjection.a();
        k.d(a10, "typeProjection.projectionKind");
        if (a10 == Variance.OUT_VARIANCE) {
            return new TypeProjectionImpl(a10, a(type).d());
        }
        if (z10) {
            return new TypeProjectionImpl(a10, a(type).c());
        }
        return f(typeProjection);
    }

    private static final md.a<md.c> d(md.c cVar) {
        md.a<g0> a10 = a(cVar.a());
        g0 a11 = a10.a();
        g0 b10 = a10.b();
        md.a<g0> a12 = a(cVar.b());
        return new md.a<>(new md.c(cVar.c(), b10, a12.a()), new md.c(cVar.c(), a11, a12.b()));
    }

    private static final g0 e(g0 g0Var, List<md.c> list) {
        int u7;
        g0Var.U0().size();
        list.size();
        u7 = s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(h((md.c) it.next()));
        }
        return o1.e(g0Var, arrayList, null, null, 6, null);
    }

    private static final TypeProjection f(TypeProjection typeProjection) {
        TypeSubstitutor g6 = TypeSubstitutor.g(new c());
        k.d(g6, "create(object : TypeCons…ojection\n        }\n    })");
        return g6.t(typeProjection);
    }

    private static final md.c g(TypeProjection typeProjection, TypeParameterDescriptor typeParameterDescriptor) {
        int i10 = a.f15457a[TypeSubstitutor.c(typeParameterDescriptor.s(), typeProjection).ordinal()];
        if (i10 == 1) {
            g0 type = typeProjection.getType();
            k.d(type, "type");
            g0 type2 = typeProjection.getType();
            k.d(type2, "type");
            return new md.c(typeParameterDescriptor, type, type2);
        }
        if (i10 == 2) {
            g0 type3 = typeProjection.getType();
            k.d(type3, "type");
            o0 I = wc.c.j(typeParameterDescriptor).I();
            k.d(I, "typeParameter.builtIns.nullableAnyType");
            return new md.c(typeParameterDescriptor, type3, I);
        }
        if (i10 != 3) {
            throw new NoWhenBranchMatchedException();
        }
        o0 H = wc.c.j(typeParameterDescriptor).H();
        k.d(H, "typeParameter.builtIns.nothingType");
        g0 type4 = typeProjection.getType();
        k.d(type4, "type");
        return new md.c(typeParameterDescriptor, H, type4);
    }

    private static final TypeProjection h(md.c cVar) {
        cVar.d();
        if (!k.a(cVar.a(), cVar.b())) {
            Variance s7 = cVar.c().s();
            Variance variance = Variance.IN_VARIANCE;
            if (s7 != variance) {
                if (!KotlinBuiltIns.m0(cVar.a()) || cVar.c().s() == variance) {
                    return KotlinBuiltIns.o0(cVar.b()) ? new TypeProjectionImpl(i(cVar, variance), cVar.a()) : new TypeProjectionImpl(i(cVar, Variance.OUT_VARIANCE), cVar.b());
                }
                return new TypeProjectionImpl(i(cVar, Variance.OUT_VARIANCE), cVar.b());
            }
        }
        return new TypeProjectionImpl(cVar.a());
    }

    private static final Variance i(md.c cVar, Variance variance) {
        return variance == cVar.c().s() ? Variance.INVARIANT : variance;
    }
}
