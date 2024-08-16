package oc;

import fb._Ranges;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import kotlin.collections.MapsJVM;
import kotlin.collections.s;
import ma.o;
import ma.u;

/* compiled from: StandardClassIds.kt */
/* loaded from: classes2.dex */
public final class j {

    /* renamed from: a, reason: collision with root package name */
    private static final FqName f16516a;

    /* renamed from: b, reason: collision with root package name */
    private static final FqName f16517b;

    static {
        FqName fqName = new FqName("java.lang");
        f16516a = fqName;
        FqName c10 = fqName.c(Name.f("annotation"));
        za.k.d(c10, "JAVA_LANG_PACKAGE.child(…identifier(\"annotation\"))");
        f16517b = c10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId k(String str) {
        return new ClassId(i.f16464a.b(), Name.f(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId l(String str) {
        return new ClassId(i.f16464a.f(), Name.f(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId m(String str) {
        return new ClassId(i.f16464a.c(), Name.f(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId n(String str) {
        return new ClassId(i.f16464a.d(), Name.f(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId o(String str) {
        return new ClassId(i.f16464a.e(), Name.f(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V> Map<V, K> p(Map<K, ? extends V> map) {
        int u7;
        int e10;
        int c10;
        Set<Map.Entry<K, ? extends V>> entrySet = map.entrySet();
        u7 = s.u(entrySet, 10);
        e10 = MapsJVM.e(u7);
        c10 = _Ranges.c(e10, 16);
        LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
        Iterator<T> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            o a10 = u.a(entry.getValue(), entry.getKey());
            linkedHashMap.put(a10.c(), a10.d());
        }
        return linkedHashMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId q(Name name) {
        i iVar = i.f16464a;
        return new ClassId(iVar.a().h(), Name.f(name.d() + iVar.a().j().d()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId r(String str) {
        return new ClassId(i.f16464a.g(), Name.f(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId s(String str) {
        return new ClassId(i.f16464a.h(), Name.f(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassId t(ClassId classId) {
        return new ClassId(i.f16464a.f(), Name.f('U' + classId.j().d()));
    }
}
