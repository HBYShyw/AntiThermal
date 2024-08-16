package hd;

import gd.TypeProjection;
import gd.TypeSubstitutor;
import gd.Variance;
import gd.g0;
import gd.h0;
import gd.h1;
import gd.o0;
import gd.v1;
import hd.KotlinTypePreparator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import pb.TypeParameterDescriptor;

/* compiled from: NewCapturedType.kt */
/* loaded from: classes2.dex */
public final class k {
    private static final List<TypeProjection> a(v1 v1Var, kd.b bVar) {
        List<ma.o> G0;
        int u7;
        if (v1Var.U0().size() != v1Var.W0().getParameters().size()) {
            return null;
        }
        List<TypeProjection> U0 = v1Var.U0();
        boolean z10 = true;
        if (!(U0 instanceof Collection) || !U0.isEmpty()) {
            Iterator<T> it = U0.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (!(((TypeProjection) it.next()).a() == Variance.INVARIANT)) {
                    z10 = false;
                    break;
                }
            }
        }
        if (z10) {
            return null;
        }
        List<TypeParameterDescriptor> parameters = v1Var.W0().getParameters();
        za.k.d(parameters, "type.constructor.parameters");
        G0 = _Collections.G0(U0, parameters);
        u7 = kotlin.collections.s.u(G0, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (ma.o oVar : G0) {
            TypeProjection typeProjection = (TypeProjection) oVar.a();
            TypeParameterDescriptor typeParameterDescriptor = (TypeParameterDescriptor) oVar.b();
            if (typeProjection.a() != Variance.INVARIANT) {
                v1 Z0 = (typeProjection.b() || typeProjection.a() != Variance.IN_VARIANCE) ? null : typeProjection.getType().Z0();
                za.k.d(typeParameterDescriptor, "parameter");
                typeProjection = ld.a.a(new i(bVar, Z0, typeProjection, typeParameterDescriptor));
            }
            arrayList.add(typeProjection);
        }
        TypeSubstitutor c10 = h1.f11827c.b(v1Var.W0(), arrayList).c();
        int size = U0.size();
        for (int i10 = 0; i10 < size; i10++) {
            TypeProjection typeProjection2 = U0.get(i10);
            TypeProjection typeProjection3 = (TypeProjection) arrayList.get(i10);
            if (typeProjection2.a() != Variance.INVARIANT) {
                List<g0> upperBounds = v1Var.W0().getParameters().get(i10).getUpperBounds();
                za.k.d(upperBounds, "type.constructor.parameters[index].upperBounds");
                ArrayList arrayList2 = new ArrayList();
                Iterator<T> it2 = upperBounds.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(KotlinTypePreparator.a.f12214a.a(c10.n((g0) it2.next(), Variance.INVARIANT).Z0()));
                }
                if (!typeProjection2.b() && typeProjection2.a() == Variance.OUT_VARIANCE) {
                    arrayList2.add(KotlinTypePreparator.a.f12214a.a(typeProjection2.getType().Z0()));
                }
                g0 type = typeProjection3.getType();
                za.k.c(type, "null cannot be cast to non-null type org.jetbrains.kotlin.types.checker.NewCapturedType");
                ((i) type).W0().f(arrayList2);
            }
        }
        return arrayList;
    }

    public static final o0 b(o0 o0Var, kd.b bVar) {
        za.k.e(o0Var, "type");
        za.k.e(bVar, "status");
        List<TypeProjection> a10 = a(o0Var, bVar);
        if (a10 != null) {
            return c(o0Var, a10);
        }
        return null;
    }

    private static final o0 c(v1 v1Var, List<? extends TypeProjection> list) {
        return h0.j(v1Var.V0(), v1Var.W0(), list, v1Var.X0(), null, 16, null);
    }
}
