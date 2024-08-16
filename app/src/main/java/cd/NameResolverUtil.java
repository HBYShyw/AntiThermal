package cd;

import lc.NameResolver;
import oc.ClassId;
import oc.Name;

/* compiled from: NameResolverUtil.kt */
/* renamed from: cd.x, reason: use source file name */
/* loaded from: classes2.dex */
public final class NameResolverUtil {
    public static final ClassId a(NameResolver nameResolver, int i10) {
        za.k.e(nameResolver, "<this>");
        ClassId f10 = ClassId.f(nameResolver.a(i10), nameResolver.b(i10));
        za.k.d(f10, "fromString(getQualifiedC… isLocalClassName(index))");
        return f10;
    }

    public static final Name b(NameResolver nameResolver, int i10) {
        za.k.e(nameResolver, "<this>");
        Name e10 = Name.e(nameResolver.getString(i10));
        za.k.d(e10, "guessByFirstCharacter(getString(index))");
        return e10;
    }
}
