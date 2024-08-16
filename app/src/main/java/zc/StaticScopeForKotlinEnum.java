package zc;

import fd.StorageManager;
import java.util.Collection;
import java.util.List;
import kotlin.collections._Collections;
import kotlin.collections.r;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import qd.SmartList;
import sc.DescriptorFactory;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: StaticScopeForKotlinEnum.kt */
/* renamed from: zc.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class StaticScopeForKotlinEnum extends MemberScopeImpl {

    /* renamed from: e, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f20466e = {Reflection.g(new PropertyReference1Impl(Reflection.b(StaticScopeForKotlinEnum.class), "functions", "getFunctions()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(StaticScopeForKotlinEnum.class), "properties", "getProperties()Ljava/util/List;"))};

    /* renamed from: b, reason: collision with root package name */
    private final ClassDescriptor f20467b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.i f20468c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.i f20469d;

    /* compiled from: StaticScopeForKotlinEnum.kt */
    /* renamed from: zc.l$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<List<? extends SimpleFunctionDescriptor>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<SimpleFunctionDescriptor> invoke() {
            List<SimpleFunctionDescriptor> m10;
            m10 = r.m(DescriptorFactory.g(StaticScopeForKotlinEnum.this.f20467b), DescriptorFactory.h(StaticScopeForKotlinEnum.this.f20467b));
            return m10;
        }
    }

    /* compiled from: StaticScopeForKotlinEnum.kt */
    /* renamed from: zc.l$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<List<? extends PropertyDescriptor>> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<PropertyDescriptor> invoke() {
            List<PropertyDescriptor> n10;
            n10 = r.n(DescriptorFactory.f(StaticScopeForKotlinEnum.this.f20467b));
            return n10;
        }
    }

    public StaticScopeForKotlinEnum(StorageManager storageManager, ClassDescriptor classDescriptor) {
        za.k.e(storageManager, "storageManager");
        za.k.e(classDescriptor, "containingClass");
        this.f20467b = classDescriptor;
        classDescriptor.getKind();
        ClassKind classKind = ClassKind.ENUM_CLASS;
        this.f20468c = storageManager.g(new a());
        this.f20469d = storageManager.g(new b());
    }

    private final List<SimpleFunctionDescriptor> l() {
        return (List) fd.m.a(this.f20468c, this, f20466e[0]);
    }

    private final List<PropertyDescriptor> m() {
        return (List) fd.m.a(this.f20469d, this, f20466e[1]);
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        List<PropertyDescriptor> m10 = m();
        SmartList smartList = new SmartList();
        for (Object obj : m10) {
            if (za.k.a(((PropertyDescriptor) obj).getName(), name)) {
                smartList.add(obj);
            }
        }
        return smartList;
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public /* bridge */ /* synthetic */ ClassifierDescriptor e(Name name, xb.b bVar) {
        return (ClassifierDescriptor) i(name, bVar);
    }

    public Void i(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return null;
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public List<CallableMemberDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar) {
        List<CallableMemberDescriptor> m02;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        m02 = _Collections.m0(l(), m());
        return m02;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // zc.MemberScopeImpl, zc.h
    /* renamed from: k, reason: merged with bridge method [inline-methods] */
    public SmartList<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        List<SimpleFunctionDescriptor> l10 = l();
        SmartList<SimpleFunctionDescriptor> smartList = new SmartList<>();
        for (Object obj : l10) {
            if (za.k.a(((SimpleFunctionDescriptor) obj).getName(), name)) {
                smartList.add(obj);
            }
        }
        return smartList;
    }
}
