package mb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections._Collections;
import kotlin.collections.s;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;

/* compiled from: CompanionObjectMapping.kt */
/* renamed from: mb.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class CompanionObjectMapping {

    /* renamed from: a, reason: collision with root package name */
    public static final CompanionObjectMapping f15213a = new CompanionObjectMapping();

    /* renamed from: b, reason: collision with root package name */
    private static final Set<ClassId> f15214b;

    static {
        int u7;
        List n02;
        List n03;
        List n04;
        Set<PrimitiveType> set = PrimitiveType.f15233j;
        u7 = s.u(set, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = set.iterator();
        while (it.hasNext()) {
            arrayList.add(StandardNames.c((PrimitiveType) it.next()));
        }
        FqName l10 = StandardNames.a.f15303h.l();
        za.k.d(l10, "string.toSafe()");
        n02 = _Collections.n0(arrayList, l10);
        FqName l11 = StandardNames.a.f15307j.l();
        za.k.d(l11, "_boolean.toSafe()");
        n03 = _Collections.n0(n02, l11);
        FqName l12 = StandardNames.a.f15325s.l();
        za.k.d(l12, "_enum.toSafe()");
        n04 = _Collections.n0(n03, l12);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator it2 = n04.iterator();
        while (it2.hasNext()) {
            linkedHashSet.add(ClassId.m((FqName) it2.next()));
        }
        f15214b = linkedHashSet;
    }

    private CompanionObjectMapping() {
    }

    public final Set<ClassId> a() {
        return f15214b;
    }

    public final Set<ClassId> b() {
        return f15214b;
    }
}
