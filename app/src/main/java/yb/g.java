package yb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.MapsJVM;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import mb.StandardNames;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;

/* compiled from: BuiltinSpecialProperties.kt */
/* loaded from: classes2.dex */
public final class g {

    /* renamed from: a, reason: collision with root package name */
    public static final g f20075a = new g();

    /* renamed from: b, reason: collision with root package name */
    private static final Map<FqName, Name> f20076b;

    /* renamed from: c, reason: collision with root package name */
    private static final Map<Name, List<Name>> f20077c;

    /* renamed from: d, reason: collision with root package name */
    private static final Set<FqName> f20078d;

    /* renamed from: e, reason: collision with root package name */
    private static final Set<Name> f20079e;

    static {
        FqName d10;
        FqName d11;
        FqName c10;
        FqName c11;
        FqName d12;
        FqName c12;
        FqName c13;
        FqName c14;
        Map<FqName, Name> l10;
        int u7;
        int e10;
        int u10;
        Set<Name> D0;
        List M;
        FqNameUnsafe fqNameUnsafe = StandardNames.a.f15325s;
        d10 = h.d(fqNameUnsafe, "name");
        d11 = h.d(fqNameUnsafe, "ordinal");
        c10 = h.c(StandardNames.a.U, "size");
        FqName fqName = StandardNames.a.Y;
        c11 = h.c(fqName, "size");
        d12 = h.d(StandardNames.a.f15301g, "length");
        c12 = h.c(fqName, "keys");
        c13 = h.c(fqName, "values");
        c14 = h.c(fqName, "entries");
        l10 = m0.l(ma.u.a(d10, Name.f("name")), ma.u.a(d11, Name.f("ordinal")), ma.u.a(c10, Name.f("size")), ma.u.a(c11, Name.f("size")), ma.u.a(d12, Name.f("length")), ma.u.a(c12, Name.f("keySet")), ma.u.a(c13, Name.f("values")), ma.u.a(c14, Name.f("entrySet")));
        f20076b = l10;
        Set<Map.Entry<FqName, Name>> entrySet = l10.entrySet();
        u7 = kotlin.collections.s.u(entrySet, 10);
        ArrayList<ma.o> arrayList = new ArrayList(u7);
        Iterator<T> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            arrayList.add(new ma.o(((FqName) entry.getKey()).g(), entry.getValue()));
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (ma.o oVar : arrayList) {
            Name name = (Name) oVar.d();
            Object obj = linkedHashMap.get(name);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(name, obj);
            }
            ((List) obj).add((Name) oVar.c());
        }
        e10 = MapsJVM.e(linkedHashMap.size());
        LinkedHashMap linkedHashMap2 = new LinkedHashMap(e10);
        for (Map.Entry entry2 : linkedHashMap.entrySet()) {
            Object key = entry2.getKey();
            M = _Collections.M((Iterable) entry2.getValue());
            linkedHashMap2.put(key, M);
        }
        f20077c = linkedHashMap2;
        Set<FqName> keySet = f20076b.keySet();
        f20078d = keySet;
        u10 = kotlin.collections.s.u(keySet, 10);
        ArrayList arrayList2 = new ArrayList(u10);
        Iterator<T> it2 = keySet.iterator();
        while (it2.hasNext()) {
            arrayList2.add(((FqName) it2.next()).g());
        }
        D0 = _Collections.D0(arrayList2);
        f20079e = D0;
    }

    private g() {
    }

    public final Map<FqName, Name> a() {
        return f20076b;
    }

    public final List<Name> b(Name name) {
        List<Name> j10;
        za.k.e(name, "name1");
        List<Name> list = f20077c.get(name);
        if (list != null) {
            return list;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    public final Set<FqName> c() {
        return f20078d;
    }

    public final Set<Name> d() {
        return f20079e;
    }
}
