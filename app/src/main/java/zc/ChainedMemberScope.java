package zc;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.MutableCollections;
import kotlin.collections._Arrays;
import kotlin.collections.r;
import kotlin.collections.s0;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import pd.scopeUtils;
import qd.SmartList;
import za.DefaultConstructorMarker;
import zc.h;

/* compiled from: ChainedMemberScope.kt */
/* renamed from: zc.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ChainedMemberScope implements h {

    /* renamed from: d, reason: collision with root package name */
    public static final a f20418d = new a(null);

    /* renamed from: b, reason: collision with root package name */
    private final String f20419b;

    /* renamed from: c, reason: collision with root package name */
    private final h[] f20420c;

    /* compiled from: ChainedMemberScope.kt */
    /* renamed from: zc.b$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final h a(String str, Iterable<? extends h> iterable) {
            za.k.e(str, "debugName");
            za.k.e(iterable, "scopes");
            SmartList smartList = new SmartList();
            for (h hVar : iterable) {
                if (hVar != h.b.f20465b) {
                    if (hVar instanceof ChainedMemberScope) {
                        MutableCollections.A(smartList, ((ChainedMemberScope) hVar).f20420c);
                    } else {
                        smartList.add(hVar);
                    }
                }
            }
            return b(str, smartList);
        }

        public final h b(String str, List<? extends h> list) {
            za.k.e(str, "debugName");
            za.k.e(list, "scopes");
            int size = list.size();
            if (size == 0) {
                return h.b.f20465b;
            }
            if (size != 1) {
                return new ChainedMemberScope(str, (h[]) list.toArray(new h[0]), null);
            }
            return list.get(0);
        }
    }

    private ChainedMemberScope(String str, h[] hVarArr) {
        this.f20419b = str;
        this.f20420c = hVarArr;
    }

    public /* synthetic */ ChainedMemberScope(String str, h[] hVarArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, hVarArr);
    }

    @Override // zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        List j10;
        Set e10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        h[] hVarArr = this.f20420c;
        int length = hVarArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            Collection<PropertyDescriptor> collection = null;
            for (h hVar : hVarArr) {
                collection = scopeUtils.a(collection, hVar.a(name, bVar));
            }
            if (collection != null) {
                return collection;
            }
            e10 = s0.e();
            return e10;
        }
        return hVarArr[0].a(name, bVar);
    }

    @Override // zc.h
    public Set<Name> b() {
        h[] hVarArr = this.f20420c;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (h hVar : hVarArr) {
            MutableCollections.z(linkedHashSet, hVar.b());
        }
        return linkedHashSet;
    }

    @Override // zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        List j10;
        Set e10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        h[] hVarArr = this.f20420c;
        int length = hVarArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            Collection<SimpleFunctionDescriptor> collection = null;
            for (h hVar : hVarArr) {
                collection = scopeUtils.a(collection, hVar.c(name, bVar));
            }
            if (collection != null) {
                return collection;
            }
            e10 = s0.e();
            return e10;
        }
        return hVarArr[0].c(name, bVar);
    }

    @Override // zc.h
    public Set<Name> d() {
        h[] hVarArr = this.f20420c;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (h hVar : hVarArr) {
            MutableCollections.z(linkedHashSet, hVar.d());
        }
        return linkedHashSet;
    }

    @Override // zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        ClassifierDescriptor classifierDescriptor = null;
        for (h hVar : this.f20420c) {
            ClassifierDescriptor e10 = hVar.e(name, bVar);
            if (e10 != null) {
                if (!(e10 instanceof ClassifierDescriptorWithTypeParameters) || !((ClassifierDescriptorWithTypeParameters) e10).U()) {
                    return e10;
                }
                if (classifierDescriptor == null) {
                    classifierDescriptor = e10;
                }
            }
        }
        return classifierDescriptor;
    }

    @Override // zc.h
    public Set<Name> f() {
        Iterable q10;
        q10 = _Arrays.q(this.f20420c);
        return j.a(q10);
    }

    @Override // zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar) {
        List j10;
        Set e10;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        h[] hVarArr = this.f20420c;
        int length = hVarArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            Collection<DeclarationDescriptor> collection = null;
            for (h hVar : hVarArr) {
                collection = scopeUtils.a(collection, hVar.g(dVar, lVar));
            }
            if (collection != null) {
                return collection;
            }
            e10 = s0.e();
            return e10;
        }
        return hVarArr[0].g(dVar, lVar);
    }

    public String toString() {
        return this.f20419b;
    }
}
