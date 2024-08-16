package pb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import oc.FqName;
import oc.Name;
import rd.Sequence;
import rd._Sequences;
import za.Lambda;

/* compiled from: PackageFragmentProviderImpl.kt */
/* renamed from: pb.n0, reason: use source file name */
/* loaded from: classes2.dex */
public final class PackageFragmentProviderImpl implements p0 {

    /* renamed from: a, reason: collision with root package name */
    private final Collection<PackageFragmentDescriptor> f16720a;

    /* compiled from: PackageFragmentProviderImpl.kt */
    /* renamed from: pb.n0$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<PackageFragmentDescriptor, FqName> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f16721e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final FqName invoke(PackageFragmentDescriptor packageFragmentDescriptor) {
            za.k.e(packageFragmentDescriptor, "it");
            return packageFragmentDescriptor.d();
        }
    }

    /* compiled from: PackageFragmentProviderImpl.kt */
    /* renamed from: pb.n0$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<FqName, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ FqName f16722e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(FqName fqName) {
            super(1);
            this.f16722e = fqName;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(FqName fqName) {
            za.k.e(fqName, "it");
            return Boolean.valueOf(!fqName.d() && za.k.a(fqName.e(), this.f16722e));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PackageFragmentProviderImpl(Collection<? extends PackageFragmentDescriptor> collection) {
        za.k.e(collection, "packageFragments");
        this.f16720a = collection;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // pb.p0
    public void a(FqName fqName, Collection<PackageFragmentDescriptor> collection) {
        za.k.e(fqName, "fqName");
        za.k.e(collection, "packageFragments");
        for (Object obj : this.f16720a) {
            if (za.k.a(((PackageFragmentDescriptor) obj).d(), fqName)) {
                collection.add(obj);
            }
        }
    }

    @Override // pb.m0
    public List<PackageFragmentDescriptor> b(FqName fqName) {
        za.k.e(fqName, "fqName");
        Collection<PackageFragmentDescriptor> collection = this.f16720a;
        ArrayList arrayList = new ArrayList();
        for (Object obj : collection) {
            if (za.k.a(((PackageFragmentDescriptor) obj).d(), fqName)) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    @Override // pb.p0
    public boolean c(FqName fqName) {
        za.k.e(fqName, "fqName");
        Collection<PackageFragmentDescriptor> collection = this.f16720a;
        if ((collection instanceof Collection) && collection.isEmpty()) {
            return true;
        }
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            if (za.k.a(((PackageFragmentDescriptor) it.next()).d(), fqName)) {
                return false;
            }
        }
        return true;
    }

    @Override // pb.m0
    public Collection<FqName> v(FqName fqName, ya.l<? super Name, Boolean> lVar) {
        Sequence K;
        Sequence w10;
        Sequence m10;
        List C;
        za.k.e(fqName, "fqName");
        za.k.e(lVar, "nameFilter");
        K = _Collections.K(this.f16720a);
        w10 = _Sequences.w(K, a.f16721e);
        m10 = _Sequences.m(w10, new b(fqName));
        C = _Sequences.C(m10);
        return C;
    }
}
