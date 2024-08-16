package zc;

import fd.StorageManager;
import gd.g0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import kotlin.collections.r;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import qd.SmartList;
import qd.collections;
import sc.OverridingUtil;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zc.ResolutionScope;

/* compiled from: GivenFunctionsMemberScope.kt */
/* renamed from: zc.e, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class GivenFunctionsMemberScope extends MemberScopeImpl {

    /* renamed from: d, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f20452d = {Reflection.g(new PropertyReference1Impl(Reflection.b(GivenFunctionsMemberScope.class), "allDescriptors", "getAllDescriptors()Ljava/util/List;"))};

    /* renamed from: b, reason: collision with root package name */
    private final ClassDescriptor f20453b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.i f20454c;

    /* compiled from: GivenFunctionsMemberScope.kt */
    /* renamed from: zc.e$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<List<? extends DeclarationDescriptor>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<DeclarationDescriptor> invoke() {
            List<DeclarationDescriptor> m02;
            List<FunctionDescriptor> i10 = GivenFunctionsMemberScope.this.i();
            m02 = _Collections.m0(i10, GivenFunctionsMemberScope.this.j(i10));
            return m02;
        }
    }

    /* compiled from: GivenFunctionsMemberScope.kt */
    /* renamed from: zc.e$b */
    /* loaded from: classes2.dex */
    public static final class b extends sc.i {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ArrayList<DeclarationDescriptor> f20456a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ GivenFunctionsMemberScope f20457b;

        b(ArrayList<DeclarationDescriptor> arrayList, GivenFunctionsMemberScope givenFunctionsMemberScope) {
            this.f20456a = arrayList;
            this.f20457b = givenFunctionsMemberScope;
        }

        @Override // sc.j
        public void a(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "fakeOverride");
            OverridingUtil.K(callableMemberDescriptor, null);
            this.f20456a.add(callableMemberDescriptor);
        }

        @Override // sc.i
        protected void e(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2) {
            za.k.e(callableMemberDescriptor, "fromSuper");
            za.k.e(callableMemberDescriptor2, "fromCurrent");
            throw new IllegalStateException(("Conflict in scope of " + this.f20457b.l() + ": " + callableMemberDescriptor + " vs " + callableMemberDescriptor2).toString());
        }
    }

    public GivenFunctionsMemberScope(StorageManager storageManager, ClassDescriptor classDescriptor) {
        za.k.e(storageManager, "storageManager");
        za.k.e(classDescriptor, "containingClass");
        this.f20453b = classDescriptor;
        this.f20454c = storageManager.g(new a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public final List<DeclarationDescriptor> j(List<? extends FunctionDescriptor> list) {
        Collection<? extends CallableMemberDescriptor> j10;
        ArrayList arrayList = new ArrayList(3);
        Collection<g0> q10 = this.f20453b.n().q();
        za.k.d(q10, "containingClass.typeConstructor.supertypes");
        ArrayList arrayList2 = new ArrayList();
        Iterator<T> it = q10.iterator();
        while (it.hasNext()) {
            MutableCollections.z(arrayList2, ResolutionScope.a.a(((g0) it.next()).u(), null, null, 3, null));
        }
        ArrayList arrayList3 = new ArrayList();
        for (Object obj : arrayList2) {
            if (obj instanceof CallableMemberDescriptor) {
                arrayList3.add(obj);
            }
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj2 : arrayList3) {
            Name name = ((CallableMemberDescriptor) obj2).getName();
            Object obj3 = linkedHashMap.get(name);
            if (obj3 == null) {
                obj3 = new ArrayList();
                linkedHashMap.put(name, obj3);
            }
            ((List) obj3).add(obj2);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            Name name2 = (Name) entry.getKey();
            List list2 = (List) entry.getValue();
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            for (Object obj4 : list2) {
                Boolean valueOf = Boolean.valueOf(((CallableMemberDescriptor) obj4) instanceof FunctionDescriptor);
                Object obj5 = linkedHashMap2.get(valueOf);
                if (obj5 == null) {
                    obj5 = new ArrayList();
                    linkedHashMap2.put(valueOf, obj5);
                }
                ((List) obj5).add(obj4);
            }
            for (Map.Entry entry2 : linkedHashMap2.entrySet()) {
                boolean booleanValue = ((Boolean) entry2.getKey()).booleanValue();
                List list3 = (List) entry2.getValue();
                OverridingUtil overridingUtil = OverridingUtil.f18440f;
                if (booleanValue) {
                    j10 = new ArrayList<>();
                    for (Object obj6 : list) {
                        if (za.k.a(((FunctionDescriptor) obj6).getName(), name2)) {
                            j10.add(obj6);
                        }
                    }
                } else {
                    j10 = r.j();
                }
                overridingUtil.v(name2, list3, j10, this.f20453b, new b(arrayList, this));
            }
        }
        return collections.c(arrayList);
    }

    private final List<DeclarationDescriptor> k() {
        return (List) fd.m.a(this.f20454c, this, f20452d[0]);
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        List<DeclarationDescriptor> k10 = k();
        SmartList smartList = new SmartList();
        for (Object obj : k10) {
            if ((obj instanceof PropertyDescriptor) && za.k.a(((PropertyDescriptor) obj).getName(), name)) {
                smartList.add(obj);
            }
        }
        return smartList;
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        List<DeclarationDescriptor> k10 = k();
        SmartList smartList = new SmartList();
        for (Object obj : k10) {
            if ((obj instanceof SimpleFunctionDescriptor) && za.k.a(((SimpleFunctionDescriptor) obj).getName(), name)) {
                smartList.add(obj);
            }
        }
        return smartList;
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar) {
        List j10;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        if (dVar.a(d.f20437p.m())) {
            return k();
        }
        j10 = r.j();
        return j10;
    }

    protected abstract List<FunctionDescriptor> i();

    /* JADX INFO: Access modifiers changed from: protected */
    public final ClassDescriptor l() {
        return this.f20453b;
    }
}
