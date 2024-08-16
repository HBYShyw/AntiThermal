package com.google.android.material.datepicker;

import java.util.Calendar;
import java.util.TimeZone;

/* compiled from: TimeSource.java */
/* renamed from: com.google.android.material.datepicker.o, reason: use source file name */
/* loaded from: classes.dex */
class TimeSource {

    /* renamed from: c, reason: collision with root package name */
    private static final TimeSource f8779c = new TimeSource(null, null);

    /* renamed from: a, reason: collision with root package name */
    private final Long f8780a;

    /* renamed from: b, reason: collision with root package name */
    private final TimeZone f8781b;

    private TimeSource(Long l10, TimeZone timeZone) {
        this.f8780a = l10;
        this.f8781b = timeZone;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TimeSource c() {
        return f8779c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Calendar a() {
        return b(this.f8781b);
    }

    Calendar b(TimeZone timeZone) {
        Calendar calendar = timeZone == null ? Calendar.getInstance() : Calendar.getInstance(timeZone);
        Long l10 = this.f8780a;
        if (l10 != null) {
            calendar.setTimeInMillis(l10.longValue());
        }
        return calendar;
    }
}
