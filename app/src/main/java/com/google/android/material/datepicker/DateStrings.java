package com.google.android.material.datepicker;

import android.text.format.DateUtils;
import androidx.core.util.Pair;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DateStrings.java */
/* renamed from: com.google.android.material.datepicker.d, reason: use source file name */
/* loaded from: classes.dex */
public class DateStrings {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Pair<String, String> a(Long l10, Long l11) {
        return b(l10, l11, null);
    }

    static Pair<String, String> b(Long l10, Long l11, SimpleDateFormat simpleDateFormat) {
        if (l10 == null && l11 == null) {
            return Pair.a(null, null);
        }
        if (l10 == null) {
            return Pair.a(null, d(l11.longValue(), simpleDateFormat));
        }
        if (l11 == null) {
            return Pair.a(d(l10.longValue(), simpleDateFormat), null);
        }
        Calendar j10 = UtcDates.j();
        Calendar l12 = UtcDates.l();
        l12.setTimeInMillis(l10.longValue());
        Calendar l13 = UtcDates.l();
        l13.setTimeInMillis(l11.longValue());
        if (simpleDateFormat != null) {
            return Pair.a(simpleDateFormat.format(new Date(l10.longValue())), simpleDateFormat.format(new Date(l11.longValue())));
        }
        if (l12.get(1) == l13.get(1)) {
            if (l12.get(1) == j10.get(1)) {
                return Pair.a(f(l10.longValue(), Locale.getDefault()), f(l11.longValue(), Locale.getDefault()));
            }
            return Pair.a(f(l10.longValue(), Locale.getDefault()), k(l11.longValue(), Locale.getDefault()));
        }
        return Pair.a(k(l10.longValue(), Locale.getDefault()), k(l11.longValue(), Locale.getDefault()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String c(long j10) {
        return d(j10, null);
    }

    static String d(long j10, SimpleDateFormat simpleDateFormat) {
        Calendar j11 = UtcDates.j();
        Calendar l10 = UtcDates.l();
        l10.setTimeInMillis(j10);
        if (simpleDateFormat != null) {
            return simpleDateFormat.format(new Date(j10));
        }
        if (j11.get(1) == l10.get(1)) {
            return e(j10);
        }
        return j(j10);
    }

    static String e(long j10) {
        return f(j10, Locale.getDefault());
    }

    static String f(long j10, Locale locale) {
        return UtcDates.b(locale).format(new Date(j10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String g(long j10) {
        return h(j10, Locale.getDefault());
    }

    static String h(long j10, Locale locale) {
        return UtcDates.c(locale).format(new Date(j10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String i(long j10) {
        return DateUtils.formatDateTime(null, j10, 8228);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String j(long j10) {
        return k(j10, Locale.getDefault());
    }

    static String k(long j10, Locale locale) {
        return UtcDates.n(locale).format(new Date(j10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String l(long j10) {
        return m(j10, Locale.getDefault());
    }

    static String m(long j10, Locale locale) {
        return UtcDates.o(locale).format(new Date(j10));
    }
}
