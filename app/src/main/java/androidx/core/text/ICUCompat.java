package androidx.core.text;

import android.icu.util.ULocale;
import java.util.Locale;

/* compiled from: ICUCompat.java */
/* renamed from: androidx.core.text.b, reason: use source file name */
/* loaded from: classes.dex */
public final class ICUCompat {

    /* compiled from: ICUCompat.java */
    /* renamed from: androidx.core.text.b$a */
    /* loaded from: classes.dex */
    static class a {
        static ULocale a(Object obj) {
            return ULocale.addLikelySubtags((ULocale) obj);
        }

        static ULocale b(Locale locale) {
            return ULocale.forLocale(locale);
        }

        static String c(Object obj) {
            return ((ULocale) obj).getScript();
        }
    }

    public static String a(Locale locale) {
        return a.c(a.a(a.b(locale)));
    }
}
