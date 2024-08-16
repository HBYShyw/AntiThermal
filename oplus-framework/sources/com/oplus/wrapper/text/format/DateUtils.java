package com.oplus.wrapper.text.format;

/* loaded from: classes.dex */
public class DateUtils {
    private DateUtils() {
    }

    public static CharSequence formatDuration(long millis) {
        return android.text.format.DateUtils.formatDuration(millis);
    }
}
