package com.android.server.timezonedetector;

import com.android.i18n.timezone.TimeZoneFinder;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class TimeZoneCanonicalizer implements Function<String, String> {
    @Override // java.util.function.Function
    public String apply(String str) {
        String findCanonicalTimeZoneId = TimeZoneFinder.getInstance().getCountryZonesFinder().findCanonicalTimeZoneId(str);
        return findCanonicalTimeZoneId == null ? str : findCanonicalTimeZoneId;
    }
}
