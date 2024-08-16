package hd;

import gb.KDeclarationContainer;
import gd.AbstractTypePreparator;
import gd.IntersectionTypeConstructor;
import gd.TypeConstructor;
import gd.TypeProjection;
import gd.Variance;
import gd.a0;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.o0;
import gd.s1;
import gd.u1;
import gd.v1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import ma.NoWhenBranchMatchedException;
import uc.IntegerValueTypeConstructor;
import za.FunctionReference;
import za.Reflection;

/* compiled from: KotlinTypePreparator.kt */
/* renamed from: hd.f, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class KotlinTypePreparator extends AbstractTypePreparator {

    /* compiled from: KotlinTypePreparator.kt */
    /* renamed from: hd.f$a */
    /* loaded from: classes2.dex */
    public static final class a extends KotlinTypePreparator {

        /* renamed from: a, reason: collision with root package name */
        public static final a f12214a = new a();

        private a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KotlinTypePreparator.kt */
    /* renamed from: hd.f$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b extends FunctionReference implements ya.l<kd.i, v1> {
        b(Object obj) {
            super(1, obj);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(KotlinTypePreparator.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "prepareType(Lorg/jetbrains/kotlin/types/model/KotlinTypeMarker;)Lorg/jetbrains/kotlin/types/UnwrappedType;";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final v1 invoke(kd.i iVar) {
            za.k.e(iVar, "p0");
            return ((KotlinTypePreparator) this.f20351f).a(iVar);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "prepareType";
        }
    }

    private final o0 c(o0 o0Var) {
        int u7;
        int u10;
        List j10;
        int u11;
        g0 type;
        TypeConstructor W0 = o0Var.W0();
        boolean z10 = false;
        IntersectionTypeConstructor intersectionTypeConstructor = null;
        r5 = null;
        v1 v1Var = null;
        if (W0 instanceof tc.c) {
            tc.c cVar = (tc.c) W0;
            TypeProjection b10 = cVar.b();
            if (!(b10.a() == Variance.IN_VARIANCE)) {
                b10 = null;
            }
            if (b10 != null && (type = b10.getType()) != null) {
                v1Var = type.Z0();
            }
            v1 v1Var2 = v1Var;
            if (cVar.d() == null) {
                TypeProjection b11 = cVar.b();
                Collection<g0> q10 = cVar.q();
                u11 = kotlin.collections.s.u(q10, 10);
                ArrayList arrayList = new ArrayList(u11);
                Iterator<T> it = q10.iterator();
                while (it.hasNext()) {
                    arrayList.add(((g0) it.next()).Z0());
                }
                cVar.f(new j(b11, arrayList, null, 4, null));
            }
            kd.b bVar = kd.b.FOR_SUBTYPING;
            j d10 = cVar.d();
            za.k.b(d10);
            return new i(bVar, d10, v1Var2, o0Var.V0(), o0Var.X0(), false, 32, null);
        }
        if (W0 instanceof IntegerValueTypeConstructor) {
            Collection<g0> q11 = ((IntegerValueTypeConstructor) W0).q();
            u10 = kotlin.collections.s.u(q11, 10);
            ArrayList arrayList2 = new ArrayList(u10);
            Iterator<T> it2 = q11.iterator();
            while (it2.hasNext()) {
                g0 p10 = s1.p((g0) it2.next(), o0Var.X0());
                za.k.d(p10, "makeNullableAsSpecified(it, type.isMarkedNullable)");
                arrayList2.add(p10);
            }
            IntersectionTypeConstructor intersectionTypeConstructor2 = new IntersectionTypeConstructor(arrayList2);
            c1 V0 = o0Var.V0();
            j10 = kotlin.collections.r.j();
            return h0.k(V0, intersectionTypeConstructor2, j10, false, o0Var.u());
        }
        if (!(W0 instanceof IntersectionTypeConstructor) || !o0Var.X0()) {
            return o0Var;
        }
        IntersectionTypeConstructor intersectionTypeConstructor3 = (IntersectionTypeConstructor) W0;
        Collection<g0> q12 = intersectionTypeConstructor3.q();
        u7 = kotlin.collections.s.u(q12, 10);
        ArrayList arrayList3 = new ArrayList(u7);
        Iterator<T> it3 = q12.iterator();
        while (it3.hasNext()) {
            arrayList3.add(ld.a.u((g0) it3.next()));
            z10 = true;
        }
        if (z10) {
            g0 e10 = intersectionTypeConstructor3.e();
            intersectionTypeConstructor = new IntersectionTypeConstructor(arrayList3).i(e10 != null ? ld.a.u(e10) : null);
        }
        if (intersectionTypeConstructor != null) {
            intersectionTypeConstructor3 = intersectionTypeConstructor;
        }
        return intersectionTypeConstructor3.d();
    }

    @Override // gd.AbstractTypePreparator
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public v1 a(kd.i iVar) {
        v1 d10;
        za.k.e(iVar, "type");
        if (iVar instanceof g0) {
            v1 Z0 = ((g0) iVar).Z0();
            if (Z0 instanceof o0) {
                d10 = c((o0) Z0);
            } else if (Z0 instanceof a0) {
                a0 a0Var = (a0) Z0;
                o0 c10 = c(a0Var.e1());
                o0 c11 = c(a0Var.f1());
                d10 = (c10 == a0Var.e1() && c11 == a0Var.f1()) ? Z0 : h0.d(c10, c11);
            } else {
                throw new NoWhenBranchMatchedException();
            }
            return u1.c(d10, Z0, new b(this));
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }
}
