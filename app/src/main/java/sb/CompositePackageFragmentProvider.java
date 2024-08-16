package sb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections._Collections;
import oc.FqName;
import oc.Name;
import pb.PackageFragmentDescriptor;
import pb.o0;
import pb.p0;

/* compiled from: CompositePackageFragmentProvider.kt */
/* renamed from: sb.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class CompositePackageFragmentProvider implements p0 {

    /* renamed from: a, reason: collision with root package name */
    private final List<pb.m0> f18286a;

    /* renamed from: b, reason: collision with root package name */
    private final String f18287b;

    /* JADX WARN: Multi-variable type inference failed */
    public CompositePackageFragmentProvider(List<? extends pb.m0> list, String str) {
        Set D0;
        za.k.e(list, "providers");
        za.k.e(str, "debugName");
        this.f18286a = list;
        this.f18287b = str;
        list.size();
        D0 = _Collections.D0(list);
        D0.size();
    }

    @Override // pb.p0
    public void a(FqName fqName, Collection<PackageFragmentDescriptor> collection) {
        za.k.e(fqName, "fqName");
        za.k.e(collection, "packageFragments");
        Iterator<pb.m0> it = this.f18286a.iterator();
        while (it.hasNext()) {
            o0.a(it.next(), fqName, collection);
        }
    }

    @Override // pb.m0
    public List<PackageFragmentDescriptor> b(FqName fqName) {
        List<PackageFragmentDescriptor> z02;
        za.k.e(fqName, "fqName");
        ArrayList arrayList = new ArrayList();
        Iterator<pb.m0> it = this.f18286a.iterator();
        while (it.hasNext()) {
            o0.a(it.next(), fqName, arrayList);
        }
        z02 = _Collections.z0(arrayList);
        return z02;
    }

    @Override // pb.p0
    public boolean c(FqName fqName) {
        za.k.e(fqName, "fqName");
        List<pb.m0> list = this.f18286a;
        if ((list instanceof Collection) && list.isEmpty()) {
            return true;
        }
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            if (!o0.b((pb.m0) it.next(), fqName)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return this.f18287b;
    }

    @Override // pb.m0
    public Collection<FqName> v(FqName fqName, ya.l<? super Name, Boolean> lVar) {
        za.k.e(fqName, "fqName");
        za.k.e(lVar, "nameFilter");
        HashSet hashSet = new HashSet();
        Iterator<pb.m0> it = this.f18286a.iterator();
        while (it.hasNext()) {
            hashSet.addAll(it.next().v(fqName, lVar));
        }
        return hashSet;
    }
}
