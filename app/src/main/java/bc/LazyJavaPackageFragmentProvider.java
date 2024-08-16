package bc;

import bc.k;
import cc.LazyJavaPackageFragment;
import fc.u;
import java.util.Collection;
import java.util.List;
import kotlin.collections.r;
import oc.FqName;
import oc.Name;
import pb.PackageFragmentDescriptor;
import pb.p0;
import qd.collections;
import ya.l;
import yb.o;
import za.Lambda;

/* compiled from: LazyJavaPackageFragmentProvider.kt */
/* renamed from: bc.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaPackageFragmentProvider implements p0 {

    /* renamed from: a, reason: collision with root package name */
    private final g f4690a;

    /* renamed from: b, reason: collision with root package name */
    private final fd.a<FqName, LazyJavaPackageFragment> f4691b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyJavaPackageFragmentProvider.kt */
    /* renamed from: bc.f$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.a<LazyJavaPackageFragment> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ u f4693f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(u uVar) {
            super(0);
            this.f4693f = uVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final LazyJavaPackageFragment invoke() {
            return new LazyJavaPackageFragment(LazyJavaPackageFragmentProvider.this.f4690a, this.f4693f);
        }
    }

    public LazyJavaPackageFragmentProvider(b bVar) {
        ma.h c10;
        za.k.e(bVar, "components");
        k.a aVar = k.a.f4706a;
        c10 = ma.k.c(null);
        g gVar = new g(bVar, aVar, c10);
        this.f4690a = gVar;
        this.f4691b = gVar.e().e();
    }

    private final LazyJavaPackageFragment e(FqName fqName) {
        u a10 = o.a(this.f4690a.a().d(), fqName, false, 2, null);
        if (a10 == null) {
            return null;
        }
        return this.f4691b.a(fqName, new a(a10));
    }

    @Override // pb.p0
    public void a(FqName fqName, Collection<PackageFragmentDescriptor> collection) {
        za.k.e(fqName, "fqName");
        za.k.e(collection, "packageFragments");
        collections.a(collection, e(fqName));
    }

    @Override // pb.m0
    public List<LazyJavaPackageFragment> b(FqName fqName) {
        List<LazyJavaPackageFragment> n10;
        za.k.e(fqName, "fqName");
        n10 = r.n(e(fqName));
        return n10;
    }

    @Override // pb.p0
    public boolean c(FqName fqName) {
        za.k.e(fqName, "fqName");
        return o.a(this.f4690a.a().d(), fqName, false, 2, null) == null;
    }

    @Override // pb.m0
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public List<FqName> v(FqName fqName, l<? super Name, Boolean> lVar) {
        List<FqName> j10;
        za.k.e(fqName, "fqName");
        za.k.e(lVar, "nameFilter");
        LazyJavaPackageFragment e10 = e(fqName);
        List<FqName> X0 = e10 != null ? e10.X0() : null;
        if (X0 != null) {
            return X0;
        }
        j10 = r.j();
        return j10;
    }

    public String toString() {
        return "LazyJavaPackageFragmentProvider of module " + this.f4690a.a().m();
    }
}
