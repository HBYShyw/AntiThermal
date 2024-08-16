package mb;

import gd.TypeProjection;
import gd.d1;
import gd.g0;
import gd.h0;
import gd.o0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections.MapsJVM;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import kotlin.collections.r;
import kotlin.collections.s;
import ma.u;
import mb.StandardNames;
import nb.FunctionClassKind;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import qb.AnnotationDescriptor;
import qb.BuiltInAnnotationDescriptor;
import qb.g;
import qd.collections;
import uc.v;

/* compiled from: functionTypes.kt */
/* renamed from: mb.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class functionTypes {
    public static final int a(g0 g0Var) {
        Object j10;
        za.k.e(g0Var, "<this>");
        AnnotationDescriptor j11 = g0Var.i().j(StandardNames.a.D);
        if (j11 == null) {
            return 0;
        }
        j10 = m0.j(j11.a(), StandardNames.f15273k);
        uc.g gVar = (uc.g) j10;
        za.k.c(gVar, "null cannot be cast to non-null type org.jetbrains.kotlin.resolve.constants.IntValue");
        return ((uc.m) gVar).b().intValue();
    }

    public static final o0 b(KotlinBuiltIns kotlinBuiltIns, qb.g gVar, g0 g0Var, List<? extends g0> list, List<? extends g0> list2, List<Name> list3, g0 g0Var2, boolean z10) {
        za.k.e(kotlinBuiltIns, "builtIns");
        za.k.e(gVar, "annotations");
        za.k.e(list, "contextReceiverTypes");
        za.k.e(list2, "parameterTypes");
        za.k.e(g0Var2, "returnType");
        List<TypeProjection> g6 = g(g0Var, list, list2, list3, g0Var2, kotlinBuiltIns);
        ClassDescriptor f10 = f(kotlinBuiltIns, list2.size() + list.size() + (g0Var == null ? 0 : 1), z10);
        if (g0Var != null) {
            gVar = t(gVar, kotlinBuiltIns);
        }
        if (!list.isEmpty()) {
            gVar = s(gVar, kotlinBuiltIns, list.size());
        }
        return h0.g(d1.b(gVar), f10, g6);
    }

    public static final Name d(g0 g0Var) {
        Object r02;
        String b10;
        za.k.e(g0Var, "<this>");
        AnnotationDescriptor j10 = g0Var.i().j(StandardNames.a.E);
        if (j10 == null) {
            return null;
        }
        r02 = _Collections.r0(j10.a().values());
        v vVar = r02 instanceof v ? (v) r02 : null;
        if (vVar != null && (b10 = vVar.b()) != null) {
            if (!Name.h(b10)) {
                b10 = null;
            }
            if (b10 != null) {
                return Name.f(b10);
            }
        }
        return null;
    }

    public static final List<g0> e(g0 g0Var) {
        int u7;
        List<g0> j10;
        za.k.e(g0Var, "<this>");
        o(g0Var);
        int a10 = a(g0Var);
        if (a10 == 0) {
            j10 = r.j();
            return j10;
        }
        List<TypeProjection> subList = g0Var.U0().subList(0, a10);
        u7 = s.u(subList, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = subList.iterator();
        while (it.hasNext()) {
            g0 type = ((TypeProjection) it.next()).getType();
            za.k.d(type, "it.type");
            arrayList.add(type);
        }
        return arrayList;
    }

    public static final ClassDescriptor f(KotlinBuiltIns kotlinBuiltIns, int i10, boolean z10) {
        za.k.e(kotlinBuiltIns, "builtIns");
        ClassDescriptor X = z10 ? kotlinBuiltIns.X(i10) : kotlinBuiltIns.C(i10);
        za.k.d(X, "if (isSuspendFunction) b…tFunction(parameterCount)");
        return X;
    }

    public static final List<TypeProjection> g(g0 g0Var, List<? extends g0> list, List<? extends g0> list2, List<Name> list3, g0 g0Var2, KotlinBuiltIns kotlinBuiltIns) {
        int u7;
        Name name;
        Map f10;
        List<? extends AnnotationDescriptor> l02;
        za.k.e(list, "contextReceiverTypes");
        za.k.e(list2, "parameterTypes");
        za.k.e(g0Var2, "returnType");
        za.k.e(kotlinBuiltIns, "builtIns");
        int i10 = 0;
        ArrayList arrayList = new ArrayList(list2.size() + list.size() + (g0Var != null ? 1 : 0) + 1);
        u7 = s.u(list, 10);
        ArrayList arrayList2 = new ArrayList(u7);
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            arrayList2.add(ld.a.a((g0) it.next()));
        }
        arrayList.addAll(arrayList2);
        collections.a(arrayList, g0Var != null ? ld.a.a(g0Var) : null);
        for (Object obj : list2) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                r.t();
            }
            g0 g0Var3 = (g0) obj;
            if (list3 == null || (name = list3.get(i10)) == null || name.g()) {
                name = null;
            }
            if (name != null) {
                FqName fqName = StandardNames.a.E;
                Name f11 = Name.f("name");
                String b10 = name.b();
                za.k.d(b10, "name.asString()");
                f10 = MapsJVM.f(u.a(f11, new v(b10)));
                BuiltInAnnotationDescriptor builtInAnnotationDescriptor = new BuiltInAnnotationDescriptor(kotlinBuiltIns, fqName, f10);
                g.a aVar = qb.g.f17195b;
                l02 = _Collections.l0(g0Var3.i(), builtInAnnotationDescriptor);
                g0Var3 = ld.a.v(g0Var3, aVar.a(l02));
            }
            arrayList.add(ld.a.a(g0Var3));
            i10 = i11;
        }
        arrayList.add(ld.a.a(g0Var2));
        return arrayList;
    }

    private static final FunctionClassKind h(FqNameUnsafe fqNameUnsafe) {
        if (!fqNameUnsafe.f() || fqNameUnsafe.e()) {
            return null;
        }
        FunctionClassKind.a aVar = FunctionClassKind.f15968i;
        String b10 = fqNameUnsafe.i().b();
        za.k.d(b10, "shortName().asString()");
        FqName e10 = fqNameUnsafe.l().e();
        za.k.d(e10, "toSafe().parent()");
        return aVar.b(b10, e10);
    }

    public static final FunctionClassKind i(DeclarationDescriptor declarationDescriptor) {
        za.k.e(declarationDescriptor, "<this>");
        if ((declarationDescriptor instanceof ClassDescriptor) && KotlinBuiltIns.A0(declarationDescriptor)) {
            return h(wc.c.m(declarationDescriptor));
        }
        return null;
    }

    public static final g0 j(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        o(g0Var);
        if (!r(g0Var)) {
            return null;
        }
        return g0Var.U0().get(a(g0Var)).getType();
    }

    public static final g0 k(g0 g0Var) {
        Object e02;
        za.k.e(g0Var, "<this>");
        o(g0Var);
        e02 = _Collections.e0(g0Var.U0());
        g0 type = ((TypeProjection) e02).getType();
        za.k.d(type, "arguments.last().type");
        return type;
    }

    public static final List<TypeProjection> l(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        o(g0Var);
        return g0Var.U0().subList(a(g0Var) + (m(g0Var) ? 1 : 0), r0.size() - 1);
    }

    public static final boolean m(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        return o(g0Var) && r(g0Var);
    }

    public static final boolean n(DeclarationDescriptor declarationDescriptor) {
        za.k.e(declarationDescriptor, "<this>");
        FunctionClassKind i10 = i(declarationDescriptor);
        return i10 == FunctionClassKind.f15969j || i10 == FunctionClassKind.f15970k;
    }

    public static final boolean o(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        ClassifierDescriptor v7 = g0Var.W0().v();
        return v7 != null && n(v7);
    }

    public static final boolean p(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        ClassifierDescriptor v7 = g0Var.W0().v();
        return (v7 != null ? i(v7) : null) == FunctionClassKind.f15969j;
    }

    public static final boolean q(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        ClassifierDescriptor v7 = g0Var.W0().v();
        return (v7 != null ? i(v7) : null) == FunctionClassKind.f15970k;
    }

    private static final boolean r(g0 g0Var) {
        return g0Var.i().j(StandardNames.a.C) != null;
    }

    public static final qb.g s(qb.g gVar, KotlinBuiltIns kotlinBuiltIns, int i10) {
        Map f10;
        List<? extends AnnotationDescriptor> l02;
        za.k.e(gVar, "<this>");
        za.k.e(kotlinBuiltIns, "builtIns");
        FqName fqName = StandardNames.a.D;
        if (gVar.a(fqName)) {
            return gVar;
        }
        g.a aVar = qb.g.f17195b;
        f10 = MapsJVM.f(u.a(StandardNames.f15273k, new uc.m(i10)));
        l02 = _Collections.l0(gVar, new BuiltInAnnotationDescriptor(kotlinBuiltIns, fqName, f10));
        return aVar.a(l02);
    }

    public static final qb.g t(qb.g gVar, KotlinBuiltIns kotlinBuiltIns) {
        Map i10;
        List<? extends AnnotationDescriptor> l02;
        za.k.e(gVar, "<this>");
        za.k.e(kotlinBuiltIns, "builtIns");
        FqName fqName = StandardNames.a.C;
        if (gVar.a(fqName)) {
            return gVar;
        }
        g.a aVar = qb.g.f17195b;
        i10 = m0.i();
        l02 = _Collections.l0(gVar, new BuiltInAnnotationDescriptor(kotlinBuiltIns, fqName, i10));
        return aVar.a(l02);
    }
}
