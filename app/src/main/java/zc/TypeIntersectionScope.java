package zc;

import gd.g0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import kotlin.collections.s;
import ma.o;
import oc.Name;
import pb.CallableDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import pd.scopeUtils;
import qd.SmartList;
import sc.overridingUtils;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: TypeIntersectionScope.kt */
/* renamed from: zc.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class TypeIntersectionScope extends AbstractScopeAdapter {

    /* renamed from: d, reason: collision with root package name */
    public static final a f20479d = new a(null);

    /* renamed from: b, reason: collision with root package name */
    private final String f20480b;

    /* renamed from: c, reason: collision with root package name */
    private final h f20481c;

    /* compiled from: TypeIntersectionScope.kt */
    /* renamed from: zc.n$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final h a(String str, Collection<? extends g0> collection) {
            int u7;
            za.k.e(str, "message");
            za.k.e(collection, "types");
            u7 = s.u(collection, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = collection.iterator();
            while (it.hasNext()) {
                arrayList.add(((g0) it.next()).u());
            }
            SmartList<h> b10 = scopeUtils.b(arrayList);
            h b11 = ChainedMemberScope.f20418d.b(str, b10);
            return b10.size() <= 1 ? b11 : new TypeIntersectionScope(str, b11, null);
        }
    }

    /* compiled from: TypeIntersectionScope.kt */
    /* renamed from: zc.n$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<CallableDescriptor, CallableDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f20482e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CallableDescriptor invoke(CallableDescriptor callableDescriptor) {
            za.k.e(callableDescriptor, "$this$selectMostSpecificInEachOverridableGroup");
            return callableDescriptor;
        }
    }

    /* compiled from: TypeIntersectionScope.kt */
    /* renamed from: zc.n$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<SimpleFunctionDescriptor, CallableDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f20483e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CallableDescriptor invoke(SimpleFunctionDescriptor simpleFunctionDescriptor) {
            za.k.e(simpleFunctionDescriptor, "$this$selectMostSpecificInEachOverridableGroup");
            return simpleFunctionDescriptor;
        }
    }

    /* compiled from: TypeIntersectionScope.kt */
    /* renamed from: zc.n$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<PropertyDescriptor, CallableDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f20484e = new d();

        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CallableDescriptor invoke(PropertyDescriptor propertyDescriptor) {
            za.k.e(propertyDescriptor, "$this$selectMostSpecificInEachOverridableGroup");
            return propertyDescriptor;
        }
    }

    private TypeIntersectionScope(String str, h hVar) {
        this.f20480b = str;
        this.f20481c = hVar;
    }

    public /* synthetic */ TypeIntersectionScope(String str, h hVar, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, hVar);
    }

    public static final h j(String str, Collection<? extends g0> collection) {
        return f20479d.a(str, collection);
    }

    @Override // zc.AbstractScopeAdapter, zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return overridingUtils.a(super.a(name, bVar), d.f20484e);
    }

    @Override // zc.AbstractScopeAdapter, zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return overridingUtils.a(super.c(name, bVar), c.f20483e);
    }

    @Override // zc.AbstractScopeAdapter, zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        List m02;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        Collection<DeclarationDescriptor> g6 = super.g(dVar, lVar);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : g6) {
            if (((DeclarationDescriptor) obj) instanceof CallableDescriptor) {
                arrayList.add(obj);
            } else {
                arrayList2.add(obj);
            }
        }
        o oVar = new o(arrayList, arrayList2);
        List list = (List) oVar.a();
        List list2 = (List) oVar.b();
        za.k.c(list, "null cannot be cast to non-null type kotlin.collections.Collection<org.jetbrains.kotlin.descriptors.CallableDescriptor>");
        m02 = _Collections.m0(overridingUtils.a(list, b.f20482e), list2);
        return m02;
    }

    @Override // zc.AbstractScopeAdapter
    protected h i() {
        return this.f20481c;
    }
}
