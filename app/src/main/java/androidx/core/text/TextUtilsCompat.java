package androidx.core.text;

import android.text.TextUtils;
import java.util.Locale;

/* compiled from: TextUtilsCompat.java */
/* renamed from: androidx.core.text.f, reason: use source file name */
/* loaded from: classes.dex */
public final class TextUtilsCompat {

    /* renamed from: a, reason: collision with root package name */
    private static final Locale f2304a = new Locale("", "");

    /* compiled from: TextUtilsCompat.java */
    /* renamed from: androidx.core.text.f$a */
    /* loaded from: classes.dex */
    static class a {
        static int a(Locale locale) {
            return TextUtils.getLayoutDirectionFromLocale(locale);
        }
    }

    public static int a(Locale locale) {
        return a.a(locale);
    }
}
