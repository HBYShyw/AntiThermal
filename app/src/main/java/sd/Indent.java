package sd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import za.Lambda;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Indent.kt */
/* renamed from: sd.n, reason: use source file name */
/* loaded from: classes2.dex */
public class Indent extends Appendable {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Indent.kt */
    /* renamed from: sd.n$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<String, String> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f18502e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke(String str) {
            za.k.e(str, "line");
            return str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Indent.kt */
    /* renamed from: sd.n$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<String, String> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f18503e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(String str) {
            super(1);
            this.f18503e = str;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke(String str) {
            za.k.e(str, "line");
            return this.f18503e + str;
        }
    }

    private static final ya.l<String, String> b(String str) {
        return str.length() == 0 ? a.f18502e : new b(str);
    }

    private static final int c(String str) {
        int length = str.length();
        int i10 = 0;
        while (true) {
            if (i10 >= length) {
                i10 = -1;
                break;
            }
            if (!sd.a.c(str.charAt(i10))) {
                break;
            }
            i10++;
        }
        return i10 == -1 ? str.length() : i10;
    }

    public static final String d(String str, String str2) {
        String invoke;
        za.k.e(str, "<this>");
        za.k.e(str2, "newIndent");
        List<String> d02 = v.d0(str);
        ArrayList arrayList = new ArrayList();
        for (Object obj : d02) {
            if (!l.s((String) obj)) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList(kotlin.collections.p.u(arrayList, 10));
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(Integer.valueOf(c((String) it.next())));
        }
        Integer num = (Integer) kotlin.collections.p.i0(arrayList2);
        int i10 = 0;
        int intValue = num != null ? num.intValue() : 0;
        int length = str.length() + (str2.length() * d02.size());
        ya.l<String, String> b10 = b(str2);
        int l10 = kotlin.collections.p.l(d02);
        ArrayList arrayList3 = new ArrayList();
        for (Object obj2 : d02) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                kotlin.collections.p.t();
            }
            String str3 = (String) obj2;
            if ((i10 == 0 || i10 == l10) && l.s(str3)) {
                str3 = null;
            } else {
                String K0 = _Strings.K0(str3, intValue);
                if (K0 != null && (invoke = b10.invoke(K0)) != null) {
                    str3 = invoke;
                }
            }
            if (str3 != null) {
                arrayList3.add(str3);
            }
            i10 = i11;
        }
        String sb2 = ((StringBuilder) kotlin.collections.p.a0(arrayList3, new StringBuilder(length), "\n", null, null, 0, null, null, 124, null)).toString();
        za.k.d(sb2, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return sb2;
    }

    public static final String e(String str, String str2, String str3) {
        int i10;
        String invoke;
        za.k.e(str, "<this>");
        za.k.e(str2, "newIndent");
        za.k.e(str3, "marginPrefix");
        if (!l.s(str3)) {
            List<String> d02 = v.d0(str);
            int length = str.length() + (str2.length() * d02.size());
            ya.l<String, String> b10 = b(str2);
            int l10 = kotlin.collections.p.l(d02);
            ArrayList arrayList = new ArrayList();
            int i11 = 0;
            for (Object obj : d02) {
                int i12 = i11 + 1;
                if (i11 < 0) {
                    kotlin.collections.p.t();
                }
                String str4 = (String) obj;
                String str5 = null;
                if ((i11 == 0 || i11 == l10) && l.s(str4)) {
                    str4 = null;
                } else {
                    int length2 = str4.length();
                    int i13 = 0;
                    while (true) {
                        if (i13 >= length2) {
                            i10 = -1;
                            break;
                        }
                        if (!sd.a.c(str4.charAt(i13))) {
                            i10 = i13;
                            break;
                        }
                        i13++;
                    }
                    if (i10 != -1) {
                        int i14 = i10;
                        if (l.C(str4, str3, i10, false, 4, null)) {
                            int length3 = i14 + str3.length();
                            za.k.c(str4, "null cannot be cast to non-null type java.lang.String");
                            str5 = str4.substring(length3);
                            za.k.d(str5, "this as java.lang.String).substring(startIndex)");
                        }
                    }
                    if (str5 != null && (invoke = b10.invoke(str5)) != null) {
                        str4 = invoke;
                    }
                }
                if (str4 != null) {
                    arrayList.add(str4);
                }
                i11 = i12;
            }
            String sb2 = ((StringBuilder) kotlin.collections.p.a0(arrayList, new StringBuilder(length), "\n", null, null, 0, null, null, 124, null)).toString();
            za.k.d(sb2, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
            return sb2;
        }
        throw new IllegalArgumentException("marginPrefix must be non-blank string.".toString());
    }

    public static String f(String str) {
        za.k.e(str, "<this>");
        return d(str, "");
    }

    public static final String g(String str, String str2) {
        za.k.e(str, "<this>");
        za.k.e(str2, "marginPrefix");
        return e(str, "", str2);
    }

    public static /* synthetic */ String h(String str, String str2, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            str2 = "|";
        }
        return g(str, str2);
    }
}
