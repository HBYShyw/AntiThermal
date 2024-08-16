package sd;

import fb.PrimitiveRanges;
import fb._Ranges;
import java.util.Collection;
import java.util.Iterator;
import kotlin.collections.AbstractList;
import kotlin.collections.PrimitiveIterators;

/* compiled from: StringsJVM.kt */
/* renamed from: sd.u */
/* loaded from: classes2.dex */
public class StringsJVM extends StringNumberConversions {
    public static boolean A(String str, String str2, int i10, boolean z10) {
        za.k.e(str, "<this>");
        za.k.e(str2, "prefix");
        if (!z10) {
            return str.startsWith(str2, i10);
        }
        return t(str, i10, str2, 0, str2.length(), z10);
    }

    public static boolean B(String str, String str2, boolean z10) {
        za.k.e(str, "<this>");
        za.k.e(str2, "prefix");
        if (!z10) {
            return str.startsWith(str2);
        }
        return t(str, 0, str2, 0, str2.length(), z10);
    }

    public static /* synthetic */ boolean C(String str, String str2, int i10, boolean z10, int i11, Object obj) {
        boolean A;
        if ((i11 & 4) != 0) {
            z10 = false;
        }
        A = A(str, str2, i10, z10);
        return A;
    }

    public static /* synthetic */ boolean D(String str, String str2, boolean z10, int i10, Object obj) {
        boolean B;
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        B = B(str, str2, z10);
        return B;
    }

    public static String n(char[] cArr) {
        za.k.e(cArr, "<this>");
        return new String(cArr);
    }

    public static String o(char[] cArr, int i10, int i11) {
        za.k.e(cArr, "<this>");
        AbstractList.f14311e.a(i10, i11, cArr.length);
        return new String(cArr, i10, i11 - i10);
    }

    public static final boolean p(String str, String str2, boolean z10) {
        za.k.e(str, "<this>");
        za.k.e(str2, "suffix");
        if (!z10) {
            return str.endsWith(str2);
        }
        return t(str, str.length() - str2.length(), str2, 0, str2.length(), true);
    }

    public static /* synthetic */ boolean q(String str, String str2, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return p(str, str2, z10);
    }

    public static boolean r(String str, String str2, boolean z10) {
        if (str == null) {
            return str2 == null;
        }
        if (!z10) {
            return str.equals(str2);
        }
        return str.equalsIgnoreCase(str2);
    }

    public static boolean s(CharSequence charSequence) {
        Iterable O;
        boolean c10;
        boolean z10;
        za.k.e(charSequence, "<this>");
        if (charSequence.length() != 0) {
            O = v.O(charSequence);
            if (!(O instanceof Collection) || !((Collection) O).isEmpty()) {
                Iterator it = O.iterator();
                while (it.hasNext()) {
                    c10 = CharJVM.c(charSequence.charAt(((PrimitiveIterators) it).b()));
                    if (!c10) {
                        z10 = false;
                        break;
                    }
                }
            }
            z10 = true;
            if (!z10) {
                return false;
            }
        }
        return true;
    }

    public static final boolean t(String str, int i10, String str2, int i11, int i12, boolean z10) {
        za.k.e(str, "<this>");
        za.k.e(str2, "other");
        if (!z10) {
            return str.regionMatches(i10, str2, i11, i12);
        }
        return str.regionMatches(z10, i10, str2, i11, i12);
    }

    public static /* synthetic */ boolean u(String str, int i10, String str2, int i11, int i12, boolean z10, int i13, Object obj) {
        if ((i13 & 16) != 0) {
            z10 = false;
        }
        return t(str, i10, str2, i11, i12, z10);
    }

    public static String v(CharSequence charSequence, int i10) {
        za.k.e(charSequence, "<this>");
        if (!(i10 >= 0)) {
            throw new IllegalArgumentException(("Count 'n' must be non-negative, but was " + i10 + '.').toString());
        }
        if (i10 == 0) {
            return "";
        }
        if (i10 != 1) {
            int length = charSequence.length();
            if (length == 0) {
                return "";
            }
            if (length != 1) {
                StringBuilder sb2 = new StringBuilder(charSequence.length() * i10);
                PrimitiveIterators it = new PrimitiveRanges(1, i10).iterator();
                while (it.hasNext()) {
                    it.b();
                    sb2.append(charSequence);
                }
                String sb3 = sb2.toString();
                za.k.d(sb3, "{\n                    va…tring()\n                }");
                return sb3;
            }
            char charAt = charSequence.charAt(0);
            char[] cArr = new char[i10];
            for (int i11 = 0; i11 < i10; i11++) {
                cArr[i11] = charAt;
            }
            return new String(cArr);
        }
        return charSequence.toString();
    }

    public static final String w(String str, char c10, char c11, boolean z10) {
        za.k.e(str, "<this>");
        if (!z10) {
            String replace = str.replace(c10, c11);
            za.k.d(replace, "this as java.lang.String…replace(oldChar, newChar)");
            return replace;
        }
        StringBuilder sb2 = new StringBuilder(str.length());
        for (int i10 = 0; i10 < str.length(); i10++) {
            char charAt = str.charAt(i10);
            if (Char.d(charAt, c10, z10)) {
                charAt = c11;
            }
            sb2.append(charAt);
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder(capacity).…builderAction).toString()");
        return sb3;
    }

    public static final String x(String str, String str2, String str3, boolean z10) {
        int c10;
        za.k.e(str, "<this>");
        za.k.e(str2, "oldValue");
        za.k.e(str3, "newValue");
        int i10 = 0;
        int R = v.R(str, str2, 0, z10);
        if (R < 0) {
            return str;
        }
        int length = str2.length();
        c10 = _Ranges.c(length, 1);
        int length2 = (str.length() - length) + str3.length();
        if (length2 >= 0) {
            StringBuilder sb2 = new StringBuilder(length2);
            do {
                sb2.append((CharSequence) str, i10, R);
                sb2.append(str3);
                i10 = R + length;
                if (R >= str.length()) {
                    break;
                }
                R = v.R(str, str2, R + c10, z10);
            } while (R > 0);
            sb2.append((CharSequence) str, i10, str.length());
            String sb3 = sb2.toString();
            za.k.d(sb3, "stringBuilder.append(this, i, length).toString()");
            return sb3;
        }
        throw new OutOfMemoryError();
    }

    public static /* synthetic */ String y(String str, char c10, char c11, boolean z10, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        return w(str, c10, c11, z10);
    }

    public static /* synthetic */ String z(String str, String str2, String str3, boolean z10, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        return x(str, str2, str3, z10);
    }
}
