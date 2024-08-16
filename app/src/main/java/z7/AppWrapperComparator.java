package z7;

import android.content.Context;
import c8.WordQuery;
import f6.f;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/* compiled from: AppWrapperComparator.java */
/* renamed from: z7.b, reason: use source file name */
/* loaded from: classes.dex */
public class AppWrapperComparator implements Comparator<AppInfoWrapper>, Serializable {

    /* renamed from: e, reason: collision with root package name */
    List<String> f20251e;

    /* renamed from: f, reason: collision with root package name */
    List<String> f20252f;

    public AppWrapperComparator(Context context, List<String> list, List<String> list2) {
        this.f20251e = new ArrayList();
        new ArrayList();
        this.f20251e = list;
        this.f20252f = list2;
    }

    private int b(String str, String str2) {
        char[] charArray = str.toCharArray();
        char[] charArray2 = str2.toCharArray();
        int length = charArray.length;
        int length2 = charArray2.length;
        int min = Math.min(length, length2);
        for (int i10 = 0; i10 < min; i10++) {
            char c10 = charArray[i10];
            char c11 = charArray2[i10];
            int d10 = d(c10);
            int d11 = d(c11);
            if (d10 != d11) {
                return d10 - d11;
            }
            if (d10 == 2) {
                if (c10 != c11) {
                    return Collator.getInstance(Locale.CHINA).compare(str, str2);
                }
            } else if (d10 == 3) {
                int e10 = e(str.substring(i10));
                int e11 = e(str2.substring(i10));
                if (e10 != e11) {
                    if (e10 == -1) {
                        return 1;
                    }
                    if (e11 == -1) {
                        return -1;
                    }
                    return e10 - e11;
                }
            } else if (d10 == 1) {
                char lowerCase = Character.toLowerCase(c10);
                char lowerCase2 = Character.toLowerCase(c11);
                if (lowerCase != lowerCase2) {
                    return lowerCase - lowerCase2;
                }
            } else if (c10 != c11) {
                return c10 - c11;
            }
        }
        return length - length2;
    }

    private char c(char c10) {
        return WordQuery.a(c10);
    }

    private int d(char c10) {
        if (c10 >= 19968 && c10 <= 40869) {
            return 2;
        }
        if (g(c10)) {
            return 3;
        }
        return f(c10) ? 1 : 4;
    }

    private int e(String str) {
        if (str == null || str.length() <= 0 || str.charAt(0) < '0' || str.charAt(0) > '9') {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(str.substring(0, 1));
    }

    private boolean f(char c10) {
        return (c10 >= 'A' && c10 <= 'Z') || (c10 >= 'a' && c10 <= 'z');
    }

    private boolean g(char c10) {
        return c10 >= '0' && c10 <= '9';
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(AppInfoWrapper appInfoWrapper, AppInfoWrapper appInfoWrapper2) {
        char c10;
        int i10 = appInfoWrapper.f20250e;
        int i11 = appInfoWrapper2.f20250e;
        if (i10 != i11) {
            return i10 > i11 ? 1 : -1;
        }
        char c11 = appInfoWrapper.f20249d;
        if (c11 != 0 && (c10 = appInfoWrapper2.f20249d) != 0 && c11 != '#' && c10 != '#' && c11 != c10) {
            return c11 > c10 ? 1 : -1;
        }
        String i12 = f.i(appInfoWrapper.f20247b.trim());
        String i13 = f.i(appInfoWrapper2.f20247b.trim());
        if (i12.length() == 0) {
            return -1;
        }
        if (i13.length() == 0) {
            return 1;
        }
        char charAt = i12.charAt(0);
        char charAt2 = i13.charAt(0);
        int d10 = d(charAt);
        int d11 = d(charAt2);
        if (d10 == 2) {
            charAt = c(charAt);
        }
        if (d11 == 2) {
            charAt2 = c(charAt2);
        }
        if ((d10 != 1 || d11 != 2) && (d10 != 2 || d11 != 1)) {
            return d10 != d11 ? d10 - d11 : b(i12, i13);
        }
        char lowerCase = Character.toLowerCase(charAt);
        char lowerCase2 = Character.toLowerCase(charAt2);
        return lowerCase == lowerCase2 ? d10 == 1 ? -1 : 1 : lowerCase - lowerCase2;
    }
}
