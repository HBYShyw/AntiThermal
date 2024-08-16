package w9;

import ja.WordQueryCompat;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/* compiled from: AppNameComparator.java */
/* renamed from: w9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class AppNameComparator implements Comparator<a> {

    /* renamed from: g, reason: collision with root package name */
    public static final AppNameComparator f19408g = new AppNameComparator();

    /* renamed from: h, reason: collision with root package name */
    public static final AppNameComparator f19409h = new AppNameComparator(true);

    /* renamed from: e, reason: collision with root package name */
    private final Collator f19410e = Collator.getInstance(Locale.CHINA);

    /* renamed from: f, reason: collision with root package name */
    private final boolean f19411f = false;

    /* compiled from: AppNameComparator.java */
    /* renamed from: w9.a$a */
    /* loaded from: classes2.dex */
    public interface a {
        int a();

        String b();

        char c();
    }

    private AppNameComparator() {
    }

    private int c(String str, String str2) {
        char[] charArray = str.toCharArray();
        char[] charArray2 = str2.toCharArray();
        int length = charArray.length;
        int length2 = charArray2.length;
        int min = Math.min(length, length2);
        for (int i10 = 0; i10 < min; i10++) {
            char c10 = charArray[i10];
            char c11 = charArray2[i10];
            int e10 = e(c10);
            int e11 = e(c11);
            if (e10 != e11) {
                return e10 - e11;
            }
            if (e10 == 2) {
                if (c10 != c11) {
                    return this.f19410e.compare(str, str2);
                }
            } else if (e10 == 3) {
                int f10 = f(str.substring(i10));
                int f11 = f(str2.substring(i10));
                if (f10 != f11) {
                    if (f10 == -1) {
                        return 1;
                    }
                    if (f11 == -1) {
                        return -1;
                    }
                    return f10 - f11;
                }
            } else if (e10 == 1) {
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

    private static int e(char c10) {
        if (c10 >= 19968 && c10 <= 40869) {
            return 2;
        }
        if (i(c10)) {
            return 3;
        }
        return h(c10) ? 1 : 4;
    }

    private int f(String str) {
        if (str == null) {
            return Integer.MAX_VALUE;
        }
        int i10 = 0;
        for (int i11 = 0; i11 < str.length() && str.charAt(i11) >= '0' && str.charAt(i11) <= '9'; i11++) {
            i10++;
        }
        if (i10 > 8) {
            return -2;
        }
        if (i10 > 0) {
            return Integer.parseInt(str.substring(0, 1));
        }
        return Integer.MAX_VALUE;
    }

    private int g(a aVar) {
        return aVar != null ? 1 : 2;
    }

    private static boolean h(char c10) {
        return (c10 >= 'A' && c10 <= 'Z') || (c10 >= 'a' && c10 <= 'z');
    }

    private static boolean i(char c10) {
        return c10 >= '0' && c10 <= '9';
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(a aVar, a aVar2) {
        int g6 = g(aVar);
        int g10 = g(aVar2);
        if (aVar == aVar2) {
            return 0;
        }
        if (g6 != g10) {
            return g6 - g10;
        }
        int a10 = aVar.a();
        int a11 = aVar2.a();
        if (a10 != a11) {
            return a10 > a11 ? 1 : -1;
        }
        char c10 = aVar.c();
        char c11 = aVar2.c();
        if (c10 != '0' && c11 != '0' && c10 != '#' && c11 != '#' && c10 != c11) {
            return c10 > c11 ? 1 : -1;
        }
        if (this.f19411f) {
            return d(aVar, aVar2);
        }
        return b(aVar, aVar2);
    }

    public int b(a aVar, a aVar2) {
        String b10 = aVar.b();
        String b11 = aVar2.b();
        if (b10.length() == 0) {
            return -1;
        }
        char charAt = b10.charAt(0);
        if (b11.length() == 0) {
            return 1;
        }
        char charAt2 = b11.charAt(0);
        int e10 = e(charAt);
        int e11 = e(charAt2);
        if (e10 != e11) {
            return e10 - e11;
        }
        if (e10 == e11 && e10 == 1) {
            char lowerCase = Character.toLowerCase(charAt);
            char lowerCase2 = Character.toLowerCase(charAt2);
            return (lowerCase != lowerCase2 || charAt == charAt2) ? lowerCase == lowerCase2 ? c(b10, b11) : lowerCase - lowerCase2 : charAt2 - charAt;
        }
        return c(b10, b11);
    }

    public int d(a aVar, a aVar2) {
        String b10 = aVar.b();
        String b11 = aVar2.b();
        if (b10 == null || b10.length() == 0) {
            return -1;
        }
        char charAt = b10.charAt(0);
        if (b11 == null || b11.length() == 0) {
            return 1;
        }
        char charAt2 = b11.charAt(0);
        int e10 = e(charAt);
        int e11 = e(charAt2);
        if (e10 == 2) {
            charAt = WordQueryCompat.a(charAt);
        }
        if (e11 == 2) {
            charAt2 = WordQueryCompat.a(charAt2);
        }
        if (e10 == 2 && e11 == 2) {
            this.f19410e.compare(b10, b11);
        } else {
            if (e10 == 1 && e11 == 2) {
                char lowerCase = Character.toLowerCase(charAt);
                char lowerCase2 = Character.toLowerCase(charAt2);
                if (lowerCase2 == lowerCase) {
                    return -1;
                }
                return lowerCase - lowerCase2;
            }
            if (e10 == 2 && e11 == 1) {
                char lowerCase3 = Character.toLowerCase(charAt);
                char lowerCase4 = Character.toLowerCase(charAt2);
                if (lowerCase4 == lowerCase3) {
                    return 1;
                }
                return lowerCase3 - lowerCase4;
            }
            if (e10 != e11) {
                return e10 - e11;
            }
            if (e10 == e11 && e10 == 1) {
                char lowerCase5 = Character.toLowerCase(charAt);
                char lowerCase6 = Character.toLowerCase(charAt2);
                return lowerCase5 == lowerCase6 ? c(b10, b11) : lowerCase5 - lowerCase6;
            }
        }
        return c(b10, b11);
    }

    private AppNameComparator(boolean z10) {
    }
}
