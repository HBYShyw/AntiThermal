package cc;

import fb._Ranges;
import fc.javaLoading;
import fc.q;
import fc.r;
import fc.w;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.MapsJVM;
import kotlin.collections._Collections;
import kotlin.collections.s;
import oc.Name;
import rd.Sequence;
import rd._Sequences;
import za.Lambda;

/* compiled from: DeclaredMemberIndex.kt */
/* loaded from: classes2.dex */
public class a implements b {

    /* renamed from: a, reason: collision with root package name */
    private final fc.g f5041a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<q, Boolean> f5042b;

    /* renamed from: c, reason: collision with root package name */
    private final ya.l<r, Boolean> f5043c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<Name, List<r>> f5044d;

    /* renamed from: e, reason: collision with root package name */
    private final Map<Name, fc.n> f5045e;

    /* renamed from: f, reason: collision with root package name */
    private final Map<Name, w> f5046f;

    /* compiled from: DeclaredMemberIndex.kt */
    /* renamed from: cc.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    static final class C0014a extends Lambda implements ya.l<r, Boolean> {
        C0014a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(r rVar) {
            za.k.e(rVar, "m");
            return Boolean.valueOf(((Boolean) a.this.f5042b.invoke(rVar)).booleanValue() && !javaLoading.c(rVar));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public a(fc.g gVar, ya.l<? super q, Boolean> lVar) {
        Sequence K;
        Sequence m10;
        Sequence K2;
        Sequence m11;
        int u7;
        int e10;
        int c10;
        za.k.e(gVar, "jClass");
        za.k.e(lVar, "memberFilter");
        this.f5041a = gVar;
        this.f5042b = lVar;
        C0014a c0014a = new C0014a();
        this.f5043c = c0014a;
        K = _Collections.K(gVar.S());
        m10 = _Sequences.m(K, c0014a);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : m10) {
            Name name = ((r) obj).getName();
            Object obj2 = linkedHashMap.get(name);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(name, obj2);
            }
            ((List) obj2).add(obj);
        }
        this.f5044d = linkedHashMap;
        K2 = _Collections.K(this.f5041a.getFields());
        m11 = _Sequences.m(K2, this.f5042b);
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Object obj3 : m11) {
            linkedHashMap2.put(((fc.n) obj3).getName(), obj3);
        }
        this.f5045e = linkedHashMap2;
        Collection<w> v7 = this.f5041a.v();
        ya.l<q, Boolean> lVar2 = this.f5042b;
        ArrayList arrayList = new ArrayList();
        for (Object obj4 : v7) {
            if (((Boolean) lVar2.invoke(obj4)).booleanValue()) {
                arrayList.add(obj4);
            }
        }
        u7 = s.u(arrayList, 10);
        e10 = MapsJVM.e(u7);
        c10 = _Ranges.c(e10, 16);
        LinkedHashMap linkedHashMap3 = new LinkedHashMap(c10);
        for (Object obj5 : arrayList) {
            linkedHashMap3.put(((w) obj5).getName(), obj5);
        }
        this.f5046f = linkedHashMap3;
    }

    @Override // cc.b
    public Set<Name> a() {
        Sequence K;
        Sequence m10;
        K = _Collections.K(this.f5041a.S());
        m10 = _Sequences.m(K, this.f5043c);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator it = m10.iterator();
        while (it.hasNext()) {
            linkedHashSet.add(((r) it.next()).getName());
        }
        return linkedHashSet;
    }

    @Override // cc.b
    public fc.n b(Name name) {
        za.k.e(name, "name");
        return this.f5045e.get(name);
    }

    @Override // cc.b
    public w c(Name name) {
        za.k.e(name, "name");
        return this.f5046f.get(name);
    }

    @Override // cc.b
    public Collection<r> d(Name name) {
        List j10;
        za.k.e(name, "name");
        List<r> list = this.f5044d.get(name);
        if (list != null) {
            return list;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // cc.b
    public Set<Name> e() {
        return this.f5046f.keySet();
    }

    @Override // cc.b
    public Set<Name> f() {
        Sequence K;
        Sequence m10;
        K = _Collections.K(this.f5041a.getFields());
        m10 = _Sequences.m(K, this.f5042b);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator it = m10.iterator();
        while (it.hasNext()) {
            linkedHashSet.add(((fc.n) it.next()).getName());
        }
        return linkedHashSet;
    }
}
