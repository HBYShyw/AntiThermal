package sd;

import fb._Ranges;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Strings.kt */
/* renamed from: sd.x, reason: use source file name */
/* loaded from: classes2.dex */
public class _Strings extends _StringsJvm {
    public static final String K0(String str, int i10) {
        int f10;
        za.k.e(str, "<this>");
        if (i10 >= 0) {
            f10 = _Ranges.f(i10, str.length());
            String substring = str.substring(f10);
            za.k.d(substring, "this as java.lang.String).substring(startIndex)");
            return substring;
        }
        throw new IllegalArgumentException(("Requested character count " + i10 + " is less than zero.").toString());
    }

    public static char L0(CharSequence charSequence) {
        int P;
        za.k.e(charSequence, "<this>");
        if (!(charSequence.length() == 0)) {
            P = v.P(charSequence);
            return charSequence.charAt(P);
        }
        throw new NoSuchElementException("Char sequence is empty.");
    }

    public static String M0(String str, int i10) {
        int f10;
        za.k.e(str, "<this>");
        if (i10 >= 0) {
            f10 = _Ranges.f(i10, str.length());
            String substring = str.substring(0, f10);
            za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            return substring;
        }
        throw new IllegalArgumentException(("Requested character count " + i10 + " is less than zero.").toString());
    }
}
