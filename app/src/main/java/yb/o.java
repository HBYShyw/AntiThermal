package yb;

import oc.FqName;

/* loaded from: classes2.dex */
public /* synthetic */ class o {
    public static /* synthetic */ fc.u a(JavaClassFinder javaClassFinder, FqName fqName, boolean z10, int i10, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: findPackage");
        }
        if ((i10 & 2) != 0) {
            z10 = true;
        }
        return javaClassFinder.b(fqName, z10);
    }
}
