package com.oplus.statistics.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint({"SimpleDateFormat"})
/* loaded from: classes2.dex */
public class TimeInfoUtil {
    public static final long MILLISECOND_OF_A_DAY = 86400000;
    public static final long MILLISECOND_OF_A_WEEK = 604800000;
    public static final String TIME_PATTERN_01 = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN_02 = "yyyyMMddHH";
    public static final String TIME_PATTERN_03 = "yyyyMMdd";

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String getFormatDate() {
        return new SimpleDateFormat(TIME_PATTERN_03).format(new Date());
    }

    public static String getFormatHour() {
        return new SimpleDateFormat(TIME_PATTERN_02).format(new Date());
    }

    public static String getFormatTime() {
        return new SimpleDateFormat(TIME_PATTERN_01).format(new Date());
    }

    public static String getFormatDate(long j10) {
        return new SimpleDateFormat(TIME_PATTERN_03).format(new Date(j10));
    }

    public static String getFormatHour(long j10) {
        return new SimpleDateFormat(TIME_PATTERN_02).format(new Date(j10));
    }

    public static String getFormatTime(long j10) {
        return new SimpleDateFormat(TIME_PATTERN_01).format(new Date(j10));
    }
}
