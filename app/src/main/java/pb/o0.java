package pb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import oc.FqName;

/* compiled from: PackageFragmentProvider.kt */
/* loaded from: classes2.dex */
public final class o0 {
    public static final void a(m0 m0Var, FqName fqName, Collection<PackageFragmentDescriptor> collection) {
        za.k.e(m0Var, "<this>");
        za.k.e(fqName, "fqName");
        za.k.e(collection, "packageFragments");
        if (m0Var instanceof p0) {
            ((p0) m0Var).a(fqName, collection);
        } else {
            collection.addAll(m0Var.b(fqName));
        }
    }

    public static final boolean b(m0 m0Var, FqName fqName) {
        za.k.e(m0Var, "<this>");
        za.k.e(fqName, "fqName");
        return m0Var instanceof p0 ? ((p0) m0Var).c(fqName) : c(m0Var, fqName).isEmpty();
    }

    public static final List<PackageFragmentDescriptor> c(m0 m0Var, FqName fqName) {
        za.k.e(m0Var, "<this>");
        za.k.e(fqName, "fqName");
        ArrayList arrayList = new ArrayList();
        a(m0Var, fqName, arrayList);
        return arrayList;
    }
}
