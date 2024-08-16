package rc;

import java.util.List;
import oc.FqNameUnsafe;
import oc.Name;
import sd.StringsJVM;

/* compiled from: RenderingUtils.kt */
/* renamed from: rc.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class RenderingUtils {
    public static final String a(FqNameUnsafe fqNameUnsafe) {
        za.k.e(fqNameUnsafe, "<this>");
        List<Name> h10 = fqNameUnsafe.h();
        za.k.d(h10, "pathSegments()");
        return c(h10);
    }

    public static final String b(Name name) {
        za.k.e(name, "<this>");
        if (!e(name)) {
            String b10 = name.b();
            za.k.d(b10, "asString()");
            return b10;
        }
        StringBuilder sb2 = new StringBuilder();
        String b11 = name.b();
        za.k.d(b11, "asString()");
        sb2.append('`' + b11);
        sb2.append('`');
        return sb2.toString();
    }

    public static final String c(List<Name> list) {
        za.k.e(list, "pathSegments");
        StringBuilder sb2 = new StringBuilder();
        for (Name name : list) {
            if (sb2.length() > 0) {
                sb2.append(".");
            }
            sb2.append(b(name));
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public static final String d(String str, String str2, String str3, String str4, String str5) {
        boolean D;
        boolean D2;
        za.k.e(str, "lowerRendered");
        za.k.e(str2, "lowerPrefix");
        za.k.e(str3, "upperRendered");
        za.k.e(str4, "upperPrefix");
        za.k.e(str5, "foldedPrefix");
        D = StringsJVM.D(str, str2, false, 2, null);
        if (D) {
            D2 = StringsJVM.D(str3, str4, false, 2, null);
            if (D2) {
                String substring = str.substring(str2.length());
                za.k.d(substring, "this as java.lang.String).substring(startIndex)");
                String substring2 = str3.substring(str4.length());
                za.k.d(substring2, "this as java.lang.String).substring(startIndex)");
                String str6 = str5 + substring;
                if (za.k.a(substring, substring2)) {
                    return str6;
                }
                if (f(substring, substring2)) {
                    return str6 + '!';
                }
            }
        }
        return null;
    }

    private static final boolean e(Name name) {
        boolean z10;
        String b10 = name.b();
        za.k.d(b10, "asString()");
        if (!KeywordStringsGenerated.f17788a.contains(b10)) {
            int i10 = 0;
            while (true) {
                if (i10 >= b10.length()) {
                    z10 = false;
                    break;
                }
                char charAt = b10.charAt(i10);
                if ((Character.isLetterOrDigit(charAt) || charAt == '_') ? false : true) {
                    z10 = true;
                    break;
                }
                i10++;
            }
            if (!z10) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x003c, code lost:
    
        if (za.k.a(r7 + '?', r8) == false) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final boolean f(String str, String str2) {
        String z10;
        boolean q10;
        za.k.e(str, "lower");
        za.k.e(str2, "upper");
        z10 = StringsJVM.z(str2, "?", "", false, 4, null);
        if (!za.k.a(str, z10)) {
            q10 = StringsJVM.q(str2, "?", false, 2, null);
            if (q10) {
            }
            if (!za.k.a('(' + str + ")?", str2)) {
                return false;
            }
        }
        return true;
    }
}
