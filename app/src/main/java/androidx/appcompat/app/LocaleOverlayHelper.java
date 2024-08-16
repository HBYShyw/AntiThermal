package androidx.appcompat.app;

import androidx.core.os.LocaleListCompat;
import java.util.LinkedHashSet;
import java.util.Locale;

/* compiled from: LocaleOverlayHelper.java */
/* renamed from: androidx.appcompat.app.m, reason: use source file name */
/* loaded from: classes.dex */
final class LocaleOverlayHelper {
    private static LocaleListCompat a(LocaleListCompat localeListCompat, LocaleListCompat localeListCompat2) {
        Locale c10;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (int i10 = 0; i10 < localeListCompat.f() + localeListCompat2.f(); i10++) {
            if (i10 < localeListCompat.f()) {
                c10 = localeListCompat.c(i10);
            } else {
                c10 = localeListCompat2.c(i10 - localeListCompat.f());
            }
            if (c10 != null) {
                linkedHashSet.add(c10);
            }
        }
        return LocaleListCompat.a((Locale[]) linkedHashSet.toArray(new Locale[linkedHashSet.size()]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LocaleListCompat b(LocaleListCompat localeListCompat, LocaleListCompat localeListCompat2) {
        if (localeListCompat != null && !localeListCompat.e()) {
            return a(localeListCompat, localeListCompat2);
        }
        return LocaleListCompat.d();
    }
}
