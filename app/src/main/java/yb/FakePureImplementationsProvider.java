package yb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.m0;
import oc.ClassId;
import oc.FqName;

/* compiled from: FakePureImplementationsProvider.kt */
/* renamed from: yb.m, reason: use source file name */
/* loaded from: classes2.dex */
public final class FakePureImplementationsProvider {

    /* renamed from: a, reason: collision with root package name */
    public static final FakePureImplementationsProvider f20121a;

    /* renamed from: b, reason: collision with root package name */
    private static final Map<ClassId, ClassId> f20122b;

    /* renamed from: c, reason: collision with root package name */
    private static final Map<FqName, FqName> f20123c;

    static {
        Map<FqName, FqName> q10;
        FakePureImplementationsProvider fakePureImplementationsProvider = new FakePureImplementationsProvider();
        f20121a = fakePureImplementationsProvider;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        f20122b = linkedHashMap;
        oc.i iVar = oc.i.f16464a;
        fakePureImplementationsProvider.c(iVar.l(), fakePureImplementationsProvider.a("java.util.ArrayList", "java.util.LinkedList"));
        fakePureImplementationsProvider.c(iVar.n(), fakePureImplementationsProvider.a("java.util.HashSet", "java.util.TreeSet", "java.util.LinkedHashSet"));
        fakePureImplementationsProvider.c(iVar.m(), fakePureImplementationsProvider.a("java.util.HashMap", "java.util.TreeMap", "java.util.LinkedHashMap", "java.util.concurrent.ConcurrentHashMap", "java.util.concurrent.ConcurrentSkipListMap"));
        ClassId m10 = ClassId.m(new FqName("java.util.function.Function"));
        za.k.d(m10, "topLevel(FqName(\"java.util.function.Function\"))");
        fakePureImplementationsProvider.c(m10, fakePureImplementationsProvider.a("java.util.function.UnaryOperator"));
        ClassId m11 = ClassId.m(new FqName("java.util.function.BiFunction"));
        za.k.d(m11, "topLevel(FqName(\"java.util.function.BiFunction\"))");
        fakePureImplementationsProvider.c(m11, fakePureImplementationsProvider.a("java.util.function.BinaryOperator"));
        ArrayList arrayList = new ArrayList(linkedHashMap.size());
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            arrayList.add(ma.u.a(((ClassId) entry.getKey()).b(), ((ClassId) entry.getValue()).b()));
        }
        q10 = m0.q(arrayList);
        f20123c = q10;
    }

    private FakePureImplementationsProvider() {
    }

    private final List<ClassId> a(String... strArr) {
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add(ClassId.m(new FqName(str)));
        }
        return arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void c(ClassId classId, List<ClassId> list) {
        Map<ClassId, ClassId> map = f20122b;
        for (Object obj : list) {
            map.put(obj, classId);
        }
    }

    public final FqName b(FqName fqName) {
        za.k.e(fqName, "classFqName");
        return f20123c.get(fqName);
    }
}
