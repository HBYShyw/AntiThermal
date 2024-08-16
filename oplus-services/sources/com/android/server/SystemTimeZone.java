package com.android.server;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.LocalLog;
import android.util.Slog;
import com.android.i18n.timezone.ZoneInfoDb;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SystemTimeZone {
    private static final boolean DEBUG = false;
    private static final String DEFAULT_TIME_ZONE_ID = "GMT";
    private static final String TAG = "SystemTimeZone";
    public static final int TIME_ZONE_CONFIDENCE_HIGH = 100;
    public static final int TIME_ZONE_CONFIDENCE_LOW = 0;
    private static final String TIME_ZONE_CONFIDENCE_SYSTEM_PROPERTY = "persist.sys.timezone_confidence";
    private static final String TIME_ZONE_SYSTEM_PROPERTY = "persist.sys.timezone";
    private static final LocalLog sTimeZoneDebugLog = new LocalLog(30, false);

    @Target({ElementType.TYPE_USE})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface TimeZoneConfidence {
    }

    private static boolean isValidTimeZoneConfidence(int i) {
        return i >= 0 && i <= 100;
    }

    private SystemTimeZone() {
    }

    public static void initializeTimeZoneSettingsIfRequired() {
        String str = SystemProperties.get(TIME_ZONE_SYSTEM_PROPERTY);
        if (isValidTimeZoneId(str)) {
            return;
        }
        String str2 = "initializeTimeZoneSettingsIfRequired():persist.sys.timezone is not valid (" + str + "); setting to " + DEFAULT_TIME_ZONE_ID;
        Slog.w(TAG, str2);
        setTimeZoneId(DEFAULT_TIME_ZONE_ID, 0, str2);
    }

    public static void addDebugLogEntry(String str) {
        sTimeZoneDebugLog.log(str);
    }

    public static boolean setTimeZoneId(String str, int i, String str2) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || !isValidTimeZoneId(str)) {
            addDebugLogEntry("setTimeZoneId: Invalid time zone ID. timeZoneId=" + str + ", confidence=" + i + ", logInfo=" + str2);
            return false;
        }
        synchronized (SystemTimeZone.class) {
            String timeZoneId = getTimeZoneId();
            if (timeZoneId == null || !timeZoneId.equals(str)) {
                try {
                    SystemProperties.set(TIME_ZONE_SYSTEM_PROPERTY, str);
                } catch (RuntimeException unused) {
                    Slog.d(TAG, "set TIME_ZONE_SYSTEM_PROPERTY prop failed.");
                }
                z = true;
            }
            boolean timeZoneConfidence = setTimeZoneConfidence(i);
            if (z || timeZoneConfidence) {
                addDebugLogEntry("Time zone or confidence set:  (new) timeZoneId=" + str + ", (new) confidence=" + i + ", logInfo=" + str2);
            }
        }
        return z;
    }

    private static boolean setTimeZoneConfidence(int i) {
        if (getTimeZoneConfidence() == i) {
            return false;
        }
        SystemProperties.set(TIME_ZONE_CONFIDENCE_SYSTEM_PROPERTY, Integer.toString(i));
        return true;
    }

    public static int getTimeZoneConfidence() {
        int i = SystemProperties.getInt(TIME_ZONE_CONFIDENCE_SYSTEM_PROPERTY, 0);
        if (isValidTimeZoneConfidence(i)) {
            return i;
        }
        return 0;
    }

    public static String getTimeZoneId() {
        return SystemProperties.get(TIME_ZONE_SYSTEM_PROPERTY);
    }

    public static void dump(PrintWriter printWriter) {
        sTimeZoneDebugLog.dump(printWriter);
    }

    private static boolean isValidTimeZoneId(String str) {
        return (str == null || str.isEmpty() || !ZoneInfoDb.getInstance().hasTimeZone(str)) ? false : true;
    }
}
