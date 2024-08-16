package sb;

import fd.StorageManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import oc.FqName;
import pb.DeclarationDescriptorVisitor;
import pb.PackageFragmentDescriptor;
import pb.PackageViewDescriptor;
import pb.o0;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zc.ChainedMemberScope;
import zc.LazyScopeAdapter;
import zc.h;

/* compiled from: LazyPackageViewDescriptorImpl.kt */
/* renamed from: sb.r, reason: use source file name */
/* loaded from: classes2.dex */
public class LazyPackageViewDescriptorImpl extends DeclarationDescriptorImpl implements PackageViewDescriptor {

    /* renamed from: l, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f18373l = {Reflection.g(new PropertyReference1Impl(Reflection.b(LazyPackageViewDescriptorImpl.class), "fragments", "getFragments()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(LazyPackageViewDescriptorImpl.class), "empty", "getEmpty()Z"))};

    /* renamed from: g, reason: collision with root package name */
    private final x f18374g;

    /* renamed from: h, reason: collision with root package name */
    private final FqName f18375h;

    /* renamed from: i, reason: collision with root package name */
    private final fd.i f18376i;

    /* renamed from: j, reason: collision with root package name */
    private final fd.i f18377j;

    /* renamed from: k, reason: collision with root package name */
    private final zc.h f18378k;

    /* compiled from: LazyPackageViewDescriptorImpl.kt */
    /* renamed from: sb.r$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<Boolean> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke() {
            return Boolean.valueOf(o0.b(LazyPackageViewDescriptorImpl.this.A0().Z0(), LazyPackageViewDescriptorImpl.this.d()));
        }
    }

    /* compiled from: LazyPackageViewDescriptorImpl.kt */
    /* renamed from: sb.r$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<List<? extends PackageFragmentDescriptor>> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<PackageFragmentDescriptor> invoke() {
            return o0.c(LazyPackageViewDescriptorImpl.this.A0().Z0(), LazyPackageViewDescriptorImpl.this.d());
        }
    }

    /* compiled from: LazyPackageViewDescriptorImpl.kt */
    /* renamed from: sb.r$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<zc.h> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final zc.h invoke() {
            int u7;
            List n02;
            if (LazyPackageViewDescriptorImpl.this.isEmpty()) {
                return h.b.f20465b;
            }
            List<PackageFragmentDescriptor> R = LazyPackageViewDescriptorImpl.this.R();
            u7 = kotlin.collections.s.u(R, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = R.iterator();
            while (it.hasNext()) {
                arrayList.add(((PackageFragmentDescriptor) it.next()).u());
            }
            n02 = _Collections.n0(arrayList, new SubpackagesScope(LazyPackageViewDescriptorImpl.this.A0(), LazyPackageViewDescriptorImpl.this.d()));
            return ChainedMemberScope.f20418d.a("package view scope for " + LazyPackageViewDescriptorImpl.this.d() + " in " + LazyPackageViewDescriptorImpl.this.A0().getName(), n02);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyPackageViewDescriptorImpl(x xVar, FqName fqName, StorageManager storageManager) {
        super(qb.g.f17195b.b(), fqName.h());
        za.k.e(xVar, "module");
        za.k.e(fqName, "fqName");
        za.k.e(storageManager, "storageManager");
        this.f18374g = xVar;
        this.f18375h = fqName;
        this.f18376i = storageManager.g(new b());
        this.f18377j = storageManager.g(new a());
        this.f18378k = new LazyScopeAdapter(storageManager, new c());
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        za.k.e(declarationDescriptorVisitor, "visitor");
        return declarationDescriptorVisitor.g(this, d10);
    }

    @Override // pb.DeclarationDescriptor
    /* renamed from: J0, reason: merged with bridge method [inline-methods] */
    public PackageViewDescriptor b() {
        if (d().d()) {
            return null;
        }
        x A0 = A0();
        FqName e10 = d().e();
        za.k.d(e10, "fqName.parent()");
        return A0.H(e10);
    }

    protected final boolean O0() {
        return ((Boolean) fd.m.a(this.f18377j, this, f18373l[1])).booleanValue();
    }

    @Override // pb.PackageViewDescriptor
    public List<PackageFragmentDescriptor> R() {
        return (List) fd.m.a(this.f18376i, this, f18373l[0]);
    }

    @Override // pb.PackageViewDescriptor
    /* renamed from: T0, reason: merged with bridge method [inline-methods] */
    public x A0() {
        return this.f18374g;
    }

    @Override // pb.PackageViewDescriptor
    public FqName d() {
        return this.f18375h;
    }

    public boolean equals(Object obj) {
        PackageViewDescriptor packageViewDescriptor = obj instanceof PackageViewDescriptor ? (PackageViewDescriptor) obj : null;
        return packageViewDescriptor != null && za.k.a(d(), packageViewDescriptor.d()) && za.k.a(A0(), packageViewDescriptor.A0());
    }

    public int hashCode() {
        return (A0().hashCode() * 31) + d().hashCode();
    }

    @Override // pb.PackageViewDescriptor
    public boolean isEmpty() {
        return O0();
    }

    @Override // pb.PackageViewDescriptor
    public zc.h u() {
        return this.f18378k;
    }
}
