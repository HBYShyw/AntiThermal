package yb;

import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;

/* compiled from: BuiltinSpecialProperties.kt */
/* loaded from: classes2.dex */
public final class h {
    /* JADX INFO: Access modifiers changed from: private */
    public static final FqName c(FqName fqName, String str) {
        FqName c10 = fqName.c(Name.f(str));
        za.k.d(c10, "child(Name.identifier(name))");
        return c10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final FqName d(FqNameUnsafe fqNameUnsafe, String str) {
        FqName l10 = fqNameUnsafe.c(Name.f(str)).l();
        za.k.d(l10, "child(Name.identifier(name)).toSafe()");
        return l10;
    }
}
