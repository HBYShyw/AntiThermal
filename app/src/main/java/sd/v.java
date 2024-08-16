package sd;

import fb.PrimitiveRanges;
import fb.Progressions;
import fb._Ranges;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.PrimitiveIterators;
import kotlin.collections._Arrays;
import kotlin.collections._ArraysJvm;
import kotlin.collections._Collections;
import rd.Sequence;
import rd._Sequences;
import za.Lambda;

/* compiled from: Strings.kt */
/* loaded from: classes2.dex */
public class v extends StringsJVM {

    /* compiled from: Strings.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.p<CharSequence, Integer, ma.o<? extends Integer, ? extends Integer>> {

        /* renamed from: e */
        final /* synthetic */ char[] f18504e;

        /* renamed from: f */
        final /* synthetic */ boolean f18505f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(char[] cArr, boolean z10) {
            super(2);
            this.f18504e = cArr;
            this.f18505f = z10;
        }

        public final ma.o<Integer, Integer> a(CharSequence charSequence, int i10) {
            za.k.e(charSequence, "$this$$receiver");
            int W = v.W(charSequence, this.f18504e, i10, this.f18505f);
            if (W < 0) {
                return null;
            }
            return ma.u.a(Integer.valueOf(W), 1);
        }

        @Override // ya.p
        public /* bridge */ /* synthetic */ ma.o<? extends Integer, ? extends Integer> invoke(CharSequence charSequence, Integer num) {
            return a(charSequence, num.intValue());
        }
    }

    /* compiled from: Strings.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.p<CharSequence, Integer, ma.o<? extends Integer, ? extends Integer>> {

        /* renamed from: e */
        final /* synthetic */ List<String> f18506e;

        /* renamed from: f */
        final /* synthetic */ boolean f18507f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(List<String> list, boolean z10) {
            super(2);
            this.f18506e = list;
            this.f18507f = z10;
        }

        public final ma.o<Integer, Integer> a(CharSequence charSequence, int i10) {
            za.k.e(charSequence, "$this$$receiver");
            ma.o N = v.N(charSequence, this.f18506e, i10, this.f18507f, false);
            if (N != null) {
                return ma.u.a(N.c(), Integer.valueOf(((String) N.d()).length()));
            }
            return null;
        }

        @Override // ya.p
        public /* bridge */ /* synthetic */ ma.o<? extends Integer, ? extends Integer> invoke(CharSequence charSequence, Integer num) {
            return a(charSequence, num.intValue());
        }
    }

    /* compiled from: Strings.kt */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<PrimitiveRanges, String> {

        /* renamed from: e */
        final /* synthetic */ CharSequence f18508e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(CharSequence charSequence) {
            super(1);
            this.f18508e = charSequence;
        }

        @Override // ya.l
        /* renamed from: a */
        public final String invoke(PrimitiveRanges primitiveRanges) {
            za.k.e(primitiveRanges, "it");
            return v.x0(this.f18508e, primitiveRanges);
        }
    }

    public static /* synthetic */ String A0(String str, char c10, String str2, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            str2 = str;
        }
        return y0(str, c10, str2);
    }

    public static /* synthetic */ String B0(String str, String str2, String str3, int i10, Object obj) {
        String z02;
        if ((i10 & 2) != 0) {
            str3 = str;
        }
        z02 = z0(str, str2, str3);
        return z02;
    }

    public static final String C0(String str, char c10, String str2) {
        int Z;
        za.k.e(str, "<this>");
        za.k.e(str2, "missingDelimiterValue");
        Z = Z(str, c10, 0, false, 6, null);
        if (Z == -1) {
            return str2;
        }
        String substring = str.substring(Z + 1, str.length());
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String D0(String str, char c10, String str2, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            str2 = str;
        }
        return C0(str, c10, str2);
    }

    public static final String E0(String str, char c10, String str2) {
        int U;
        za.k.e(str, "<this>");
        za.k.e(str2, "missingDelimiterValue");
        U = U(str, c10, 0, false, 6, null);
        if (U == -1) {
            return str2;
        }
        String substring = str.substring(0, U);
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final boolean F(CharSequence charSequence, char c10, boolean z10) {
        int U;
        za.k.e(charSequence, "<this>");
        U = U(charSequence, c10, 0, z10, 2, null);
        return U >= 0;
    }

    public static final String F0(String str, String str2, String str3) {
        int V;
        za.k.e(str, "<this>");
        za.k.e(str2, "delimiter");
        za.k.e(str3, "missingDelimiterValue");
        V = V(str, str2, 0, false, 6, null);
        if (V == -1) {
            return str3;
        }
        String substring = str.substring(0, V);
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final boolean G(CharSequence charSequence, CharSequence charSequence2, boolean z10) {
        int V;
        za.k.e(charSequence, "<this>");
        za.k.e(charSequence2, "other");
        if (charSequence2 instanceof String) {
            V = V(charSequence, (String) charSequence2, 0, z10, 2, null);
            if (V >= 0) {
                return true;
            }
        } else if (T(charSequence, charSequence2, 0, charSequence.length(), z10, false, 16, null) >= 0) {
            return true;
        }
        return false;
    }

    public static /* synthetic */ String G0(String str, char c10, String str2, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            str2 = str;
        }
        return E0(str, c10, str2);
    }

    public static /* synthetic */ boolean H(CharSequence charSequence, char c10, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return F(charSequence, c10, z10);
    }

    public static /* synthetic */ String H0(String str, String str2, String str3, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            str3 = str;
        }
        return F0(str, str2, str3);
    }

    public static /* synthetic */ boolean I(CharSequence charSequence, CharSequence charSequence2, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return G(charSequence, charSequence2, z10);
    }

    public static String I0(String str, String str2, String str3) {
        int a02;
        za.k.e(str, "<this>");
        za.k.e(str2, "delimiter");
        za.k.e(str3, "missingDelimiterValue");
        a02 = a0(str, str2, 0, false, 6, null);
        if (a02 == -1) {
            return str3;
        }
        String substring = str.substring(0, a02);
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final boolean J(CharSequence charSequence, char c10, boolean z10) {
        int P;
        za.k.e(charSequence, "<this>");
        if (charSequence.length() > 0) {
            P = P(charSequence);
            if (Char.d(charSequence.charAt(P), c10, z10)) {
                return true;
            }
        }
        return false;
    }

    public static CharSequence J0(CharSequence charSequence) {
        boolean c10;
        za.k.e(charSequence, "<this>");
        int length = charSequence.length() - 1;
        int i10 = 0;
        boolean z10 = false;
        while (i10 <= length) {
            c10 = CharJVM.c(charSequence.charAt(!z10 ? i10 : length));
            if (z10) {
                if (!c10) {
                    break;
                }
                length--;
            } else if (c10) {
                i10++;
            } else {
                z10 = true;
            }
        }
        return charSequence.subSequence(i10, length + 1);
    }

    public static final boolean K(CharSequence charSequence, CharSequence charSequence2, boolean z10) {
        boolean q10;
        za.k.e(charSequence, "<this>");
        za.k.e(charSequence2, "suffix");
        if (!z10 && (charSequence instanceof String) && (charSequence2 instanceof String)) {
            q10 = StringsJVM.q((String) charSequence, (String) charSequence2, false, 2, null);
            return q10;
        }
        return i0(charSequence, charSequence.length() - charSequence2.length(), charSequence2, 0, charSequence2.length(), z10);
    }

    public static /* synthetic */ boolean L(CharSequence charSequence, char c10, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return J(charSequence, c10, z10);
    }

    public static /* synthetic */ boolean M(CharSequence charSequence, CharSequence charSequence2, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return K(charSequence, charSequence2, z10);
    }

    public static final ma.o<Integer, String> N(CharSequence charSequence, Collection<String> collection, int i10, boolean z10, boolean z11) {
        int P;
        int f10;
        Progressions i11;
        Object obj;
        Object obj2;
        int c10;
        Object p02;
        if (!z10 && collection.size() == 1) {
            p02 = _Collections.p0(collection);
            String str = (String) p02;
            int V = !z11 ? V(charSequence, str, i10, false, 4, null) : a0(charSequence, str, i10, false, 4, null);
            if (V < 0) {
                return null;
            }
            return ma.u.a(Integer.valueOf(V), str);
        }
        if (z11) {
            P = P(charSequence);
            f10 = _Ranges.f(i10, P);
            i11 = _Ranges.i(f10, 0);
        } else {
            c10 = _Ranges.c(i10, 0);
            i11 = new PrimitiveRanges(c10, charSequence.length());
        }
        if (charSequence instanceof String) {
            int d10 = i11.d();
            int e10 = i11.e();
            int f11 = i11.f();
            if ((f11 > 0 && d10 <= e10) || (f11 < 0 && e10 <= d10)) {
                while (true) {
                    Iterator<T> it = collection.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            obj2 = null;
                            break;
                        }
                        obj2 = it.next();
                        String str2 = (String) obj2;
                        if (StringsJVM.t(str2, 0, (String) charSequence, d10, str2.length(), z10)) {
                            break;
                        }
                    }
                    String str3 = (String) obj2;
                    if (str3 == null) {
                        if (d10 == e10) {
                            break;
                        }
                        d10 += f11;
                    } else {
                        return ma.u.a(Integer.valueOf(d10), str3);
                    }
                }
            }
        } else {
            int d11 = i11.d();
            int e11 = i11.e();
            int f12 = i11.f();
            if ((f12 > 0 && d11 <= e11) || (f12 < 0 && e11 <= d11)) {
                while (true) {
                    Iterator<T> it2 = collection.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            obj = null;
                            break;
                        }
                        obj = it2.next();
                        String str4 = (String) obj;
                        if (i0(str4, 0, charSequence, d11, str4.length(), z10)) {
                            break;
                        }
                    }
                    String str5 = (String) obj;
                    if (str5 == null) {
                        if (d11 == e11) {
                            break;
                        }
                        d11 += f12;
                    } else {
                        return ma.u.a(Integer.valueOf(d11), str5);
                    }
                }
            }
        }
        return null;
    }

    public static PrimitiveRanges O(CharSequence charSequence) {
        za.k.e(charSequence, "<this>");
        return new PrimitiveRanges(0, charSequence.length() - 1);
    }

    public static int P(CharSequence charSequence) {
        za.k.e(charSequence, "<this>");
        return charSequence.length() - 1;
    }

    public static final int Q(CharSequence charSequence, char c10, int i10, boolean z10) {
        za.k.e(charSequence, "<this>");
        if (!z10 && (charSequence instanceof String)) {
            return ((String) charSequence).indexOf(c10, i10);
        }
        return W(charSequence, new char[]{c10}, i10, z10);
    }

    public static final int R(CharSequence charSequence, String str, int i10, boolean z10) {
        za.k.e(charSequence, "<this>");
        za.k.e(str, "string");
        if (!z10 && (charSequence instanceof String)) {
            return ((String) charSequence).indexOf(str, i10);
        }
        return T(charSequence, str, i10, charSequence.length(), z10, false, 16, null);
    }

    private static final int S(CharSequence charSequence, CharSequence charSequence2, int i10, int i11, boolean z10, boolean z11) {
        int P;
        int f10;
        int c10;
        Progressions i12;
        int c11;
        int f11;
        if (!z11) {
            c11 = _Ranges.c(i10, 0);
            f11 = _Ranges.f(i11, charSequence.length());
            i12 = new PrimitiveRanges(c11, f11);
        } else {
            P = P(charSequence);
            f10 = _Ranges.f(i10, P);
            c10 = _Ranges.c(i11, 0);
            i12 = _Ranges.i(f10, c10);
        }
        if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
            int d10 = i12.d();
            int e10 = i12.e();
            int f12 = i12.f();
            if ((f12 <= 0 || d10 > e10) && (f12 >= 0 || e10 > d10)) {
                return -1;
            }
            while (!StringsJVM.t((String) charSequence2, 0, (String) charSequence, d10, charSequence2.length(), z10)) {
                if (d10 == e10) {
                    return -1;
                }
                d10 += f12;
            }
            return d10;
        }
        int d11 = i12.d();
        int e11 = i12.e();
        int f13 = i12.f();
        if ((f13 <= 0 || d11 > e11) && (f13 >= 0 || e11 > d11)) {
            return -1;
        }
        while (!i0(charSequence2, 0, charSequence, d11, charSequence2.length(), z10)) {
            if (d11 == e11) {
                return -1;
            }
            d11 += f13;
        }
        return d11;
    }

    static /* synthetic */ int T(CharSequence charSequence, CharSequence charSequence2, int i10, int i11, boolean z10, boolean z11, int i12, Object obj) {
        if ((i12 & 16) != 0) {
            z11 = false;
        }
        return S(charSequence, charSequence2, i10, i11, z10, z11);
    }

    public static /* synthetic */ int U(CharSequence charSequence, char c10, int i10, boolean z10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            i10 = 0;
        }
        if ((i11 & 4) != 0) {
            z10 = false;
        }
        return Q(charSequence, c10, i10, z10);
    }

    public static /* synthetic */ int V(CharSequence charSequence, String str, int i10, boolean z10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            i10 = 0;
        }
        if ((i11 & 4) != 0) {
            z10 = false;
        }
        return R(charSequence, str, i10, z10);
    }

    public static final int W(CharSequence charSequence, char[] cArr, int i10, boolean z10) {
        int c10;
        int P;
        boolean z11;
        char S;
        za.k.e(charSequence, "<this>");
        za.k.e(cArr, "chars");
        if (!z10 && cArr.length == 1 && (charSequence instanceof String)) {
            S = _Arrays.S(cArr);
            return ((String) charSequence).indexOf(S, i10);
        }
        c10 = _Ranges.c(i10, 0);
        P = P(charSequence);
        PrimitiveIterators it = new PrimitiveRanges(c10, P).iterator();
        while (it.hasNext()) {
            int b10 = it.b();
            char charAt = charSequence.charAt(b10);
            int length = cArr.length;
            int i11 = 0;
            while (true) {
                if (i11 >= length) {
                    z11 = false;
                    break;
                }
                if (Char.d(cArr[i11], charAt, z10)) {
                    z11 = true;
                    break;
                }
                i11++;
            }
            if (z11) {
                return b10;
            }
        }
        return -1;
    }

    public static final int X(CharSequence charSequence, char c10, int i10, boolean z10) {
        za.k.e(charSequence, "<this>");
        if (!z10 && (charSequence instanceof String)) {
            return ((String) charSequence).lastIndexOf(c10, i10);
        }
        return b0(charSequence, new char[]{c10}, i10, z10);
    }

    public static final int Y(CharSequence charSequence, String str, int i10, boolean z10) {
        za.k.e(charSequence, "<this>");
        za.k.e(str, "string");
        if (!z10 && (charSequence instanceof String)) {
            return ((String) charSequence).lastIndexOf(str, i10);
        }
        return S(charSequence, str, i10, 0, z10, true);
    }

    public static /* synthetic */ int Z(CharSequence charSequence, char c10, int i10, boolean z10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            i10 = P(charSequence);
        }
        if ((i11 & 4) != 0) {
            z10 = false;
        }
        return X(charSequence, c10, i10, z10);
    }

    public static /* synthetic */ int a0(CharSequence charSequence, String str, int i10, boolean z10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            i10 = P(charSequence);
        }
        if ((i11 & 4) != 0) {
            z10 = false;
        }
        return Y(charSequence, str, i10, z10);
    }

    public static final int b0(CharSequence charSequence, char[] cArr, int i10, boolean z10) {
        int P;
        int f10;
        char S;
        za.k.e(charSequence, "<this>");
        za.k.e(cArr, "chars");
        if (!z10 && cArr.length == 1 && (charSequence instanceof String)) {
            S = _Arrays.S(cArr);
            return ((String) charSequence).lastIndexOf(S, i10);
        }
        P = P(charSequence);
        for (f10 = _Ranges.f(i10, P); -1 < f10; f10--) {
            char charAt = charSequence.charAt(f10);
            int length = cArr.length;
            boolean z11 = false;
            int i11 = 0;
            while (true) {
                if (i11 >= length) {
                    break;
                }
                if (Char.d(cArr[i11], charAt, z10)) {
                    z11 = true;
                    break;
                }
                i11++;
            }
            if (z11) {
                return f10;
            }
        }
        return -1;
    }

    public static final Sequence<String> c0(CharSequence charSequence) {
        za.k.e(charSequence, "<this>");
        return s0(charSequence, new String[]{"\r\n", "\n", "\r"}, false, 0, 6, null);
    }

    public static final List<String> d0(CharSequence charSequence) {
        List<String> C;
        za.k.e(charSequence, "<this>");
        C = _Sequences.C(c0(charSequence));
        return C;
    }

    private static final Sequence<PrimitiveRanges> e0(CharSequence charSequence, char[] cArr, int i10, boolean z10, int i11) {
        l0(i11);
        return new e(charSequence, i10, i11, new a(cArr, z10));
    }

    private static final Sequence<PrimitiveRanges> f0(CharSequence charSequence, String[] strArr, int i10, boolean z10, int i11) {
        List e10;
        l0(i11);
        e10 = _ArraysJvm.e(strArr);
        return new e(charSequence, i10, i11, new b(e10, z10));
    }

    static /* synthetic */ Sequence g0(CharSequence charSequence, char[] cArr, int i10, boolean z10, int i11, int i12, Object obj) {
        if ((i12 & 2) != 0) {
            i10 = 0;
        }
        if ((i12 & 4) != 0) {
            z10 = false;
        }
        if ((i12 & 8) != 0) {
            i11 = 0;
        }
        return e0(charSequence, cArr, i10, z10, i11);
    }

    static /* synthetic */ Sequence h0(CharSequence charSequence, String[] strArr, int i10, boolean z10, int i11, int i12, Object obj) {
        if ((i12 & 2) != 0) {
            i10 = 0;
        }
        if ((i12 & 4) != 0) {
            z10 = false;
        }
        if ((i12 & 8) != 0) {
            i11 = 0;
        }
        return f0(charSequence, strArr, i10, z10, i11);
    }

    public static final boolean i0(CharSequence charSequence, int i10, CharSequence charSequence2, int i11, int i12, boolean z10) {
        za.k.e(charSequence, "<this>");
        za.k.e(charSequence2, "other");
        if (i11 < 0 || i10 < 0 || i10 > charSequence.length() - i12 || i11 > charSequence2.length() - i12) {
            return false;
        }
        for (int i13 = 0; i13 < i12; i13++) {
            if (!Char.d(charSequence.charAt(i10 + i13), charSequence2.charAt(i11 + i13), z10)) {
                return false;
            }
        }
        return true;
    }

    public static String j0(String str, CharSequence charSequence) {
        za.k.e(str, "<this>");
        za.k.e(charSequence, "prefix");
        if (!w0(str, charSequence, false, 2, null)) {
            return str;
        }
        String substring = str.substring(charSequence.length());
        za.k.d(substring, "this as java.lang.String).substring(startIndex)");
        return substring;
    }

    public static String k0(String str, CharSequence charSequence) {
        za.k.e(str, "<this>");
        za.k.e(charSequence, "suffix");
        if (!M(str, charSequence, false, 2, null)) {
            return str;
        }
        String substring = str.substring(0, str.length() - charSequence.length());
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static final void l0(int i10) {
        if (i10 >= 0) {
            return;
        }
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + i10).toString());
    }

    public static final List<String> m0(CharSequence charSequence, char[] cArr, boolean z10, int i10) {
        Iterable i11;
        int u7;
        za.k.e(charSequence, "<this>");
        za.k.e(cArr, "delimiters");
        if (cArr.length == 1) {
            return o0(charSequence, String.valueOf(cArr[0]), z10, i10);
        }
        i11 = _Sequences.i(g0(charSequence, cArr, 0, z10, i10, 2, null));
        u7 = kotlin.collections.s.u(i11, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = i11.iterator();
        while (it.hasNext()) {
            arrayList.add(x0(charSequence, (PrimitiveRanges) it.next()));
        }
        return arrayList;
    }

    public static final List<String> n0(CharSequence charSequence, String[] strArr, boolean z10, int i10) {
        Iterable i11;
        int u7;
        za.k.e(charSequence, "<this>");
        za.k.e(strArr, "delimiters");
        if (strArr.length == 1) {
            String str = strArr[0];
            if (!(str.length() == 0)) {
                return o0(charSequence, str, z10, i10);
            }
        }
        i11 = _Sequences.i(h0(charSequence, strArr, 0, z10, i10, 2, null));
        u7 = kotlin.collections.s.u(i11, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = i11.iterator();
        while (it.hasNext()) {
            arrayList.add(x0(charSequence, (PrimitiveRanges) it.next()));
        }
        return arrayList;
    }

    private static final List<String> o0(CharSequence charSequence, String str, boolean z10, int i10) {
        List<String> e10;
        l0(i10);
        int i11 = 0;
        int R = R(charSequence, str, 0, z10);
        if (R != -1 && i10 != 1) {
            boolean z11 = i10 > 0;
            ArrayList arrayList = new ArrayList(z11 ? _Ranges.f(i10, 10) : 10);
            do {
                arrayList.add(charSequence.subSequence(i11, R).toString());
                i11 = str.length() + R;
                if (z11 && arrayList.size() == i10 - 1) {
                    break;
                }
                R = R(charSequence, str, i11, z10);
            } while (R != -1);
            arrayList.add(charSequence.subSequence(i11, charSequence.length()).toString());
            return arrayList;
        }
        e10 = CollectionsJVM.e(charSequence.toString());
        return e10;
    }

    public static /* synthetic */ List p0(CharSequence charSequence, char[] cArr, boolean z10, int i10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            z10 = false;
        }
        if ((i11 & 4) != 0) {
            i10 = 0;
        }
        return m0(charSequence, cArr, z10, i10);
    }

    public static /* synthetic */ List q0(CharSequence charSequence, String[] strArr, boolean z10, int i10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            z10 = false;
        }
        if ((i11 & 4) != 0) {
            i10 = 0;
        }
        return n0(charSequence, strArr, z10, i10);
    }

    public static final Sequence<String> r0(CharSequence charSequence, String[] strArr, boolean z10, int i10) {
        Sequence<String> w10;
        za.k.e(charSequence, "<this>");
        za.k.e(strArr, "delimiters");
        w10 = _Sequences.w(h0(charSequence, strArr, 0, z10, i10, 2, null), new c(charSequence));
        return w10;
    }

    public static /* synthetic */ Sequence s0(CharSequence charSequence, String[] strArr, boolean z10, int i10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            z10 = false;
        }
        if ((i11 & 4) != 0) {
            i10 = 0;
        }
        return r0(charSequence, strArr, z10, i10);
    }

    public static final boolean t0(CharSequence charSequence, char c10, boolean z10) {
        za.k.e(charSequence, "<this>");
        return charSequence.length() > 0 && Char.d(charSequence.charAt(0), c10, z10);
    }

    public static final boolean u0(CharSequence charSequence, CharSequence charSequence2, boolean z10) {
        boolean D;
        za.k.e(charSequence, "<this>");
        za.k.e(charSequence2, "prefix");
        if (!z10 && (charSequence instanceof String) && (charSequence2 instanceof String)) {
            D = StringsJVM.D((String) charSequence, (String) charSequence2, false, 2, null);
            return D;
        }
        return i0(charSequence, 0, charSequence2, 0, charSequence2.length(), z10);
    }

    public static /* synthetic */ boolean v0(CharSequence charSequence, char c10, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return t0(charSequence, c10, z10);
    }

    public static /* synthetic */ boolean w0(CharSequence charSequence, CharSequence charSequence2, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return u0(charSequence, charSequence2, z10);
    }

    public static final String x0(CharSequence charSequence, PrimitiveRanges primitiveRanges) {
        za.k.e(charSequence, "<this>");
        za.k.e(primitiveRanges, "range");
        return charSequence.subSequence(primitiveRanges.l().intValue(), primitiveRanges.k().intValue() + 1).toString();
    }

    public static final String y0(String str, char c10, String str2) {
        int U;
        za.k.e(str, "<this>");
        za.k.e(str2, "missingDelimiterValue");
        U = U(str, c10, 0, false, 6, null);
        if (U == -1) {
            return str2;
        }
        String substring = str.substring(U + 1, str.length());
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static String z0(String str, String str2, String str3) {
        int V;
        za.k.e(str, "<this>");
        za.k.e(str2, "delimiter");
        za.k.e(str3, "missingDelimiterValue");
        V = V(str, str2, 0, false, 6, null);
        if (V == -1) {
            return str3;
        }
        String substring = str.substring(V + str2.length(), str.length());
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }
}
