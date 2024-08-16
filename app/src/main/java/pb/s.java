package pb;

import oc.FqName;
import oc.Name;

/* compiled from: descriptorUtil.kt */
/* loaded from: classes2.dex */
public final class s {
    public static final ClassifierDescriptor a(DeclarationDescriptor declarationDescriptor) {
        za.k.e(declarationDescriptor, "<this>");
        DeclarationDescriptor b10 = declarationDescriptor.b();
        if (b10 == null || (declarationDescriptor instanceof PackageFragmentDescriptor)) {
            return null;
        }
        if (!b(b10)) {
            return a(b10);
        }
        if (b10 instanceof ClassifierDescriptor) {
            return (ClassifierDescriptor) b10;
        }
        return null;
    }

    public static final boolean b(DeclarationDescriptor declarationDescriptor) {
        za.k.e(declarationDescriptor, "<this>");
        return declarationDescriptor.b() instanceof PackageFragmentDescriptor;
    }

    public static final ClassDescriptor c(ModuleDescriptor moduleDescriptor, FqName fqName, xb.b bVar) {
        ClassifierDescriptor classifierDescriptor;
        zc.h F0;
        za.k.e(moduleDescriptor, "<this>");
        za.k.e(fqName, "fqName");
        za.k.e(bVar, "lookupLocation");
        if (fqName.d()) {
            return null;
        }
        FqName e10 = fqName.e();
        za.k.d(e10, "fqName.parent()");
        zc.h u7 = moduleDescriptor.H(e10).u();
        Name g6 = fqName.g();
        za.k.d(g6, "fqName.shortName()");
        ClassifierDescriptor e11 = u7.e(g6, bVar);
        ClassDescriptor classDescriptor = e11 instanceof ClassDescriptor ? (ClassDescriptor) e11 : null;
        if (classDescriptor != null) {
            return classDescriptor;
        }
        FqName e12 = fqName.e();
        za.k.d(e12, "fqName.parent()");
        ClassDescriptor c10 = c(moduleDescriptor, e12, bVar);
        if (c10 == null || (F0 = c10.F0()) == null) {
            classifierDescriptor = null;
        } else {
            Name g10 = fqName.g();
            za.k.d(g10, "fqName.shortName()");
            classifierDescriptor = F0.e(g10, bVar);
        }
        if (classifierDescriptor instanceof ClassDescriptor) {
            return (ClassDescriptor) classifierDescriptor;
        }
        return null;
    }
}
