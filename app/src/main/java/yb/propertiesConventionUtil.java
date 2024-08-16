package yb;

import java.util.List;
import oc.Name;
import od.capitalizeDecapitalize;
import sd.StringsJVM;

/* compiled from: propertiesConventionUtil.kt */
/* renamed from: yb.f0, reason: use source file name */
/* loaded from: classes2.dex */
public final class propertiesConventionUtil {
    public static final List<Name> a(Name name) {
        List<Name> n10;
        za.k.e(name, "name");
        String b10 = name.b();
        za.k.d(b10, "name.asString()");
        if (JvmAbi.c(b10)) {
            n10 = kotlin.collections.r.n(b(name));
            return n10;
        }
        if (JvmAbi.d(b10)) {
            return f(name);
        }
        return g.f20075a.b(name);
    }

    public static final Name b(Name name) {
        za.k.e(name, "methodName");
        Name e10 = e(name, "get", false, null, 12, null);
        return e10 == null ? e(name, "is", false, null, 8, null) : e10;
    }

    public static final Name c(Name name, boolean z10) {
        za.k.e(name, "methodName");
        return e(name, "set", false, z10 ? "is" : null, 4, null);
    }

    private static final Name d(Name name, String str, boolean z10, String str2) {
        boolean D;
        String j02;
        String j03;
        if (name.g()) {
            return null;
        }
        String d10 = name.d();
        za.k.d(d10, "methodName.identifier");
        boolean z11 = false;
        D = StringsJVM.D(d10, str, false, 2, null);
        if (!D || d10.length() == str.length()) {
            return null;
        }
        char charAt = d10.charAt(str.length());
        if ('a' <= charAt && charAt < '{') {
            z11 = true;
        }
        if (z11) {
            return null;
        }
        if (str2 != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            j03 = sd.v.j0(d10, str);
            sb2.append(j03);
            return Name.f(sb2.toString());
        }
        if (!z10) {
            return name;
        }
        j02 = sd.v.j0(d10, str);
        String c10 = capitalizeDecapitalize.c(j02, true);
        if (Name.h(c10)) {
            return Name.f(c10);
        }
        return null;
    }

    static /* synthetic */ Name e(Name name, String str, boolean z10, String str2, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            z10 = true;
        }
        if ((i10 & 8) != 0) {
            str2 = null;
        }
        return d(name, str, z10, str2);
    }

    public static final List<Name> f(Name name) {
        List<Name> o10;
        za.k.e(name, "methodName");
        o10 = kotlin.collections.r.o(c(name, false), c(name, true));
        return o10;
    }
}
