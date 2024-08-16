package mb;

import gd.g0;
import gd.s1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import ma.u;
import oc.ClassId;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PackageFragmentDescriptor;

/* compiled from: UnsignedType.kt */
/* loaded from: classes2.dex */
public final class o {

    /* renamed from: a, reason: collision with root package name */
    public static final o f15357a = new o();

    /* renamed from: b, reason: collision with root package name */
    private static final Set<Name> f15358b;

    /* renamed from: c, reason: collision with root package name */
    private static final Set<Name> f15359c;

    /* renamed from: d, reason: collision with root package name */
    private static final HashMap<ClassId, ClassId> f15360d;

    /* renamed from: e, reason: collision with root package name */
    private static final HashMap<ClassId, ClassId> f15361e;

    /* renamed from: f, reason: collision with root package name */
    private static final HashMap<m, Name> f15362f;

    /* renamed from: g, reason: collision with root package name */
    private static final Set<Name> f15363g;

    static {
        Set<Name> D0;
        Set<Name> D02;
        HashMap<m, Name> k10;
        n[] values = n.values();
        ArrayList arrayList = new ArrayList(values.length);
        for (n nVar : values) {
            arrayList.add(nVar.d());
        }
        D0 = _Collections.D0(arrayList);
        f15358b = D0;
        m[] values2 = m.values();
        ArrayList arrayList2 = new ArrayList(values2.length);
        for (m mVar : values2) {
            arrayList2.add(mVar.b());
        }
        D02 = _Collections.D0(arrayList2);
        f15359c = D02;
        f15360d = new HashMap<>();
        f15361e = new HashMap<>();
        k10 = m0.k(u.a(m.f15342g, Name.f("ubyteArrayOf")), u.a(m.f15343h, Name.f("ushortArrayOf")), u.a(m.f15344i, Name.f("uintArrayOf")), u.a(m.f15345j, Name.f("ulongArrayOf")));
        f15362f = k10;
        n[] values3 = n.values();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (n nVar2 : values3) {
            linkedHashSet.add(nVar2.b().j());
        }
        f15363g = linkedHashSet;
        for (n nVar3 : n.values()) {
            f15360d.put(nVar3.b(), nVar3.c());
            f15361e.put(nVar3.c(), nVar3.b());
        }
    }

    private o() {
    }

    public static final boolean d(g0 g0Var) {
        ClassifierDescriptor v7;
        za.k.e(g0Var, "type");
        if (s1.w(g0Var) || (v7 = g0Var.W0().v()) == null) {
            return false;
        }
        return f15357a.c(v7);
    }

    public final ClassId a(ClassId classId) {
        za.k.e(classId, "arrayClassId");
        return f15360d.get(classId);
    }

    public final boolean b(Name name) {
        za.k.e(name, "name");
        return f15363g.contains(name);
    }

    public final boolean c(DeclarationDescriptor declarationDescriptor) {
        za.k.e(declarationDescriptor, "descriptor");
        DeclarationDescriptor b10 = declarationDescriptor.b();
        return (b10 instanceof PackageFragmentDescriptor) && za.k.a(((PackageFragmentDescriptor) b10).d(), StandardNames.f15283u) && f15358b.contains(declarationDescriptor.getName());
    }
}
