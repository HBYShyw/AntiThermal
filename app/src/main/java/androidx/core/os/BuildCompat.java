package androidx.core.os;

import android.os.Build;
import java.util.Locale;

/* compiled from: BuildCompat.java */
/* renamed from: androidx.core.os.a, reason: use source file name */
/* loaded from: classes.dex */
public class BuildCompat {
    protected static boolean a(String str, String str2) {
        if ("REL".equals(str2)) {
            return false;
        }
        Locale locale = Locale.ROOT;
        return str2.toUpperCase(locale).compareTo(str.toUpperCase(locale)) >= 0;
    }

    @Deprecated
    public static boolean b() {
        return true;
    }

    public static boolean c() {
        return Build.VERSION.SDK_INT >= 33 || a("Tiramisu", Build.VERSION.CODENAME);
    }
}
