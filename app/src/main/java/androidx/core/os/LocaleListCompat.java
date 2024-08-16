package androidx.core.os;

import android.os.LocaleList;
import androidx.core.text.ICUCompat;
import java.util.Locale;

/* compiled from: LocaleListCompat.java */
/* renamed from: androidx.core.os.f, reason: use source file name */
/* loaded from: classes.dex */
public final class LocaleListCompat {

    /* renamed from: b, reason: collision with root package name */
    private static final LocaleListCompat f2215b = a(new Locale[0]);

    /* renamed from: a, reason: collision with root package name */
    private final LocaleListInterface f2216a;

    /* compiled from: LocaleListCompat.java */
    /* renamed from: androidx.core.os.f$a */
    /* loaded from: classes.dex */
    static class a {

        /* renamed from: a, reason: collision with root package name */
        private static final Locale[] f2217a = {new Locale("en", "XA"), new Locale("ar", "XB")};

        static Locale a(String str) {
            return Locale.forLanguageTag(str);
        }

        private static boolean b(Locale locale) {
            for (Locale locale2 : f2217a) {
                if (locale2.equals(locale)) {
                    return true;
                }
            }
            return false;
        }

        static boolean c(Locale locale, Locale locale2) {
            if (locale.equals(locale2)) {
                return true;
            }
            if (!locale.getLanguage().equals(locale2.getLanguage()) || b(locale) || b(locale2)) {
                return false;
            }
            String a10 = ICUCompat.a(locale);
            if (a10.isEmpty()) {
                String country = locale.getCountry();
                return country.isEmpty() || country.equals(locale2.getCountry());
            }
            return a10.equals(ICUCompat.a(locale2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LocaleListCompat.java */
    /* renamed from: androidx.core.os.f$b */
    /* loaded from: classes.dex */
    public static class b {
        static LocaleList a(Locale... localeArr) {
            return new LocaleList(localeArr);
        }

        static LocaleList b() {
            return LocaleList.getAdjustedDefault();
        }

        static LocaleList c() {
            return LocaleList.getDefault();
        }
    }

    private LocaleListCompat(LocaleListInterface localeListInterface) {
        this.f2216a = localeListInterface;
    }

    public static LocaleListCompat a(Locale... localeArr) {
        return h(b.a(localeArr));
    }

    public static LocaleListCompat b(String str) {
        if (str != null && !str.isEmpty()) {
            String[] split = str.split(",", -1);
            int length = split.length;
            Locale[] localeArr = new Locale[length];
            for (int i10 = 0; i10 < length; i10++) {
                localeArr[i10] = a.a(split[i10]);
            }
            return a(localeArr);
        }
        return d();
    }

    public static LocaleListCompat d() {
        return f2215b;
    }

    public static LocaleListCompat h(LocaleList localeList) {
        return new LocaleListCompat(new LocaleListPlatformWrapper(localeList));
    }

    public Locale c(int i10) {
        return this.f2216a.get(i10);
    }

    public boolean e() {
        return this.f2216a.isEmpty();
    }

    public boolean equals(Object obj) {
        return (obj instanceof LocaleListCompat) && this.f2216a.equals(((LocaleListCompat) obj).f2216a);
    }

    public int f() {
        return this.f2216a.size();
    }

    public String g() {
        return this.f2216a.a();
    }

    public int hashCode() {
        return this.f2216a.hashCode();
    }

    public String toString() {
        return this.f2216a.toString();
    }
}
